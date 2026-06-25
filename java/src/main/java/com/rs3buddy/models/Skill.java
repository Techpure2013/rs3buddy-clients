
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * One skill cell on the skills interface — anchored on the skill's icon, with the two level numbers drawn beside it read out exactly (glyph-matched, not OCR).
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "level",
    "base",
    "rect"
})
@Generated("jsonschema2pojo")
public class Skill {

    /**
     * Skill name — one of  {@link  SkillName }  (e.g. "attack", "herblore", "necromancy"). Typed as a plain string on the result so an unexpected trained name never breaks a read; pass a `SkillName` to `skills.read(name)`.
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Skill name \u2014 one of  {@link  SkillName }  (e.g. \"attack\", \"herblore\", \"necromancy\"). Typed as a plain string on the result so an unexpected trained name never breaks a read; pass a `SkillName` to `skills.read(name)`.")
    private String name;
    /**
     * Current (live) level — equals `base` normally, LOWER when a debuff drains the skill, HIGHER when a potion boosts it. Use this for live logic. `null` when no level number is drawn.
     * (Required)
     * 
     */
    @JsonProperty("level")
    @JsonPropertyDescription("Current (live) level \u2014 equals `base` normally, LOWER when a debuff drains the skill, HIGHER when a potion boosts it. Use this for live logic. `null` when no level number is drawn.")
    private Double level;
    /**
     * Base (trained) level — what the skill sits at unbuffed. `null` when none is drawn.
     * (Required)
     * 
     */
    @JsonProperty("base")
    @JsonPropertyDescription("Base (trained) level \u2014 what the skill sits at unbuffed. `null` when none is drawn.")
    private Double base;
    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("rect")
    @JsonPropertyDescription("A located UI rect in window px (top-left x/y + size).")
    private SkillRect rect;

    /**
     * Skill name — one of  {@link  SkillName }  (e.g. "attack", "herblore", "necromancy"). Typed as a plain string on the result so an unexpected trained name never breaks a read; pass a `SkillName` to `skills.read(name)`.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Current (live) level — equals `base` normally, LOWER when a debuff drains the skill, HIGHER when a potion boosts it. Use this for live logic. `null` when no level number is drawn.
     * (Required)
     * 
     */
    @JsonProperty("level")
    public Double getLevel() {
        return level;
    }

    /**
     * Base (trained) level — what the skill sits at unbuffed. `null` when none is drawn.
     * (Required)
     * 
     */
    @JsonProperty("base")
    public Double getBase() {
        return base;
    }

    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("rect")
    public SkillRect getRect() {
        return rect;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Skill.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("level");
        sb.append('=');
        sb.append(((this.level == null)?"<null>":this.level));
        sb.append(',');
        sb.append("base");
        sb.append('=');
        sb.append(((this.base == null)?"<null>":this.base));
        sb.append(',');
        sb.append("rect");
        sb.append('=');
        sb.append(((this.rect == null)?"<null>":this.rect));
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
        result = ((result* 31)+((this.rect == null)? 0 :this.rect.hashCode()));
        result = ((result* 31)+((this.level == null)? 0 :this.level.hashCode()));
        result = ((result* 31)+((this.base == null)? 0 :this.base.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Skill) == false) {
            return false;
        }
        Skill rhs = ((Skill) other);
        return (((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.rect == rhs.rect)||((this.rect!= null)&&this.rect.equals(rhs.rect))))&&((this.level == rhs.level)||((this.level!= null)&&this.level.equals(rhs.level))))&&((this.base == rhs.base)||((this.base!= null)&&this.base.equals(rhs.base))));
    }

}
