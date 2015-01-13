package us.kbase.kbasegenefamilies.prepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import us.kbase.auth.AuthException;
import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.auth.TokenFormatException;
import us.kbase.common.service.Tuple2;
import us.kbase.common.service.Tuple3;
import us.kbase.common.service.Tuple4;
import us.kbase.common.service.UObject;
import us.kbase.common.service.UnauthorizedException;
import us.kbase.kbasegenefamilies.util.Utils;
import us.kbase.workspace.WorkspaceClient;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KBaseGenomeIdMapping {
	public static final String SEARCH_URL = "http://kbase.us/services/search/getResults";
	private static final String ncbiNamesDmpFilePath = "/Users/rsutormin/Work/2013-12-14_ncbi_tax/names.dmp";
	private static final String targetJsonFile = "kbase_taxids.json";
	private static final String wsUrl = "http://dev04.berkeley.kbase.us:7058";  // "https://kbase.us/services/ws/";
	private static final String genomeWsName = "KBasePublicGenomesLoad";
	private static final String genomeWsType = "KBaseGenomes.Genome";

	private static final Map<Integer, Integer> taxIdOldToNew = new TreeMap<Integer, Integer>();
	
	static {
		taxIdOldToNew.put(647653, 862751);
		taxIdOldToNew.put(347254, 1081659);
		taxIdOldToNew.put(216593, 574521);
		taxIdOldToNew.put(12367, 10857);
		taxIdOldToNew.put(702448, 791166);
		taxIdOldToNew.put(320386, 412022);
		taxIdOldToNew.put(269483, 482957);
		taxIdOldToNew.put(314269, 287752);
		taxIdOldToNew.put(439483, 929558);
		taxIdOldToNew.put(28359, 31757);
		taxIdOldToNew.put(1027845, 1112254);
		taxIdOldToNew.put(1115514, 630626);
		taxIdOldToNew.put(272994, 1016998);
		taxIdOldToNew.put(1027843, 1112267);
		taxIdOldToNew.put(634178, 568819);
		taxIdOldToNew.put(313593, 360293);
		taxIdOldToNew.put(1076650, 666685);
		taxIdOldToNew.put(1027844, 1112250);
		taxIdOldToNew.put(320387, 412021);
		taxIdOldToNew.put(1126229, 1003195);
		taxIdOldToNew.put(320838, 215167);
		taxIdOldToNew.put(1027846, 1112252);
	}
	
	public static void main(String[] args) throws Exception {
		prepareKBaseTaxIdMappingToKBaseIdAndScientificName();
	}
	
	private static void prepareKBaseTaxIdMappingToKBaseIdAndScientificName() 
			throws Exception {
		WorkspaceClient client = client(props(new File("config.cfg")));
		Set<String> genomeObjectNames = new HashSet<String>();
		for (Tuple2<String, String> refAndName : Utils.listAllObjectsRefAndName(client, genomeWsName, genomeWsType)) {
			genomeObjectNames.add(refAndName.getE2());
		}
		Map<Integer, String> taxIds = loadTaxNames();
		Map<Integer, Tuple3<String, String, String>> taxIdMap = new TreeMap<Integer, Tuple3<String, String, String>>();

		boolean done = false;
		int itemsPerPage = 13000;
		int pageNum = 1;

		/* Pull all pages of results from the Search service
		 * The last page will either be empty, or contain less than the 'itemsPerPage' limit.
		 */
		while (!done) {
			String query = "itemsPerPage=" + itemsPerPage + "&page=" + pageNum + "&q=*&category=genomes";

			final BufferedReader br = new BufferedReader(new InputStreamReader(new URL(SEARCH_URL + "?" + query).openStream()));
			SearchResult result = new ObjectMapper().readValue(br, SearchResult.class);
			br.close();

			Map<Integer, Integer> tax2ver = new TreeMap<Integer, Integer>();

			for (GenomeItem genome : result.getItems()) {
				
				String taxId = genome.getGenomeSourceId();
				String[] taxIdAndVersion = taxId.split("\\.");
				taxId = taxIdAndVersion[0];
				int taxIdNum;
				try {
					taxIdNum = Integer.parseInt(taxId);
				} catch (NumberFormatException ex) {
					continue;
				}
				if (taxIdOldToNew.containsKey(taxIdNum))
					taxIdNum = taxIdOldToNew.get(taxIdNum);
				
				int newVersion = taxIdAndVersion.length == 1 ? -1 : Integer.parseInt(taxIdAndVersion[1]);
				if (tax2ver.containsKey(taxIdNum) && tax2ver.get(taxIdNum) >= newVersion)
					continue;
				tax2ver.put(taxIdNum, newVersion);

				if (!taxIds.containsKey(taxIdNum)) {
					System.out.println("Tax id " + taxIdNum + " is not found (" + genome.getScientificName() + ")");
					continue;
				}
				
				taxIdMap.put(taxIdNum, 
						new Tuple3<String, String, String>()
						.withE1(genome.getGenomeId())
						.withE2(genome.getScientificName())
						.withE3(genome.getDomain()));
			}
			if (result.getItemCount() < itemsPerPage)
				done = true;
			else
				pageNum++;
		}
		
		List<Tuple4<Integer, String, String, String>> taxIdKBaseIdGenomeNameDomainType = 
				new ArrayList<Tuple4<Integer, String, String, String>>();
		for (int taxId : taxIdMap.keySet()) {
			Tuple3<String, String, String> value = taxIdMap.get(taxId);
			String kbaseId = value.getE1();
			String genomeName = value.getE2();
			if (!genomeObjectNames.contains(kbaseId)) {
				System.out.println("Genome [" + genomeName + "] (" + kbaseId + ") is not stored in workspace");
				continue;
			}
			String domain = value.getE3();
			String domainChar;
			if (domain == null || domain.isEmpty()) {
				domainChar = " ";
			} else if (domain.equals("Bacteria")) {
				domainChar = "B";
			} else if (domain.equals("Eukaryota")) {
				domainChar = "E";
			} else if (domain.equals("Viruses")) {
				domainChar = "V";
			} else if (domain.equals("Archaea")) {
				domainChar = "A";
			} else {
				System.out.println("Unknown genome domain type: " + domain);
				domainChar = domain.substring(0, 1);
			}
			taxIdKBaseIdGenomeNameDomainType.add(new Tuple4<Integer, String, String, String>()
					.withE1(taxId).withE2(kbaseId).withE3(genomeName).withE4(domainChar));
		}
		System.out.println("Count: " + taxIdKBaseIdGenomeNameDomainType.size());
		
		File f = new File(targetJsonFile);
		UObject.getMapper().writeValue(f, taxIdKBaseIdGenomeNameDomainType);
	}

	public static Map<Integer, String> loadTaxNames() throws IOException {
		Pattern div = Pattern.compile(Pattern.quote("\t|\t")); 
		Map<Integer, String> nodeMap = new HashMap<Integer, String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(ncbiNamesDmpFilePath)));
		try {
			while (true) {
				String l = br.readLine();
				if (l == null)
					break;
				if (l.trim().length() == 0)
					continue;
				if (l.endsWith("\t|"))
					l = l.substring(0, l.length() - 2);
				String[] parts = div.split(l);
				if (parts.length != 4)
					throw new IllegalStateException("Wrong line format: [" + l + "]");
				if (!parts[3].equals("scientific name"))
					continue;
				int nodeId = Integer.parseInt(parts[0]);
				nodeMap.put(nodeId, parts[1]);
			}
			return nodeMap;
		} finally {
			br.close();
		}
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

	public static class SearchResult {
		public int getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}

		public Facets getFacets() {
			return facets;
		}

		public void setFacets(Facets facets) {
			this.facets = facets;
		}

		public int getItemCount() {
			return itemCount;
		}

		public void setItemCount(int itemCount) {
			this.itemCount = itemCount;
		}

		public List<GenomeItem> getItems() {
			return items;
		}

		public void setItems(List<GenomeItem> items) {
			this.items = items;
		}

		public int getItemsPerPage() {
			return itemsPerPage;
		}

		public void setItemsPerPage(int itemsPerPage) {
			this.itemsPerPage = itemsPerPage;
		}

		public Navigation getNavigation() {
			return navigation;
		}

		public void setNavigation(Navigation navigation) {
			this.navigation = navigation;
		}

		public long getTotalResults() {
			return totalResults;
		}

		public void setTotalResults(long totalResults) {
			this.totalResults = totalResults;
		}

		private int currentPage;
		private Facets facets;
		private int itemCount;
		private List<GenomeItem> items;
		private int itemsPerPage;
		private Navigation navigation;
		private long totalResults;
	}

	public static class GenomeItem {

		private boolean complete;
		private String domain;
		@JsonProperty("gc_content")
		private float gcContent;
		@JsonProperty("genome_dna_size")
		private long genomeDnaSize;
		@JsonProperty("genome_id")
		private String genomeId;
		@JsonProperty("genome_source_id")
		private String genomeSourceId;
		@JsonProperty("genome_source")
		private String genomeSource;	
		@JsonProperty("num_contigs")
		private int numContigs;
		@JsonProperty("object_type")
		private String objectType;
		@JsonProperty("scientific_name")
		private String scientificName;
		private String taxonomy;
		@JsonProperty("object_id")
		private String objectId;
	    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

		public String getGenomeSource() {
			return genomeSource;
		}
		public void setGenomeSource(String genomeSource) {
			this.genomeSource = genomeSource;
		}
		public boolean isComplete() {
			return complete;
		}
		public void setComplete(boolean complete) {
			this.complete = complete;
		}
		public String getDomain() {
			return domain;
		}
		public void setDomain(String domain) {
			this.domain = domain;
		}
		public float getGcContent() {
			return gcContent;
		}
		public void setGcContent(float gcContent) {
			this.gcContent = gcContent;
		}
		public long getGenomeDnaSize() {
			return genomeDnaSize;
		}
		public void setGenomeDnaSize(long genomeDnaSize) {
			this.genomeDnaSize = genomeDnaSize;
		}
		public String getGenomeId() {
			return genomeId;
		}
		public void setGenomeId(String genomeId) {
			this.genomeId = genomeId;
		}
		public String getGenomeSourceId() {
			return genomeSourceId;
		}
		public void setGenomeSourceId(String genomeSourceId) {
			this.genomeSourceId = genomeSourceId;
		}
		public int getNumContigs() {
			return numContigs;
		}
		public void setNumContigs(int numContigs) {
			this.numContigs = numContigs;
		}
		public String getObjectType() {
			return objectType;
		}
		public void setObjectType(String objectType) {
			this.objectType = objectType;
		}
		public String getScientificName() {
			return scientificName;
		}
		public void setScientificName(String scientificName) {
			this.scientificName = scientificName;
		}
		public String getTaxonomy() {
			return taxonomy;
		}
		public void setTaxonomy(String taxonomy) {
			this.taxonomy = taxonomy;
		}	
	    @JsonAnyGetter
	    public Map<String, Object> getAdditionalProperties() {
	        return this.additionalProperties;
	    }
	    @JsonAnySetter
	    public void setAdditionalProperties(String name, Object value) {
	        this.additionalProperties.put(name, value);
	    }
	}

	public static class Facets {
		public List<?> getComplete() {
			return complete;
		}
		public void setComplete(List<?> complete) {
			this.complete = complete;
		}
		public List<?> getDomain() {
			return domain;
		}
		public void setDomain(List<?> domain) {
			this.domain = domain;
		}
		private List<?> complete;
		private List<?> domain;
	}

	public static class Navigation {
		private String first;
		private String last;
		private String next;
		private String self;
		private String previous;

		public String getFirst() {
			return first;
		}
		public void setFirst(String first) {
			this.first = first;
		}
		public String getLast() {
			return last;
		}
		public void setLast(String last) {
			this.last = last;
		}
		public String getNext() {
			return next;
		}
		public void setNext(String next) {
			this.next = next;
		}
		public String getSelf() {
			return self;
		}
		public void setSelf(String self) {
			this.self = self;
		}
		public String getPrevious() {
			return previous;
		}
		public void setPrevious(String previous) {
			this.previous = previous;
		}
	}
}
