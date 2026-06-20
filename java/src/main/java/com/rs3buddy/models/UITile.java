
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Tile anchor for world-space widgets.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "x",
    "z",
    "floor"
})
@Generated("jsonschema2pojo")
public class UITile {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("x")
    private Double x;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("z")
    private Double z;
    @JsonProperty("floor")
    private Double floor;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("x")
    public Double getX() {
        return x;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("z")
    public Double getZ() {
        return z;
    }

    @JsonProperty("floor")
    public Double getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(UITile.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("x");
        sb.append('=');
        sb.append(((this.x == null)?"<null>":this.x));
        sb.append(',');
        sb.append("z");
        sb.append('=');
        sb.append(((this.z == null)?"<null>":this.z));
        sb.append(',');
        sb.append("floor");
        sb.append('=');
        sb.append(((this.floor == null)?"<null>":this.floor));
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
        result = ((result* 31)+((this.z == null)? 0 :this.z.hashCode()));
        result = ((result* 31)+((this.floor == null)? 0 :this.floor.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UITile) == false) {
            return false;
        }
        UITile rhs = ((UITile) other);
        return ((((this.x == rhs.x)||((this.x!= null)&&this.x.equals(rhs.x)))&&((this.z == rhs.z)||((this.z!= null)&&this.z.equals(rhs.z))))&&((this.floor == rhs.floor)||((this.floor!= null)&&this.floor.equals(rhs.floor))));
    }

}
