
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Result of reading the buff bar (GET /api/buffs). Buffs and debuffs are returned in separate arrays — both made of the same `Buff` shape — ALL from a single capture, so every reading is from the same frame.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ok",
    "stale",
    "ageMs",
    "buffs",
    "debuffs"
})
@Generated("jsonschema2pojo")
public class BuffsReadResult {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ok")
    private Boolean ok;
    /**
     * True when no fresh capture was available yet and the cache was empty.
     * (Required)
     * 
     */
    @JsonProperty("stale")
    @JsonPropertyDescription("True when no fresh capture was available yet and the cache was empty.")
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
     * Active buffs (green-bordered cells).
     * (Required)
     * 
     */
    @JsonProperty("buffs")
    @JsonPropertyDescription("Active buffs (green-bordered cells).")
    private List<Buff> buffs = new ArrayList<Buff>();
    /**
     * Active debuffs (red-bordered cells).
     * (Required)
     * 
     */
    @JsonProperty("debuffs")
    @JsonPropertyDescription("Active debuffs (red-bordered cells).")
    private List<Buff> debuffs = new ArrayList<Buff>();

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
     * True when no fresh capture was available yet and the cache was empty.
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
     * Active buffs (green-bordered cells).
     * (Required)
     * 
     */
    @JsonProperty("buffs")
    public List<Buff> getBuffs() {
        return buffs;
    }

    /**
     * Active debuffs (red-bordered cells).
     * (Required)
     * 
     */
    @JsonProperty("debuffs")
    public List<Buff> getDebuffs() {
        return debuffs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BuffsReadResult.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("buffs");
        sb.append('=');
        sb.append(((this.buffs == null)?"<null>":this.buffs));
        sb.append(',');
        sb.append("debuffs");
        sb.append('=');
        sb.append(((this.debuffs == null)?"<null>":this.debuffs));
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
        result = ((result* 31)+((this.buffs == null)? 0 :this.buffs.hashCode()));
        result = ((result* 31)+((this.debuffs == null)? 0 :this.debuffs.hashCode()));
        result = ((result* 31)+((this.ok == null)? 0 :this.ok.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BuffsReadResult) == false) {
            return false;
        }
        BuffsReadResult rhs = ((BuffsReadResult) other);
        return ((((((this.ageMs == rhs.ageMs)||((this.ageMs!= null)&&this.ageMs.equals(rhs.ageMs)))&&((this.stale == rhs.stale)||((this.stale!= null)&&this.stale.equals(rhs.stale))))&&((this.buffs == rhs.buffs)||((this.buffs!= null)&&this.buffs.equals(rhs.buffs))))&&((this.debuffs == rhs.debuffs)||((this.debuffs!= null)&&this.debuffs.equals(rhs.debuffs))))&&((this.ok == rhs.ok)||((this.ok!= null)&&this.ok.equals(rhs.ok))));
    }

}
