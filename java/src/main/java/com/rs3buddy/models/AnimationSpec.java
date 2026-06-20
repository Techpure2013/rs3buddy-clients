
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "kind",
    "period",
    "amplitude"
})
@Generated("jsonschema2pojo")
public class AnimationSpec {

    /**
     * Optional animation effect driven shader-side.
     * (Required)
     * 
     */
    @JsonProperty("kind")
    @JsonPropertyDescription("Optional animation effect driven shader-side.")
    private AnimationSpec.AnimationKind kind;
    /**
     * Cycle duration in seconds. Default 1.5s.
     * 
     */
    @JsonProperty("period")
    @JsonPropertyDescription("Cycle duration in seconds. Default 1.5s.")
    private Double period;
    /**
     * Animation amplitude (kind-dependent). Default 1.0.
     * 
     */
    @JsonProperty("amplitude")
    @JsonPropertyDescription("Animation amplitude (kind-dependent). Default 1.0.")
    private Double amplitude;

    /**
     * Optional animation effect driven shader-side.
     * (Required)
     * 
     */
    @JsonProperty("kind")
    public AnimationSpec.AnimationKind getKind() {
        return kind;
    }

    /**
     * Cycle duration in seconds. Default 1.5s.
     * 
     */
    @JsonProperty("period")
    public Double getPeriod() {
        return period;
    }

    /**
     * Animation amplitude (kind-dependent). Default 1.0.
     * 
     */
    @JsonProperty("amplitude")
    public Double getAmplitude() {
        return amplitude;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AnimationSpec.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("kind");
        sb.append('=');
        sb.append(((this.kind == null)?"<null>":this.kind));
        sb.append(',');
        sb.append("period");
        sb.append('=');
        sb.append(((this.period == null)?"<null>":this.period));
        sb.append(',');
        sb.append("amplitude");
        sb.append('=');
        sb.append(((this.amplitude == null)?"<null>":this.amplitude));
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
        result = ((result* 31)+((this.period == null)? 0 :this.period.hashCode()));
        result = ((result* 31)+((this.amplitude == null)? 0 :this.amplitude.hashCode()));
        result = ((result* 31)+((this.kind == null)? 0 :this.kind.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AnimationSpec) == false) {
            return false;
        }
        AnimationSpec rhs = ((AnimationSpec) other);
        return ((((this.period == rhs.period)||((this.period!= null)&&this.period.equals(rhs.period)))&&((this.amplitude == rhs.amplitude)||((this.amplitude!= null)&&this.amplitude.equals(rhs.amplitude))))&&((this.kind == rhs.kind)||((this.kind!= null)&&this.kind.equals(rhs.kind))));
    }


    /**
     * Optional animation effect driven shader-side.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum AnimationKind {

        PULSE("pulse"),
        FLOW("flow"),
        FADE("fade"),
        ROTATE("rotate");
        private final String value;
        private final static Map<String, AnimationSpec.AnimationKind> CONSTANTS = new HashMap<String, AnimationSpec.AnimationKind>();

        static {
            for (AnimationSpec.AnimationKind c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        AnimationKind(String value) {
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
        public static AnimationSpec.AnimationKind fromValue(String value) {
            AnimationSpec.AnimationKind constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
