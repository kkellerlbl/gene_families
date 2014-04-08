
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
 * <p>Original spec-file type: SearchDomainsParams</p>
 * <pre>
 * genome_ref genome - genome for domain annotation process
 * dms_ref dms_ref - set of domain models that will be searched in defined genome
 * list<string> domain_types - type list (if empty list all types are used) defining subset 
 *         of domain models extracted from dms_ref
 * string out_workspace - output workspace
 * string out_result_id - id of resulting object of type DomainAnnotation
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "genome",
    "dms_ref",
    "domain_types",
    "out_workspace",
    "out_result_id"
})
public class SearchDomainsParams {

    @JsonProperty("genome")
    private java.lang.String genome;
    @JsonProperty("dms_ref")
    private java.lang.String dmsRef;
    @JsonProperty("domain_types")
    private List<String> domainTypes;
    @JsonProperty("out_workspace")
    private java.lang.String outWorkspace;
    @JsonProperty("out_result_id")
    private java.lang.String outResultId;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("genome")
    public java.lang.String getGenome() {
        return genome;
    }

    @JsonProperty("genome")
    public void setGenome(java.lang.String genome) {
        this.genome = genome;
    }

    public SearchDomainsParams withGenome(java.lang.String genome) {
        this.genome = genome;
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
        return ((((((((((((("SearchDomainsParams"+" [genome=")+ genome)+", dmsRef=")+ dmsRef)+", domainTypes=")+ domainTypes)+", outWorkspace=")+ outWorkspace)+", outResultId=")+ outResultId)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
