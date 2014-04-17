
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
 * <p>Original spec-file type: DomainAlignments</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "genome_ref",
    "alignments"
})
public class DomainAlignments {

    @JsonProperty("genome_ref")
    private java.lang.String genomeRef;
    @JsonProperty("alignments")
    private Map<String, Map<String, Map<String, String>>> alignments;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("genome_ref")
    public java.lang.String getGenomeRef() {
        return genomeRef;
    }

    @JsonProperty("genome_ref")
    public void setGenomeRef(java.lang.String genomeRef) {
        this.genomeRef = genomeRef;
    }

    public DomainAlignments withGenomeRef(java.lang.String genomeRef) {
        this.genomeRef = genomeRef;
        return this;
    }

    @JsonProperty("alignments")
    public Map<String, Map<String, Map<String, String>>> getAlignments() {
        return alignments;
    }

    @JsonProperty("alignments")
    public void setAlignments(Map<String, Map<String, Map<String, String>>> alignments) {
        this.alignments = alignments;
    }

    public DomainAlignments withAlignments(Map<String, Map<String, Map<String, String>>> alignments) {
        this.alignments = alignments;
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
        return ((((((("DomainAlignments"+" [genomeRef=")+ genomeRef)+", alignments=")+ alignments)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
