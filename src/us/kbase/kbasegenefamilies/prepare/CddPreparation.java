package us.kbase.kbasegenefamilies.prepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;

import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.workspace.GetModuleInfoParams;
import us.kbase.workspace.ListModuleVersionsParams;
import us.kbase.workspace.RegisterTypespecParams;
import us.kbase.workspace.WorkspaceClient;

public class CddPreparation {
	
	private static String wsUrl = "http://140.221.84.209:7058";  // "https://kbase.us/services/ws/";

	public static void main(String[] args) throws Exception {
		reg();
	}
	
	private static AuthToken getAuthToken() throws Exception {
		return AuthService.login("nardevuser1", "nardevuser2").getToken();
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
				"DomainCluster", 
				"DomainClusterSearchResult"
		};
		//wc.requestModuleOwnership(module);
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
		//System.out.println(wc.releaseModule(module));
	}

}
