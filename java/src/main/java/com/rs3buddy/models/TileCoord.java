
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "x",
    "z"
})
@Generated("jsonschema2pojo")
public class TileCoord {

    /**
     * Absolute tile X (= floor(world.x / 512)).
     * (Required)
     * 
     */
    @JsonProperty("x")
    @JsonPropertyDescription("Absolute tile X (= floor(world.x / 512)).")
    private Double x;
    /**
     * Absolute tile Z (= floor(world.z / 512)).
     * (Required)
     * 
     */
    @JsonProperty("z")
    @JsonPropertyDescription("Absolute tile Z (= floor(world.z / 512)).")
    private Double z;

    /**
     * Absolute tile X (= floor(world.x / 512)).
     * (Required)
     * 
     */
    @JsonProperty("x")
    public Double getX() {
        return x;
    }

    /**
     * Absolute tile Z (= floor(world.z / 512)).
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
        sb.append(TileCoord.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("x");
        sb.append('=');
        sb.append(((this.x == null)?"<null>":this.x));
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
        result = ((result* 31)+((this.z == null)? 0 :this.z.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TileCoord) == false) {
            return false;
        }
        TileCoord rhs = ((TileCoord) other);
        return (((this.x == rhs.x)||((this.x!= null)&&this.x.equals(rhs.x)))&&((this.z == rhs.z)||((this.z!= null)&&this.z.equals(rhs.z))));
    }

}
