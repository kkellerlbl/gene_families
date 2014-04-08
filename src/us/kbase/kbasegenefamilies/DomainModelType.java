
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
 * <p>Original spec-file type: DomainModelType</p>
 * <pre>
 * string domain_type - we now have all types from CDD database: 'CHL', 'COG', 'KOG', 
 *         'LOAD', 'MTH', 'PHA', 'PLN', 'PRK', 'PTZ', 'TIGR', 'cd', 'pfam', 'smart'. 
 * string version - version of domain type release
 * string date - release date (for example now the last CDD revision date is 
 *         2014-02-20)
 * string source_name - name of source (resource like CDD)
 * string source_url - ftp/http where data was downloaded from
 * string source_version - optional, use it in case it's different from version of 
 *         domain type
 * string description - short description of this domain type/source
 * int is_full_length - if 1 then there could be found only 1 domain copy of this 
 *         type in protein
 * int is_cdd - if 1 then next cdd fields should be used for search
 * string cdd_rps_blast_version - now we support RPS-blast version 2.2.29
 * @optional source_version
 * @optional cdd_rps_blast_version
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "domain_type",
    "version",
    "date",
    "source_name",
    "source_url",
    "source_version",
    "description",
    "is_full_length",
    "is_cdd",
    "cdd_rps_blast_version"
})
public class DomainModelType {

    @JsonProperty("domain_type")
    private String domainType;
    @JsonProperty("version")
    private String version;
    @JsonProperty("date")
    private String date;
    @JsonProperty("source_name")
    private String sourceName;
    @JsonProperty("source_url")
    private String sourceUrl;
    @JsonProperty("source_version")
    private String sourceVersion;
    @JsonProperty("description")
    private String description;
    @JsonProperty("is_full_length")
    private Long isFullLength;
    @JsonProperty("is_cdd")
    private Long isCdd;
    @JsonProperty("cdd_rps_blast_version")
    private String cddRpsBlastVersion;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("domain_type")
    public String getDomainType() {
        return domainType;
    }

    @JsonProperty("domain_type")
    public void setDomainType(String domainType) {
        this.domainType = domainType;
    }

    public DomainModelType withDomainType(String domainType) {
        this.domainType = domainType;
        return this;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public DomainModelType withVersion(String version) {
        this.version = version;
        return this;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    public DomainModelType withDate(String date) {
        this.date = date;
        return this;
    }

    @JsonProperty("source_name")
    public String getSourceName() {
        return sourceName;
    }

    @JsonProperty("source_name")
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public DomainModelType withSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    @JsonProperty("source_url")
    public String getSourceUrl() {
        return sourceUrl;
    }

    @JsonProperty("source_url")
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public DomainModelType withSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        return this;
    }

    @JsonProperty("source_version")
    public String getSourceVersion() {
        return sourceVersion;
    }

    @JsonProperty("source_version")
    public void setSourceVersion(String sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    public DomainModelType withSourceVersion(String sourceVersion) {
        this.sourceVersion = sourceVersion;
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

    public DomainModelType withDescription(String description) {
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

    public DomainModelType withIsFullLength(Long isFullLength) {
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

    public DomainModelType withIsCdd(Long isCdd) {
        this.isCdd = isCdd;
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

    public DomainModelType withCddRpsBlastVersion(String cddRpsBlastVersion) {
        this.cddRpsBlastVersion = cddRpsBlastVersion;
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
        return ((((((((((((((((((((((("DomainModelType"+" [domainType=")+ domainType)+", version=")+ version)+", date=")+ date)+", sourceName=")+ sourceName)+", sourceUrl=")+ sourceUrl)+", sourceVersion=")+ sourceVersion)+", description=")+ description)+", isFullLength=")+ isFullLength)+", isCdd=")+ isCdd)+", cddRpsBlastVersion=")+ cddRpsBlastVersion)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
