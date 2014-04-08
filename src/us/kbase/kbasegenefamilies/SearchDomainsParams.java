
package us.kbase.kbasegenefamilies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: search_domains_params</p>
 * <pre>
 * list<genome_ref> genomes - genome list
 * dms_ref dms_ref - set of domain models that will be searched in defined genomes (optional field, you can use clusters_for_extension instead)
 * dsr_ref clusters_for_extension - clusters already constructed for another set of genomes (public ones for example)
 * list<string> domain_types - type list (if empty list all types are used) defining subset of domain models extracted from dms_ref/clusters_for_extension
 * string out_workspace - output workspace
 * string out_result_id - id of resulting object of type DomainSearchResult
 * int is_genome_annotation_stored_outside - default value is 0
 * string genome_annotation_id_prefix - used for genome domain annotation objects id generation ([prefix.]genome_name[.suffix])
 * string genome_annotation_id_suffix - used for genome domain annotation objects id generation ([prefix.]genome_name[.suffix])
 * int is_domain_cluster_data_stored_outside - default value is 0
 * string domain_cluster_data_id_prefix - used for domain cluster objects id generation ([prefix.]domain_name[.suffix])
 * string domain_cluster_data_id_suffix - used for domain cluster objects id generation ([prefix.]domain_name[.suffix])
 * @optional dms_ref
 * @optional clusters_for_extension
 * @optional is_genome_annotation_stored_outside
 * @optional genome_annotation_id_prefix
 * @optional genome_annotation_id_suffix
 * @optional is_domain_cluster_data_stored_outside
 * @optional domain_cluster_data_id_prefix
 * @optional domain_cluster_data_id_suffix
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "genomes",
    "dms_ref",
    "clusters_for_extension",
    "domain_types",
    "out_workspace",
    "out_result_id",
    "is_genome_annotation_stored_outside",
    "genome_annotation_id_prefix",
    "genome_annotation_id_suffix",
    "is_domain_cluster_data_stored_outside",
    "domain_cluster_data_id_prefix",
    "domain_cluster_data_id_suffix"
})
public class SearchDomainsParams {

    @JsonProperty("genomes")
    private List<String> genomes;
    @JsonProperty("dms_ref")
    private java.lang.String dmsRef;
    @JsonProperty("clusters_for_extension")
    private java.lang.String clustersForExtension;
    @JsonProperty("domain_types")
    private List<String> domainTypes;
    @JsonProperty("out_workspace")
    private java.lang.String outWorkspace;
    @JsonProperty("out_result_id")
    private java.lang.String outResultId;
    @JsonProperty("is_genome_annotation_stored_outside")
    private Long isGenomeAnnotationStoredOutside;
    @JsonProperty("genome_annotation_id_prefix")
    private java.lang.String genomeAnnotationIdPrefix;
    @JsonProperty("genome_annotation_id_suffix")
    private java.lang.String genomeAnnotationIdSuffix;
    @JsonProperty("is_domain_cluster_data_stored_outside")
    private Long isDomainClusterDataStoredOutside;
    @JsonProperty("domain_cluster_data_id_prefix")
    private java.lang.String domainClusterDataIdPrefix;
    @JsonProperty("domain_cluster_data_id_suffix")
    private java.lang.String domainClusterDataIdSuffix;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("genomes")
    public List<String> getGenomes() {
        return genomes;
    }

    @JsonProperty("genomes")
    public void setGenomes(List<String> genomes) {
        this.genomes = genomes;
    }

    public SearchDomainsParams withGenomes(List<String> genomes) {
        this.genomes = genomes;
        return this;
    }

    @JsonProperty("dms_ref")
    public java.lang.String getDmsRef() {
        return dmsRef;
    }

    @JsonProperty("dms_ref")
    public void setDmsRef(java.lang.String dmsRef) {
        this.dmsRef = dmsRef;
    }

    public SearchDomainsParams withDmsRef(java.lang.String dmsRef) {
        this.dmsRef = dmsRef;
        return this;
    }

    @JsonProperty("clusters_for_extension")
    public java.lang.String getClustersForExtension() {
        return clustersForExtension;
    }

    @JsonProperty("clusters_for_extension")
    public void setClustersForExtension(java.lang.String clustersForExtension) {
        this.clustersForExtension = clustersForExtension;
    }

    public SearchDomainsParams withClustersForExtension(java.lang.String clustersForExtension) {
        this.clustersForExtension = clustersForExtension;
        return this;
    }

    @JsonProperty("domain_types")
    public List<String> getDomainTypes() {
        return domainTypes;
    }

    @JsonProperty("domain_types")
    public void setDomainTypes(List<String> domainTypes) {
        this.domainTypes = domainTypes;
    }

    public SearchDomainsParams withDomainTypes(List<String> domainTypes) {
        this.domainTypes = domainTypes;
        return this;
    }

    @JsonProperty("out_workspace")
    public java.lang.String getOutWorkspace() {
        return outWorkspace;
    }

    @JsonProperty("out_workspace")
    public void setOutWorkspace(java.lang.String outWorkspace) {
        this.outWorkspace = outWorkspace;
    }

    public SearchDomainsParams withOutWorkspace(java.lang.String outWorkspace) {
        this.outWorkspace = outWorkspace;
        return this;
    }

    @JsonProperty("out_result_id")
    public java.lang.String getOutResultId() {
        return outResultId;
    }

    @JsonProperty("out_result_id")
    public void setOutResultId(java.lang.String outResultId) {
        this.outResultId = outResultId;
    }

    public SearchDomainsParams withOutResultId(java.lang.String outResultId) {
        this.outResultId = outResultId;
        return this;
    }

    @JsonProperty("is_genome_annotation_stored_outside")
    public Long getIsGenomeAnnotationStoredOutside() {
        return isGenomeAnnotationStoredOutside;
    }

    @JsonProperty("is_genome_annotation_stored_outside")
    public void setIsGenomeAnnotationStoredOutside(Long isGenomeAnnotationStoredOutside) {
        this.isGenomeAnnotationStoredOutside = isGenomeAnnotationStoredOutside;
    }

    public SearchDomainsParams withIsGenomeAnnotationStoredOutside(Long isGenomeAnnotationStoredOutside) {
        this.isGenomeAnnotationStoredOutside = isGenomeAnnotationStoredOutside;
        return this;
    }

    @JsonProperty("genome_annotation_id_prefix")
    public java.lang.String getGenomeAnnotationIdPrefix() {
        return genomeAnnotationIdPrefix;
    }

    @JsonProperty("genome_annotation_id_prefix")
    public void setGenomeAnnotationIdPrefix(java.lang.String genomeAnnotationIdPrefix) {
        this.genomeAnnotationIdPrefix = genomeAnnotationIdPrefix;
    }

    public SearchDomainsParams withGenomeAnnotationIdPrefix(java.lang.String genomeAnnotationIdPrefix) {
        this.genomeAnnotationIdPrefix = genomeAnnotationIdPrefix;
        return this;
    }

    @JsonProperty("genome_annotation_id_suffix")
    public java.lang.String getGenomeAnnotationIdSuffix() {
        return genomeAnnotationIdSuffix;
    }

    @JsonProperty("genome_annotation_id_suffix")
    public void setGenomeAnnotationIdSuffix(java.lang.String genomeAnnotationIdSuffix) {
        this.genomeAnnotationIdSuffix = genomeAnnotationIdSuffix;
    }

    public SearchDomainsParams withGenomeAnnotationIdSuffix(java.lang.String genomeAnnotationIdSuffix) {
        this.genomeAnnotationIdSuffix = genomeAnnotationIdSuffix;
        return this;
    }

    @JsonProperty("is_domain_cluster_data_stored_outside")
    public Long getIsDomainClusterDataStoredOutside() {
        return isDomainClusterDataStoredOutside;
    }

    @JsonProperty("is_domain_cluster_data_stored_outside")
    public void setIsDomainClusterDataStoredOutside(Long isDomainClusterDataStoredOutside) {
        this.isDomainClusterDataStoredOutside = isDomainClusterDataStoredOutside;
    }

    public SearchDomainsParams withIsDomainClusterDataStoredOutside(Long isDomainClusterDataStoredOutside) {
        this.isDomainClusterDataStoredOutside = isDomainClusterDataStoredOutside;
        return this;
    }

    @JsonProperty("domain_cluster_data_id_prefix")
    public java.lang.String getDomainClusterDataIdPrefix() {
        return domainClusterDataIdPrefix;
    }

    @JsonProperty("domain_cluster_data_id_prefix")
    public void setDomainClusterDataIdPrefix(java.lang.String domainClusterDataIdPrefix) {
        this.domainClusterDataIdPrefix = domainClusterDataIdPrefix;
    }

    public SearchDomainsParams withDomainClusterDataIdPrefix(java.lang.String domainClusterDataIdPrefix) {
        this.domainClusterDataIdPrefix = domainClusterDataIdPrefix;
        return this;
    }

    @JsonProperty("domain_cluster_data_id_suffix")
    public java.lang.String getDomainClusterDataIdSuffix() {
        return domainClusterDataIdSuffix;
    }

    @JsonProperty("domain_cluster_data_id_suffix")
    public void setDomainClusterDataIdSuffix(java.lang.String domainClusterDataIdSuffix) {
        this.domainClusterDataIdSuffix = domainClusterDataIdSuffix;
    }

    public SearchDomainsParams withDomainClusterDataIdSuffix(java.lang.String domainClusterDataIdSuffix) {
        this.domainClusterDataIdSuffix = domainClusterDataIdSuffix;
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
        return ((((((((((((((((((((((((((("SearchDomainsParams"+" [genomes=")+ genomes)+", dmsRef=")+ dmsRef)+", clustersForExtension=")+ clustersForExtension)+", domainTypes=")+ domainTypes)+", outWorkspace=")+ outWorkspace)+", outResultId=")+ outResultId)+", isGenomeAnnotationStoredOutside=")+ isGenomeAnnotationStoredOutside)+", genomeAnnotationIdPrefix=")+ genomeAnnotationIdPrefix)+", genomeAnnotationIdSuffix=")+ genomeAnnotationIdSuffix)+", isDomainClusterDataStoredOutside=")+ isDomainClusterDataStoredOutside)+", domainClusterDataIdPrefix=")+ domainClusterDataIdPrefix)+", domainClusterDataIdSuffix=")+ domainClusterDataIdSuffix)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
