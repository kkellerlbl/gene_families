package us.kbase.kbasegenefamilies.prepare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import us.kbase.auth.AuthException;
import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.auth.TokenFormatException;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.Tuple2;
import us.kbase.common.service.UObject;
import us.kbase.common.service.UnauthorizedException;
import us.kbase.kbasegenefamilies.util.Utils;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.WorkspaceClient;

import com.fasterxml.jackson.core.type.TypeReference;

public class GenomeAnnotationChache {
	private static final String wsUrl = "http://dev04.berkeley.kbase.us:7058";  // "https://kbase.us/services/ws/";
	private static final String genomeWsName = "KBasePublicGenomesLoad";
	private static final String domainWsName = "KBasePublicGeneDomains";
	private static final String genomeWsType = "KBaseGenomes.Genome";
	private static final String domainAnnotationWsType = "KBaseGeneFamilies.DomainAnnotation";
	private static final String domainAlignmentsWsType = "KBaseGeneFamilies.DomainAlignments";

	public static void cacheDomainAnnotation(Properties props) throws Exception {
		File tempDir = new File(get(props, "temp.dir"));
		File outputDir = new File(tempDir, "annotation");
		if (!outputDir.exists())
			outputDir.mkdir();
		Map<String, String> annotNameToRefMap = new TreeMap<String, String>();
		WorkspaceClient client = client(props);
		for (Tuple2<String, String> refAndName : Utils.listAllObjectsRefAndName(client, domainWsName, domainAnnotationWsType))
			annotNameToRefMap.put(refAndName.getE2(), refAndName.getE1());
		Map<String, String> alignNameToRefMap = new TreeMap<String, String>();
		for (Tuple2<String, String> refAndName : Utils.listAllObjectsRefAndName(client, domainWsName, domainAlignmentsWsType))
			alignNameToRefMap.put(refAndName.getE2(), refAndName.getE1());
		int count = 0;
		File genomeKbIdToRefsFile = new File(outputDir, "genome_refs.json");
		Map<String, Tuple2<String, String>> genomeKbIdToRefs = new TreeMap<String, Tuple2<String, String>>();
		if (genomeKbIdToRefsFile.exists()) {
			Map<String, Tuple2<String, String>> map = UObject.getMapper().readValue(genomeKbIdToRefsFile, new TypeReference<Map<String, Tuple2<String, String>>>() {});
			genomeKbIdToRefs.putAll(map);
		}
		int genomeCount = 0;
		long annotationRetrivalCommonTime = 0;
		for (Tuple2<String, String> refAndName : Utils.listAllObjectsRefAndName(client, genomeWsName, genomeWsType)) {
			String genomeRef = refAndName.getE1();
			String genomeObjectName = refAndName.getE2();
			if (genomeKbIdToRefs.containsKey(genomeObjectName))
				continue;
			String genomeAnnotationObjectName = genomeObjectName + ".domains";
			String genomeAlignmentsObjectName = genomeObjectName + ".alignments";
			String annotRef = annotNameToRefMap.get(genomeAnnotationObjectName);
			String alignRef = alignNameToRefMap.get(genomeAlignmentsObjectName);
			if (annotRef != null && alignRef != null) {
				genomeKbIdToRefs.put(genomeObjectName, new Tuple2<String, String>().withE1(annotRef).withE2(alignRef));				
				count++;
				System.out.println("Genome " + genomeObjectName + ", " + genomeRef);
				File f1 = new File(outputDir, "domains_" + annotRef.replace('/', '_') + ".json.gz");
				if (!f1.exists()) {
					long time = System.currentTimeMillis();
					UObject obj = null;
					for (int i = 0; i < 5; i++) {
						try {
							obj = loadData(client, new ObjectIdentity().withWorkspace(domainWsName).withName(genomeAnnotationObjectName));
							break;
						} catch (Exception ex) {
							System.err.println("Error loading object " + genomeAnnotationObjectName + " (attempt " + (i + 1) + "): " + ex.getMessage());
						}
					}
					if (obj != null) {
						OutputStream os = new GZIPOutputStream(new FileOutputStream(f1));
						UObject.getMapper().writeValue(os, obj);
						os.close();
						time = System.currentTimeMillis() - time;
						genomeCount++;
						annotationRetrivalCommonTime += time;
						if (genomeCount % 100 == 0)
							System.out.println("Info: average genome annotation loading time: " + (annotationRetrivalCommonTime / genomeCount) + " ms");
					}
				}
				File f2 = new File(outputDir, "alignments_" + alignRef.replace('/', '_') + ".json.gz");
				if (!f2.exists()) {
					UObject obj = null;
					for (int i = 0; i < 5; i++) {
						try {
							obj = loadData(client, new ObjectIdentity().withWorkspace(domainWsName).withName(genomeAlignmentsObjectName));
							break;
						} catch (Exception ex) {
							System.err.println("Error loading object " + genomeAlignmentsObjectName + " (attempt " + (i + 1) + "): " + ex.getMessage());
						}
					}
					if (obj != null) {
						OutputStream os = new GZIPOutputStream(new FileOutputStream(f2));
						UObject.getMapper().writeValue(os, obj);
						os.close();
					}
				}
			}
		}
		UObject.getMapper().writeValue(genomeKbIdToRefsFile, genomeKbIdToRefs);
		System.out.println("Number of added genomes with domain annotation: " + count);
	}

	private static UObject loadData(WorkspaceClient client, ObjectIdentity oi) throws IOException, JsonClientException {
		return client.getObjects(Arrays.asList(oi)).get(0).getData();
	}

	private static WorkspaceClient client(Properties props)
			throws UnauthorizedException, IOException, MalformedURLException,
			TokenFormatException, AuthException {
		return client(token(props));
	}
	
	private static WorkspaceClient client(String token)
			throws UnauthorizedException, IOException, MalformedURLException,
			TokenFormatException, AuthException {
		WorkspaceClient client = new WorkspaceClient(new URL(wsUrl), new AuthToken(token));
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
