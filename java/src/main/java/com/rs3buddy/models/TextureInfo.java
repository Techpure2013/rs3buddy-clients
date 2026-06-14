
package com.rs3buddy.models;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Info about a loaded texture.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "width",
    "height",
    "format",
    "source"
})
@Generated("jsonschema2pojo")
public class TextureInfo {

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
    @JsonProperty("format")
    private String format;
    /**
     * Where this entry was sourced from. Useful for debugging UIs.
     * 
     */
    @JsonProperty("source")
    @JsonPropertyDescription("Where this entry was sourced from. Useful for debugging UIs.")
    private TextureInfo.Source source;

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

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("format")
    public String getFormat() {
        return format;
    }

    /**
     * Where this entry was sourced from. Useful for debugging UIs.
     * 
     */
    @JsonProperty("source")
    public TextureInfo.Source getSource() {
        return source;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TextureInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("format");
        sb.append('=');
        sb.append(((this.format == null)?"<null>":this.format));
        sb.append(',');
        sb.append("source");
        sb.append('=');
        sb.append(((this.source == null)?"<null>":this.source));
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
        result = ((result* 31)+((this.format == null)? 0 :this.format.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.source == null)? 0 :this.source.hashCode()));
        result = ((result* 31)+((this.height == null)? 0 :this.height.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TextureInfo) == false) {
            return false;
        }
        TextureInfo rhs = ((TextureInfo) other);
        return ((((((this.width == rhs.width)||((this.width!= null)&&this.width.equals(rhs.width)))&&((this.format == rhs.format)||((this.format!= null)&&this.format.equals(rhs.format))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.source == rhs.source)||((this.source!= null)&&this.source.equals(rhs.source))))&&((this.height == rhs.height)||((this.height!= null)&&this.height.equals(rhs.height))));
    }


    /**
     * Where this entry was sourced from. Useful for debugging UIs.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum Source {

        SNAPSHOT("snapshot"),
        FRAME_HARVEST("frame-harvest");
        private final String value;
        private final static Map<String, TextureInfo.Source> CONSTANTS = new HashMap<String, TextureInfo.Source>();

        static {
            for (TextureInfo.Source c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Source(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static TextureInfo.Source fromValue(String value) {
            TextureInfo.Source constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
