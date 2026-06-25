
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Result of reading the bars (GET /api/bars). `bars` always starts with the four stat bars in fixed order (hitpoints, adrenaline, prayer, summoning), followed by every dynamic bar currently on screen — ALL from a single capture, so every reading is from the same frame (no two-endpoint drift). Check each bar's `found` / `fillPct` / `value`.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ok",
    "stale",
    "ageMs",
    "bars",
    "began",
    "ended"
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
     * The bars: the four stat bars (always present) followed by any dynamic bars on screen. Every entry has the same `Bar` shape.
     * (Required)
     * 
     */
    @JsonProperty("bars")
    @JsonPropertyDescription("The bars: the four stat bars (always present) followed by any dynamic bars on screen. Every entry has the same `Bar` shape.")
    private List<Bar> bars = new ArrayList<Bar>();
    /**
     * Names (or combos, when unnamed) of dynamic bar types that appeared on this poll.
     * (Required)
     * 
     */
    @JsonProperty("began")
    @JsonPropertyDescription("Names (or combos, when unnamed) of dynamic bar types that appeared on this poll.")
    private List<String> began = new ArrayList<String>();
    /**
     * Names (or combos, when unnamed) of dynamic bar types that vanished on this poll.
     * (Required)
     * 
     */
    @JsonProperty("ended")
    @JsonPropertyDescription("Names (or combos, when unnamed) of dynamic bar types that vanished on this poll.")
    private List<String> ended = new ArrayList<String>();

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
     * The bars: the four stat bars (always present) followed by any dynamic bars on screen. Every entry has the same `Bar` shape.
     * (Required)
     * 
     */
    @JsonProperty("bars")
    public List<Bar> getBars() {
        return bars;
    }

    /**
     * Names (or combos, when unnamed) of dynamic bar types that appeared on this poll.
     * (Required)
     * 
     */
    @JsonProperty("began")
    public List<String> getBegan() {
        return began;
    }

    /**
     * Names (or combos, when unnamed) of dynamic bar types that vanished on this poll.
     * (Required)
     * 
     */
    @JsonProperty("ended")
    public List<String> getEnded() {
        return ended;
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
        sb.append("began");
        sb.append('=');
        sb.append(((this.began == null)?"<null>":this.began));
        sb.append(',');
        sb.append("ended");
        sb.append('=');
        sb.append(((this.ended == null)?"<null>":this.ended));
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
        result = ((result* 31)+((this.began == null)? 0 :this.began.hashCode()));
        result = ((result* 31)+((this.ended == null)? 0 :this.ended.hashCode()));
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
        return (((((((this.ageMs == rhs.ageMs)||((this.ageMs!= null)&&this.ageMs.equals(rhs.ageMs)))&&((this.stale == rhs.stale)||((this.stale!= null)&&this.stale.equals(rhs.stale))))&&((this.began == rhs.began)||((this.began!= null)&&this.began.equals(rhs.began))))&&((this.ended == rhs.ended)||((this.ended!= null)&&this.ended.equals(rhs.ended))))&&((this.ok == rhs.ok)||((this.ok!= null)&&this.ok.equals(rhs.ok))))&&((this.bars == rhs.bars)||((this.bars!= null)&&this.bars.equals(rhs.bars))));
    }

}
