package us.kbase.kbasegenefamilies.prepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import us.kbase.auth.AuthException;
import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.auth.TokenFormatException;
import us.kbase.common.service.Tuple2;
import us.kbase.common.service.Tuple5;
import us.kbase.common.service.UObject;
import us.kbase.common.service.UnauthorizedException;
import us.kbase.kbasegenefamilies.DomainAlignments;
import us.kbase.kbasegenefamilies.DomainAnnotation;
import us.kbase.kbasegenefamilies.DomainModelSet;
import us.kbase.kbasegenefamilies.util.Utils;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.SubObjectIdentity;
import us.kbase.workspace.WorkspaceClient;

public class DomainClusterPreparation {
	private static final String wsUrl = "http://dev04.berkeley.kbase.us:7058";  // "https://kbase.us/services/ws/";
	private static final String genomeWsName = "KBasePublicGenomesLoad";
	private static final String genomeWsType = "KBaseGenomes.Genome";
	private static final String domainWsName = "KBasePublicGeneDomains";
	private static final String defaultDomainSetObjectName = "BacterialProteinDomains.set";
	private static final String domainAnnotationWsType = "KBaseGeneFamilies.DomainAnnotation";
	private static final String domainAlignmentsWsType = "KBaseGeneFamilies.DomainAlignments";

	public static void main(String[] args) throws Exception {
		Properties props = props(new File("config.cfg"));
		File tempDir = new File(get(props, "temp.dir"));
		File annotDir = new File(tempDir, "annotation");
		WorkspaceClient client = client(props);
		Set<String> domainRefs = getRestrictedDomainRefs(client);
		System.out.println("Domain count: " + domainRefs.size());
		Map<String, String> annotNameToRefMap = new TreeMap<String, String>();
		for (Tuple2<String, String> refAndName : Utils.listAllObjectsRefAndName(client, domainWsName, domainAnnotationWsType))
			annotNameToRefMap.put(refAndName.getE2(), refAndName.getE1());
		Map<String, String> alignNameToRefMap = new TreeMap<String, String>();
		for (Tuple2<String, String> refAndName : Utils.listAllObjectsRefAndName(client, domainWsName, domainAlignmentsWsType))
			alignNameToRefMap.put(refAndName.getE2(), refAndName.getE1());
		File clusterDir = new File(tempDir, "clusters");
		if (!clusterDir.exists())
			clusterDir.mkdir();
		Set<String> processedGenomeRefs = new HashSet<String>();
		File processedGenomesFile = new File(clusterDir, "processed_genomes.txt");
		if (processedGenomesFile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(processedGenomesFile));
			while (true) {
				String l = br.readLine();
				if (l == null)
					break;
				if (l.isEmpty())
					continue;
				processedGenomeRefs.add(l);
			}
			br.close();
		}
		for (Tuple2<String, String> refAndName : Utils.listAllObjectsRefAndName(client, genomeWsName, genomeWsType)) {
			String genomeRef = refAndName.getE1();
			if (processedGenomeRefs.contains(genomeRef)) {
				System.out.println("Genome was already processed: " + genomeRef);
				continue;
			}
			String genomeObjectName = refAndName.getE2();
			String genomeAnnotationObjectName = genomeObjectName + ".domains";
			String genomeAlignmentsObjectName = genomeObjectName + ".alignments";
			String annotRef = annotNameToRefMap.get(genomeAnnotationObjectName);
			String alignRef = alignNameToRefMap.get(genomeAlignmentsObjectName);
			if (annotRef == null || alignRef == null)
				continue;
			File f1 = new File(annotDir, "domains_" + annotRef.replace('/', '_') + ".json.gz");
			File f2 = new File(annotDir, "alignments_" + alignRef.replace('/', '_') + ".json.gz");
			if (f1.exists() && f2.exists()) {
				InputStream is = new GZIPInputStream(new FileInputStream(f1));
				DomainAnnotation annot = UObject.getMapper().readValue(is, DomainAnnotation.class);
				InputStream is2 = new GZIPInputStream(new FileInputStream(f2));
				DomainAlignments align = UObject.getMapper().readValue(is2, DomainAlignments.class);
				if (!annot.getGenomeRef().equals(genomeRef))
					throw new IllegalStateException();
				if (!align.getGenomeRef().equals(genomeRef))
					throw new IllegalStateException();
				long time = System.currentTimeMillis();
				Map<String, String> genome = null;
				for (int i = 0; i < 5; i++)
					try {
						genome = client.getObjectSubset(Arrays.asList(new SubObjectIdentity().withRef(genomeRef)
								.withIncluded(Arrays.asList("scientific_name", "domain")))).get(0).getData().asInstance();
					} catch (Exception e) {
						System.err.println(genomeRef + " (" + i + "): " + e.getMessage());
					}
				if (genome == null)
					continue;
				String name = genome.get("scientific_name");
				String domain = genome.get("domain");
				if (domain == null || !(domain.equals("Bacteria") || domain.equals("Archaea"))) {
					time = System.currentTimeMillis() - time;
					putGenomeIntoProcessedList(processedGenomesFile, genomeRef);
					System.out.println("\tGenome [" + genomeObjectName + "] (" + name + "), is skipped cuase domain=[" + domain + "], time=" + time);
					continue;
				}
				int features = 0;
				int domains = 0;
				//typedef tuple<int start_in_feature,int stop_in_feature,float evalue,
				//	float bitscore, float domain_coverage> domain_place;
				//typedef tuple<string feature_id,int feature_start,int feature_stop,int feature_dir,
				//	mapping<domain_model_ref,list<domain_place>>> annotation_element;
				for (Map.Entry<String, List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>>>> contigElements : annot.getData().entrySet()) {
					String contigId = contigElements.getKey();
					for (Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>> element : contigElements.getValue()) {
						String featureId = element.getE1();
						if (annot.getFeatureToContigAndIndex().get(featureId) == null) {
							if (element.getE5().isEmpty())
								continue;
							System.err.println("No feature index for [" + featureId + "] but domain map has size=" + element.getE5().size());
							continue;
						}
						long featureIndex = annot.getFeatureToContigAndIndex().get(featureId).getE2();
						features++;
						for (Map.Entry<String, List<Tuple5<Long, Long, Double, Double, Double>>> domainPlaces : element.getE5().entrySet()) {
							String domainRef = domainPlaces.getKey();
							if (!domainRefs.contains(domainRef))
								continue;
							File clusterFile = new File(clusterDir, "cluster_" + domainRef.replace('/', '_') + ".txt");
							File msaFile = new File(clusterDir, "msa_" + domainRef.replace('/', '_') + ".txt");
							PrintWriter clusterPw = new PrintWriter(new FileWriter(clusterFile, true));
							PrintWriter msaPw = new PrintWriter(new FileWriter(msaFile, true));
							for (Tuple5<Long, Long, Double, Double, Double> domainPlace : domainPlaces.getValue()) {
								String alignedSeq = align.getAlignments().get(domainRef).get(featureId).get("" + domainPlace.getE1());
								clusterPw.println("[*]\t" + contigId + "\t" + featureId + "\t" + featureIndex + "\t" + 
										domainPlace.getE1() + "\t" + domainPlace.getE2() + "\t" + domainPlace.getE3() + "\t" + 
										domainPlace.getE4() + "\t" + domainPlace.getE5());
								msaPw.println("[*]\t" + genomeRef + "\t" + featureId + "\t" + domainPlace.getE1() + "\t" + alignedSeq);
								domains++;
							}
							clusterPw.close();
							msaPw.close();
						}
					}
				}
				time = System.currentTimeMillis() - time;
				putGenomeIntoProcessedList(processedGenomesFile, genomeRef);
				System.out.println("Genome [" + genomeObjectName + "], features=" + features + ", domains=" + domains + ", time=" + time);
			}
		}
	}

	private static void putGenomeIntoProcessedList(File processedGenomesFile,
			String genomeRef) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(processedGenomesFile, true));
		pw.println(genomeRef);
		pw.close();
	}
	
	private static Set<String> getRestrictedDomainRefs(WorkspaceClient client) throws Exception {
		Set<String> ret = new HashSet<String>();
		DomainModelSet rootSet = getObject(client, domainWsName + "/" + defaultDomainSetObjectName, DomainModelSet.class);
		for (String parRef : rootSet.getParentRefs()) {
			DomainModelSet subSet = getObject(client, parRef, DomainModelSet.class);
			if (subSet.getSetName().endsWith(" pfam") || subSet.getSetName().endsWith(" COG"))
				ret.addAll(subSet.getDomainModelRefs());
		}
		return ret;
	}
	
	private static <T> T getObject(WorkspaceClient client, String ref, Class<T> type) throws Exception {
		return client.getObjects(Arrays.asList(new ObjectIdentity().withRef(ref))).get(0).getData().asClassInstance(type);
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
	
	private static Properties props(File configFile)
			throws FileNotFoundException, IOException {
		Properties props = new Properties();
		InputStream is = new FileInputStream(configFile);
		props.load(is);
		is.close();
		return props;
	}

}
