
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * One bar's reading — the SAME shape for EVERY bar, whether it's a persistent stat orb (hitpoints / adrenaline / prayer / summoning) or a transient bar detected on screen (a skilling action, a conjure timer, an activity bar, …). Fields that don't apply to a given bar are `null`; the shape never changes, so one code path reads them all.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "combo",
    "found",
    "fillPct",
    "value",
    "max",
    "text",
    "rect"
})
@Generated("jsonschema2pojo")
public class Bar {

    /**
     * Identifier. For the four stats it is the stat name. For a detected bar it is the registered friendly name (set via POST /api/bars/name) if one exists, else its colour-signature `combo`. Pass it to `bars.read(name)` to read just this bar.
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Identifier. For the four stats it is the stat name. For a detected bar it is the registered friendly name (set via POST /api/bars/name) if one exists, else its colour-signature `combo`. Pass it to `bars.read(name)` to read just this bar.")
    private String name;
    /**
     * Colour-signature type id for a detected bar (stable across frames as the bar fills or moves); `null` for the four stat bars.
     * (Required)
     * 
     */
    @JsonProperty("combo")
    @JsonPropertyDescription("Colour-signature type id for a detected bar (stable across frames as the bar fills or moves); `null` for the four stat bars.")
    private String combo;
    /**
     * True when the bar is currently drawn on screen.
     * (Required)
     * 
     */
    @JsonProperty("found")
    @JsonPropertyDescription("True when the bar is currently drawn on screen.")
    private Boolean found;
    /**
     * Exact fill 0–100 read from the bar's GPU geometry (the drawn fill width). Present whenever the bar is on screen — even when no number is drawn — so this is the value to use for %-based logic such as alert thresholds.
     * (Required)
     * 
     */
    @JsonProperty("fillPct")
    @JsonPropertyDescription("Exact fill 0\u2013100 read from the bar's GPU geometry (the drawn fill width). Present whenever the bar is on screen \u2014 even when no number is drawn \u2014 so this is the value to use for %-based logic such as alert thresholds.")
    private Double fillPct;
    /**
     * Current value, recovered from the digit glyphs the game itself draws at the bar (e.g. an orb's "10200"). Exact — matched against the font glyphs, not OCR — it only requires the digits to be on screen at capture. `null` when the bar draws no number.
     * (Required)
     * 
     */
    @JsonProperty("value")
    @JsonPropertyDescription("Current value, recovered from the digit glyphs the game itself draws at the bar (e.g. an orb's \"10200\"). Exact \u2014 matched against the font glyphs, not OCR \u2014 it only requires the digits to be on screen at capture. `null` when the bar draws no number.")
    private Double value;
    /**
     * Max value when the bar shows "current / max" (e.g. hitpoints 10200/10200 → 10200); `null` when only a single number (or none) is drawn.
     * (Required)
     * 
     */
    @JsonProperty("max")
    @JsonPropertyDescription("Max value when the bar shows \"current / max\" (e.g. hitpoints 10200/10200 \u2192 10200); `null` when only a single number (or none) is drawn.")
    private Double max;
    /**
     * Raw recognised text incl. separators, e.g. "10200/10200" or "100%"; `null` when none.
     * (Required)
     * 
     */
    @JsonProperty("text")
    @JsonPropertyDescription("Raw recognised text incl. separators, e.g. \"10200/10200\" or \"100%\"; `null` when none.")
    private String text;
    /**
     * The bar's screen rect in window px, or `null` when it is not on screen.
     * (Required)
     * 
     */
    @JsonProperty("rect")
    @JsonPropertyDescription("The bar's screen rect in window px, or `null` when it is not on screen.")
    private Object rect;

    /**
     * Identifier. For the four stats it is the stat name. For a detected bar it is the registered friendly name (set via POST /api/bars/name) if one exists, else its colour-signature `combo`. Pass it to `bars.read(name)` to read just this bar.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Colour-signature type id for a detected bar (stable across frames as the bar fills or moves); `null` for the four stat bars.
     * (Required)
     * 
     */
    @JsonProperty("combo")
    public String getCombo() {
        return combo;
    }

    /**
     * True when the bar is currently drawn on screen.
     * (Required)
     * 
     */
    @JsonProperty("found")
    public Boolean getFound() {
        return found;
    }

    /**
     * Exact fill 0–100 read from the bar's GPU geometry (the drawn fill width). Present whenever the bar is on screen — even when no number is drawn — so this is the value to use for %-based logic such as alert thresholds.
     * (Required)
     * 
     */
    @JsonProperty("fillPct")
    public Double getFillPct() {
        return fillPct;
    }

    /**
     * Current value, recovered from the digit glyphs the game itself draws at the bar (e.g. an orb's "10200"). Exact — matched against the font glyphs, not OCR — it only requires the digits to be on screen at capture. `null` when the bar draws no number.
     * (Required)
     * 
     */
    @JsonProperty("value")
    public Double getValue() {
        return value;
    }

    /**
     * Max value when the bar shows "current / max" (e.g. hitpoints 10200/10200 → 10200); `null` when only a single number (or none) is drawn.
     * (Required)
     * 
     */
    @JsonProperty("max")
    public Double getMax() {
        return max;
    }

    /**
     * Raw recognised text incl. separators, e.g. "10200/10200" or "100%"; `null` when none.
     * (Required)
     * 
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * The bar's screen rect in window px, or `null` when it is not on screen.
     * (Required)
     * 
     */
    @JsonProperty("rect")
    public Object getRect() {
        return rect;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Bar.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("combo");
        sb.append('=');
        sb.append(((this.combo == null)?"<null>":this.combo));
        sb.append(',');
        sb.append("found");
        sb.append('=');
        sb.append(((this.found == null)?"<null>":this.found));
        sb.append(',');
        sb.append("fillPct");
        sb.append('=');
        sb.append(((this.fillPct == null)?"<null>":this.fillPct));
        sb.append(',');
        sb.append("value");
        sb.append('=');
        sb.append(((this.value == null)?"<null>":this.value));
        sb.append(',');
        sb.append("max");
        sb.append('=');
        sb.append(((this.max == null)?"<null>":this.max));
        sb.append(',');
        sb.append("text");
        sb.append('=');
        sb.append(((this.text == null)?"<null>":this.text));
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
        result = ((result* 31)+((this.rect == null)? 0 :this.rect.hashCode()));
        result = ((result* 31)+((this.fillPct == null)? 0 :this.fillPct.hashCode()));
        result = ((result* 31)+((this.found == null)? 0 :this.found.hashCode()));
        result = ((result* 31)+((this.max == null)? 0 :this.max.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.combo == null)? 0 :this.combo.hashCode()));
        result = ((result* 31)+((this.text == null)? 0 :this.text.hashCode()));
        result = ((result* 31)+((this.value == null)? 0 :this.value.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Bar) == false) {
            return false;
        }
        Bar rhs = ((Bar) other);
        return (((((((((this.rect == rhs.rect)||((this.rect!= null)&&this.rect.equals(rhs.rect)))&&((this.fillPct == rhs.fillPct)||((this.fillPct!= null)&&this.fillPct.equals(rhs.fillPct))))&&((this.found == rhs.found)||((this.found!= null)&&this.found.equals(rhs.found))))&&((this.max == rhs.max)||((this.max!= null)&&this.max.equals(rhs.max))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.combo == rhs.combo)||((this.combo!= null)&&this.combo.equals(rhs.combo))))&&((this.text == rhs.text)||((this.text!= null)&&this.text.equals(rhs.text))))&&((this.value == rhs.value)||((this.value!= null)&&this.value.equals(rhs.value))));
    }

}
