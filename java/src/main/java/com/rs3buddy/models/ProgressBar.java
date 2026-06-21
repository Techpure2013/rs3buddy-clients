
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * One progress bar detected this frame (raw snapshot; no stable id — bars may move).
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "combo",
    "name",
    "x",
    "y",
    "w",
    "percent",
    "confident"
})
@Generated("jsonschema2pojo")
public class ProgressBar {

    /**
     * Colour-signature key identifying the bar TYPE (sorted colorHashes).
     * (Required)
     * 
     */
    @JsonProperty("combo")
    @JsonPropertyDescription("Colour-signature key identifying the bar TYPE (sorted colorHashes).")
    private String combo;
    /**
     * Friendly name for this combo if one is registered, else null.
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Friendly name for this combo if one is registered, else null.")
    private String name;
    /**
     * Screen x of the bar's left edge (window px).
     * (Required)
     * 
     */
    @JsonProperty("x")
    @JsonPropertyDescription("Screen x of the bar's left edge (window px).")
    private Double x;
    /**
     * Screen y (window px).
     * (Required)
     * 
     */
    @JsonProperty("y")
    @JsonPropertyDescription("Screen y (window px).")
    private Double y;
    /**
     * Drawn width (window px).
     * (Required)
     * 
     */
    @JsonProperty("w")
    @JsonPropertyDescription("Drawn width (window px).")
    private Double w;
    /**
     * Fill percent, 0-100.
     * (Required)
     * 
     */
    @JsonProperty("percent")
    @JsonPropertyDescription("Fill percent, 0-100.")
    private Double percent;
    /**
     * True once both a track + fill colour have been seen (a real bar, not a stray piece).
     * (Required)
     * 
     */
    @JsonProperty("confident")
    @JsonPropertyDescription("True once both a track + fill colour have been seen (a real bar, not a stray piece).")
    private Boolean confident;

    /**
     * Colour-signature key identifying the bar TYPE (sorted colorHashes).
     * (Required)
     * 
     */
    @JsonProperty("combo")
    public String getCombo() {
        return combo;
    }

    /**
     * Friendly name for this combo if one is registered, else null.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Screen x of the bar's left edge (window px).
     * (Required)
     * 
     */
    @JsonProperty("x")
    public Double getX() {
        return x;
    }

    /**
     * Screen y (window px).
     * (Required)
     * 
     */
    @JsonProperty("y")
    public Double getY() {
        return y;
    }

    /**
     * Drawn width (window px).
     * (Required)
     * 
     */
    @JsonProperty("w")
    public Double getW() {
        return w;
    }

    /**
     * Fill percent, 0-100.
     * (Required)
     * 
     */
    @JsonProperty("percent")
    public Double getPercent() {
        return percent;
    }

    /**
     * True once both a track + fill colour have been seen (a real bar, not a stray piece).
     * (Required)
     * 
     */
    @JsonProperty("confident")
    public Boolean getConfident() {
        return confident;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ProgressBar.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("combo");
        sb.append('=');
        sb.append(((this.combo == null)?"<null>":this.combo));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("x");
        sb.append('=');
        sb.append(((this.x == null)?"<null>":this.x));
        sb.append(',');
        sb.append("y");
        sb.append('=');
        sb.append(((this.y == null)?"<null>":this.y));
        sb.append(',');
        sb.append("w");
        sb.append('=');
        sb.append(((this.w == null)?"<null>":this.w));
        sb.append(',');
        sb.append("percent");
        sb.append('=');
        sb.append(((this.percent == null)?"<null>":this.percent));
        sb.append(',');
        sb.append("confident");
        sb.append('=');
        sb.append(((this.confident == null)?"<null>":this.confident));
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
        result = ((result* 31)+((this.w == null)? 0 :this.w.hashCode()));
        result = ((result* 31)+((this.confident == null)? 0 :this.confident.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.x == null)? 0 :this.x.hashCode()));
        result = ((result* 31)+((this.combo == null)? 0 :this.combo.hashCode()));
        result = ((result* 31)+((this.y == null)? 0 :this.y.hashCode()));
        result = ((result* 31)+((this.percent == null)? 0 :this.percent.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProgressBar) == false) {
            return false;
        }
        ProgressBar rhs = ((ProgressBar) other);
        return ((((((((this.w == rhs.w)||((this.w!= null)&&this.w.equals(rhs.w)))&&((this.confident == rhs.confident)||((this.confident!= null)&&this.confident.equals(rhs.confident))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.x == rhs.x)||((this.x!= null)&&this.x.equals(rhs.x))))&&((this.combo == rhs.combo)||((this.combo!= null)&&this.combo.equals(rhs.combo))))&&((this.y == rhs.y)||((this.y!= null)&&this.y.equals(rhs.y))))&&((this.percent == rhs.percent)||((this.percent!= null)&&this.percent.equals(rhs.percent))));
    }

}
