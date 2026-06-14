
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * The recognised chat text region in window px.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "x0",
    "y0",
    "x1",
    "y1"
})
@Generated("jsonschema2pojo")
public class ChatBox {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("x0")
    private Double x0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("y0")
    private Double y0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("x1")
    private Double x1;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("y1")
    private Double y1;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("x0")
    public Double getX0() {
        return x0;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("y0")
    public Double getY0() {
        return y0;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("x1")
    public Double getX1() {
        return x1;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("y1")
    public Double getY1() {
        return y1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatBox.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("x0");
        sb.append('=');
        sb.append(((this.x0 == null)?"<null>":this.x0));
        sb.append(',');
        sb.append("y0");
        sb.append('=');
        sb.append(((this.y0 == null)?"<null>":this.y0));
        sb.append(',');
        sb.append("x1");
        sb.append('=');
        sb.append(((this.x1 == null)?"<null>":this.x1));
        sb.append(',');
        sb.append("y1");
        sb.append('=');
        sb.append(((this.y1 == null)?"<null>":this.y1));
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
        result = ((result* 31)+((this.x0 == null)? 0 :this.x0 .hashCode()));
        result = ((result* 31)+((this.y1 == null)? 0 :this.y1 .hashCode()));
        result = ((result* 31)+((this.x1 == null)? 0 :this.x1 .hashCode()));
        result = ((result* 31)+((this.y0 == null)? 0 :this.y0 .hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ChatBox) == false) {
            return false;
        }
        ChatBox rhs = ((ChatBox) other);
        return (((((this.x0 == rhs.x0)||((this.x0 != null)&&this.x0 .equals(rhs.x0)))&&((this.y1 == rhs.y1)||((this.y1 != null)&&this.y1 .equals(rhs.y1))))&&((this.x1 == rhs.x1)||((this.x1 != null)&&this.x1 .equals(rhs.x1))))&&((this.y0 == rhs.y0)||((this.y0 != null)&&this.y0 .equals(rhs.y0))));
    }

}
