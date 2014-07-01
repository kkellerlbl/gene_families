package us.kbase.kbasegenefamilies.prepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.Tuple11;
import us.kbase.common.service.UObject;
import us.kbase.common.service.test.Tuple2;
import us.kbase.kbasegenefamilies.DomainModel;
import us.kbase.kbasegenefamilies.DomainModelSet;
import us.kbase.kbasegenefamilies.DomainModelType;
import us.kbase.kbasegenefamilies.prepare.ScorematParser.DomainModelTypeStorage;
import us.kbase.workspace.CreateWorkspaceParams;
import us.kbase.workspace.GetModuleInfoParams;
import us.kbase.workspace.ListModuleVersionsParams;
import us.kbase.workspace.ListObjectsParams;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.RegisterTypespecParams;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.WorkspaceClient;
import us.kbase.workspace.WorkspaceIdentity;

public class CddPreparation {
	
	private static final String defaultConfigFile = "config.cfg";	// "config_prod.cfg";
	private static final String wsUrl = "http://dev04.berkeley.kbase.us:7058";  // "https://kbase.us/services/ws/";
	//private static final String wsUrl = "https://kbase.us/services/ws/";
	private static final String scorematFilesDir = "/Users/rsutormin/Work/2014-03-17_trees/smp";
	private static final String domainWsName = "KBasePublicGeneDomains";
	private static final String domainModelWsType = "KBaseGeneFamilies.DomainModel";
	private static final String domainTypeWsType = "KBaseGeneFamilies.DomainModelType";
	private static final String domainSetWsType = "KBaseGeneFamilies.DomainModelSet";
	private static final int modelBufferMaxSize = 100;
	private static final String bacterialDomainSetObjectName = "BacterialProteinDomains.set";

	public static void main(String[] args) throws Exception {
		reg();
		//processSmps();
		//storeBacterialDomainModelSet();
	}
	
	private static void storeBacterialDomainModelSet() throws Exception {
		WorkspaceClient wc = createWsClient(getAuthToken());
		List<String> typeRefs = new ArrayList<String>();
		for (Tuple2<DomainModelType, String> typeDescrRef : loadTypes(domainWsName, wc).values())
			if (!typeDescrRef.getE1().getTypeName().startsWith("KOG"))
				typeRefs.add(typeDescrRef.getE2());
		List<String> setParentRefs = new ArrayList<String>();
		for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info : 
				wc.listObjects(new ListObjectsParams().withWorkspaces(Arrays.asList(domainWsName)).withType(domainSetWsType))) {
			String objName = info.getE2();
			if (!objName.startsWith("KOG"))
				setParentRefs.add(getRefFromObjectInfo(info));
		}
		storeBacterialDomainModelSet(setParentRefs, typeRefs, wc);
	}
	
	private static void processSmps() throws Exception {
		final WorkspaceClient wc = createWsClient(getAuthToken());
		checkWorkspaceName(domainWsName, wc);
		Map<String, Tuple2<DomainModelType, String>> typeToDescrRef = loadTypes(domainWsName, wc);
		final Map<String, DomainModelType> refToType = new HashMap<String, DomainModelType>();
		for (Tuple2<DomainModelType, String> entry : typeToDescrRef.values())
			refToType.put(entry.getE2(), entry.getE1());
		DomainModelTypeStorage typeStorage = new DomainModelTypeStorage() {
			@Override
			public String storeDomainModelTypeAndGetRef(DomainModelType type) throws IOException, JsonClientException {
				String ref = saveIntoWorkspaceAndGetRef(wc, domainWsName, domainTypeWsType, type.getTypeName(), type);
				System.out.println("Type " + type.getTypeName() + " is saved into ref=" + ref);
				refToType.put(ref, type);
				return ref;
			}
		};
		Map<String, List<DomainModel>> typeNameToModelBuffer = new TreeMap<String, List<DomainModel>>();
		Map<String, List<String>> typeNameToModelRefList = new TreeMap<String, List<String>>();
		for (File f : new File(scorematFilesDir).listFiles()) {
			if (f.getName().endsWith(".smp")) {
				DomainModel model = ScorematParser.constructDomainModel(f, typeToDescrRef, typeStorage);
				if (model == null)
					continue;
				String typeRef = model.getDomainTypeRef();
				String typeName = refToType.get(typeRef).getTypeName();
				List<DomainModel> buffer = typeNameToModelBuffer.get(typeName);
				if (buffer == null) {
					buffer = new ArrayList<DomainModel>();
					typeNameToModelBuffer.put(typeName, buffer);
				}
				buffer.add(model);
				List<String> modelRefs = typeNameToModelRefList.get(typeName);
				if (modelRefs == null) {
					modelRefs = new ArrayList<String>();
					typeNameToModelRefList.put(typeName, modelRefs);
				}
				checkFlushBuffer(typeName, buffer, modelRefs, wc, false);
			}
		}
		List<String> setParentRefs = new ArrayList<String>();
		List<String> bacterialTypeRefs = new ArrayList<String>();
		for (String typeName : typeNameToModelBuffer.keySet()) {
			List<DomainModel> buffer = typeNameToModelBuffer.get(typeName);
			List<String> modelRefs = typeNameToModelRefList.get(typeName);
			checkFlushBuffer(typeName, buffer, modelRefs, wc, true);
			System.out.println("Type " + typeName + ": size=" + modelRefs.size());
			String typeRef = typeToDescrRef.get(typeName).getE2();
			DomainModelSet dms = new DomainModelSet()
				.withSetName("Domains of type " + typeName)
				.withParentRefs(Collections.<String>emptyList())
				.withTypes(Arrays.asList(typeRef))
				.withDomainModelRefs(modelRefs);
			String domainSetRef = saveIntoWorkspaceAndGetRef(wc, domainWsName, domainSetWsType, 
					typeName + ".set", dms);
			if (!typeName.equals("KOG")) {
				setParentRefs.add(domainSetRef);
				bacterialTypeRefs.add(typeRef);
			}
		}
		storeBacterialDomainModelSet(setParentRefs, bacterialTypeRefs, wc);
	}

	private static void storeBacterialDomainModelSet(List<String> parentRefs, 
			List<String> typeRefs, WorkspaceClient wc) throws Exception {
		DomainModelSet dms = new DomainModelSet()
			.withSetName("Bacterial protein domains")
			.withParentRefs(parentRefs)
			.withTypes(typeRefs)
			.withDomainModelRefs(Collections.<String>emptyList());
		saveIntoWorkspaceAndGetRef(wc, domainWsName, domainSetWsType, 
				bacterialDomainSetObjectName, dms);
	}
	
	private static String saveIntoWorkspaceAndGetRef(WorkspaceClient wc, String wsName, String objectType, 
			String objectName, Object object) throws IOException, JsonClientException {
		return getRefFromObjectInfo(wc.saveObjects(new SaveObjectsParams().withWorkspace(wsName)
				.withObjects(Arrays.asList(new ObjectSaveData().withType(objectType).withName(objectName)
						.withData(new UObject(object))))).get(0));
	}
	
	private static void checkFlushBuffer(String typeName, List<DomainModel> buffer, List<String> modelRefs, 
			WorkspaceClient wc, boolean flushAnyway) throws IOException, JsonClientException {
		if (buffer.size() < modelBufferMaxSize && !flushAnyway)
			return;
		List<ObjectSaveData> objects = new ArrayList<ObjectSaveData>();
		for (DomainModel model : buffer)
			objects.add(new ObjectSaveData().withType(domainModelWsType)
					.withName(model.getDomainName() + ".domain").withData(new UObject(model)));
		List<Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>>> infos = 
				wc.saveObjects(new SaveObjectsParams().withWorkspace(domainWsName)
				.withObjects(objects));
		for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info : infos)
			modelRefs.add(getRefFromObjectInfo(info));
		buffer.clear();
		System.out.println("Type " + typeName + ": size=" + modelRefs.size());
	}
	
	private static void checkWorkspaceName(String wsName, WorkspaceClient wc) throws IOException, JsonClientException {
		try {
			wc.getWorkspaceInfo(new WorkspaceIdentity().withWorkspace(wsName));
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			wc.createWorkspace(new CreateWorkspaceParams().withGlobalread("r").withWorkspace(wsName));
		}
	}
	
	private static Map<String, Tuple2<DomainModelType, String>> loadTypes(String wsName, WorkspaceClient wc) throws IOException, JsonClientException {
		Map<String, Tuple2<DomainModelType, String>> ret = new TreeMap<String, Tuple2<DomainModelType, String>>();
		for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info : 
				wc.listObjects(new ListObjectsParams().withWorkspaces(Arrays.asList(wsName)).withType(domainTypeWsType))) {
			String ref = getRefFromObjectInfo(info);
			DomainModelType type = wc.getObjects(Arrays.asList(new ObjectIdentity().withRef(ref))).get(0).getData().asClassInstance(DomainModelType.class);
			Tuple2<DomainModelType, String> typeDescr = new Tuple2<DomainModelType, String>().withE1(type).withE2(ref);
			ret.put(type.getTypeName(), typeDescr);
		}
		return ret;
	}
	
	private static String getRefFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
		return info.getE7() + "/" + info.getE1() + "/" + info.getE5();
	}
	
	private static AuthToken getAuthToken() throws Exception {
		Properties props = new Properties();
		InputStream is = new FileInputStream(new File(defaultConfigFile));
		props.load(is);
		is.close();
		return AuthService.login(props.getProperty("user"), props.getProperty("password")).getToken();
	}

	private static WorkspaceClient createWsClient(AuthToken token) throws Exception {
		WorkspaceClient ret = new WorkspaceClient(new URL(wsUrl), token);
		ret.setAuthAllowedForHttp(true);
		return ret;
	}

	private static void reg() throws Exception {
		WorkspaceClient wc = createWsClient(getAuthToken());
		String module = "KBaseGeneFamilies";
		String[] types = {
				"DomainModelType", 
				"DomainModel", 
				"DomainModelSet", 
				"DomainAnnotation", 
				"DomainAlignments", 
				"DomainCluster", 
				"DomainClusterSearchResult"
		};
		//wc.requestModuleOwnership(module);
		//if (true)
		//	return;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(new File("KBaseGeneFamilies.spec")));
		while (true) {
			String l = br.readLine();
			if (l == null)
				break;
			sb.append(l).append("\n");
		}
		br.close();
		System.out.println(sb.toString());
		System.out.println(wc.registerTypespec(new RegisterTypespecParams()
				.withSpec(sb.toString()).withNewTypes(Arrays.asList(types)).withDryrun(0L)));
		System.out.println(wc.listModuleVersions(new ListModuleVersionsParams().withMod(module)));
		System.out.println(wc.getModuleInfo(new GetModuleInfoParams().withMod(module)));
		System.out.println(wc.releaseModule(module));
	}

}
