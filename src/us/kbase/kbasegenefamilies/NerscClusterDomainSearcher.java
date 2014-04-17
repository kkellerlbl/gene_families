package us.kbase.kbasegenefamilies;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.type.TypeReference;

import us.kbase.auth.AuthException;
import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.auth.TokenFormatException;
import us.kbase.common.service.Tuple11;
import us.kbase.common.service.Tuple2;
import us.kbase.common.service.UObject;
import us.kbase.common.service.UnauthorizedException;
import us.kbase.workspace.GetObjectInfoNewParams;
import us.kbase.workspace.ListObjectsParams;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.WorkspaceClient;

public class NerscClusterDomainSearcher {	
	private static final String wsUrl = "http://140.221.84.209:7058";  // "https://kbase.us/services/ws/";
	private static final String genomeWsName = "KBasePublicGenomesLoad";
	private static final String domainWsName = "KBasePublicGeneDomains";
	private static final String genomeWsType = "KBaseGenomes.Genome";
	private static final String defaultDomainSetObjectName = "BacterialProteinDomains.set";
	private static final String domainAnnotationWsType = "KBaseGeneFamilies.DomainAnnotation";
	private static final String domainAlignmentsWsType = "KBaseGeneFamilies.DomainAlignments";

	public static void main(String[] args) throws Exception {
		if (args.length < 1 || args.length > 3) {
			System.err.println("Usage: <program> <config_file> [{<genome_ref_list_file> | <core_count> <out_dir>}]");
			return;
		}
		Properties props = new Properties();
		InputStream is = new FileInputStream(new File(args[0]));
		props.load(is);
		is.close();
		File tempDir = new File(get(props, "temp.dir"));
		if (args.length == 3) {
			Set<String> allRefs = new TreeSet<String>();
			for (int partNum = 0; ; partNum++) {
				int sizeOfPart = 0;
				for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info : 
					client(props).listObjects(new ListObjectsParams().withWorkspaces(Arrays.asList(genomeWsName))
							.withType(genomeWsType).withLimit(10000L).withSkip(partNum * 10000L))) {
					String genomeRef = DomainSearchTask.getRefFromObjectInfo(info);
					String genomeObjectName = info.getE2();
					String genomeAnnotationObjectName = genomeObjectName + ".domains";
					String genomeAlignmentsObjectName = genomeObjectName + ".alignments";
					if (objectExists(props, domainWsName, genomeAnnotationObjectName) &&
							objectExists(props, domainWsName, genomeAlignmentsObjectName)) {
						System.out.println("Genome was already processed and skipped: ref=" + genomeRef + ", name=" + genomeObjectName);
						continue;
					}
					allRefs.add(genomeRef);
					sizeOfPart++;
				}
				if (sizeOfPart == 0)
					break;
			}
			int coreCount = Integer.parseInt(args[1]);
			List<List<String>> refListChunks = new ArrayList<List<String>>();
			int chunkPos = 0;
			for (String genomeRef : allRefs) {
				List<String> list;
				if (chunkPos < refListChunks.size()) {
					list = refListChunks.get(chunkPos);
				} else {
					list = new ArrayList<String>();
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
		ObjectStorage objectStorage = DomainSearchTask.createDefaultObjectStorage(client(props));
		String domainModelSetRef = domainWsName + "/" + defaultDomainSetObjectName;
		DomainSearchTask task = new DomainSearchTask(tempDir, objectStorage);
		if (args.length == 1) {
			task.prepareDomainModels(token(props), domainModelSetRef);
			return;
		}
		List<String> refList = UObject.getMapper().readValue(new File(args[1]), new TypeReference<List<String>>() {});
		for (String genomeRef : refList) {
			Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info = 
					client(props).getObjectInfoNew(new GetObjectInfoNewParams().withObjects(
							Arrays.asList(new ObjectIdentity().withRef(genomeRef)))).get(0);
			String genomeObjectName = info.getE2();
			String genomeAnnotationObjectName = genomeObjectName + ".domains";
			String genomeAlignmentsObjectName = genomeObjectName + ".alignments";
			if (objectExists(props, domainWsName, genomeAnnotationObjectName) &&
					objectExists(props, domainWsName, genomeAlignmentsObjectName)) {
				System.out.println("Genome was already processed and skipped: ref=" + genomeRef + ", name=" + genomeObjectName);
				continue;
			}
			try {
				long time = System.currentTimeMillis();
				Tuple2<DomainAnnotation, DomainAlignments> domainsAndAlignments = 
						task.runDomainSearch(token(props), domainModelSetRef, genomeRef);
				DomainAlignments alignments = domainsAndAlignments.getE2();
				WorkspaceClient client = client(props);
				String alignmentsRef = DomainSearchTask.getRefFromObjectInfo(client.saveObjects(
						new SaveObjectsParams().withWorkspace(domainWsName).withObjects(
						Arrays.asList(new ObjectSaveData().withType(domainAlignmentsWsType)
								.withName(genomeAlignmentsObjectName).withData(new UObject(alignments))))).get(0));
				DomainAnnotation domains = domainsAndAlignments.getE1();
				domains.setAlignmentsRef(alignmentsRef);
				client.saveObjects(new SaveObjectsParams().withWorkspace(domainWsName).withObjects(
						Arrays.asList(new ObjectSaveData().withType(domainAnnotationWsType)
								.withName(genomeAnnotationObjectName).withData(new UObject(domains)))));
				System.out.println("Time: " + (System.currentTimeMillis() - time));
			} catch (Exception ex) {
				System.err.println("Error processing genome " + genomeObjectName + " (" + genomeRef + "):");
				ex.printStackTrace();
			}
		}
	}

	private static boolean objectExists(Properties props, String wsName, String objectName) throws Exception {
		WorkspaceClient client = client(props);
		List<?> ret = client.getObjectInfoNew(new GetObjectInfoNewParams().withIgnoreErrors(1L).withObjects(
				Arrays.asList(new ObjectIdentity().withWorkspace(wsName).withName(objectName))));
		return ret != null && ret.size() > 0 && ret.get(0) != null;
	}
	
	private static WorkspaceClient client(Properties props)
			throws UnauthorizedException, IOException, MalformedURLException,
			TokenFormatException, AuthException {
		WorkspaceClient client = new WorkspaceClient(new URL(wsUrl), new AuthToken(token(props)));
		client.setAuthAllowedForHttp(true);
		return client;
	}

	private static String token(Properties props) throws AuthException,
			IOException {
		return AuthService.login(get(props, "user"), get(props, "password")).getToken().toString();
	}
	
	private static String get(Properties props, String propName) {
		String ret = props.getProperty(propName);
		if (ret == null)
			throw new IllegalStateException("Property is not defined: " + propName);
		return ret;
	}

}
