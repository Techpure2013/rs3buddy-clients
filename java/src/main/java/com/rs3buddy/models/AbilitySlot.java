
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * One recognised action-bar slot.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "rect",
    "atlas",
    "activating",
    "onCooldown",
    "cooldownText",
    "cooldownSeconds",
    "usable",
    "color"
})
@Generated("jsonschema2pojo")
public class AbilitySlot {

    /**
     * Ability id WITHOUT the "ability:" prefix, e.g. "anticipation".
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Ability id WITHOUT the \"ability:\" prefix, e.g. \"anticipation\".")
    private String name;
    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("rect")
    @JsonPropertyDescription("A located UI rect in window px (top-left x/y + size).")
    private AbilityRect rect;
    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("atlas")
    @JsonPropertyDescription("A located UI rect in window px (top-left x/y + size).")
    private AbilityRect atlas;
    /**
     * True on the frame(s) RS3 paints the activation-flash sweep over this slot. NOTE: the sweep is the GLOBAL-cooldown animation — it flashes across the WHOLE bar on any cast, so it is NOT per-ability. To tell which slot actually fired, use the cooldown timer (`onCooldown`), not this.
     * (Required)
     * 
     */
    @JsonProperty("activating")
    @JsonPropertyDescription("True on the frame(s) RS3 paints the activation-flash sweep over this slot. NOTE: the sweep is the GLOBAL-cooldown animation \u2014 it flashes across the WHOLE bar on any cast, so it is NOT per-ability. To tell which slot actually fired, use the cooldown timer (`onCooldown`), not this.")
    private Boolean activating;
    /**
     * True when a cooldown number overlaps the slot.
     * (Required)
     * 
     */
    @JsonProperty("onCooldown")
    @JsonPropertyDescription("True when a cooldown number overlaps the slot.")
    private Boolean onCooldown;
    /**
     * Recognised cooldown text, e.g. "5" or "1:23"; "" when none.
     * (Required)
     * 
     */
    @JsonProperty("cooldownText")
    @JsonPropertyDescription("Recognised cooldown text, e.g. \"5\" or \"1:23\"; \"\" when none.")
    private String cooldownText;
    /**
     * Parsed cooldown in whole seconds, or null when unreadable / not on CD.
     * (Required)
     * 
     */
    @JsonProperty("cooldownSeconds")
    @JsonPropertyDescription("Parsed cooldown in whole seconds, or null when unreadable / not on CD.")
    private Double cooldownSeconds;
    /**
     * False when the icon is greyed out (not enough resource / unavailable). Derived from the per-vertex tint, so it depends on correct tint capture.
     * (Required)
     * 
     */
    @JsonProperty("usable")
    @JsonPropertyDescription("False when the icon is greyed out (not enough resource / unavailable). Derived from the per-vertex tint, so it depends on correct tint capture.")
    private Boolean usable;
    /**
     * The slot's per-vertex tint [r, g, b], 0-255 (a grey triple ⇒ unusable).
     * (Required)
     * 
     */
    @JsonProperty("color")
    @JsonPropertyDescription("The slot's per-vertex tint [r, g, b], 0-255 (a grey triple \u21d2 unusable).")
    private List<Double> color = new ArrayList<Double>();

    /**
     * Ability id WITHOUT the "ability:" prefix, e.g. "anticipation".
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("rect")
    public AbilityRect getRect() {
        return rect;
    }

    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("atlas")
    public AbilityRect getAtlas() {
        return atlas;
    }

    /**
     * True on the frame(s) RS3 paints the activation-flash sweep over this slot. NOTE: the sweep is the GLOBAL-cooldown animation — it flashes across the WHOLE bar on any cast, so it is NOT per-ability. To tell which slot actually fired, use the cooldown timer (`onCooldown`), not this.
     * (Required)
     * 
     */
    @JsonProperty("activating")
    public Boolean getActivating() {
        return activating;
    }

    /**
     * True when a cooldown number overlaps the slot.
     * (Required)
     * 
     */
    @JsonProperty("onCooldown")
    public Boolean getOnCooldown() {
        return onCooldown;
    }

    /**
     * Recognised cooldown text, e.g. "5" or "1:23"; "" when none.
     * (Required)
     * 
     */
    @JsonProperty("cooldownText")
    public String getCooldownText() {
        return cooldownText;
    }

    /**
     * Parsed cooldown in whole seconds, or null when unreadable / not on CD.
     * (Required)
     * 
     */
    @JsonProperty("cooldownSeconds")
    public Double getCooldownSeconds() {
        return cooldownSeconds;
    }

    /**
     * False when the icon is greyed out (not enough resource / unavailable). Derived from the per-vertex tint, so it depends on correct tint capture.
     * (Required)
     * 
     */
    @JsonProperty("usable")
    public Boolean getUsable() {
        return usable;
    }

    /**
     * The slot's per-vertex tint [r, g, b], 0-255 (a grey triple ⇒ unusable).
     * (Required)
     * 
     */
    @JsonProperty("color")
    public List<Double> getColor() {
        return color;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AbilitySlot.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("rect");
        sb.append('=');
        sb.append(((this.rect == null)?"<null>":this.rect));
        sb.append(',');
        sb.append("atlas");
        sb.append('=');
        sb.append(((this.atlas == null)?"<null>":this.atlas));
        sb.append(',');
        sb.append("activating");
        sb.append('=');
        sb.append(((this.activating == null)?"<null>":this.activating));
        sb.append(',');
        sb.append("onCooldown");
        sb.append('=');
        sb.append(((this.onCooldown == null)?"<null>":this.onCooldown));
        sb.append(',');
        sb.append("cooldownText");
        sb.append('=');
        sb.append(((this.cooldownText == null)?"<null>":this.cooldownText));
        sb.append(',');
        sb.append("cooldownSeconds");
        sb.append('=');
        sb.append(((this.cooldownSeconds == null)?"<null>":this.cooldownSeconds));
        sb.append(',');
        sb.append("usable");
        sb.append('=');
        sb.append(((this.usable == null)?"<null>":this.usable));
        sb.append(',');
        sb.append("color");
        sb.append('=');
        sb.append(((this.color == null)?"<null>":this.color));
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
        result = ((result* 31)+((this.rect == null)? 0 :this.rect.hashCode()));
        result = ((result* 31)+((this.usable == null)? 0 :this.usable.hashCode()));
        result = ((result* 31)+((this.color == null)? 0 :this.color.hashCode()));
        result = ((result* 31)+((this.atlas == null)? 0 :this.atlas.hashCode()));
        result = ((result* 31)+((this.onCooldown == null)? 0 :this.onCooldown.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.activating == null)? 0 :this.activating.hashCode()));
        result = ((result* 31)+((this.cooldownText == null)? 0 :this.cooldownText.hashCode()));
        result = ((result* 31)+((this.cooldownSeconds == null)? 0 :this.cooldownSeconds.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AbilitySlot) == false) {
            return false;
        }
        AbilitySlot rhs = ((AbilitySlot) other);
        return ((((((((((this.rect == rhs.rect)||((this.rect!= null)&&this.rect.equals(rhs.rect)))&&((this.usable == rhs.usable)||((this.usable!= null)&&this.usable.equals(rhs.usable))))&&((this.color == rhs.color)||((this.color!= null)&&this.color.equals(rhs.color))))&&((this.atlas == rhs.atlas)||((this.atlas!= null)&&this.atlas.equals(rhs.atlas))))&&((this.onCooldown == rhs.onCooldown)||((this.onCooldown!= null)&&this.onCooldown.equals(rhs.onCooldown))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.activating == rhs.activating)||((this.activating!= null)&&this.activating.equals(rhs.activating))))&&((this.cooldownText == rhs.cooldownText)||((this.cooldownText!= null)&&this.cooldownText.equals(rhs.cooldownText))))&&((this.cooldownSeconds == rhs.cooldownSeconds)||((this.cooldownSeconds!= null)&&this.cooldownSeconds.equals(rhs.cooldownSeconds))));
    }

}
