
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * A bar TYPE that just went fully gone this poll (count -> 0 past the grace window).
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "combo",
    "name",
    "maxPercent"
})
@Generated("jsonschema2pojo")
public class ProgressEnded {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("combo")
    private String combo;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * The highest fill % this type reached before it vanished.
     * (Required)
     * 
     */
    @JsonProperty("maxPercent")
    @JsonPropertyDescription("The highest fill % this type reached before it vanished.")
    private Double maxPercent;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("combo")
    public String getCombo() {
        return combo;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * The highest fill % this type reached before it vanished.
     * (Required)
     * 
     */
    @JsonProperty("maxPercent")
    public Double getMaxPercent() {
        return maxPercent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ProgressEnded.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("combo");
        sb.append('=');
        sb.append(((this.combo == null)?"<null>":this.combo));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("maxPercent");
        sb.append('=');
        sb.append(((this.maxPercent == null)?"<null>":this.maxPercent));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.maxPercent == null)? 0 :this.maxPercent.hashCode()));
        result = ((result* 31)+((this.combo == null)? 0 :this.combo.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProgressEnded) == false) {
            return false;
        }
        ProgressEnded rhs = ((ProgressEnded) other);
        return ((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.maxPercent == rhs.maxPercent)||((this.maxPercent!= null)&&this.maxPercent.equals(rhs.maxPercent))))&&((this.combo == rhs.combo)||((this.combo!= null)&&this.combo.equals(rhs.combo))));
    }

}
