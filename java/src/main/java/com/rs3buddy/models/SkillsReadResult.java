
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Result of reading the skills interface (GET /api/skills) — one `Skill` per skill cell currently on screen, all from a single capture.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ok",
    "stale",
    "ageMs",
    "skills"
})
@Generated("jsonschema2pojo")
public class SkillsReadResult {

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
     * The skills currently on screen (each with current `level` + `base`).
     * (Required)
     * 
     */
    @JsonProperty("skills")
    @JsonPropertyDescription("The skills currently on screen (each with current `level` + `base`).")
    private List<Skill> skills = new ArrayList<Skill>();

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
     * The skills currently on screen (each with current `level` + `base`).
     * (Required)
     * 
     */
    @JsonProperty("skills")
    public List<Skill> getSkills() {
        return skills;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SkillsReadResult.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("skills");
        sb.append('=');
        sb.append(((this.skills == null)?"<null>":this.skills));
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
        result = ((result* 31)+((this.skills == null)? 0 :this.skills.hashCode()));
        result = ((result* 31)+((this.ageMs == null)? 0 :this.ageMs.hashCode()));
        result = ((result* 31)+((this.stale == null)? 0 :this.stale.hashCode()));
        result = ((result* 31)+((this.ok == null)? 0 :this.ok.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SkillsReadResult) == false) {
            return false;
        }
        SkillsReadResult rhs = ((SkillsReadResult) other);
        return (((((this.skills == rhs.skills)||((this.skills!= null)&&this.skills.equals(rhs.skills)))&&((this.ageMs == rhs.ageMs)||((this.ageMs!= null)&&this.ageMs.equals(rhs.ageMs))))&&((this.stale == rhs.stale)||((this.stale!= null)&&this.stale.equals(rhs.stale))))&&((this.ok == rhs.ok)||((this.ok!= null)&&this.ok.equals(rhs.ok))));
    }

}
