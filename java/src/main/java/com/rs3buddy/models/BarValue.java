
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
 * One status bar's reading.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "found",
    "value",
    "max",
    "text",
    "anchor",
    "region"
})
@Generated("jsonschema2pojo")
public class BarValue {

    /**
     * The four readable RS3 status bars / orbs.
     * (Required)
     * 
     */
    @JsonProperty("name")
    @JsonPropertyDescription("The four readable RS3 status bars / orbs.")
    private BarValue.BarName name;
    /**
     * True when the bar's anchor sprite was located on screen.
     * (Required)
     * 
     */
    @JsonProperty("found")
    @JsonPropertyDescription("True when the bar's anchor sprite was located on screen.")
    private Boolean found;
    /**
     * Parsed numeric value (a percentage for adrenaline), or null when the anchor was found but no digits could be recognised in its region.
     * (Required)
     * 
     */
    @JsonProperty("value")
    @JsonPropertyDescription("Parsed numeric value (a percentage for adrenaline), or null when the anchor was found but no digits could be recognised in its region.")
    private Double value;
    /**
     * The max value when the bar shows "current / max" (e.g. hitpoints 10200/10200 → max 10200); null when only a single number is shown.
     * (Required)
     * 
     */
    @JsonProperty("max")
    @JsonPropertyDescription("The max value when the bar shows \"current / max\" (e.g. hitpoints 10200/10200 \u2192 max 10200); null when only a single number is shown.")
    private Double max;
    /**
     * Raw recognised text incl. separators, e.g. "10200/10200" or "100%".
     * (Required)
     * 
     */
    @JsonProperty("text")
    @JsonPropertyDescription("Raw recognised text incl. separators, e.g. \"10200/10200\" or \"100%\".")
    private String text;
    /**
     * The anchor sprite's screen rect, or null when the bar was not found.
     * (Required)
     * 
     */
    @JsonProperty("anchor")
    @JsonPropertyDescription("The anchor sprite's screen rect, or null when the bar was not found.")
    private Object anchor;
    /**
     * The region scanned for the value, or null when the bar was not found.
     * (Required)
     * 
     */
    @JsonProperty("region")
    @JsonPropertyDescription("The region scanned for the value, or null when the bar was not found.")
    private Object region;

    /**
     * The four readable RS3 status bars / orbs.
     * (Required)
     * 
     */
    @JsonProperty("name")
    public BarValue.BarName getName() {
        return name;
    }

    /**
     * True when the bar's anchor sprite was located on screen.
     * (Required)
     * 
     */
    @JsonProperty("found")
    public Boolean getFound() {
        return found;
    }

    /**
     * Parsed numeric value (a percentage for adrenaline), or null when the anchor was found but no digits could be recognised in its region.
     * (Required)
     * 
     */
    @JsonProperty("value")
    public Double getValue() {
        return value;
    }

    /**
     * The max value when the bar shows "current / max" (e.g. hitpoints 10200/10200 → max 10200); null when only a single number is shown.
     * (Required)
     * 
     */
    @JsonProperty("max")
    public Double getMax() {
        return max;
    }

    /**
     * Raw recognised text incl. separators, e.g. "10200/10200" or "100%".
     * (Required)
     * 
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * The anchor sprite's screen rect, or null when the bar was not found.
     * (Required)
     * 
     */
    @JsonProperty("anchor")
    public Object getAnchor() {
        return anchor;
    }

    /**
     * The region scanned for the value, or null when the bar was not found.
     * (Required)
     * 
     */
    @JsonProperty("region")
    public Object getRegion() {
        return region;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BarValue.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("found");
        sb.append('=');
        sb.append(((this.found == null)?"<null>":this.found));
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
        sb.append("anchor");
        sb.append('=');
        sb.append(((this.anchor == null)?"<null>":this.anchor));
        sb.append(',');
        sb.append("region");
        sb.append('=');
        sb.append(((this.region == null)?"<null>":this.region));
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
        result = ((result* 31)+((this.found == null)? 0 :this.found.hashCode()));
        result = ((result* 31)+((this.max == null)? 0 :this.max.hashCode()));
        result = ((result* 31)+((this.anchor == null)? 0 :this.anchor.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.text == null)? 0 :this.text.hashCode()));
        result = ((result* 31)+((this.region == null)? 0 :this.region.hashCode()));
        result = ((result* 31)+((this.value == null)? 0 :this.value.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BarValue) == false) {
            return false;
        }
        BarValue rhs = ((BarValue) other);
        return ((((((((this.found == rhs.found)||((this.found!= null)&&this.found.equals(rhs.found)))&&((this.max == rhs.max)||((this.max!= null)&&this.max.equals(rhs.max))))&&((this.anchor == rhs.anchor)||((this.anchor!= null)&&this.anchor.equals(rhs.anchor))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.text == rhs.text)||((this.text!= null)&&this.text.equals(rhs.text))))&&((this.region == rhs.region)||((this.region!= null)&&this.region.equals(rhs.region))))&&((this.value == rhs.value)||((this.value!= null)&&this.value.equals(rhs.value))));
    }


    /**
     * The four readable RS3 status bars / orbs.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum BarName {

        HITPOINTS("hitpoints"),
        ADRENALINE("adrenaline"),
        PRAYER("prayer"),
        SUMMONING("summoning");
        private final String value;
        private final static Map<String, BarValue.BarName> CONSTANTS = new HashMap<String, BarValue.BarName>();

        static {
            for (BarValue.BarName c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        BarName(String value) {
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
        public static BarValue.BarName fromValue(String value) {
            BarValue.BarName constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
