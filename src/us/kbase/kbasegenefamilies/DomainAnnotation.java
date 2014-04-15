
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
import us.kbase.common.service.Tuple4;
import us.kbase.common.service.Tuple5;


/**
 * <p>Original spec-file type: DomainAnnotation</p>
 * <pre>
 * genome_ref genome - reference to genome
 * mapping<contig_id, list<annotation_element>> data - 
 *         list of entrances of different domains into proteins of annotated genome
 *         (annotation_element -> typedef tuple<string feature_id,int feature_start,int feature_stop,
 *                 int feature_dir,mapping<domain_model_ref,list<domain_place>>>;
 *         domain_place -> tuple<int start_in_feature,int stop_in_feature,float evalue,
 *                 float bitscore>).
 * mapping<domain_model_ref,mapping<contig_id,mapping<string feature_id,
 *         mapping<int start_in_feature,string alignment_with_profile>>>> alignments - 
 *                 alignments of protein sequences against domain profiles
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "genome",
    "data",
    "alignments"
})
public class DomainAnnotation {

    @JsonProperty("genome")
    private java.lang.String genome;
    @JsonProperty("data")
    private Map<String, List<Tuple5 <String, Long, Long, Long, Map<String, List<Tuple4 <Long, Long, Double, Double>>>>>> data;
    @JsonProperty("alignments")
    private Map<String, Map<String, Map<String, Map<Long, String>>>> alignments;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("genome")
    public java.lang.String getGenome() {
        return genome;
    }

    @JsonProperty("genome")
    public void setGenome(java.lang.String genome) {
        this.genome = genome;
    }

    public DomainAnnotation withGenome(java.lang.String genome) {
        this.genome = genome;
        return this;
    }

    @JsonProperty("data")
    public Map<String, List<Tuple5 <String, Long, Long, Long, Map<String, List<Tuple4 <Long, Long, Double, Double>>>>>> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Map<String, List<Tuple5 <String, Long, Long, Long, Map<String, List<Tuple4 <Long, Long, Double, Double>>>>>> data) {
        this.data = data;
    }

    public DomainAnnotation withData(Map<String, List<Tuple5 <String, Long, Long, Long, Map<String, List<Tuple4 <Long, Long, Double, Double>>>>>> data) {
        this.data = data;
        return this;
    }

    @JsonProperty("alignments")
    public Map<String, Map<String, Map<String, Map<Long, String>>>> getAlignments() {
        return alignments;
    }

    @JsonProperty("alignments")
    public void setAlignments(Map<String, Map<String, Map<String, Map<Long, String>>>> alignments) {
        this.alignments = alignments;
    }

    public DomainAnnotation withAlignments(Map<String, Map<String, Map<String, Map<Long, String>>>> alignments) {
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
        return ((((((((("DomainAnnotation"+" [genome=")+ genome)+", data=")+ data)+", alignments=")+ alignments)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
