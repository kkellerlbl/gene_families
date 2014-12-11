
package us.kbase.kbasegenefamilies;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import us.kbase.kbasetrees.MSA;
import us.kbase.kbasetrees.Tree;


/**
 * <p>Original spec-file type: DomainClusterSearchResult</p>
 * <pre>
 * dcsr_ref parent_ref - optional reference to parent domain clusters search results
 * dms_ref used_dms_ref - domain models used for search
 * mapping<genome_ref, DomainAnnotation> annotations - found domains in genomes that user 
 *         defined as input data for domain search
 * mapping<genome_ref, DomainAlignment> alignments - alignments for found domains in genomes 
 *         that user defined as input data for domain search
 * mapping<genome_ref, domain_annotation_ref> annotation_refs - domain annotation references 
 *         in case we don't want to store annotations and alignments inside result object
 * mapping<domain_accession, DomainCluster> domain_clusters - clusters constructed based on 
 *         query_genomes plus genomes from parent object
 * mapping<domain_accession, domain_cluster_ref> domain_cluster_refs - references to clusters 
 *         in case we don't want to store these clusters inside search result object
 * mapping<domain_accession, KBaseTrees.MSA> msas - multiple alignment objects where all domain sequences 
 *         are collected (keys in these MSA objects are constructed according to such pattern: 
 *         <genome_ref>_<feature_id>_<start_in_feature>), in case this field is not set or has
 *         empty mapping msa_refs field should be used
 * mapping<domain_accession, ws_alignment_id> msa_refs - references to multiple alignment objects 
 *         where all domain sequences are collected (keys in these MSA objects are constructed 
 *         according to such pattern: <genome_ref>_<feature_id>_<start_in_feature>)
 * mapping<domain_accession, KBaseTrees.Tree> trees - trees built for MSAs stored in msas field
 * mapping<domain_accession, ws_tree_id> tree_refs - trees built for MSAs stored in msa_refs field
 * @optional parent_ref
 * @optional used_dms_ref
 * @optional annotations
 * @optional alignments
 * @optional annotation_refs
 * @optional domain_clusters
 * @optional domain_cluster_refs
 * @optional msas
 * @optional msa_refs
 * @optional trees
 * @optional tree_refs
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "parent_ref",
    "used_dms_ref",
    "annotations",
    "alignments",
    "annotation_refs",
    "genome_statistics",
    "domain_clusters",
    "domain_cluster_refs",
    "msas",
    "msa_refs",
    "domain_cluster_statistics",
    "trees",
    "tree_refs"
})
public class DomainClusterSearchResult {

    @JsonProperty("parent_ref")
    private java.lang.String parentRef;
    @JsonProperty("used_dms_ref")
    private java.lang.String usedDmsRef;
    @JsonProperty("annotations")
    private Map<String, DomainAnnotation> annotations;
    @JsonProperty("alignments")
    private Map<String, DomainAlignments> alignments;
    @JsonProperty("annotation_refs")
    private Map<String, String> annotationRefs;
    @JsonProperty("genome_statistics")
    private Map<String, GenomeStat> genomeStatistics;
    @JsonProperty("domain_clusters")
    private Map<String, DomainCluster> domainClusters;
    @JsonProperty("domain_cluster_refs")
    private Map<String, String> domainClusterRefs;
    @JsonProperty("msas")
    private Map<String, MSA> msas;
    @JsonProperty("msa_refs")
    private Map<String, String> msaRefs;
    @JsonProperty("domain_cluster_statistics")
    private Map<String, DomainClusterStat> domainClusterStatistics;
    @JsonProperty("trees")
    private Map<String, Tree> trees;
    @JsonProperty("tree_refs")
    private Map<String, String> treeRefs;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("parent_ref")
    public java.lang.String getParentRef() {
        return parentRef;
    }

    @JsonProperty("parent_ref")
    public void setParentRef(java.lang.String parentRef) {
        this.parentRef = parentRef;
    }

    public DomainClusterSearchResult withParentRef(java.lang.String parentRef) {
        this.parentRef = parentRef;
        return this;
    }

    @JsonProperty("used_dms_ref")
    public java.lang.String getUsedDmsRef() {
        return usedDmsRef;
    }

    @JsonProperty("used_dms_ref")
    public void setUsedDmsRef(java.lang.String usedDmsRef) {
        this.usedDmsRef = usedDmsRef;
    }

    public DomainClusterSearchResult withUsedDmsRef(java.lang.String usedDmsRef) {
        this.usedDmsRef = usedDmsRef;
        return this;
    }

    @JsonProperty("annotations")
    public Map<String, DomainAnnotation> getAnnotations() {
        return annotations;
    }

    @JsonProperty("annotations")
    public void setAnnotations(Map<String, DomainAnnotation> annotations) {
        this.annotations = annotations;
    }

    public DomainClusterSearchResult withAnnotations(Map<String, DomainAnnotation> annotations) {
        this.annotations = annotations;
        return this;
    }

    @JsonProperty("alignments")
    public Map<String, DomainAlignments> getAlignments() {
        return alignments;
    }

    @JsonProperty("alignments")
    public void setAlignments(Map<String, DomainAlignments> alignments) {
        this.alignments = alignments;
    }

    public DomainClusterSearchResult withAlignments(Map<String, DomainAlignments> alignments) {
        this.alignments = alignments;
        return this;
    }

    @JsonProperty("annotation_refs")
    public Map<String, String> getAnnotationRefs() {
        return annotationRefs;
    }

    @JsonProperty("annotation_refs")
    public void setAnnotationRefs(Map<String, String> annotationRefs) {
        this.annotationRefs = annotationRefs;
    }

    public DomainClusterSearchResult withAnnotationRefs(Map<String, String> annotationRefs) {
        this.annotationRefs = annotationRefs;
        return this;
    }

    @JsonProperty("genome_statistics")
    public Map<String, GenomeStat> getGenomeStatistics() {
        return genomeStatistics;
    }

    @JsonProperty("genome_statistics")
    public void setGenomeStatistics(Map<String, GenomeStat> genomeStatistics) {
        this.genomeStatistics = genomeStatistics;
    }

    public DomainClusterSearchResult withGenomeStatistics(Map<String, GenomeStat> genomeStatistics) {
        this.genomeStatistics = genomeStatistics;
        return this;
    }

    @JsonProperty("domain_clusters")
    public Map<String, DomainCluster> getDomainClusters() {
        return domainClusters;
    }

    @JsonProperty("domain_clusters")
    public void setDomainClusters(Map<String, DomainCluster> domainClusters) {
        this.domainClusters = domainClusters;
    }

    public DomainClusterSearchResult withDomainClusters(Map<String, DomainCluster> domainClusters) {
        this.domainClusters = domainClusters;
        return this;
    }

    @JsonProperty("domain_cluster_refs")
    public Map<String, String> getDomainClusterRefs() {
        return domainClusterRefs;
    }

    @JsonProperty("domain_cluster_refs")
    public void setDomainClusterRefs(Map<String, String> domainClusterRefs) {
        this.domainClusterRefs = domainClusterRefs;
    }

    public DomainClusterSearchResult withDomainClusterRefs(Map<String, String> domainClusterRefs) {
        this.domainClusterRefs = domainClusterRefs;
        return this;
    }

    @JsonProperty("msas")
    public Map<String, MSA> getMsas() {
        return msas;
    }

    @JsonProperty("msas")
    public void setMsas(Map<String, MSA> msas) {
        this.msas = msas;
    }

    public DomainClusterSearchResult withMsas(Map<String, MSA> msas) {
        this.msas = msas;
        return this;
    }

    @JsonProperty("msa_refs")
    public Map<String, String> getMsaRefs() {
        return msaRefs;
    }

    @JsonProperty("msa_refs")
    public void setMsaRefs(Map<String, String> msaRefs) {
        this.msaRefs = msaRefs;
    }

    public DomainClusterSearchResult withMsaRefs(Map<String, String> msaRefs) {
        this.msaRefs = msaRefs;
        return this;
    }

    @JsonProperty("domain_cluster_statistics")
    public Map<String, DomainClusterStat> getDomainClusterStatistics() {
        return domainClusterStatistics;
    }

    @JsonProperty("domain_cluster_statistics")
    public void setDomainClusterStatistics(Map<String, DomainClusterStat> domainClusterStatistics) {
        this.domainClusterStatistics = domainClusterStatistics;
    }

    public DomainClusterSearchResult withDomainClusterStatistics(Map<String, DomainClusterStat> domainClusterStatistics) {
        this.domainClusterStatistics = domainClusterStatistics;
        return this;
    }

    @JsonProperty("trees")
    public Map<String, Tree> getTrees() {
        return trees;
    }

    @JsonProperty("trees")
    public void setTrees(Map<String, Tree> trees) {
        this.trees = trees;
    }

    public DomainClusterSearchResult withTrees(Map<String, Tree> trees) {
        this.trees = trees;
        return this;
    }

    @JsonProperty("tree_refs")
    public Map<String, String> getTreeRefs() {
        return treeRefs;
    }

    @JsonProperty("tree_refs")
    public void setTreeRefs(Map<String, String> treeRefs) {
        this.treeRefs = treeRefs;
    }

    public DomainClusterSearchResult withTreeRefs(Map<String, String> treeRefs) {
        this.treeRefs = treeRefs;
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
        return ((((((((((((((((((((((((((((("DomainClusterSearchResult"+" [parentRef=")+ parentRef)+", usedDmsRef=")+ usedDmsRef)+", annotations=")+ annotations)+", alignments=")+ alignments)+", annotationRefs=")+ annotationRefs)+", genomeStatistics=")+ genomeStatistics)+", domainClusters=")+ domainClusters)+", domainClusterRefs=")+ domainClusterRefs)+", msas=")+ msas)+", msaRefs=")+ msaRefs)+", domainClusterStatistics=")+ domainClusterStatistics)+", trees=")+ trees)+", treeRefs=")+ treeRefs)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
