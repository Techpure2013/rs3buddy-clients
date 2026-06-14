
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Screen-space bounding box (in screen pixels) where a draw landed. Computed from the draw's vertex-position range in the shared `_vertexPositions` array. UI draws (`drawType === "ui" && targetFbo === 0`) are the only draws that get a usable bbox here today; world draws use model+projection matrices the server doesn't apply, so their raw positions are 3D world coords with no direct screen mapping.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "x",
    "y",
    "w",
    "h"
})
@Generated("jsonschema2pojo")
public class ScreenBbox {

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
    @JsonProperty("y")
    private Double y;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("w")
    private Double w;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("h")
    private Double h;

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
    @JsonProperty("y")
    public Double getY() {
        return y;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("w")
    public Double getW() {
        return w;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("h")
    public Double getH() {
        return h;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ScreenBbox.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("x");
        sb.append('=');
        sb.append(((this.x == null)?"<null>":this.x));
        sb.append(',');
        sb.append("y");
        sb.append('=');
        sb.append(((this.y == null)?"<null>":this.y));
        sb.append(',');
        sb.append("w");
        sb.append('=');
        sb.append(((this.w == null)?"<null>":this.w));
        sb.append(',');
        sb.append("h");
        sb.append('=');
        sb.append(((this.h == null)?"<null>":this.h));
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
        result = ((result* 31)+((this.h == null)? 0 :this.h.hashCode()));
        result = ((result* 31)+((this.y == null)? 0 :this.y.hashCode()));
        result = ((result* 31)+((this.w == null)? 0 :this.w.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ScreenBbox) == false) {
            return false;
        }
        ScreenBbox rhs = ((ScreenBbox) other);
        return (((((this.x == rhs.x)||((this.x!= null)&&this.x.equals(rhs.x)))&&((this.h == rhs.h)||((this.h!= null)&&this.h.equals(rhs.h))))&&((this.y == rhs.y)||((this.y!= null)&&this.y.equals(rhs.y))))&&((this.w == rhs.w)||((this.w!= null)&&this.w.equals(rhs.w))));
    }

}
