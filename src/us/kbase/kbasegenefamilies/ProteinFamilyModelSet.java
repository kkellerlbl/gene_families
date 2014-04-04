
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
 * <p>Original spec-file type: ProteinFamilyModelSet</p>
 * <pre>
 * string set_name - name of model set
 * mapping<pfm_name, pfm_ref> data - mapping from family name to reference to family object
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "set_name",
    "data"
})
public class ProteinFamilyModelSet {

    @JsonProperty("set_name")
    private java.lang.String setName;
    @JsonProperty("data")
    private Map<String, String> data;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("set_name")
    public java.lang.String getSetName() {
        return setName;
    }

    @JsonProperty("set_name")
    public void setSetName(java.lang.String setName) {
        this.setName = setName;
    }

    public ProteinFamilyModelSet withSetName(java.lang.String setName) {
        this.setName = setName;
        return this;
    }

    @JsonProperty("data")
    public Map<String, String> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public ProteinFamilyModelSet withData(Map<String, String> data) {
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
        return ((((((("ProteinFamilyModelSet"+" [setName=")+ setName)+", data=")+ data)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
