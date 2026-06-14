
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Located chatbox anchors + the text region the reader scanned.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "allChat",
    "cog",
    "quickChat",
    "box"
})
@Generated("jsonschema2pojo")
public class ChatLocate {

    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("allChat")
    @JsonPropertyDescription("A located UI rect in window px (top-left x/y + size).")
    private ChatRect allChat;
    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("cog")
    @JsonPropertyDescription("A located UI rect in window px (top-left x/y + size).")
    private ChatRect cog;
    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("quickChat")
    @JsonPropertyDescription("A located UI rect in window px (top-left x/y + size).")
    private ChatRect quickChat;
    /**
     * The recognised chat text region in window px.
     * (Required)
     * 
     */
    @JsonProperty("box")
    @JsonPropertyDescription("The recognised chat text region in window px.")
    private ChatBox box;

    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("allChat")
    public ChatRect getAllChat() {
        return allChat;
    }

    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("cog")
    public ChatRect getCog() {
        return cog;
    }

    /**
     * A located UI rect in window px (top-left x/y + size).
     * (Required)
     * 
     */
    @JsonProperty("quickChat")
    public ChatRect getQuickChat() {
        return quickChat;
    }

    /**
     * The recognised chat text region in window px.
     * (Required)
     * 
     */
    @JsonProperty("box")
    public ChatBox getBox() {
        return box;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatLocate.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("allChat");
        sb.append('=');
        sb.append(((this.allChat == null)?"<null>":this.allChat));
        sb.append(',');
        sb.append("cog");
        sb.append('=');
        sb.append(((this.cog == null)?"<null>":this.cog));
        sb.append(',');
        sb.append("quickChat");
        sb.append('=');
        sb.append(((this.quickChat == null)?"<null>":this.quickChat));
        sb.append(',');
        sb.append("box");
        sb.append('=');
        sb.append(((this.box == null)?"<null>":this.box));
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
        result = ((result* 31)+((this.allChat == null)? 0 :this.allChat.hashCode()));
        result = ((result* 31)+((this.cog == null)? 0 :this.cog.hashCode()));
        result = ((result* 31)+((this.box == null)? 0 :this.box.hashCode()));
        result = ((result* 31)+((this.quickChat == null)? 0 :this.quickChat.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ChatLocate) == false) {
            return false;
        }
        ChatLocate rhs = ((ChatLocate) other);
        return (((((this.allChat == rhs.allChat)||((this.allChat!= null)&&this.allChat.equals(rhs.allChat)))&&((this.cog == rhs.cog)||((this.cog!= null)&&this.cog.equals(rhs.cog))))&&((this.box == rhs.box)||((this.box!= null)&&this.box.equals(rhs.box))))&&((this.quickChat == rhs.quickChat)||((this.quickChat!= null)&&this.quickChat.equals(rhs.quickChat))));
    }

}
