
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * A contiguous same-colour segment of a line (e.g. clan tag, sender name, the message body). Render each run in its own colour to reproduce a multi-colour line.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "text",
    "color"
})
@Generated("jsonschema2pojo")
public class ChatRun {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("text")
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
     * 
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatRun.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("text");
        sb.append('=');
        sb.append(((this.text == null)?"<null>":this.text));
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
        result = ((result* 31)+((this.color == null)? 0 :this.color.hashCode()));
        result = ((result* 31)+((this.text == null)? 0 :this.text.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ChatRun) == false) {
            return false;
        }
        ChatRun rhs = ((ChatRun) other);
        return (((this.color == rhs.color)||((this.color!= null)&&this.color.equals(rhs.color)))&&((this.text == rhs.text)||((this.text!= null)&&this.text.equals(rhs.text))));
    }

}
