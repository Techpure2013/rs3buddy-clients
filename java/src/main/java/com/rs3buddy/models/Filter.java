
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Only capture draws matching these filters.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "shaderId",
    "meshId",
    "targetFbo",
    "drawType"
})
@Generated("jsonschema2pojo")
public class Filter {

    @JsonProperty("shaderId")
    private Double shaderId;
    @JsonProperty("meshId")
    private Double meshId;
    @JsonProperty("targetFbo")
    private Double targetFbo;
    /**
     * Restrict capture to a single draw class ("ui" | "floor" | "animated" | "water" | "particles"). Non-matching draws are skipped at record time, so their vertex buffers and textures are never read back (no glGet* GPU sync). The overlay passes "ui".
     * 
     */
    @JsonProperty("drawType")
    @JsonPropertyDescription("Restrict capture to a single draw class (\"ui\" | \"floor\" | \"animated\" | \"water\" | \"particles\"). Non-matching draws are skipped at record time, so their vertex buffers and textures are never read back (no glGet* GPU sync). The overlay passes \"ui\".")
    private String drawType;

    @JsonProperty("shaderId")
    public Double getShaderId() {
        return shaderId;
    }

    @JsonProperty("meshId")
    public Double getMeshId() {
        return meshId;
    }

    @JsonProperty("targetFbo")
    public Double getTargetFbo() {
        return targetFbo;
    }

    /**
     * Restrict capture to a single draw class ("ui" | "floor" | "animated" | "water" | "particles"). Non-matching draws are skipped at record time, so their vertex buffers and textures are never read back (no glGet* GPU sync). The overlay passes "ui".
     * 
     */
    @JsonProperty("drawType")
    public String getDrawType() {
        return drawType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Filter.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("shaderId");
        sb.append('=');
        sb.append(((this.shaderId == null)?"<null>":this.shaderId));
        sb.append(',');
        sb.append("meshId");
        sb.append('=');
        sb.append(((this.meshId == null)?"<null>":this.meshId));
        sb.append(',');
        sb.append("targetFbo");
        sb.append('=');
        sb.append(((this.targetFbo == null)?"<null>":this.targetFbo));
        sb.append(',');
        sb.append("drawType");
        sb.append('=');
        sb.append(((this.drawType == null)?"<null>":this.drawType));
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
        result = ((result* 31)+((this.targetFbo == null)? 0 :this.targetFbo.hashCode()));
        result = ((result* 31)+((this.drawType == null)? 0 :this.drawType.hashCode()));
        result = ((result* 31)+((this.meshId == null)? 0 :this.meshId.hashCode()));
        result = ((result* 31)+((this.shaderId == null)? 0 :this.shaderId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Filter) == false) {
            return false;
        }
        Filter rhs = ((Filter) other);
        return (((((this.targetFbo == rhs.targetFbo)||((this.targetFbo!= null)&&this.targetFbo.equals(rhs.targetFbo)))&&((this.drawType == rhs.drawType)||((this.drawType!= null)&&this.drawType.equals(rhs.drawType))))&&((this.meshId == rhs.meshId)||((this.meshId!= null)&&this.meshId.equals(rhs.meshId))))&&((this.shaderId == rhs.shaderId)||((this.shaderId!= null)&&this.shaderId.equals(rhs.shaderId))));
    }

}
