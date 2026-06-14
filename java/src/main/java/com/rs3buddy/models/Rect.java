
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * A rectangle on screen.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "x",
    "y",
    "width",
    "height"
})
@Generated("jsonschema2pojo")
public class Rect {

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
    @JsonProperty("width")
    private Double width;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("height")
    private Double height;

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
    @JsonProperty("width")
    public Double getWidth() {
        return width;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("height")
    public Double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Rect.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("x");
        sb.append('=');
        sb.append(((this.x == null)?"<null>":this.x));
        sb.append(',');
        sb.append("y");
        sb.append('=');
        sb.append(((this.y == null)?"<null>":this.y));
        sb.append(',');
        sb.append("width");
        sb.append('=');
        sb.append(((this.width == null)?"<null>":this.width));
        sb.append(',');
        sb.append("height");
        sb.append('=');
        sb.append(((this.height == null)?"<null>":this.height));
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
        result = ((result* 31)+((this.width == null)? 0 :this.width.hashCode()));
        result = ((result* 31)+((this.y == null)? 0 :this.y.hashCode()));
        result = ((result* 31)+((this.height == null)? 0 :this.height.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Rect) == false) {
            return false;
        }
        Rect rhs = ((Rect) other);
        return (((((this.x == rhs.x)||((this.x!= null)&&this.x.equals(rhs.x)))&&((this.width == rhs.width)||((this.width!= null)&&this.width.equals(rhs.width))))&&((this.y == rhs.y)||((this.y!= null)&&this.y.equals(rhs.y))))&&((this.height == rhs.height)||((this.height!= null)&&this.height.equals(rhs.height))));
    }

}
