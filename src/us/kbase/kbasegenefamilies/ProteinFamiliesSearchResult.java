
package us.kbase.kbasegenefamilies;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: ProteinFamiliesSearchResult</p>
 * <pre>
 * pfsr_ref parent_ref - optional reference to parent cluster search results
 * mapping<genome_ref, ProteinFamilyAnnotation> genomes - genomes that user passed as input data for protein family search
 * mapping<genome_ref, pfa_ref> genome_refs - genome references in case we don't want to store genomes themselves
 * mapping<pfm_ref, ProteinFamilyCluster> pf_clusters - clusters constructed based on query_genomes plus genomes from parent object
 * mapping<pfm_ref, pfc_ref> pf_cluster_refs - references to clusters in case we don't want to store clusters themselves
 * @optional parent_ref
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "parent_ref",
    "genomes",
    "genome_refs",
    "pf_clusters",
    "pf_cluster_refs"
})
public class ProteinFamiliesSearchResult {

    @JsonProperty("parent_ref")
    private java.lang.String parentRef;
    @JsonProperty("genomes")
    private Map<String, ProteinFamilyAnnotation> genomes;
    @JsonProperty("genome_refs")
    private Map<String, String> genomeRefs;
    @JsonProperty("pf_clusters")
    private Map<String, ProteinFamilyCluster> pfClusters;
    @JsonProperty("pf_cluster_refs")
    private Map<String, String> pfClusterRefs;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("parent_ref")
    public java.lang.String getParentRef() {
        return parentRef;
    }

    @JsonProperty("parent_ref")
    public void setParentRef(java.lang.String parentRef) {
        this.parentRef = parentRef;
    }

    public ProteinFamiliesSearchResult withParentRef(java.lang.String parentRef) {
        this.parentRef = parentRef;
        return this;
    }

    @JsonProperty("genomes")
    public Map<String, ProteinFamilyAnnotation> getGenomes() {
        return genomes;
    }

    @JsonProperty("genomes")
    public void setGenomes(Map<String, ProteinFamilyAnnotation> genomes) {
        this.genomes = genomes;
    }

    public ProteinFamiliesSearchResult withGenomes(Map<String, ProteinFamilyAnnotation> genomes) {
        this.genomes = genomes;
        return this;
    }

    @JsonProperty("genome_refs")
    public Map<String, String> getGenomeRefs() {
        return genomeRefs;
    }

    @JsonProperty("genome_refs")
    public void setGenomeRefs(Map<String, String> genomeRefs) {
        this.genomeRefs = genomeRefs;
    }

    public ProteinFamiliesSearchResult withGenomeRefs(Map<String, String> genomeRefs) {
        this.genomeRefs = genomeRefs;
        return this;
    }

    @JsonProperty("pf_clusters")
    public Map<String, ProteinFamilyCluster> getPfClusters() {
        return pfClusters;
    }

    @JsonProperty("pf_clusters")
    public void setPfClusters(Map<String, ProteinFamilyCluster> pfClusters) {
        this.pfClusters = pfClusters;
    }

    public ProteinFamiliesSearchResult withPfClusters(Map<String, ProteinFamilyCluster> pfClusters) {
        this.pfClusters = pfClusters;
        return this;
    }

    @JsonProperty("pf_cluster_refs")
    public Map<String, String> getPfClusterRefs() {
        return pfClusterRefs;
    }

    @JsonProperty("pf_cluster_refs")
    public void setPfClusterRefs(Map<String, String> pfClusterRefs) {
        this.pfClusterRefs = pfClusterRefs;
    }

    public ProteinFamiliesSearchResult withPfClusterRefs(Map<String, String> pfClusterRefs) {
        this.pfClusterRefs = pfClusterRefs;
        return this;
    }

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public java.lang.String toString() {
        return ((((((((((((("ProteinFamiliesSearchResult"+" [parentRef=")+ parentRef)+", genomes=")+ genomes)+", genomeRefs=")+ genomeRefs)+", pfClusters=")+ pfClusters)+", pfClusterRefs=")+ pfClusterRefs)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
