package us.kbase.kbasegenefamilies;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.type.TypeReference;

import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.Tuple11;
import us.kbase.common.service.UObject;
import us.kbase.workspace.GetObjectInfoNewParams;
import us.kbase.workspace.ListObjectsParams;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.WorkspaceClient;

public class NerskClusterDomainSearcher {	
	private static final String wsUrl = "http://140.221.84.209:7058";  // "https://kbase.us/services/ws/";
	private static final String genomeWsName = "KBasePublicGenomesLoad";
	private static final String domainWsName = "KBasePublicGeneDomains";
	private static final String genomeWsType = "KBaseGenomes.Genome";
	private static final String defaultDomainSetObjectName = "BacterialProteinDomains.set";
	private static final String domainAnnotationWsType = "KBaseGeneFamilies.DomainAnnotation";

	public static void main(String[] args) throws Exception {
		if (args.length < 1 || args.length > 3) {
			System.err.println("Usage: <program> <config_file> [{<genome_ref_list_file> | <core_count> <out_dir>}]");
			return;
		}
		Properties props = new Properties();
		InputStream is = new FileInputStream(new File(args[0]));
		props.load(is);
		is.close();
		String token = AuthService.login(get(props, "user"), get(props, "password")).getToken().toString();
		WorkspaceClient client = new WorkspaceClient(new URL(wsUrl), new AuthToken(token));
		client.setAuthAllowedForHttp(true);
		if (args.length == 3) {
			Set<String> allRefs = new TreeSet<String>();
			for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info : 
				client.listObjects(new ListObjectsParams().withWorkspaces(Arrays.asList(genomeWsName)).withType(genomeWsType))) {
				String genomeRef = DomainSearchTask.getRefFromObjectInfo(info);
				allRefs.add(genomeRef);
			}
			int coreCount = Integer.parseInt(args[1]);
			List<List<String>> refListChunks = new ArrayList<List<String>>();
			int chunkPos = 0;
			for (String genomeRef : allRefs) {
				List<String> list;
				if (chunkPos < refListChunks.size()) {
					list = refListChunks.get(chunkPos);
				} else {
					list = new ArrayList<>();
					refListChunks.add(list);
				}
				list.add(genomeRef);
				chunkPos = (chunkPos + 1) % coreCount;
			}
			for (chunkPos = 0; chunkPos < coreCount; chunkPos++) {
				File f = new File(args[2], "genome_refs_" + chunkPos + ".txt");
				UObject.getMapper().writeValue(f, refListChunks.get(chunkPos));
			}
			return;
		}
		ObjectStorage objectStorage = DomainSearchTask.createDefaultObjectStorage(client);
		String domainModelSetRef = domainWsName + "/" + defaultDomainSetObjectName;
		DomainSearchTask task = new DomainSearchTask(new File(get(props, "temp.dir")), objectStorage);
		if (args.length == 1) {
			task.prepareDomainModels(token, domainModelSetRef);
			return;
		}
		List<String> refList = UObject.getMapper().readValue(new File(args[1]), new TypeReference<List<String>>() {});
		for (String genomeRef : refList) {
			Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info = 
				client.getObjectInfoNew(new GetObjectInfoNewParams().withObjects(
						Arrays.asList(new ObjectIdentity().withRef(genomeRef)))).get(0);
			String genomeObjectName = info.getE2();
			try {
				long time = System.currentTimeMillis();
				DomainAnnotation domains = task.runDomainSearch(token, domainModelSetRef, genomeRef);
				System.out.println("Time: " + (System.currentTimeMillis() - time));
				System.out.println(client.saveObjects(new SaveObjectsParams().withWorkspace(domainWsName).withObjects(
						Arrays.asList(new ObjectSaveData().withType(domainAnnotationWsType)
								.withName(genomeObjectName + ".domains").withData(new UObject(domains))))));
			} catch (Exception ex) {
				System.err.println("Error processing genome " + genomeObjectName + " (" + genomeRef + "):");
				ex.printStackTrace();
			}
		}
	}
	
	private static String get(Properties props, String propName) {
		String ret = props.getProperty(propName);
		if (ret == null)
			throw new IllegalStateException("Property is not defined: " + propName);
		return ret;
	}

}
