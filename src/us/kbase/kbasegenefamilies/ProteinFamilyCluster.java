
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
import us.kbase.common.service.Tuple9;


/**
 * <p>Original spec-file type: ProteinFamilyCluster</p>
 * <pre>
 * pfm_ref model - reference to protein family model
 * pfc_ref parent_ref - optional reference to parent cluster (containing data describing some common set of genomes)
 * list<tuple<genome_ref,string contig_id,string feature_id,int feature_start,int feature_stop,int start_in_feature,int stop_in_feature,float evalue,float bitscore>> data - 
 *         list of entrances of this family into different genomes
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
public class ProteinFamilyCluster {

    @JsonProperty("model")
    private java.lang.String model;
    @JsonProperty("parent_ref")
    private java.lang.String parentRef;
    @JsonProperty("data")
    private List<Tuple9 <String, String, String, Long, Long, Long, Long, Double, Double>> data;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("model")
    public java.lang.String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(java.lang.String model) {
        this.model = model;
    }

    public ProteinFamilyCluster withModel(java.lang.String model) {
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

    public ProteinFamilyCluster withParentRef(java.lang.String parentRef) {
        this.parentRef = parentRef;
        return this;
    }

    @JsonProperty("data")
    public List<Tuple9 <String, String, String, Long, Long, Long, Long, Double, Double>> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<Tuple9 <String, String, String, Long, Long, Long, Long, Double, Double>> data) {
        this.data = data;
    }

    public ProteinFamilyCluster withData(List<Tuple9 <String, String, String, Long, Long, Long, Long, Double, Double>> data) {
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
        return ((((((((("ProteinFamilyCluster"+" [model=")+ model)+", parentRef=")+ parentRef)+", data=")+ data)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
