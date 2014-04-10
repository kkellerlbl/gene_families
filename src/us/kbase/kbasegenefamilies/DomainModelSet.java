
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
 * <p>Original spec-file type: DomainModelSet</p>
 * <pre>
 * string set_name - name of model set
 * list<dms_ref> parent_refs - optional references to inherited domains
 * list<domain_model_type_ref> types - types of models in data
 * list<domain_model_ref> data - mapping from domain name to reference to 
 *         domain model object
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "set_name",
    "parent_refs",
    "types",
    "domain_model_refs"
})
public class DomainModelSet {

    @JsonProperty("set_name")
    private java.lang.String setName;
    @JsonProperty("parent_refs")
    private List<String> parentRefs;
    @JsonProperty("types")
    private List<String> types;
    @JsonProperty("domain_model_refs")
    private List<String> domainModelRefs;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("set_name")
    public java.lang.String getSetName() {
        return setName;
    }

    @JsonProperty("set_name")
    public void setSetName(java.lang.String setName) {
        this.setName = setName;
    }

    public DomainModelSet withSetName(java.lang.String setName) {
        this.setName = setName;
        return this;
    }

    @JsonProperty("parent_refs")
    public List<String> getParentRefs() {
        return parentRefs;
    }

    @JsonProperty("parent_refs")
    public void setParentRefs(List<String> parentRefs) {
        this.parentRefs = parentRefs;
    }

    public DomainModelSet withParentRefs(List<String> parentRefs) {
        this.parentRefs = parentRefs;
        return this;
    }

    @JsonProperty("types")
    public List<String> getTypes() {
        return types;
    }

    @JsonProperty("types")
    public void setTypes(List<String> types) {
        this.types = types;
    }

    public DomainModelSet withTypes(List<String> types) {
        this.types = types;
        return this;
    }

    @JsonProperty("domain_model_refs")
    public List<String> getDomainModelRefs() {
        return domainModelRefs;
    }

    @JsonProperty("domain_model_refs")
    public void setDomainModelRefs(List<String> domainModelRefs) {
        this.domainModelRefs = domainModelRefs;
    }

    public DomainModelSet withDomainModelRefs(List<String> domainModelRefs) {
        this.domainModelRefs = domainModelRefs;
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
        return ((((((((((("DomainModelSet"+" [setName=")+ setName)+", parentRefs=")+ parentRefs)+", types=")+ types)+", domainModelRefs=")+ domainModelRefs)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
