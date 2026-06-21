
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Result of GET /api/progress. Detects every progress bar by shape, identifies the TYPE by colour signature (`combo`, or a friendly `name` if registered), measures fill %, and tracks begin/end per type. Pass `?name=` / `?combo=` to read just one bar type.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ok",
    "ageMs",
    "count",
    "bars",
    "groups",
    "began",
    "ended"
})
@Generated("jsonschema2pojo")
public class ProgressReadResult {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ok")
    private Boolean ok;
    /**
     * Age of the cached glyph set this read used, in ms.
     * (Required)
     * 
     */
    @JsonProperty("ageMs")
    @JsonPropertyDescription("Age of the cached glyph set this read used, in ms.")
    private Double ageMs;
    /**
     * Number of bar TYPES on screen (== groups.length).
     * (Required)
     * 
     */
    @JsonProperty("count")
    @JsonPropertyDescription("Number of bar TYPES on screen (== groups.length).")
    private Double count;
    /**
     * Raw per-bar snapshot this frame.
     * (Required)
     * 
     */
    @JsonProperty("bars")
    @JsonPropertyDescription("Raw per-bar snapshot this frame.")
    private List<ProgressBar> bars = new ArrayList<ProgressBar>();
    /**
     * Per-type aggregate (count + each fill %).
     * (Required)
     * 
     */
    @JsonProperty("groups")
    @JsonPropertyDescription("Per-type aggregate (count + each fill %).")
    private List<ProgressGroup> groups = new ArrayList<ProgressGroup>();
    /**
     * Bar types that appeared this poll.
     * (Required)
     * 
     */
    @JsonProperty("began")
    @JsonPropertyDescription("Bar types that appeared this poll.")
    private List<ProgressBegan> began = new ArrayList<ProgressBegan>();
    /**
     * Bar types that went fully gone this poll.
     * (Required)
     * 
     */
    @JsonProperty("ended")
    @JsonPropertyDescription("Bar types that went fully gone this poll.")
    private List<ProgressEnded> ended = new ArrayList<ProgressEnded>();

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
     * Age of the cached glyph set this read used, in ms.
     * (Required)
     * 
     */
    @JsonProperty("ageMs")
    public Double getAgeMs() {
        return ageMs;
    }

    /**
     * Number of bar TYPES on screen (== groups.length).
     * (Required)
     * 
     */
    @JsonProperty("count")
    public Double getCount() {
        return count;
    }

    /**
     * Raw per-bar snapshot this frame.
     * (Required)
     * 
     */
    @JsonProperty("bars")
    public List<ProgressBar> getBars() {
        return bars;
    }

    /**
     * Per-type aggregate (count + each fill %).
     * (Required)
     * 
     */
    @JsonProperty("groups")
    public List<ProgressGroup> getGroups() {
        return groups;
    }

    /**
     * Bar types that appeared this poll.
     * (Required)
     * 
     */
    @JsonProperty("began")
    public List<ProgressBegan> getBegan() {
        return began;
    }

    /**
     * Bar types that went fully gone this poll.
     * (Required)
     * 
     */
    @JsonProperty("ended")
    public List<ProgressEnded> getEnded() {
        return ended;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ProgressReadResult.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ok");
        sb.append('=');
        sb.append(((this.ok == null)?"<null>":this.ok));
        sb.append(',');
        sb.append("ageMs");
        sb.append('=');
        sb.append(((this.ageMs == null)?"<null>":this.ageMs));
        sb.append(',');
        sb.append("count");
        sb.append('=');
        sb.append(((this.count == null)?"<null>":this.count));
        sb.append(',');
        sb.append("bars");
        sb.append('=');
        sb.append(((this.bars == null)?"<null>":this.bars));
        sb.append(',');
        sb.append("groups");
        sb.append('=');
        sb.append(((this.groups == null)?"<null>":this.groups));
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
        result = ((result* 31)+((this.began == null)? 0 :this.began.hashCode()));
        result = ((result* 31)+((this.count == null)? 0 :this.count.hashCode()));
        result = ((result* 31)+((this.ended == null)? 0 :this.ended.hashCode()));
        result = ((result* 31)+((this.groups == null)? 0 :this.groups.hashCode()));
        result = ((result* 31)+((this.ok == null)? 0 :this.ok.hashCode()));
        result = ((result* 31)+((this.bars == null)? 0 :this.bars.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProgressReadResult) == false) {
            return false;
        }
        ProgressReadResult rhs = ((ProgressReadResult) other);
        return ((((((((this.ageMs == rhs.ageMs)||((this.ageMs!= null)&&this.ageMs.equals(rhs.ageMs)))&&((this.began == rhs.began)||((this.began!= null)&&this.began.equals(rhs.began))))&&((this.count == rhs.count)||((this.count!= null)&&this.count.equals(rhs.count))))&&((this.ended == rhs.ended)||((this.ended!= null)&&this.ended.equals(rhs.ended))))&&((this.groups == rhs.groups)||((this.groups!= null)&&this.groups.equals(rhs.groups))))&&((this.ok == rhs.ok)||((this.ok!= null)&&this.ok.equals(rhs.ok))))&&((this.bars == rhs.bars)||((this.bars!= null)&&this.bars.equals(rhs.bars))));
    }

}
