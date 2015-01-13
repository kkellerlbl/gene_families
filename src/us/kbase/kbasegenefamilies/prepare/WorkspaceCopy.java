package us.kbase.kbasegenefamilies.prepare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import us.kbase.auth.AuthException;
import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.auth.TokenFormatException;
import us.kbase.common.service.Tuple2;
import us.kbase.common.service.UnauthorizedException;
import us.kbase.kbasegenefamilies.util.Utils;
import us.kbase.workspace.WorkspaceClient;

public class WorkspaceCopy {
	private static final String wsDevUrl = "http://140.221.84.209:7058";
	private static final String wsProdUrl = "https://kbase.us/services/ws/";

	private static final String genomeWsName = "KBasePublicGenomesLoad";
	private static final String domainWsName = "KBasePublicGeneDomains";
	private static final String genomeWsType = "KBaseGenomes.Genome";
	private static final String defaultDomainSetObjectName = "BacterialProteinDomains.set";
	private static final String domainAnnotationWsType = "KBaseGeneFamilies.DomainAnnotation";
	private static final String domainAlignmentsWsType = "KBaseGeneFamilies.DomainAlignments";

	public static void main(String[] args) throws Exception {
		compareGenomes(props(args));
	}
	
	private static void compareGenomes(Properties cfg) throws Exception {
		WorkspaceClient devCl = client(cfg, wsDevUrl);
		WorkspaceClient prodCl = client(cfg, wsProdUrl);
		List<Tuple2<String, String>> devGenomeRefName = listGenomes(devCl);
		List<Tuple2<String, String>> prodGenomeRefName = listGenomes(prodCl);
		System.out.println(devGenomeRefName.size() + ", " + prodGenomeRefName.size());
	}
	
	private static List<Tuple2<String, String>> listGenomes(WorkspaceClient client) throws Exception {
		return Utils.listAllObjectsRefAndName(client, genomeWsName, genomeWsType);
	}
	
	private static Properties props(String[] args)
			throws FileNotFoundException, IOException {
		Properties props = new Properties();
		InputStream is = new FileInputStream(new File(args[0]));
		props.load(is);
		is.close();
		return props;
	}

	private static WorkspaceClient client(Properties props, String wsUrl)
			throws UnauthorizedException, IOException, MalformedURLException,
			TokenFormatException, AuthException {
		return client(token(props), wsUrl);
	}
	
	private static WorkspaceClient client(String token, String wsUrl)
			throws UnauthorizedException, IOException, MalformedURLException,
			TokenFormatException, AuthException {
		WorkspaceClient client = new WorkspaceClient(new URL(wsUrl), new AuthToken(token));
		client.setAuthAllowedForHttp(true);
		return client;
	}

	private static String token(Properties props) throws AuthException, IOException {
		return AuthService.login(get(props, "user"), get(props, "password")).getToken().toString();
	}

	private static String get(Properties props, String propName) {
		String ret = props.getProperty(propName);
		if (ret == null)
			throw new IllegalStateException("Property is not defined: " + propName);
		return ret;
	}

}
