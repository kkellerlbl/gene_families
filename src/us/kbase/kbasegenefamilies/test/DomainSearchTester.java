package us.kbase.kbasegenefamilies.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import us.kbase.auth.AuthException;
import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.auth.TokenFormatException;
import us.kbase.common.service.Tuple7;
import us.kbase.common.service.UnauthorizedException;
import us.kbase.kbasegenefamilies.ConstructDomainClustersBuilder;
import us.kbase.kbasegenefamilies.ConstructDomainClustersParams;
import us.kbase.kbasegenefamilies.DefaultTaskBuilder;
import us.kbase.kbasegenefamilies.DomainClusterSearchResult;
import us.kbase.kbasegenefamilies.DomainClusterStat;
import us.kbase.kbasegenefamilies.GenomeStat;
import us.kbase.kbasegenefamilies.KBaseGeneFamiliesClient;
import us.kbase.kbasegenefamilies.ObjectStorage;
import us.kbase.kbasegenefamilies.SearchDomainsAndConstructClustersBuilder;
import us.kbase.kbasegenefamilies.SearchDomainsAndConstructClustersParams;
import us.kbase.kbasegenefamilies.SearchDomainsParams;
import us.kbase.userandjobstate.UserAndJobStateClient;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.WorkspaceClient;

public class DomainSearchTester {
	private static final String wsName = "nardevuser1:home";
	private static final String wsUrl = "http://dev04.berkeley.kbase.us:7058";
	private static final String domainWsName = "KBasePublicGeneDomains";
	private static final String defaultDCSRObjectName = "CogAndPfam.dcsr";
	private static final String geneFamiliesUrl = "http://140.221.67.204:8123";
	private static final String cogPfamDomainSetObjectName = "CogAndPfamDomains.set";
	
	public static void main(String[] args) throws Exception {
		Properties props = props(new File("config.cfg"));
		String token = token(props);
		WorkspaceClient client = client(token);
		//File tempDir = new File(get(props, "temp.dir"));
		//runDomainSearchLocally(client, token, tempDir);
		//printClusters(client, domainWsName + "/" + defaultDCSRObjectName, new File("par_dcsr_test.txt"));
		//runDomainSearchRemotely(client, token);
		//String annotRef = runDomainSearchOnly(client, token);
		runDomainClustersExtension(client, token, wsName + "/TempAnnotation");
		//printClusters(client, wsName + "/TempDomains3", new File("dcsr3_test.txt"));
		//runDomainClustersExtensionLocally(client, token, tempDir);
	}
	
	private static void runDomainSearchLocally(WorkspaceClient client, String token, File tempDir) throws Exception {
		String genomeRef = wsName + "/Burkholderia_YI23_uid81081.genome";
		String outId = "TempDomains";
		ObjectStorage st = DefaultTaskBuilder.createDefaultObjectStorage(client);
		SearchDomainsAndConstructClustersBuilder builder = new SearchDomainsAndConstructClustersBuilder(tempDir, st);
		String dcsrRef = domainWsName + "/" + defaultDCSRObjectName;
		builder.run(token, new SearchDomainsAndConstructClustersParams().withClustersForExtension(dcsrRef)
				.withGenomes(Arrays.asList(genomeRef)).withOutWorkspace(wsName).withOutResultId(outId), "temp_job_id", wsName + "/" + outId);
		printClusters(client, wsName + "/" + outId, new File("dcsr_test.txt"));
	}

	private static void runDomainClustersExtensionLocally(WorkspaceClient client, String token, File tempDir) throws Exception {
		String annotRef = wsName + "/TempAnnotation";
		String outId = "TempDomains3";
		String dcsrRef = domainWsName + "/" + defaultDCSRObjectName;
		ObjectStorage st = DefaultTaskBuilder.createDefaultObjectStorage(client);
		ConstructDomainClustersBuilder builder = new ConstructDomainClustersBuilder(tempDir, st);
		builder.run(token, new ConstructDomainClustersParams().withClustersForExtension(dcsrRef)
				.withGenomeAnnotations(Arrays.asList(annotRef)).withOutWorkspace(wsName).withOutResultId(outId),
				"temp_job_id", wsName + "/" + outId);
		printClusters(client, wsName + "/" + outId, new File("dcsr3_test.txt"));
	}
	
	private static void printClusters(WorkspaceClient client, String dcsrRef, File out) throws Exception {
		DomainClusterSearchResult dcsr = getObject(client, dcsrRef, DomainClusterSearchResult.class);
		PrintWriter pw = new PrintWriter(out);
		pw.println("-= Genomes =-");
		for (GenomeStat gs : dcsr.getGenomeStatistics().values()) {
			pw.println("Genome " + gs.getGenomeRef() + ", " + gs.getKbaseId() + ", " + gs.getScientificName() + ": " +
					"f=" + gs.getFeatures() + ", fwd=" + gs.getFeaturesWithDomains() + ", d=" + gs.getDomains() + ", dm=" + gs.getDomainModels());
		}
		pw.println();
		if (dcsr.getParentRef() != null) {
			DomainClusterSearchResult parentDcsr = getObject(client, dcsr.getParentRef(), DomainClusterSearchResult.class);
			pw.println("-= Public genomes =-");
			for (GenomeStat gs : parentDcsr.getGenomeStatistics().values()) {
				pw.println("Genome " + gs.getGenomeRef() + ", " + gs.getKbaseId() + ", " + gs.getScientificName() + ": " +
						"f=" + gs.getFeatures() + ", fwd=" + gs.getFeaturesWithDomains() + ", d=" + gs.getDomains() + ", dm=" + gs.getDomainModels());
			}
			pw.println();
		}
		pw.println("-= Domains =-");
		for (DomainClusterStat dcs : dcsr.getDomainClusterStatistics().values()) {
			pw.println("Domain " + dcs.getDomainModelRef() + ", " + dcs.getName() + ": g=" + dcs.getGenomes() + ", " +
					"f=" + dcs.getFeatures() + ", d=" + dcs.getDomains());
		}
		pw.close();
	}
	
	private static void runDomainSearchRemotely(WorkspaceClient client, String token) throws Exception {
		KBaseGeneFamiliesClient gf = new KBaseGeneFamiliesClient(new URL(geneFamiliesUrl), new AuthToken(token));
		gf.setIsInsecureHttpConnectionAllowed(true);
		String genomeRef = wsName + "/Burkholderia_YI23_uid81081.genome";
		String outId = "TempDomains2";
		String dcsrRef = domainWsName + "/" + defaultDCSRObjectName;
		String jobId = gf.searchDomainsAndConstructClusters(new SearchDomainsAndConstructClustersParams().withClustersForExtension(dcsrRef)
				.withGenomes(Arrays.asList(genomeRef)).withOutWorkspace(wsName).withOutResultId(outId));
		UserAndJobStateClient jscl = new UserAndJobStateClient(new URL("https://kbase.us/services/userandjobstate/"), new AuthToken(token));
		jscl.setAllSSLCertificatesTrusted(true);
		jscl.setIsInsecureHttpConnectionAllowed(true);
		for (int iter = 0; ; iter++) {
			Tuple7<String, String, String, Long, String, Long, Long> data = jscl.getJobStatus(jobId);
			String status = data.getE3();
    		Long complete = data.getE6();
    		Long wasError = data.getE7();
			System.out.println("Status (" + iter + "): " + status);
			if (complete == 1L) {
				if (wasError == 0L) {
					String wsRef = jscl.getResults(jobId).getWorkspaceids().get(0);
					printClusters(client, wsRef, new File("dcsr2_test.txt"));
				} else {
					System.out.println("Detailed error:");
					System.out.println(jscl.getDetailedError(jobId));
				}
				break;
			}
			Thread.sleep(12000);
		}
	}
	
	private static String runDomainSearchOnly(WorkspaceClient client, String token) throws Exception {
		KBaseGeneFamiliesClient gf = new KBaseGeneFamiliesClient(new URL(geneFamiliesUrl), new AuthToken(token));
		gf.setIsInsecureHttpConnectionAllowed(true);
		String genomeRef = wsName + "/Burkholderia_YI23_uid81081.genome";
		String outId = "TempAnnotation";
		String dmsRef = domainWsName + "/" + cogPfamDomainSetObjectName;
		String jobId = gf.searchDomains(new SearchDomainsParams().withDmsRef(dmsRef)
				.withGenome(genomeRef).withOutWorkspace(wsName).withOutResultId(outId));
		UserAndJobStateClient jscl = new UserAndJobStateClient(new URL("https://kbase.us/services/userandjobstate/"), new AuthToken(token));
		jscl.setAllSSLCertificatesTrusted(true);
		jscl.setIsInsecureHttpConnectionAllowed(true);
		String ret = null;
		for (int iter = 0; ; iter++) {
			Tuple7<String, String, String, Long, String, Long, Long> data = jscl.getJobStatus(jobId);
			String status = data.getE3();
    		Long complete = data.getE6();
    		Long wasError = data.getE7();
			System.out.println("Status (" + iter + "): " + status);
			if (complete == 1L) {
				if (wasError == 0L) {
					ret = jscl.getResults(jobId).getWorkspaceids().get(0);
					System.out.println("Annotation reference: " + ret);
				} else {
					System.out.println("Detailed error:");
					System.out.println(jscl.getDetailedError(jobId));
				}
				break;
			}
			Thread.sleep(12000);
		}
		return ret;
	}
	
	private static void runDomainClustersExtension(WorkspaceClient client, String token, String annotRef) throws Exception {
		KBaseGeneFamiliesClient gf = new KBaseGeneFamiliesClient(new URL(geneFamiliesUrl), new AuthToken(token));
		gf.setIsInsecureHttpConnectionAllowed(true);
		//String genomeRef = wsName + "/Burkholderia_YI23_uid81081.genome";
		String outId = "TempDomains3";
		String dcsrRef = domainWsName + "/" + defaultDCSRObjectName;
		String jobId = gf.constructDomainClusters(new ConstructDomainClustersParams().withClustersForExtension(dcsrRef)
				.withGenomeAnnotations(Arrays.asList(annotRef)).withOutWorkspace(wsName).withOutResultId(outId));
		UserAndJobStateClient jscl = new UserAndJobStateClient(new URL("https://kbase.us/services/userandjobstate/"), new AuthToken(token));
		jscl.setAllSSLCertificatesTrusted(true);
		jscl.setIsInsecureHttpConnectionAllowed(true);
		for (int iter = 0; ; iter++) {
			Tuple7<String, String, String, Long, String, Long, Long> data = jscl.getJobStatus(jobId);
			String status = data.getE3();
    		Long complete = data.getE6();
    		Long wasError = data.getE7();
			System.out.println("Status (" + iter + "): " + status);
			if (complete == 1L) {
				if (wasError == 0L) {
					String wsRef = jscl.getResults(jobId).getWorkspaceids().get(0);
					printClusters(client, wsRef, new File("dcsr3_test.txt"));
				} else {
					System.out.println("Detailed error:");
					System.out.println(jscl.getDetailedError(jobId));
				}
				break;
			}
			Thread.sleep(12000);
		}
	}
	
	private static <T> T getObject(WorkspaceClient client, String ref, Class<T> type) throws Exception {
		return client.getObjects(Arrays.asList(new ObjectIdentity().withRef(ref))).get(0).getData().asClassInstance(type);
	}

	private static Properties props(File cfgFile)
			throws FileNotFoundException, IOException {
		Properties props = new Properties();
		InputStream is = new FileInputStream(cfgFile);
		props.load(is);
		is.close();
		return props;
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
