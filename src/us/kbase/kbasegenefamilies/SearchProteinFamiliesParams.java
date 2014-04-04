
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
 * <p>Original spec-file type: search_protein_families_params</p>
 * <pre>
 * list<genome_ref> genomes - genome list
 * list<string> family_types - type list (if empty list all types are used)
 * string out_workspace - output workspace
 * string out_result_id - id of resulting object of type ProteinFamiliesSearchResult
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "genomes",
    "family_types",
    "out_workspace",
    "out_result_id"
})
public class SearchProteinFamiliesParams {

    @JsonProperty("genomes")
    private List<String> genomes;
    @JsonProperty("family_types")
    private List<String> familyTypes;
    @JsonProperty("out_workspace")
    private java.lang.String outWorkspace;
    @JsonProperty("out_result_id")
    private java.lang.String outResultId;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("genomes")
    public List<String> getGenomes() {
        return genomes;
    }

    @JsonProperty("genomes")
    public void setGenomes(List<String> genomes) {
        this.genomes = genomes;
    }

    public SearchProteinFamiliesParams withGenomes(List<String> genomes) {
        this.genomes = genomes;
        return this;
    }

    @JsonProperty("family_types")
    public List<String> getFamilyTypes() {
        return familyTypes;
    }

    @JsonProperty("family_types")
    public void setFamilyTypes(List<String> familyTypes) {
        this.familyTypes = familyTypes;
    }

    public SearchProteinFamiliesParams withFamilyTypes(List<String> familyTypes) {
        this.familyTypes = familyTypes;
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

    public SearchProteinFamiliesParams withOutWorkspace(java.lang.String outWorkspace) {
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

    public SearchProteinFamiliesParams withOutResultId(java.lang.String outResultId) {
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
        return ((((((((((("SearchProteinFamiliesParams"+" [genomes=")+ genomes)+", familyTypes=")+ familyTypes)+", outWorkspace=")+ outWorkspace)+", outResultId=")+ outResultId)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
