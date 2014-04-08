
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
import us.kbase.common.service.Tuple7;


/**
 * <p>Original spec-file type: DomainCluster</p>
 * <pre>
 * domain_model_ref model - reference to domain model
 * domain_cluster_ref parent_ref - optional reference to parent cluster (containing data describing some common set of genomes)
 * mapping<genome_ref,list<tuple<string contig_id,string feature_id,int feature_list_pos,int number_of_copies,float best_evalue,
 *         float best_bitscore,string best_profile_alignment>>> data - list of entrances of this domain into different genomes
 * @optional parent_ref
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "model",
    "parent_ref",
    "data"
})
public class DomainCluster {

    @JsonProperty("model")
    private java.lang.String model;
    @JsonProperty("parent_ref")
    private java.lang.String parentRef;
    @JsonProperty("data")
    private Map<String, List<Tuple7 <String, String, Long, Long, Double, Double, String>>> data;
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
    public Map<String, List<Tuple7 <String, String, Long, Long, Double, Double, String>>> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Map<String, List<Tuple7 <String, String, Long, Long, Double, Double, String>>> data) {
        this.data = data;
    }

    public DomainCluster withData(Map<String, List<Tuple7 <String, String, Long, Long, Double, Double, String>>> data) {
        this.data = data;
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
        return ((((((((("DomainCluster"+" [model=")+ model)+", parentRef=")+ parentRef)+", data=")+ data)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
