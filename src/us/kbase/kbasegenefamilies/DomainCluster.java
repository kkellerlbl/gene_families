
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
 * <p>Original spec-file type: DomainCluster</p>
 * <pre>
 * domain_model_ref model - reference to domain model
 * domain_cluster_ref parent_ref - optional reference to parent cluster (containing data 
 *         describing some common set of genomes)
 * mapping<genome_ref,list<domain_cluster_element>> data - list of entrances of this domain 
 *         into different genomes (domain_cluster_element -> ;
 *         domain_place -> tuple<int start_in_feature,int stop_in_feature,float evalue,
 *                 float bitscore,float domain_coverage>).
 * ws_alignment_id msa_ref - reference to multiple alignment object where all domain 
 *         sequences are collected (keys in this MSA object are constructed according to this 
 *         pattern: <genome_ref>_<feature_id>_<start_in_feature>)
 * msa_set_ref msa_set_ref - alternative way to refer to MSA, it works together with
 *         msa_set_index (see details of key structure in description for msa_ref field)
 * int msa_set_index - alternative way to refer to MSA, it works together with
 *         msa_set_ref (see details of key structure in description for msa_ref field)
 * @optional parent_ref
 * @optional msa_ref
 * @optional msa_set_ref msa_set_index
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "model",
    "parent_ref",
    "data",
    "msa_ref",
    "msa_set_ref",
    "msa_set_index"
})
public class DomainCluster {

    @JsonProperty("model")
    private java.lang.String model;
    @JsonProperty("parent_ref")
    private java.lang.String parentRef;
    @JsonProperty("data")
    private Map<String, List<Tuple4 <String, String, Long, List<Tuple5 <Long, Long, Double, Double, Double>>>>> data;
    @JsonProperty("msa_ref")
    private java.lang.String msaRef;
    @JsonProperty("msa_set_ref")
    private java.lang.String msaSetRef;
    @JsonProperty("msa_set_index")
    private java.lang.Long msaSetIndex;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("model")
    public java.lang.String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(java.lang.String model) {
        this.model = model;
    }

    public DomainCluster withModel(java.lang.String model) {
        this.model = model;
        return this;
    }

    @JsonProperty("parent_ref")
    public java.lang.String getParentRef() {
        return parentRef;
    }

    @JsonProperty("parent_ref")
    public void setParentRef(java.lang.String parentRef) {
        this.parentRef = parentRef;
    }

    public DomainCluster withParentRef(java.lang.String parentRef) {
        this.parentRef = parentRef;
        return this;
    }

    @JsonProperty("data")
    public Map<String, List<Tuple4 <String, String, Long, List<Tuple5 <Long, Long, Double, Double, Double>>>>> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Map<String, List<Tuple4 <String, String, Long, List<Tuple5 <Long, Long, Double, Double, Double>>>>> data) {
        this.data = data;
    }

    public DomainCluster withData(Map<String, List<Tuple4 <String, String, Long, List<Tuple5 <Long, Long, Double, Double, Double>>>>> data) {
        this.data = data;
        return this;
    }

    @JsonProperty("msa_ref")
    public java.lang.String getMsaRef() {
        return msaRef;
    }

    @JsonProperty("msa_ref")
    public void setMsaRef(java.lang.String msaRef) {
        this.msaRef = msaRef;
    }

    public DomainCluster withMsaRef(java.lang.String msaRef) {
        this.msaRef = msaRef;
        return this;
    }

    @JsonProperty("msa_set_ref")
    public java.lang.String getMsaSetRef() {
        return msaSetRef;
    }

    @JsonProperty("msa_set_ref")
    public void setMsaSetRef(java.lang.String msaSetRef) {
        this.msaSetRef = msaSetRef;
    }

    public DomainCluster withMsaSetRef(java.lang.String msaSetRef) {
        this.msaSetRef = msaSetRef;
        return this;
    }

    @JsonProperty("msa_set_index")
    public java.lang.Long getMsaSetIndex() {
        return msaSetIndex;
    }

    @JsonProperty("msa_set_index")
    public void setMsaSetIndex(java.lang.Long msaSetIndex) {
        this.msaSetIndex = msaSetIndex;
    }

    public DomainCluster withMsaSetIndex(java.lang.Long msaSetIndex) {
        this.msaSetIndex = msaSetIndex;
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
        return ((((((((((((((("DomainCluster"+" [model=")+ model)+", parentRef=")+ parentRef)+", data=")+ data)+", msaRef=")+ msaRef)+", msaSetRef=")+ msaSetRef)+", msaSetIndex=")+ msaSetIndex)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
