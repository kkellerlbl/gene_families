
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
 * <p>Original spec-file type: DomainClusterStat</p>
 * <pre>
 * Aggregated data for every domain cluster.
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "domain_accession",
    "name",
    "genomes",
    "features",
    "domains"
})
public class DomainClusterStat {

    @JsonProperty("domain_accession")
    private String domainAccession;
    @JsonProperty("name")
    private String name;
    @JsonProperty("genomes")
    private Long genomes;
    @JsonProperty("features")
    private Long features;
    @JsonProperty("domains")
    private Long domains;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("domain_accession")
    public String getDomainAccession() {
        return domainAccession;
    }

    @JsonProperty("domain_accession")
    public void setDomainAccession(String domainAccession) {
        this.domainAccession = domainAccession;
    }

    public DomainClusterStat withDomainAccession(String domainAccession) {
        this.domainAccession = domainAccession;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public DomainClusterStat withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("genomes")
    public Long getGenomes() {
        return genomes;
    }

    @JsonProperty("genomes")
    public void setGenomes(Long genomes) {
        this.genomes = genomes;
    }

    public DomainClusterStat withGenomes(Long genomes) {
        this.genomes = genomes;
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

    public DomainClusterStat withFeatures(Long features) {
        this.features = features;
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

    public DomainClusterStat withDomains(Long domains) {
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
        return ((((((((((((("DomainClusterStat"+" [domainAccession=")+ domainAccession)+", name=")+ name)+", genomes=")+ genomes)+", features=")+ features)+", domains=")+ domains)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
