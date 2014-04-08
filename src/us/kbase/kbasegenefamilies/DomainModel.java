
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
 * string domain_type - type is one of 'CHL', 'COG', 'KOG', 'LOAD', 'MTH', 'PHA', 'PLN', 'PRK', 'PTZ', 
 *         'TIGR', 'cd', 'pfam', 'smart'. 
 * string description - short description like domain functional role
 * int is_full_length - if 1 then there could be found only 1 domain copy of this type in protein
 * int is_cdd - if 1 then next cdd fields should be used for search
 * string cdd_scoremat_file - main file used in RPS-blast
 * string cdd_consensus_seq - consensus of domain multiple alignment
 * double cdd_threshold - threshold for RPS-blast (default value is 9.82)
 * string cdd_rps_blast_version - now we support RPS-blast version 2.2.29                
 * string cdd_revision_date - now the last cdd revision date is 2014-02-20
 * @optional cdd_scoremat_gzip_file
 * @optional cdd_consensus_seq
 * @optional cdd_threshold
 * @optional cdd_rps_blast_version
 * @optional cdd_revision_date
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "domain_name",
    "domain_type",
    "description",
    "is_full_length",
    "is_cdd",
    "cdd_scoremat_gzip_file",
    "cdd_consensus_seq",
    "cdd_threshold",
    "cdd_rps_blast_version",
    "cdd_revision_date"
})
public class DomainModel {

    @JsonProperty("domain_name")
    private String domainName;
    @JsonProperty("domain_type")
    private String domainType;
    @JsonProperty("description")
    private String description;
    @JsonProperty("is_full_length")
    private Long isFullLength;
    @JsonProperty("is_cdd")
    private Long isCdd;
    @JsonProperty("cdd_scoremat_gzip_file")
    private String cddScorematGzipFile;
    @JsonProperty("cdd_consensus_seq")
    private String cddConsensusSeq;
    @JsonProperty("cdd_threshold")
    private Double cddThreshold;
    @JsonProperty("cdd_rps_blast_version")
    private String cddRpsBlastVersion;
    @JsonProperty("cdd_revision_date")
    private String cddRevisionDate;
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

    @JsonProperty("is_full_length")
    public Long getIsFullLength() {
        return isFullLength;
    }

    @JsonProperty("is_full_length")
    public void setIsFullLength(Long isFullLength) {
        this.isFullLength = isFullLength;
    }

    public DomainModel withIsFullLength(Long isFullLength) {
        this.isFullLength = isFullLength;
        return this;
    }

    @JsonProperty("is_cdd")
    public Long getIsCdd() {
        return isCdd;
    }

    @JsonProperty("is_cdd")
    public void setIsCdd(Long isCdd) {
        this.isCdd = isCdd;
    }

    public DomainModel withIsCdd(Long isCdd) {
        this.isCdd = isCdd;
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

    @JsonProperty("cdd_rps_blast_version")
    public String getCddRpsBlastVersion() {
        return cddRpsBlastVersion;
    }

    @JsonProperty("cdd_rps_blast_version")
    public void setCddRpsBlastVersion(String cddRpsBlastVersion) {
        this.cddRpsBlastVersion = cddRpsBlastVersion;
    }

    public DomainModel withCddRpsBlastVersion(String cddRpsBlastVersion) {
        this.cddRpsBlastVersion = cddRpsBlastVersion;
        return this;
    }

    @JsonProperty("cdd_revision_date")
    public String getCddRevisionDate() {
        return cddRevisionDate;
    }

    @JsonProperty("cdd_revision_date")
    public void setCddRevisionDate(String cddRevisionDate) {
        this.cddRevisionDate = cddRevisionDate;
    }

    public DomainModel withCddRevisionDate(String cddRevisionDate) {
        this.cddRevisionDate = cddRevisionDate;
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
        return ((((((((((((((((((((((("DomainModel"+" [domainName=")+ domainName)+", domainType=")+ domainType)+", description=")+ description)+", isFullLength=")+ isFullLength)+", isCdd=")+ isCdd)+", cddScorematGzipFile=")+ cddScorematGzipFile)+", cddConsensusSeq=")+ cddConsensusSeq)+", cddThreshold=")+ cddThreshold)+", cddRpsBlastVersion=")+ cddRpsBlastVersion)+", cddRevisionDate=")+ cddRevisionDate)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
