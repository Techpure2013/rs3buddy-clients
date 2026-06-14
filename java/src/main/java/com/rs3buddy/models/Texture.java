
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "slot",
    "id",
    "width",
    "height"
})
@Generated("jsonschema2pojo")
public class Texture {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("slot")
    private Double slot;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    private Double id;
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
    @JsonProperty("slot")
    public Double getSlot() {
        return slot;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public Double getId() {
        return id;
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
        sb.append(Texture.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("slot");
        sb.append('=');
        sb.append(((this.slot == null)?"<null>":this.slot));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
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
        result = ((result* 31)+((this.width == null)? 0 :this.width.hashCode()));
        result = ((result* 31)+((this.slot == null)? 0 :this.slot.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.height == null)? 0 :this.height.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Texture) == false) {
            return false;
        }
        Texture rhs = ((Texture) other);
        return (((((this.width == rhs.width)||((this.width!= null)&&this.width.equals(rhs.width)))&&((this.slot == rhs.slot)||((this.slot!= null)&&this.slot.equals(rhs.slot))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.height == rhs.height)||((this.height!= null)&&this.height.equals(rhs.height))));
    }

}
