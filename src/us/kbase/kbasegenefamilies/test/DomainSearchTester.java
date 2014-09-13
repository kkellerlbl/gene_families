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
import us.kbase.common.service.UnauthorizedException;
import us.kbase.kbasegenefamilies.DefaultTaskBuilder;
import us.kbase.kbasegenefamilies.DomainClusterSearchResult;
import us.kbase.kbasegenefamilies.DomainClusterStat;
import us.kbase.kbasegenefamilies.GenomeStat;
import us.kbase.kbasegenefamilies.ObjectStorage;
import us.kbase.kbasegenefamilies.SearchDomainsAndConstructClustersBuilder;
import us.kbase.kbasegenefamilies.SearchDomainsAndConstructClustersParams;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.WorkspaceClient;

public class DomainSearchTester {
	private static final String wsName = "nardevuser1:home";
	private static final String wsUrl = "http://dev04.berkeley.kbase.us:7058";
	private static final String domainWsName = "KBasePublicGeneDomains";
	private static final String defaultDCSRObjectName = "CogAndPfam.dcsr";
	
	public static void main(String[] args) throws Exception {
		Properties props = props(new File("config.cfg"));
		String token = token(props);
		WorkspaceClient client = client(token);
		ObjectStorage st = DefaultTaskBuilder.createDefaultObjectStorage(client);
		File tempDir = new File(get(props, "temp.dir"));
		String genomeRef = wsName + "/Burkholderia_YI23_uid81081.genome";
		String outId = "TempDomains";
		SearchDomainsAndConstructClustersBuilder builder = new SearchDomainsAndConstructClustersBuilder(tempDir, st);
		String dcsrRef = domainWsName + "/" + defaultDCSRObjectName;
		builder.run(token, new SearchDomainsAndConstructClustersParams().withClustersForExtension(dcsrRef)
				.withGenomes(Arrays.asList(genomeRef)).withOutWorkspace(wsName).withOutResultId(outId), "temp_job_id", wsName + "/" + outId);
		DomainClusterSearchResult dcsr = getObject(client, wsName + "/" + outId, DomainClusterSearchResult.class);
		String parentDcsrRef = dcsr.getParentRef();
		DomainClusterSearchResult parentDcsr = getObject(client, parentDcsrRef, DomainClusterSearchResult.class);
		PrintWriter pw = new PrintWriter(new File("dcsr_test.txt"));
		pw.println("-= User genomes =-");
		for (GenomeStat gs : dcsr.getGenomeStatistics().values()) {
			pw.println("Genome " + gs.getGenomeRef() + ", " + gs.getKbaseId() + ", " + gs.getScientificName() + ": " +
					"f=" + gs.getFeatures() + ", fwd=" + gs.getFeaturesWithDomains() + ", d=" + gs.getDomains() + ", dm=" + gs.getDomainModels());
		}
		pw.println();
		pw.println("-= Public genomes =-");
		for (GenomeStat gs : parentDcsr.getGenomeStatistics().values()) {
			pw.println("Genome " + gs.getGenomeRef() + ", " + gs.getKbaseId() + ", " + gs.getScientificName() + ": " +
					"f=" + gs.getFeatures() + ", fwd=" + gs.getFeaturesWithDomains() + ", d=" + gs.getDomains() + ", dm=" + gs.getDomainModels());
		}
		pw.println();
		pw.println("-= Domains =-");
		for (DomainClusterStat dcs : dcsr.getDomainClusterStatistics().values()) {
			pw.println("Domain " + dcs.getDomainModelRef() + ", " + dcs.getName() + ": g=" + dcs.getGenomes() + ", " +
					"f=" + dcs.getFeatures() + ", d=" + dcs.getDomains());
		}
		pw.close();
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
