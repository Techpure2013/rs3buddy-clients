
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Result of reading the chatbox (GET /api/chat).
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ok",
    "font",
    "lineCount",
    "stale",
    "ageMs",
    "lines",
    "locate"
})
@Generated("jsonschema2pojo")
public class ChatReadResult {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ok")
    private Boolean ok;
    /**
     * Dominant font this read, or null if nothing was recognised.
     * (Required)
     * 
     */
    @JsonProperty("font")
    @JsonPropertyDescription("Dominant font this read, or null if nothing was recognised.")
    private String font;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("lineCount")
    private Double lineCount;
    /**
     * False whenever a fresh capture happened on this call.
     * (Required)
     * 
     */
    @JsonProperty("stale")
    @JsonPropertyDescription("False whenever a fresh capture happened on this call.")
    private Boolean stale;
    /**
     * Age of the cached glyph set this read used, in ms.
     * (Required)
     * 
     */
    @JsonProperty("ageMs")
    @JsonPropertyDescription("Age of the cached glyph set this read used, in ms.")
    private Double ageMs;
    /**
     * Lines ordered top-to-bottom (oldest first).
     * (Required)
     * 
     */
    @JsonProperty("lines")
    @JsonPropertyDescription("Lines ordered top-to-bottom (oldest first).")
    private List<ChatLine> lines = new ArrayList<ChatLine>();
    /**
     * Located chatbox anchors + the text region the reader scanned.
     * (Required)
     * 
     */
    @JsonProperty("locate")
    @JsonPropertyDescription("Located chatbox anchors + the text region the reader scanned.")
    private ChatLocate locate;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ok")
    public Boolean getOk() {
        return ok;
    }

    /**
     * Dominant font this read, or null if nothing was recognised.
     * (Required)
     * 
     */
    @JsonProperty("font")
    public String getFont() {
        return font;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("lineCount")
    public Double getLineCount() {
        return lineCount;
    }

    /**
     * False whenever a fresh capture happened on this call.
     * (Required)
     * 
     */
    @JsonProperty("stale")
    public Boolean getStale() {
        return stale;
    }

    /**
     * Age of the cached glyph set this read used, in ms.
     * (Required)
     * 
     */
    @JsonProperty("ageMs")
    public Double getAgeMs() {
        return ageMs;
    }

    /**
     * Lines ordered top-to-bottom (oldest first).
     * (Required)
     * 
     */
    @JsonProperty("lines")
    public List<ChatLine> getLines() {
        return lines;
    }

    /**
     * Located chatbox anchors + the text region the reader scanned.
     * (Required)
     * 
     */
    @JsonProperty("locate")
    public ChatLocate getLocate() {
        return locate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatReadResult.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ok");
        sb.append('=');
        sb.append(((this.ok == null)?"<null>":this.ok));
        sb.append(',');
        sb.append("font");
        sb.append('=');
        sb.append(((this.font == null)?"<null>":this.font));
        sb.append(',');
        sb.append("lineCount");
        sb.append('=');
        sb.append(((this.lineCount == null)?"<null>":this.lineCount));
        sb.append(',');
        sb.append("stale");
        sb.append('=');
        sb.append(((this.stale == null)?"<null>":this.stale));
        sb.append(',');
        sb.append("ageMs");
        sb.append('=');
        sb.append(((this.ageMs == null)?"<null>":this.ageMs));
        sb.append(',');
        sb.append("lines");
        sb.append('=');
        sb.append(((this.lines == null)?"<null>":this.lines));
        sb.append(',');
        sb.append("locate");
        sb.append('=');
        sb.append(((this.locate == null)?"<null>":this.locate));
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
        result = ((result* 31)+((this.ageMs == null)? 0 :this.ageMs.hashCode()));
        result = ((result* 31)+((this.stale == null)? 0 :this.stale.hashCode()));
        result = ((result* 31)+((this.locate == null)? 0 :this.locate.hashCode()));
        result = ((result* 31)+((this.ok == null)? 0 :this.ok.hashCode()));
        result = ((result* 31)+((this.lines == null)? 0 :this.lines.hashCode()));
        result = ((result* 31)+((this.lineCount == null)? 0 :this.lineCount.hashCode()));
        result = ((result* 31)+((this.font == null)? 0 :this.font.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ChatReadResult) == false) {
            return false;
        }
        ChatReadResult rhs = ((ChatReadResult) other);
        return ((((((((this.ageMs == rhs.ageMs)||((this.ageMs!= null)&&this.ageMs.equals(rhs.ageMs)))&&((this.stale == rhs.stale)||((this.stale!= null)&&this.stale.equals(rhs.stale))))&&((this.locate == rhs.locate)||((this.locate!= null)&&this.locate.equals(rhs.locate))))&&((this.ok == rhs.ok)||((this.ok!= null)&&this.ok.equals(rhs.ok))))&&((this.lines == rhs.lines)||((this.lines!= null)&&this.lines.equals(rhs.lines))))&&((this.lineCount == rhs.lineCount)||((this.lineCount!= null)&&this.lineCount.equals(rhs.lineCount))))&&((this.font == rhs.font)||((this.font!= null)&&this.font.equals(rhs.font))));
    }

}
