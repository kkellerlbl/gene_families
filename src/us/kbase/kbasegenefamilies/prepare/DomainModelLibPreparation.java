package us.kbase.kbasegenefamilies.prepare;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.Tuple3;
import us.kbase.common.service.UObject;
import us.kbase.kbasegenefamilies.DomainModel;
import us.kbase.shock.client.BasicShockClient;
import us.kbase.shock.client.ShockACLType;
import us.kbase.shock.client.ShockNode;
import us.kbase.shock.client.ShockNodeId;

public class DomainModelLibPreparation {
    private static final String shockUrl = "https://kbase.us/services/shock-api/";
    private static final String libFolder = "/Users/rsutormin/Programs/eclipse/workspace/gene_families/dbs/";
    private static final String[] pfamLibs = {"Cog", "Smart"};
    private static final String[] hmmLibs = {"Pfam-A.hmm", "TIGRFAMs_14.0_HMM.LIB"};
	
    public static void main(String[] args) throws Exception {
	for (String lib : pfamLibs)
	    parseCdd(lib);
	for (String lib : hmmLibs)
	    parseHmm(lib);
    }
	
    private static Map<String, String> prepareShockNodes(String libName, String token) throws Exception {
	File dir = new File(libFolder);
	Map<String, String> ret = new TreeMap<String, String>();
	for (File f : dir.listFiles()) {
	    if (!f.isFile())
		continue;
	    if (!f.getName().startsWith(libName))
		continue;
	    AuthToken auth = new AuthToken(token);
	    BasicShockClient client = new BasicShockClient(new URL(shockUrl), auth);
	    InputStream is = new BufferedInputStream(new FileInputStream(f));
	    ShockNode sn = client.addNode(new TreeMap<String, Object>(), is, f.getName(), "JSON");
	    String shockNodeId = sn.getId().getId();
	    String user = auth.getClientId();
	    client.removeFromNodeAcl(sn.getId(), Arrays.asList(user), new ShockACLType(ShockACLType.READ));
	    ret.put(f.getName(), shockNodeId);
	    System.out.println(f.getName() + "\t" + shockNodeId);
	}
	new ObjectMapper().writeValue(new File(dir, "files_" + libName + ".json"), ret);
	return ret;
    }
	
    private static List<DomainModel> parseHmm(String libName) throws Exception {
	List<DomainModel> ret = new ArrayList<DomainModel>();
	BufferedReader br = new BufferedReader(new FileReader(new File(libFolder, libName)));
	String acc = null;
	String name = null;
	String desc = null;
	Integer leng = null;
	for (int lnum = 0;; lnum++) {
	    String l = br.readLine();
	    if (l == null)
		break;
	    if (l.length() < 6)
		continue;
	    String key = l.substring(0, 6).trim();
	    String value = l.substring(6).trim();
	    if (key.equals("ACC")) {
		acc = value;
	    } else if (key.equals("NAME")) {
		name = value;
	    } else if (key.equals("DESC")) {
		desc = value;
	    } else if (key.equals("LENG")) {
		leng = Integer.parseInt(value);
	    } else if (key.equals("HMM")) {
		if (acc == null || desc == null || leng == null) {
		    br.close();
		    throw new IllegalStateException("Wrong line in [" + libName + ":" + lnum + "]: " + l);
		}
		ret.add(new DomainModel().withAccession(acc).withLength((long)leng)
			.withDescription(desc).withModelType("PSSM").withName(name));
		acc = null;
		name = null;
		desc = null;
		leng = null;
				
	    }
	}
	br.close();
	UObject.getMapper().writeValue(new File(libFolder, "domains_" + libName + ".json"), ret);
	return ret;
    }

    private static List<DomainModel> parseCdd(String libName) throws Exception {
	String prefix = libName.toLowerCase();
	List<DomainModel> ret = new ArrayList<DomainModel>();
	BufferedReader br = new BufferedReader(new FileReader(new File(libFolder, "cddid.tbl")));
	while (true) {
	    String l = br.readLine();
	    if (l == null)
		break;
	    String[] parts = l.split(Pattern.quote("\t"));
	    String inner = parts[0];
	    String acc = parts[1];
	    if (!acc.toLowerCase().startsWith(prefix))
		continue;
	    String name = parts[2];
	    String desc = parts[3];
	    int leng = Integer.parseInt(parts[4]);
	    ret.add(new DomainModel().withAccession(acc).withCddId(inner).withLength((long)leng)
		    .withDescription(desc).withModelType("PSSM").withName(name));
	}
	br.close();
	UObject.getMapper().writeValue(new File(libFolder, "domains_" + libName + ".json"), ret);
	return ret;
    }
	
    private static String token() throws Exception {
	return token(props(new File("config_prod.cfg")));
    }
	
    private static String token(Properties props) throws Exception {
	String user = props.getProperty("user");
	String password = props.getProperty("password");
	if (user == null && password == null)
	    return null;
	return AuthService.login(user, password).getToken().toString();
    }

    private static Properties props(File configFile) throws Exception {
	Properties props = new Properties();
	if (configFile.exists()) {
	    InputStream is = new FileInputStream(configFile);
	    props.load(is);
	    is.close();
	}
	return props;
    }

    private static void checkShockNodeIsPublic(String shockNodeId, OutputStream os) throws Exception {
	try {
	    AuthToken token = new AuthToken(token(props(new File("config_dev.cfg"))));
	    BasicShockClient client = new BasicShockClient(new URL(shockUrl), token);
	    client.getFile(new ShockNodeId(shockNodeId), os);
	    os.close();
	    System.out.println("Shock node was read");
	} catch (Exception ex) {
	    System.out.println("Error reading shock node: " + ex.getMessage());
	}
    }

    private static void removeShockNode(String shockNodeId) throws Exception {
	AuthToken token = new AuthToken(token());
	BasicShockClient client = new BasicShockClient(new URL(shockUrl), token);
	client.deleteNode(new ShockNodeId(shockNodeId));
    }
}
