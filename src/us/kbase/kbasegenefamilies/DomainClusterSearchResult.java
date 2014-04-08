
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
 * <p>Original spec-file type: DomainClusterSearchResult</p>
 * <pre>
 * dcsr_ref parent_ref - optional reference to parent domain clusters search results
 * dms_ref used_dms_ref - domain models used for search
 * mapping<genome_ref, DomainAnnotation> annotations - found domains in genomes that user 
 *         defined as input data for domain search
 * mapping<genome_ref, domain_annotation_ref> annotation_refs - domain annotation references 
 *         in case we don't want to store it inside search result object
 * mapping<domain_model_ref, DomainCluster> domain_clusters - clusters constructed based on 
 *         query_genomes plus genomes from parent object
 * mapping<domain_model_ref, domain_cluster_ref> domain_cluster_refs - references to clusters 
 *         in case we don't want to store these clusters inside search result object
 * @optional parent_ref
 * @optional annotations
 * @optional annotation_refs
 * @optional domain_clusters
 * @optional domain_cluster_refs
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "parent_ref",
    "used_dms_ref",
    "annotations",
    "annotation_refs",
    "domain_clusters",
    "domain_cluster_refs"
})
public class DomainClusterSearchResult {

    @JsonProperty("parent_ref")
    private java.lang.String parentRef;
    @JsonProperty("used_dms_ref")
    private java.lang.String usedDmsRef;
    @JsonProperty("annotations")
    private Map<String, DomainAnnotation> annotations;
    @JsonProperty("annotation_refs")
    private Map<String, String> annotationRefs;
    @JsonProperty("domain_clusters")
    private Map<String, DomainCluster> domainClusters;
    @JsonProperty("domain_cluster_refs")
    private Map<String, String> domainClusterRefs;
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
        return ((((((((((((((("DomainClusterSearchResult"+" [parentRef=")+ parentRef)+", usedDmsRef=")+ usedDmsRef)+", annotations=")+ annotations)+", annotationRefs=")+ annotationRefs)+", domainClusters=")+ domainClusters)+", domainClusterRefs=")+ domainClusterRefs)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
