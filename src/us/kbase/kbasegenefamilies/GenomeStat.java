
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
 * <p>Original spec-file type: GenomeStat</p>
 * <pre>
 * Aggreagated data for every genome.
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "genome_ref",
    "kbase_id",
    "scientific_name",
    "features",
    "features_with_domains",
    "domain_models",
    "domains"
})
public class GenomeStat {

    @JsonProperty("genome_ref")
    private String genomeRef;
    @JsonProperty("kbase_id")
    private String kbaseId;
    @JsonProperty("scientific_name")
    private String scientificName;
    @JsonProperty("features")
    private Long features;
    @JsonProperty("features_with_domains")
    private Long featuresWithDomains;
    @JsonProperty("domain_models")
    private Long domainModels;
    @JsonProperty("domains")
    private Long domains;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("genome_ref")
    public String getGenomeRef() {
        return genomeRef;
    }

    @JsonProperty("genome_ref")
    public void setGenomeRef(String genomeRef) {
        this.genomeRef = genomeRef;
    }

    public GenomeStat withGenomeRef(String genomeRef) {
        this.genomeRef = genomeRef;
        return this;
    }

    @JsonProperty("kbase_id")
    public String getKbaseId() {
        return kbaseId;
    }

    @JsonProperty("kbase_id")
    public void setKbaseId(String kbaseId) {
        this.kbaseId = kbaseId;
    }

    public GenomeStat withKbaseId(String kbaseId) {
        this.kbaseId = kbaseId;
        return this;
    }

    @JsonProperty("scientific_name")
    public String getScientificName() {
        return scientificName;
    }

    @JsonProperty("scientific_name")
    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public GenomeStat withScientificName(String scientificName) {
        this.scientificName = scientificName;
        return this;
    }

    @JsonProperty("features")
    public Long getFeatures() {
        return features;
    }

    @JsonProperty("features")
    public void setFeatures(Long features) {
        this.features = features;
    }

    public GenomeStat withFeatures(Long features) {
        this.features = features;
        return this;
    }

    @JsonProperty("features_with_domains")
    public Long getFeaturesWithDomains() {
        return featuresWithDomains;
    }

    @JsonProperty("features_with_domains")
    public void setFeaturesWithDomains(Long featuresWithDomains) {
        this.featuresWithDomains = featuresWithDomains;
    }

    public GenomeStat withFeaturesWithDomains(Long featuresWithDomains) {
        this.featuresWithDomains = featuresWithDomains;
        return this;
    }

    @JsonProperty("domain_models")
    public Long getDomainModels() {
        return domainModels;
    }

    @JsonProperty("domain_models")
    public void setDomainModels(Long domainModels) {
        this.domainModels = domainModels;
    }

    public GenomeStat withDomainModels(Long domainModels) {
        this.domainModels = domainModels;
        return this;
    }

    @JsonProperty("domains")
    public Long getDomains() {
        return domains;
    }

    @JsonProperty("domains")
    public void setDomains(Long domains) {
        this.domains = domains;
    }

    public GenomeStat withDomains(Long domains) {
        this.domains = domains;
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
        return ((((((((((((((((("GenomeStat"+" [genomeRef=")+ genomeRef)+", kbaseId=")+ kbaseId)+", scientificName=")+ scientificName)+", features=")+ features)+", featuresWithDomains=")+ featuresWithDomains)+", domainModels=")+ domainModels)+", domains=")+ domains)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
