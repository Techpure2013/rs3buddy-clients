
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * A world position in tile coordinates.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tileX",
    "tileZ",
    "worldX",
    "worldZ"
})
@Generated("jsonschema2pojo")
public class Position {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tileX")
    private Double tileX;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tileZ")
    private Double tileZ;
    /**
     * World X in RS3 engine units (tileX * 512).
     * (Required)
     * 
     */
    @JsonProperty("worldX")
    @JsonPropertyDescription("World X in RS3 engine units (tileX * 512).")
    private Double worldX;
    /**
     * World Z in RS3 engine units (tileZ * 512).
     * (Required)
     * 
     */
    @JsonProperty("worldZ")
    @JsonPropertyDescription("World Z in RS3 engine units (tileZ * 512).")
    private Double worldZ;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tileX")
    public Double getTileX() {
        return tileX;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tileZ")
    public Double getTileZ() {
        return tileZ;
    }

    /**
     * World X in RS3 engine units (tileX * 512).
     * (Required)
     * 
     */
    @JsonProperty("worldX")
    public Double getWorldX() {
        return worldX;
    }

    /**
     * World Z in RS3 engine units (tileZ * 512).
     * (Required)
     * 
     */
    @JsonProperty("worldZ")
    public Double getWorldZ() {
        return worldZ;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Position.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("tileX");
        sb.append('=');
        sb.append(((this.tileX == null)?"<null>":this.tileX));
        sb.append(',');
        sb.append("tileZ");
        sb.append('=');
        sb.append(((this.tileZ == null)?"<null>":this.tileZ));
        sb.append(',');
        sb.append("worldX");
        sb.append('=');
        sb.append(((this.worldX == null)?"<null>":this.worldX));
        sb.append(',');
        sb.append("worldZ");
        sb.append('=');
        sb.append(((this.worldZ == null)?"<null>":this.worldZ));
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
        result = ((result* 31)+((this.tileZ == null)? 0 :this.tileZ.hashCode()));
        result = ((result* 31)+((this.worldZ == null)? 0 :this.worldZ.hashCode()));
        result = ((result* 31)+((this.worldX == null)? 0 :this.worldX.hashCode()));
        result = ((result* 31)+((this.tileX == null)? 0 :this.tileX.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Position) == false) {
            return false;
        }
        Position rhs = ((Position) other);
        return (((((this.tileZ == rhs.tileZ)||((this.tileZ!= null)&&this.tileZ.equals(rhs.tileZ)))&&((this.worldZ == rhs.worldZ)||((this.worldZ!= null)&&this.worldZ.equals(rhs.worldZ))))&&((this.worldX == rhs.worldX)||((this.worldX!= null)&&this.worldX.equals(rhs.worldX))))&&((this.tileX == rhs.tileX)||((this.tileX!= null)&&this.tileX.equals(rhs.tileX))));
    }

}
