
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
 * <p>Original spec-file type: ProteinFamilyModel</p>
 * <pre>
 * pfm_name family_name - protein family model name
 * string family_type - type like 'pfam', 'tigrfam', ... (we need to register enum of these values somewhere)
 * string description - short description like domain functional role
 * int is_cdd - if 1 then next cdd fields should be used for search
 * string cdd_scoremat_file - main file used in RPS-blast
 * string cdd_consensus_seq - consensus of family multiple alignment
 * double cdd_threshold - threshold for RPS-blast (default value is 9.82)
 * string cdd_rps_blast_version - now we support RPS-blast version 2.2.29                
 * string cdd_revision_date - now the last cdd revision date is 2014-02-20
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "family_name",
    "description",
    "is_cdd",
    "cdd_scoremat_gzip_file",
    "cdd_consensus_seq",
    "cdd_threshold",
    "cdd_rps_blast_version",
    "cdd_revision_date"
})
public class ProteinFamilyModel {

    @JsonProperty("family_name")
    private String familyName;
    @JsonProperty("description")
    private String description;
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

    @JsonProperty("family_name")
    public String getFamilyName() {
        return familyName;
    }

    @JsonProperty("family_name")
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public ProteinFamilyModel withFamilyName(String familyName) {
        this.familyName = familyName;
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

    public ProteinFamilyModel withDescription(String description) {
        this.description = description;
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

    public ProteinFamilyModel withIsCdd(Long isCdd) {
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

    public ProteinFamilyModel withCddScorematGzipFile(String cddScorematGzipFile) {
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

    public ProteinFamilyModel withCddConsensusSeq(String cddConsensusSeq) {
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

    public ProteinFamilyModel withCddThreshold(Double cddThreshold) {
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

    public ProteinFamilyModel withCddRpsBlastVersion(String cddRpsBlastVersion) {
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

    public ProteinFamilyModel withCddRevisionDate(String cddRevisionDate) {
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
        return ((((((((((((((((((("ProteinFamilyModel"+" [familyName=")+ familyName)+", description=")+ description)+", isCdd=")+ isCdd)+", cddScorematGzipFile=")+ cddScorematGzipFile)+", cddConsensusSeq=")+ cddConsensusSeq)+", cddThreshold=")+ cddThreshold)+", cddRpsBlastVersion=")+ cddRpsBlastVersion)+", cddRevisionDate=")+ cddRevisionDate)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
