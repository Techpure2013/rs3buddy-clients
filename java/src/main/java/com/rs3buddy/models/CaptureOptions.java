
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Options for captureFrame().
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "includeMesh",
    "includeTexturePixels",
    "filter",
    "detectPlayer"
})
@Generated("jsonschema2pojo")
public class CaptureOptions {

    /**
     * Include full mesh vertex data (slower). Default: false.
     * 
     */
    @JsonProperty("includeMesh")
    @JsonPropertyDescription("Include full mesh vertex data (slower). Default: false.")
    private Boolean includeMesh;
    /**
     * Include texture pixel snapshots (slower). Default: false.
     * 
     */
    @JsonProperty("includeTexturePixels")
    @JsonPropertyDescription("Include texture pixel snapshots (slower). Default: false.")
    private Boolean includeTexturePixels;
    /**
     * Only capture draws matching these filters.
     * 
     */
    @JsonProperty("filter")
    @JsonPropertyDescription("Only capture draws matching these filters.")
    private Filter filter;
    /**
     * Run player (occlusion-mesh) detection during this capture. Off by default — the server does NO player introspection on a normal frame grab. Only trackPlayer() (→ /api/player) sets this, so the per-frame path stays stall-free. When set, the captured frame carries the player's world tile (frame.playerPosition).
     * 
     */
    @JsonProperty("detectPlayer")
    @JsonPropertyDescription("Run player (occlusion-mesh) detection during this capture. Off by default \u2014 the server does NO player introspection on a normal frame grab. Only trackPlayer() (\u2192 /api/player) sets this, so the per-frame path stays stall-free. When set, the captured frame carries the player's world tile (frame.playerPosition).")
    private Boolean detectPlayer;

    /**
     * Include full mesh vertex data (slower). Default: false.
     * 
     */
    @JsonProperty("includeMesh")
    public Boolean getIncludeMesh() {
        return includeMesh;
    }

    /**
     * Include texture pixel snapshots (slower). Default: false.
     * 
     */
    @JsonProperty("includeTexturePixels")
    public Boolean getIncludeTexturePixels() {
        return includeTexturePixels;
    }

    /**
     * Only capture draws matching these filters.
     * 
     */
    @JsonProperty("filter")
    public Filter getFilter() {
        return filter;
    }

    /**
     * Run player (occlusion-mesh) detection during this capture. Off by default — the server does NO player introspection on a normal frame grab. Only trackPlayer() (→ /api/player) sets this, so the per-frame path stays stall-free. When set, the captured frame carries the player's world tile (frame.playerPosition).
     * 
     */
    @JsonProperty("detectPlayer")
    public Boolean getDetectPlayer() {
        return detectPlayer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CaptureOptions.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("includeMesh");
        sb.append('=');
        sb.append(((this.includeMesh == null)?"<null>":this.includeMesh));
        sb.append(',');
        sb.append("includeTexturePixels");
        sb.append('=');
        sb.append(((this.includeTexturePixels == null)?"<null>":this.includeTexturePixels));
        sb.append(',');
        sb.append("filter");
        sb.append('=');
        sb.append(((this.filter == null)?"<null>":this.filter));
        sb.append(',');
        sb.append("detectPlayer");
        sb.append('=');
        sb.append(((this.detectPlayer == null)?"<null>":this.detectPlayer));
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
        result = ((result* 31)+((this.includeTexturePixels == null)? 0 :this.includeTexturePixels.hashCode()));
        result = ((result* 31)+((this.filter == null)? 0 :this.filter.hashCode()));
        result = ((result* 31)+((this.detectPlayer == null)? 0 :this.detectPlayer.hashCode()));
        result = ((result* 31)+((this.includeMesh == null)? 0 :this.includeMesh.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CaptureOptions) == false) {
            return false;
        }
        CaptureOptions rhs = ((CaptureOptions) other);
        return (((((this.includeTexturePixels == rhs.includeTexturePixels)||((this.includeTexturePixels!= null)&&this.includeTexturePixels.equals(rhs.includeTexturePixels)))&&((this.filter == rhs.filter)||((this.filter!= null)&&this.filter.equals(rhs.filter))))&&((this.detectPlayer == rhs.detectPlayer)||((this.detectPlayer!= null)&&this.detectPlayer.equals(rhs.detectPlayer))))&&((this.includeMesh == rhs.includeMesh)||((this.includeMesh!= null)&&this.includeMesh.equals(rhs.includeMesh))));
    }

}
