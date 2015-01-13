package us.kbase.kbasegenefamilies.prepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.Tuple2;
import us.kbase.common.utils.FastaWriter;
import us.kbase.kbasegenefamilies.util.Utils;
import us.kbase.kbasegenomes.Feature;
import us.kbase.kbasegenomes.Genome;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.WorkspaceClient;

public class GenomeExport {
	private static final String wsUrl = "https://kbase.us/services/ws/";
	private static final String genomeWsName = "KBasePublicGenomesV4";
	private static final String genomeWsType = "KBaseGenomes.Genome";

	public static void main(String[] args) throws Exception {
		File rootDir = args.length == 0 ? new File(".") : new File(args[0]);
		File listFile = new File(rootDir, "svd_gnm_id_lst.txt");
		Set<String> processedGenomes = loadProcessedGenomes(listFile);
		Properties props = props(new File("config.cfg"));
		WorkspaceClient client = client(props);
		List<Tuple2<String, String>> refNames = Utils.listAllObjectsRefAndName(client, genomeWsName, genomeWsType);
		Collections.sort(refNames, new Comparator<Tuple2<String, String>>() {
			@Override
			public int compare(Tuple2<String, String> o1, Tuple2<String, String> o2) {
				try {
					int n1 = Integer.parseInt(o1.getE2().substring(5));
					int n2 = Integer.parseInt(o2.getE2().substring(5));
					return Integer.valueOf(n1).compareTo(n2);
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					return o1.getE2().compareTo(o2.getE2());
				}
			}
		});
		System.out.println("There are " + refNames.size() + " public genomes in '" + genomeWsName + "' workspace");
		for (int genomePos = 0; genomePos < refNames.size(); genomePos++) {
			long time = System.currentTimeMillis();
			Tuple2<String, String> refAndName = refNames.get(genomePos);
			String genomeRef = refAndName.getE1();
			String genomeObjectName = refAndName.getE2();
			if (processedGenomes.contains(genomeObjectName))
				continue;
			Genome genome = null;
			try {
				genome = client.getObjects(Arrays.asList(
						new ObjectIdentity().withRef(genomeRef))).get(0).getData().asClassInstance(Genome.class);
			} catch (Exception ex) {
				System.err.println("Error downloading genome " + genomeObjectName + " (ref=" + genomeRef + "):");
				ex.printStackTrace();
				continue;
			}
			String domain = genome.getDomain();
			if (domain == null || domain.isEmpty()) {
				domain = "Unknown";
			}
			try {
				File dir = new File(rootDir, domain);
				if (!dir.exists())
					dir.mkdirs();
				exportFasta(genomeObjectName, genome, dir);
				time = System.currentTimeMillis() - time;
				System.out.println("" + genomePos + "\t" + genomeObjectName + "\t" + genome.getScientificName() + 
						"\t" + domain + "\t" + time + " ms.");
				putGenomeIntoProcessedList(listFile, genomeObjectName);
			} catch (Exception ex) {
				System.err.println("Error exporting genome " + genome.getScientificName() + " (" + genomeObjectName + "):");
				ex.printStackTrace();
			}
		}
	}
	
	private static Set<String> loadProcessedGenomes(File processedGenomesFile) throws Exception {
		Set<String> processedGenomes = new LinkedHashSet<String>();
		if (processedGenomesFile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(processedGenomesFile));
			while (true) {
				String l = br.readLine();
				if (l == null)
					break;
				if (l.isEmpty())
					continue;
				processedGenomes.add(l);
			}
			br.close();
		}
		return processedGenomes;
	}
	
	private static void putGenomeIntoProcessedList(File processedGenomesFile,
			String genomeId) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(processedGenomesFile, true));
		pw.println(genomeId);
		pw.close();
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

	private static WorkspaceClient client(Properties props) throws Exception {
		return client(token(props));
	}
	
	private static WorkspaceClient client(String token) throws Exception {
		WorkspaceClient client;
		if (token == null) {
			client = new WorkspaceClient(new URL(wsUrl));
		} else {
			client = new WorkspaceClient(new URL(wsUrl), new AuthToken(token));
		}
		client.setAuthAllowedForHttp(true);
		return client;
	}
	
	private static void exportFasta(String genomeId, Genome genome, File targetDir) throws Exception {
		if (genomeId.startsWith("kb|"))
			genomeId = genomeId.substring(3);
		File targetFasta = new File(targetDir, genomeId + ".fa");
		FastaWriter fw = new FastaWriter(targetFasta);
		for (int pos = 0; pos < genome.getFeatures().size(); pos++) {
			Feature feat = genome.getFeatures().get(pos);
			String seq = feat.getProteinTranslation();
			if (feat.getLocation().size() != 1)
				continue;
			if (seq != null && !seq.isEmpty())
				fw.write("" + pos, seq);
		}
		fw.close();
	}
}
