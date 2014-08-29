
package us.kbase.kbasetrees;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: MSASetElement</p>
 * <pre>
 * Type for MSA collection element. There could be mutual exclusively 
 * defined either ref or data field.
 * @optional metadata
 * @optional ref
 * @optional data
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "metadata",
    "ref",
    "data"
})
public class MSASetElement {

    @JsonProperty("metadata")
    private Map<String, String> metadata;
    @JsonProperty("ref")
    private java.lang.String ref;
    /**
     * <p>Original spec-file type: MSA</p>
     * <pre>
     * Type for multiple sequence alignment.
     * sequence_type - 'protein' in case sequences are amino acids, 'dna' in case of 
     *         nucleotides.
     * int alignment_length - number of columns in alignment.
     * mapping<row_id, sequence> alignment - mapping from sequence id to aligned sequence.
     * list<row_id> row_order - list of sequence ids defining alignment order (optional). 
     * ws_alignment_id parent_msa_ref - reference to parental alignment object to which 
     *         this object adds some new aligned sequences (it could be useful in case of
     *         profile alignments where you don't need to insert new gaps in original msa).
     * @optional name description sequence_type
     * @optional trim_info alignment_attributes row_order 
     * @optional default_row_labels ws_refs kb_refs
     * @optional parent_msa_ref
     * </pre>
     * 
     */
    @JsonProperty("data")
    private MSA data;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("metadata")
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public MSASetElement withMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    @JsonProperty("ref")
    public java.lang.String getRef() {
        return ref;
    }

    @JsonProperty("ref")
    public void setRef(java.lang.String ref) {
        this.ref = ref;
    }

    public MSASetElement withRef(java.lang.String ref) {
        this.ref = ref;
        return this;
    }

    /**
     * <p>Original spec-file type: MSA</p>
     * <pre>
     * Type for multiple sequence alignment.
     * sequence_type - 'protein' in case sequences are amino acids, 'dna' in case of 
     *         nucleotides.
     * int alignment_length - number of columns in alignment.
     * mapping<row_id, sequence> alignment - mapping from sequence id to aligned sequence.
     * list<row_id> row_order - list of sequence ids defining alignment order (optional). 
     * ws_alignment_id parent_msa_ref - reference to parental alignment object to which 
     *         this object adds some new aligned sequences (it could be useful in case of
     *         profile alignments where you don't need to insert new gaps in original msa).
     * @optional name description sequence_type
     * @optional trim_info alignment_attributes row_order 
     * @optional default_row_labels ws_refs kb_refs
     * @optional parent_msa_ref
     * </pre>
     * 
     */
    @JsonProperty("data")
    public MSA getData() {
        return data;
    }

    /**
     * <p>Original spec-file type: MSA</p>
     * <pre>
     * Type for multiple sequence alignment.
     * sequence_type - 'protein' in case sequences are amino acids, 'dna' in case of 
     *         nucleotides.
     * int alignment_length - number of columns in alignment.
     * mapping<row_id, sequence> alignment - mapping from sequence id to aligned sequence.
     * list<row_id> row_order - list of sequence ids defining alignment order (optional). 
     * ws_alignment_id parent_msa_ref - reference to parental alignment object to which 
     *         this object adds some new aligned sequences (it could be useful in case of
     *         profile alignments where you don't need to insert new gaps in original msa).
     * @optional name description sequence_type
     * @optional trim_info alignment_attributes row_order 
     * @optional default_row_labels ws_refs kb_refs
     * @optional parent_msa_ref
     * </pre>
     * 
     */
    @JsonProperty("data")
    public void setData(MSA data) {
        this.data = data;
    }

    public MSASetElement withData(MSA data) {
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
        return ((((((((("MSASetElement"+" [metadata=")+ metadata)+", ref=")+ ref)+", data=")+ data)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
