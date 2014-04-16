
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
 * <p>Original spec-file type: DomainAnnotation</p>
 * <pre>
 * genome_ref genome_ref - reference to genome
 * mapping<contig_id, list<annotation_element>> data - 
 *         list of entrances of different domains into proteins of annotated genome
 *         (annotation_element -> typedef tuple<string feature_id,int feature_start,int feature_stop,
 *                 int feature_dir,mapping<domain_model_ref,list<domain_place>>>;
 *         domain_place -> tuple<int start_in_feature,int stop_in_feature,float evalue,
 *                 float bitscore,float domain_coverage>).
 * mapping<contig_id, tuple<int size,int features>> contig_to_size_and_feature_count - 
 *         feature count and nucleotide size of every contig
 * mapping<string feature_id, tuple<contig_id,int feature_index> feature_to_contig_and_index - 
 *         index of every feature in feature list in every contig
 * mapping<domain_model_ref,mapping<string feature_id,
 *         mapping<string start_in_feature,string alignment_with_profile>>> alignments - 
 *                 alignments of protein sequences against domain profiles
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "genome_ref",
    "data",
    "contig_to_size_and_feature_count",
    "feature_to_contig_and_index",
    "alignments"
})
public class DomainAnnotation {

    @JsonProperty("genome_ref")
    private java.lang.String genomeRef;
    @JsonProperty("data")
    private Map<String, List<us.kbase.common.service.Tuple5 <String, Long, Long, Long, Map<String, List<us.kbase.common.service.Tuple5 <Long, Long, Double, Double, Double>>>>>> data;
    @JsonProperty("contig_to_size_and_feature_count")
    private Map<String, us.kbase.common.service.Tuple2 <Long, Long>> contigToSizeAndFeatureCount;
    @JsonProperty("feature_to_contig_and_index")
    private Map<String, us.kbase.common.service.Tuple2 <String, Long>> featureToContigAndIndex;
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

    public DomainAnnotation withGenomeRef(java.lang.String genomeRef) {
        this.genomeRef = genomeRef;
        return this;
    }

    @JsonProperty("data")
    public Map<String, List<us.kbase.common.service.Tuple5 <String, Long, Long, Long, Map<String, List<us.kbase.common.service.Tuple5 <Long, Long, Double, Double, Double>>>>>> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Map<String, List<us.kbase.common.service.Tuple5 <String, Long, Long, Long, Map<String, List<us.kbase.common.service.Tuple5 <Long, Long, Double, Double, Double>>>>>> data) {
        this.data = data;
    }

    public DomainAnnotation withData(Map<String, List<us.kbase.common.service.Tuple5 <String, Long, Long, Long, Map<String, List<us.kbase.common.service.Tuple5 <Long, Long, Double, Double, Double>>>>>> data) {
        this.data = data;
        return this;
    }

    @JsonProperty("contig_to_size_and_feature_count")
    public Map<String, us.kbase.common.service.Tuple2 <Long, Long>> getContigToSizeAndFeatureCount() {
        return contigToSizeAndFeatureCount;
    }

    @JsonProperty("contig_to_size_and_feature_count")
    public void setContigToSizeAndFeatureCount(Map<String, us.kbase.common.service.Tuple2 <Long, Long>> contigToSizeAndFeatureCount) {
        this.contigToSizeAndFeatureCount = contigToSizeAndFeatureCount;
    }

    public DomainAnnotation withContigToSizeAndFeatureCount(Map<String, us.kbase.common.service.Tuple2 <Long, Long>> contigToSizeAndFeatureCount) {
        this.contigToSizeAndFeatureCount = contigToSizeAndFeatureCount;
        return this;
    }

    @JsonProperty("feature_to_contig_and_index")
    public Map<String, us.kbase.common.service.Tuple2 <String, Long>> getFeatureToContigAndIndex() {
        return featureToContigAndIndex;
    }

    @JsonProperty("feature_to_contig_and_index")
    public void setFeatureToContigAndIndex(Map<String, us.kbase.common.service.Tuple2 <String, Long>> featureToContigAndIndex) {
        this.featureToContigAndIndex = featureToContigAndIndex;
    }

    public DomainAnnotation withFeatureToContigAndIndex(Map<String, us.kbase.common.service.Tuple2 <String, Long>> featureToContigAndIndex) {
        this.featureToContigAndIndex = featureToContigAndIndex;
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

    public DomainAnnotation withAlignments(Map<String, Map<String, Map<String, String>>> alignments) {
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
        return ((((((((((((("DomainAnnotation"+" [genomeRef=")+ genomeRef)+", data=")+ data)+", contigToSizeAndFeatureCount=")+ contigToSizeAndFeatureCount)+", featureToContigAndIndex=")+ featureToContigAndIndex)+", alignments=")+ alignments)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
