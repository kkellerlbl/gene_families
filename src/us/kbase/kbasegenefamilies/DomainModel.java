
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
 * <p>Original spec-file type: DomainModel</p>
 * <pre>
 * domain_name domain_name - domain model name
 * domain_model_type_ref domain_type - type of domain. 
 * string description - short description like domain functional role
 * string cdd_scoremat_file - main file used in RPS-blast
 * string cdd_consensus_seq - consensus of domain multiple alignment
 * double cdd_threshold - threshold for RPS-blast (default value is 9.82)
 * @optional cdd_scoremat_gzip_file
 * @optional cdd_consensus_seq
 * @optional cdd_threshold
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "domain_name",
    "domain_type",
    "description",
    "cdd_scoremat_gzip_file",
    "cdd_consensus_seq",
    "cdd_threshold"
})
public class DomainModel {

    @JsonProperty("domain_name")
    private String domainName;
    @JsonProperty("domain_type")
    private String domainType;
    @JsonProperty("description")
    private String description;
    @JsonProperty("cdd_scoremat_gzip_file")
    private String cddScorematGzipFile;
    @JsonProperty("cdd_consensus_seq")
    private String cddConsensusSeq;
    @JsonProperty("cdd_threshold")
    private Double cddThreshold;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("domain_name")
    public String getDomainName() {
        return domainName;
    }

    @JsonProperty("domain_name")
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public DomainModel withDomainName(String domainName) {
        this.domainName = domainName;
        return this;
    }

    @JsonProperty("domain_type")
    public String getDomainType() {
        return domainType;
    }

    @JsonProperty("domain_type")
    public void setDomainType(String domainType) {
        this.domainType = domainType;
    }

    public DomainModel withDomainType(String domainType) {
        this.domainType = domainType;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public DomainModel withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("cdd_scoremat_gzip_file")
    public String getCddScorematGzipFile() {
        return cddScorematGzipFile;
    }

    @JsonProperty("cdd_scoremat_gzip_file")
    public void setCddScorematGzipFile(String cddScorematGzipFile) {
        this.cddScorematGzipFile = cddScorematGzipFile;
    }

    public DomainModel withCddScorematGzipFile(String cddScorematGzipFile) {
        this.cddScorematGzipFile = cddScorematGzipFile;
        return this;
    }

    @JsonProperty("cdd_consensus_seq")
    public String getCddConsensusSeq() {
        return cddConsensusSeq;
    }

    @JsonProperty("cdd_consensus_seq")
    public void setCddConsensusSeq(String cddConsensusSeq) {
        this.cddConsensusSeq = cddConsensusSeq;
    }

    public DomainModel withCddConsensusSeq(String cddConsensusSeq) {
        this.cddConsensusSeq = cddConsensusSeq;
        return this;
    }

    @JsonProperty("cdd_threshold")
    public Double getCddThreshold() {
        return cddThreshold;
    }

    @JsonProperty("cdd_threshold")
    public void setCddThreshold(Double cddThreshold) {
        this.cddThreshold = cddThreshold;
    }

    public DomainModel withCddThreshold(Double cddThreshold) {
        this.cddThreshold = cddThreshold;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return ((((((((((((((("DomainModel"+" [domainName=")+ domainName)+", domainType=")+ domainType)+", description=")+ description)+", cddScorematGzipFile=")+ cddScorematGzipFile)+", cddConsensusSeq=")+ cddConsensusSeq)+", cddThreshold=")+ cddThreshold)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
