
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * One recognised glyph on a chat line.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "char",
    "x",
    "color"
})
@Generated("jsonschema2pojo")
public class ChatGlyph {

    /**
     * The recognised character.
     * (Required)
     * 
     */
    @JsonProperty("char")
    @JsonPropertyDescription("The recognised character.")
    private String _char;
    /**
     * Screen x (window px) of the glyph's left edge.
     * (Required)
     * 
     */
    @JsonProperty("x")
    @JsonPropertyDescription("Screen x (window px) of the glyph's left edge.")
    private Double x;
    /**
     * RGB triple, 0-255 per channel: [r, g, b].
     * (Required)
     * 
     */
    @JsonProperty("color")
    @JsonPropertyDescription("RGB triple, 0-255 per channel: [r, g, b].")
    private List<Double> color = new ArrayList<Double>();

    /**
     * The recognised character.
     * (Required)
     * 
     */
    @JsonProperty("char")
    public String getChar() {
        return _char;
    }

    /**
     * Screen x (window px) of the glyph's left edge.
     * (Required)
     * 
     */
    @JsonProperty("x")
    public Double getX() {
        return x;
    }

    /**
     * RGB triple, 0-255 per channel: [r, g, b].
     * (Required)
     * 
     */
    @JsonProperty("color")
    public List<Double> getColor() {
        return color;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatGlyph.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("_char");
        sb.append('=');
        sb.append(((this._char == null)?"<null>":this._char));
        sb.append(',');
        sb.append("x");
        sb.append('=');
        sb.append(((this.x == null)?"<null>":this.x));
        sb.append(',');
        sb.append("color");
        sb.append('=');
        sb.append(((this.color == null)?"<null>":this.color));
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
        result = ((result* 31)+((this._char == null)? 0 :this._char.hashCode()));
        result = ((result* 31)+((this.color == null)? 0 :this.color.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ChatGlyph) == false) {
            return false;
        }
        ChatGlyph rhs = ((ChatGlyph) other);
        return ((((this.x == rhs.x)||((this.x!= null)&&this.x.equals(rhs.x)))&&((this._char == rhs._char)||((this._char!= null)&&this._char.equals(rhs._char))))&&((this.color == rhs.color)||((this.color!= null)&&this.color.equals(rhs.color))));
    }

}
