
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * One chat line. Lines are ordered top-to-bottom (oldest first).
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "y",
    "text",
    "color",
    "runs",
    "glyphs"
})
@Generated("jsonschema2pojo")
public class ChatLine {

    /**
     * Baseline y (window px).
     * (Required)
     * 
     */
    @JsonProperty("y")
    @JsonPropertyDescription("Baseline y (window px).")
    private Double y;
    /**
     * The line's text, with spaces inserted on x-gaps.
     * (Required)
     * 
     */
    @JsonProperty("text")
    @JsonPropertyDescription("The line's text, with spaces inserted on x-gaps.")
    private String text;
    /**
     * RGB triple, 0-255 per channel: [r, g, b].
     * (Required)
     * 
     */
    @JsonProperty("color")
    @JsonPropertyDescription("RGB triple, 0-255 per channel: [r, g, b].")
    private List<Double> color = new ArrayList<Double>();
    /**
     * Contiguous same-colour segments (tag / name / message).
     * (Required)
     * 
     */
    @JsonProperty("runs")
    @JsonPropertyDescription("Contiguous same-colour segments (tag / name / message).")
    private List<ChatRun> runs = new ArrayList<ChatRun>();
    /**
     * Per-glyph detail.
     * (Required)
     * 
     */
    @JsonProperty("glyphs")
    @JsonPropertyDescription("Per-glyph detail.")
    private List<ChatGlyph> glyphs = new ArrayList<ChatGlyph>();

    /**
     * Baseline y (window px).
     * (Required)
     * 
     */
    @JsonProperty("y")
    public Double getY() {
        return y;
    }

    /**
     * The line's text, with spaces inserted on x-gaps.
     * (Required)
     * 
     */
    @JsonProperty("text")
    public String getText() {
        return text;
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

    /**
     * Contiguous same-colour segments (tag / name / message).
     * (Required)
     * 
     */
    @JsonProperty("runs")
    public List<ChatRun> getRuns() {
        return runs;
    }

    /**
     * Per-glyph detail.
     * (Required)
     * 
     */
    @JsonProperty("glyphs")
    public List<ChatGlyph> getGlyphs() {
        return glyphs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatLine.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("y");
        sb.append('=');
        sb.append(((this.y == null)?"<null>":this.y));
        sb.append(',');
        sb.append("text");
        sb.append('=');
        sb.append(((this.text == null)?"<null>":this.text));
        sb.append(',');
        sb.append("color");
        sb.append('=');
        sb.append(((this.color == null)?"<null>":this.color));
        sb.append(',');
        sb.append("runs");
        sb.append('=');
        sb.append(((this.runs == null)?"<null>":this.runs));
        sb.append(',');
        sb.append("glyphs");
        sb.append('=');
        sb.append(((this.glyphs == null)?"<null>":this.glyphs));
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
        result = ((result* 31)+((this.y == null)? 0 :this.y.hashCode()));
        result = ((result* 31)+((this.glyphs == null)? 0 :this.glyphs.hashCode()));
        result = ((result* 31)+((this.text == null)? 0 :this.text.hashCode()));
        result = ((result* 31)+((this.color == null)? 0 :this.color.hashCode()));
        result = ((result* 31)+((this.runs == null)? 0 :this.runs.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ChatLine) == false) {
            return false;
        }
        ChatLine rhs = ((ChatLine) other);
        return ((((((this.y == rhs.y)||((this.y!= null)&&this.y.equals(rhs.y)))&&((this.glyphs == rhs.glyphs)||((this.glyphs!= null)&&this.glyphs.equals(rhs.glyphs))))&&((this.text == rhs.text)||((this.text!= null)&&this.text.equals(rhs.text))))&&((this.color == rhs.color)||((this.color!= null)&&this.color.equals(rhs.color))))&&((this.runs == rhs.runs)||((this.runs!= null)&&this.runs.equals(rhs.runs))));
    }

}
