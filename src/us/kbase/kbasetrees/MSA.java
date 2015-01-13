
package us.kbase.kbasetrees;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import us.kbase.common.service.Tuple4;


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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "description",
    "sequence_type",
    "alignment_length",
    "alignment",
    "trim_info",
    "alignment_attributes",
    "row_order",
    "default_row_labels",
    "ws_refs",
    "kb_refs",
    "parent_msa_ref"
})
public class MSA {

    @JsonProperty("name")
    private java.lang.String name;
    @JsonProperty("description")
    private java.lang.String description;
    @JsonProperty("sequence_type")
    private java.lang.String sequenceType;
    @JsonProperty("alignment_length")
    private java.lang.Long alignmentLength;
    @JsonProperty("alignment")
    private Map<String, String> alignment;
    @JsonProperty("trim_info")
    private Map<String, Tuple4 <Long, Long, Long, String>> trimInfo;
    @JsonProperty("alignment_attributes")
    private Map<String, String> alignmentAttributes;
    @JsonProperty("row_order")
    private List<String> rowOrder;
    @JsonProperty("default_row_labels")
    private Map<String, String> defaultRowLabels;
    @JsonProperty("ws_refs")
    private Map<String, Map<String, List<String>>> wsRefs;
    @JsonProperty("kb_refs")
    private Map<String, Map<String, List<String>>> kbRefs;
    @JsonProperty("parent_msa_ref")
    private java.lang.String parentMsaRef;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public MSA withName(java.lang.String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("description")
    public java.lang.String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public MSA withDescription(java.lang.String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("sequence_type")
    public java.lang.String getSequenceType() {
        return sequenceType;
    }

    @JsonProperty("sequence_type")
    public void setSequenceType(java.lang.String sequenceType) {
        this.sequenceType = sequenceType;
    }

    public MSA withSequenceType(java.lang.String sequenceType) {
        this.sequenceType = sequenceType;
        return this;
    }

    @JsonProperty("alignment_length")
    public java.lang.Long getAlignmentLength() {
        return alignmentLength;
    }

    @JsonProperty("alignment_length")
    public void setAlignmentLength(java.lang.Long alignmentLength) {
        this.alignmentLength = alignmentLength;
    }

    public MSA withAlignmentLength(java.lang.Long alignmentLength) {
        this.alignmentLength = alignmentLength;
        return this;
    }

    @JsonProperty("alignment")
    public Map<String, String> getAlignment() {
        return alignment;
    }

    @JsonProperty("alignment")
    public void setAlignment(Map<String, String> alignment) {
        this.alignment = alignment;
    }

    public MSA withAlignment(Map<String, String> alignment) {
        this.alignment = alignment;
        return this;
    }

    @JsonProperty("trim_info")
    public Map<String, Tuple4 <Long, Long, Long, String>> getTrimInfo() {
        return trimInfo;
    }

    @JsonProperty("trim_info")
    public void setTrimInfo(Map<String, Tuple4 <Long, Long, Long, String>> trimInfo) {
        this.trimInfo = trimInfo;
    }

    public MSA withTrimInfo(Map<String, Tuple4 <Long, Long, Long, String>> trimInfo) {
        this.trimInfo = trimInfo;
        return this;
    }

    @JsonProperty("alignment_attributes")
    public Map<String, String> getAlignmentAttributes() {
        return alignmentAttributes;
    }

    @JsonProperty("alignment_attributes")
    public void setAlignmentAttributes(Map<String, String> alignmentAttributes) {
        this.alignmentAttributes = alignmentAttributes;
    }

    public MSA withAlignmentAttributes(Map<String, String> alignmentAttributes) {
        this.alignmentAttributes = alignmentAttributes;
        return this;
    }

    @JsonProperty("row_order")
    public List<String> getRowOrder() {
        return rowOrder;
    }

    @JsonProperty("row_order")
    public void setRowOrder(List<String> rowOrder) {
        this.rowOrder = rowOrder;
    }

    public MSA withRowOrder(List<String> rowOrder) {
        this.rowOrder = rowOrder;
        return this;
    }

    @JsonProperty("default_row_labels")
    public Map<String, String> getDefaultRowLabels() {
        return defaultRowLabels;
    }

    @JsonProperty("default_row_labels")
    public void setDefaultRowLabels(Map<String, String> defaultRowLabels) {
        this.defaultRowLabels = defaultRowLabels;
    }

    public MSA withDefaultRowLabels(Map<String, String> defaultRowLabels) {
        this.defaultRowLabels = defaultRowLabels;
        return this;
    }

    @JsonProperty("ws_refs")
    public Map<String, Map<String, List<String>>> getWsRefs() {
        return wsRefs;
    }

    @JsonProperty("ws_refs")
    public void setWsRefs(Map<String, Map<String, List<String>>> wsRefs) {
        this.wsRefs = wsRefs;
    }

    public MSA withWsRefs(Map<String, Map<String, List<String>>> wsRefs) {
        this.wsRefs = wsRefs;
        return this;
    }

    @JsonProperty("kb_refs")
    public Map<String, Map<String, List<String>>> getKbRefs() {
        return kbRefs;
    }

    @JsonProperty("kb_refs")
    public void setKbRefs(Map<String, Map<String, List<String>>> kbRefs) {
        this.kbRefs = kbRefs;
    }

    public MSA withKbRefs(Map<String, Map<String, List<String>>> kbRefs) {
        this.kbRefs = kbRefs;
        return this;
    }

    @JsonProperty("parent_msa_ref")
    public java.lang.String getParentMsaRef() {
        return parentMsaRef;
    }

    @JsonProperty("parent_msa_ref")
    public void setParentMsaRef(java.lang.String parentMsaRef) {
        this.parentMsaRef = parentMsaRef;
    }

    public MSA withParentMsaRef(java.lang.String parentMsaRef) {
        this.parentMsaRef = parentMsaRef;
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
        return ((((((((((((((((((((((((((("MSA"+" [name=")+ name)+", description=")+ description)+", sequenceType=")+ sequenceType)+", alignmentLength=")+ alignmentLength)+", alignment=")+ alignment)+", trimInfo=")+ trimInfo)+", alignmentAttributes=")+ alignmentAttributes)+", rowOrder=")+ rowOrder)+", defaultRowLabels=")+ defaultRowLabels)+", wsRefs=")+ wsRefs)+", kbRefs=")+ kbRefs)+", parentMsaRef=")+ parentMsaRef)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
