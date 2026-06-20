
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Result of reading the status bars (GET /api/bars). `bars` is always the four bars in a fixed order (hitpoints, adrenaline, prayer, summoning); check each bar's `found` / `value`.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ok",
    "stale",
    "ageMs",
    "bars"
})
@Generated("jsonschema2pojo")
public class BarsReadResult {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ok")
    private Boolean ok;
    /**
     * False whenever a fresh capture happened on this call.
     * (Required)
     * 
     */
    @JsonProperty("stale")
    @JsonPropertyDescription("False whenever a fresh capture happened on this call.")
    private Boolean stale;
    /**
     * Age of the cached glyph set this read used, in ms.
     * (Required)
     * 
     */
    @JsonProperty("ageMs")
    @JsonPropertyDescription("Age of the cached glyph set this read used, in ms.")
    private Double ageMs;
    /**
     * The four status bars, in fixed order.
     * (Required)
     * 
     */
    @JsonProperty("bars")
    @JsonPropertyDescription("The four status bars, in fixed order.")
    private List<BarValue> bars = new ArrayList<BarValue>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ok")
    public Boolean getOk() {
        return ok;
    }

    /**
     * False whenever a fresh capture happened on this call.
     * (Required)
     * 
     */
    @JsonProperty("stale")
    public Boolean getStale() {
        return stale;
    }

    /**
     * Age of the cached glyph set this read used, in ms.
     * (Required)
     * 
     */
    @JsonProperty("ageMs")
    public Double getAgeMs() {
        return ageMs;
    }

    /**
     * The four status bars, in fixed order.
     * (Required)
     * 
     */
    @JsonProperty("bars")
    public List<BarValue> getBars() {
        return bars;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BarsReadResult.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ok");
        sb.append('=');
        sb.append(((this.ok == null)?"<null>":this.ok));
        sb.append(',');
        sb.append("stale");
        sb.append('=');
        sb.append(((this.stale == null)?"<null>":this.stale));
        sb.append(',');
        sb.append("ageMs");
        sb.append('=');
        sb.append(((this.ageMs == null)?"<null>":this.ageMs));
        sb.append(',');
        sb.append("bars");
        sb.append('=');
        sb.append(((this.bars == null)?"<null>":this.bars));
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
        result = ((result* 31)+((this.ageMs == null)? 0 :this.ageMs.hashCode()));
        result = ((result* 31)+((this.stale == null)? 0 :this.stale.hashCode()));
        result = ((result* 31)+((this.ok == null)? 0 :this.ok.hashCode()));
        result = ((result* 31)+((this.bars == null)? 0 :this.bars.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BarsReadResult) == false) {
            return false;
        }
        BarsReadResult rhs = ((BarsReadResult) other);
        return (((((this.ageMs == rhs.ageMs)||((this.ageMs!= null)&&this.ageMs.equals(rhs.ageMs)))&&((this.stale == rhs.stale)||((this.stale!= null)&&this.stale.equals(rhs.stale))))&&((this.ok == rhs.ok)||((this.ok!= null)&&this.ok.equals(rhs.ok))))&&((this.bars == rhs.bars)||((this.bars!= null)&&this.bars.equals(rhs.bars))));
    }

}
