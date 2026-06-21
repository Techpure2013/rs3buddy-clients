
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Per bar-TYPE aggregate: how many of this type are on screen + each one's fill.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "combo",
    "name",
    "count",
    "stableCount",
    "percents",
    "minPercent",
    "maxPercent",
    "confident"
})
@Generated("jsonschema2pojo")
public class ProgressGroup {

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
     * Bars of this type detected THIS frame (can dip on a flickered frame).
     * (Required)
     * 
     */
    @JsonProperty("count")
    @JsonPropertyDescription("Bars of this type detected THIS frame (can dip on a flickered frame).")
    private Double count;
    /**
     * Flicker-proof count: the max seen across the grace window.
     * (Required)
     * 
     */
    @JsonProperty("stableCount")
    @JsonPropertyDescription("Flicker-proof count: the max seen across the grace window.")
    private Double stableCount;
    /**
     * Each bar's fill percent, highest first.
     * (Required)
     * 
     */
    @JsonProperty("percents")
    @JsonPropertyDescription("Each bar's fill percent, highest first.")
    private List<Double> percents = new ArrayList<Double>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("minPercent")
    private Double minPercent;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("maxPercent")
    private Double maxPercent;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("confident")
    private Boolean confident;

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
     * Bars of this type detected THIS frame (can dip on a flickered frame).
     * (Required)
     * 
     */
    @JsonProperty("count")
    public Double getCount() {
        return count;
    }

    /**
     * Flicker-proof count: the max seen across the grace window.
     * (Required)
     * 
     */
    @JsonProperty("stableCount")
    public Double getStableCount() {
        return stableCount;
    }

    /**
     * Each bar's fill percent, highest first.
     * (Required)
     * 
     */
    @JsonProperty("percents")
    public List<Double> getPercents() {
        return percents;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("minPercent")
    public Double getMinPercent() {
        return minPercent;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("maxPercent")
    public Double getMaxPercent() {
        return maxPercent;
    }

    /**
     * 
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
        sb.append(ProgressGroup.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("combo");
        sb.append('=');
        sb.append(((this.combo == null)?"<null>":this.combo));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("count");
        sb.append('=');
        sb.append(((this.count == null)?"<null>":this.count));
        sb.append(',');
        sb.append("stableCount");
        sb.append('=');
        sb.append(((this.stableCount == null)?"<null>":this.stableCount));
        sb.append(',');
        sb.append("percents");
        sb.append('=');
        sb.append(((this.percents == null)?"<null>":this.percents));
        sb.append(',');
        sb.append("minPercent");
        sb.append('=');
        sb.append(((this.minPercent == null)?"<null>":this.minPercent));
        sb.append(',');
        sb.append("maxPercent");
        sb.append('=');
        sb.append(((this.maxPercent == null)?"<null>":this.maxPercent));
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
        result = ((result* 31)+((this.maxPercent == null)? 0 :this.maxPercent.hashCode()));
        result = ((result* 31)+((this.stableCount == null)? 0 :this.stableCount.hashCode()));
        result = ((result* 31)+((this.confident == null)? 0 :this.confident.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.count == null)? 0 :this.count.hashCode()));
        result = ((result* 31)+((this.combo == null)? 0 :this.combo.hashCode()));
        result = ((result* 31)+((this.percents == null)? 0 :this.percents.hashCode()));
        result = ((result* 31)+((this.minPercent == null)? 0 :this.minPercent.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProgressGroup) == false) {
            return false;
        }
        ProgressGroup rhs = ((ProgressGroup) other);
        return (((((((((this.maxPercent == rhs.maxPercent)||((this.maxPercent!= null)&&this.maxPercent.equals(rhs.maxPercent)))&&((this.stableCount == rhs.stableCount)||((this.stableCount!= null)&&this.stableCount.equals(rhs.stableCount))))&&((this.confident == rhs.confident)||((this.confident!= null)&&this.confident.equals(rhs.confident))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.count == rhs.count)||((this.count!= null)&&this.count.equals(rhs.count))))&&((this.combo == rhs.combo)||((this.combo!= null)&&this.combo.equals(rhs.combo))))&&((this.percents == rhs.percents)||((this.percents!= null)&&this.percents.equals(rhs.percents))))&&((this.minPercent == rhs.minPercent)||((this.minPercent!= null)&&this.minPercent.equals(rhs.minPercent))));
    }

}
