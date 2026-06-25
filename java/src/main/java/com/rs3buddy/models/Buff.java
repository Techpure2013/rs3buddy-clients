
package com.rs3buddy.models;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * One buff or debuff cell on the buff bar. Buffs and debuffs share the SAME shape — they're told apart by `kind` (the cell's border tint: green = buff, red = debuff) and returned in separate `buffs` / `debuffs` arrays by the read.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "kind",
    "name",
    "iconColorHash",
    "value",
    "text",
    "rect"
})
@Generated("jsonschema2pojo")
public class Buff {

    /**
     * "buff" (green-bordered cell) or "debuff" (red-bordered cell).
     * (Required)
     * 
     */
    @JsonProperty("kind")
    @JsonPropertyDescription("\"buff\" (green-bordered cell) or \"debuff\" (red-bordered cell).")
    private Buff.Kind kind;
    /**
     * Friendly effect name (e.g. "bone_shield", "prayer_renewal"), resolved from the icon's COLOUR signature; `null` when the icon isn't named yet. Train an unknown icon with `buffs.name(iconColorHash, "…")` (POST /api/buffs/name).
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("Friendly effect name (e.g. \"bone_shield\", \"prayer_renewal\"), resolved from the icon's COLOUR signature; `null` when the icon isn't named yet. Train an unknown icon with `buffs.name(iconColorHash, \"\u2026\")` (POST /api/buffs/name).")
    private String name;
    /**
     * Colour-hash identity of the cell's icon (stable across game sessions). This is the key you pass to `buffs.name()` to give the effect a `name`; `null` when no icon was found in the cell.
     * (Required)
     * 
     */
    @JsonProperty("iconColorHash")
    @JsonPropertyDescription("Colour-hash identity of the cell's icon (stable across game sessions). This is the key you pass to `buffs.name()` to give the effect a `name`; `null` when no icon was found in the cell.")
    private Double iconColorHash;
    /**
     * The numeric timer/stack drawn in the cell (e.g. a 50-second shield, 5 stacks), recovered from the digit glyphs the game draws — exact, not OCR. `null` when no number is drawn.
     * (Required)
     * 
     */
    @JsonProperty("value")
    @JsonPropertyDescription("The numeric timer/stack drawn in the cell (e.g. a 50-second shield, 5 stacks), recovered from the digit glyphs the game draws \u2014 exact, not OCR. `null` when no number is drawn.")
    private Double value;
    /**
     * Raw recognised text in the cell incl. non-digit chars (e.g. "5m"); `null` when none.
     * (Required)
     * 
     */
    @JsonProperty("text")
    @JsonPropertyDescription("Raw recognised text in the cell incl. non-digit chars (e.g. \"5m\"); `null` when none.")
    private String text;
    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("rect")
    @JsonPropertyDescription("A located UI rect in window px (top-left x/y + size).")
    private BuffRect rect;

    /**
     * "buff" (green-bordered cell) or "debuff" (red-bordered cell).
     * (Required)
     * 
     */
    @JsonProperty("kind")
    public Buff.Kind getKind() {
        return kind;
    }

    /**
     * Friendly effect name (e.g. "bone_shield", "prayer_renewal"), resolved from the icon's COLOUR signature; `null` when the icon isn't named yet. Train an unknown icon with `buffs.name(iconColorHash, "…")` (POST /api/buffs/name).
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Colour-hash identity of the cell's icon (stable across game sessions). This is the key you pass to `buffs.name()` to give the effect a `name`; `null` when no icon was found in the cell.
     * (Required)
     * 
     */
    @JsonProperty("iconColorHash")
    public Double getIconColorHash() {
        return iconColorHash;
    }

    /**
     * The numeric timer/stack drawn in the cell (e.g. a 50-second shield, 5 stacks), recovered from the digit glyphs the game draws — exact, not OCR. `null` when no number is drawn.
     * (Required)
     * 
     */
    @JsonProperty("value")
    public Double getValue() {
        return value;
    }

    /**
     * Raw recognised text in the cell incl. non-digit chars (e.g. "5m"); `null` when none.
     * (Required)
     * 
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("rect")
    public BuffRect getRect() {
        return rect;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Buff.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("kind");
        sb.append('=');
        sb.append(((this.kind == null)?"<null>":this.kind));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("iconColorHash");
        sb.append('=');
        sb.append(((this.iconColorHash == null)?"<null>":this.iconColorHash));
        sb.append(',');
        sb.append("value");
        sb.append('=');
        sb.append(((this.value == null)?"<null>":this.value));
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
        result = ((result* 31)+((this.kind == null)? 0 :this.kind.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.text == null)? 0 :this.text.hashCode()));
        result = ((result* 31)+((this.value == null)? 0 :this.value.hashCode()));
        result = ((result* 31)+((this.iconColorHash == null)? 0 :this.iconColorHash.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Buff) == false) {
            return false;
        }
        Buff rhs = ((Buff) other);
        return (((((((this.rect == rhs.rect)||((this.rect!= null)&&this.rect.equals(rhs.rect)))&&((this.kind == rhs.kind)||((this.kind!= null)&&this.kind.equals(rhs.kind))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.text == rhs.text)||((this.text!= null)&&this.text.equals(rhs.text))))&&((this.value == rhs.value)||((this.value!= null)&&this.value.equals(rhs.value))))&&((this.iconColorHash == rhs.iconColorHash)||((this.iconColorHash!= null)&&this.iconColorHash.equals(rhs.iconColorHash))));
    }


    /**
     * "buff" (green-bordered cell) or "debuff" (red-bordered cell).
     * 
     */
    @Generated("jsonschema2pojo")
    public enum Kind {

        BUFF("buff"),
        DEBUFF("debuff");
        private final String value;
        private final static Map<String, Buff.Kind> CONSTANTS = new HashMap<String, Buff.Kind>();

        static {
            for (Buff.Kind c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Kind(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Buff.Kind fromValue(String value) {
            Buff.Kind constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
