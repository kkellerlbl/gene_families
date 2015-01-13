package us.kbase.kbasegenefamilies.prepare;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenomeIdTaxMapping {
	public static final String SEARCH_URL = "http://kbase.us/services/search/getResults";

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
				
				int taxVersion = taxIdAndVersion.length == 1 ? -1 : Integer.parseInt(taxIdAndVersion[1]);
				System.out.println("Genome: taxid=" + taxIdNum + ", taxver=" + taxVersion + ", " +
						"id=" + genome.getGenomeId() + ", name=" + genome.getScientificName() +
						", domain=" + genome.getDomain());
			}
			if (result.getItemCount() < itemsPerPage)
				done = true;
			else
				pageNum++;
		}
	}

	public static class SearchResult {
		private int currentPage;
		private Facets facets;
		private int itemCount;
		private List<GenomeItem> items;
		private int itemsPerPage;
		private Navigation navigation;
		private long totalResults;
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
		private List<?> complete;
		private List<?> domain;
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
