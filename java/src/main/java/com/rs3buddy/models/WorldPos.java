
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "x",
    "y",
    "z"
})
@Generated("jsonschema2pojo")
public class WorldPos {

    /**
     * Absolute world X (RS3 game units, ~100k–4M typical range).
     * (Required)
     * 
     */
    @JsonProperty("x")
    @JsonPropertyDescription("Absolute world X (RS3 game units, ~100k\u20134M typical range).")
    private Double x;
    /**
     * Absolute world Y (height; floor 0 ≈ 1152, floor 3 ≈ 5040).
     * (Required)
     * 
     */
    @JsonProperty("y")
    @JsonPropertyDescription("Absolute world Y (height; floor 0 \u2248 1152, floor 3 \u2248 5040).")
    private Double y;
    /**
     * Absolute world Z.
     * (Required)
     * 
     */
    @JsonProperty("z")
    @JsonPropertyDescription("Absolute world Z.")
    private Double z;

    /**
     * Absolute world X (RS3 game units, ~100k–4M typical range).
     * (Required)
     * 
     */
    @JsonProperty("x")
    public Double getX() {
        return x;
    }

    /**
     * Absolute world Y (height; floor 0 ≈ 1152, floor 3 ≈ 5040).
     * (Required)
     * 
     */
    @JsonProperty("y")
    public Double getY() {
        return y;
    }

    /**
     * Absolute world Z.
     * (Required)
     * 
     */
    @JsonProperty("z")
    public Double getZ() {
        return z;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(WorldPos.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("x");
        sb.append('=');
        sb.append(((this.x == null)?"<null>":this.x));
        sb.append(',');
        sb.append("y");
        sb.append('=');
        sb.append(((this.y == null)?"<null>":this.y));
        sb.append(',');
        sb.append("z");
        sb.append('=');
        sb.append(((this.z == null)?"<null>":this.z));
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
        result = ((result* 31)+((this.x == null)? 0 :this.x.hashCode()));
        result = ((result* 31)+((this.y == null)? 0 :this.y.hashCode()));
        result = ((result* 31)+((this.z == null)? 0 :this.z.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof WorldPos) == false) {
            return false;
        }
        WorldPos rhs = ((WorldPos) other);
        return ((((this.x == rhs.x)||((this.x!= null)&&this.x.equals(rhs.x)))&&((this.y == rhs.y)||((this.y!= null)&&this.y.equals(rhs.y))))&&((this.z == rhs.z)||((this.z!= null)&&this.z.equals(rhs.z))));
    }

}
