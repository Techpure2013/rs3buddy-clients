
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Simplified draw call info — one thing the GPU drew this frame.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "index",
    "shaderId",
    "vertexCount",
    "meshId",
    "targetFbo",
    "viewport",
    "textures",
    "uniforms",
    "sampleRect",
    "spriteRect",
    "screenBbox",
    "rotationM12",
    "rotationM21",
    "composedQuads"
})
@Generated("jsonschema2pojo")
public class DrawInfo {

    /**
     * Draw call index within the frame (0-based).
     * (Required)
     * 
     */
    @JsonProperty("index")
    @JsonPropertyDescription("Draw call index within the frame (0-based).")
    private Double index;
    /**
     * GL program / shader ID.
     * (Required)
     * 
     */
    @JsonProperty("shaderId")
    @JsonPropertyDescription("GL program / shader ID.")
    private Double shaderId;
    /**
     * Total vertices drawn.
     * (Required)
     * 
     */
    @JsonProperty("vertexCount")
    @JsonPropertyDescription("Total vertices drawn.")
    private Double vertexCount;
    /**
     * Internal mesh ID (same mesh = same geometry).
     * (Required)
     * 
     */
    @JsonProperty("meshId")
    @JsonPropertyDescription("Internal mesh ID (same mesh = same geometry).")
    private Double meshId;
    /**
     * Which framebuffer this was drawn to (0 = screen).
     * (Required)
     * 
     */
    @JsonProperty("targetFbo")
    @JsonPropertyDescription("Which framebuffer this was drawn to (0 = screen).")
    private Double targetFbo;
    /**
     * A rectangle on screen.
     * (Required)
     * 
     */
    @JsonProperty("viewport")
    @JsonPropertyDescription("A rectangle on screen.")
    private Rect viewport;
    /**
     * Textures bound during this draw.
     * (Required)
     * 
     */
    @JsonProperty("textures")
    @JsonPropertyDescription("Textures bound during this draw.")
    private List<Texture> textures = new ArrayList<Texture>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("uniforms")
    private UniformValueMap uniforms;
    /**
     * Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.
     * 
     */
    @JsonProperty("sampleRect")
    @JsonPropertyDescription("Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.")
    private SampleRect sampleRect;
    /**
     * EXACT sprite sub-rectangle in atlas pixels — the whole sprite the draw rendered, NOT just the sampled sub-region. Computed from the per-vertex `aTextureUVAtlasMin` + `aTextureUVAtlasExtents` vertex attributes when the RS3 UI shader carries them. Multiplying the UV values by the bound atlas's pixel dimensions reproduces the sprite origin and extent exactly.
     * 
     * When the bound program lacks these attributes (most non-UI shaders), `spriteRect` is `undefined` and consumers fall back to  {@link  SampleRect } . The Live-UI "View in Atlas" cross-link prefers `spriteRect` because it boxes the WHOLE sprite (which is what the user wants to see), not the sub-sample the draw happened to read.
     * 
     */
    @JsonProperty("spriteRect")
    @JsonPropertyDescription("EXACT sprite sub-rectangle in atlas pixels \u2014 the whole sprite the draw rendered, NOT just the sampled sub-region. Computed from the per-vertex `aTextureUVAtlasMin` + `aTextureUVAtlasExtents` vertex attributes when the RS3 UI shader carries them. Multiplying the UV values by the bound atlas's pixel dimensions reproduces the sprite origin and extent exactly.\n\nWhen the bound program lacks these attributes (most non-UI shaders), `spriteRect` is `undefined` and consumers fall back to  {@link  SampleRect } . The Live-UI \"View in Atlas\" cross-link prefers `spriteRect` because it boxes the WHOLE sprite (which is what the user wants to see), not the sub-sample the draw happened to read.")
    private SpriteRect spriteRect;
    /**
     * Screen-space bounding box (in screen pixels) where a draw landed. Computed from the draw's vertex-position range in the shared `_vertexPositions` array. UI draws (`drawType === "ui" && targetFbo === 0`) are the only draws that get a usable bbox here today; world draws use model+projection matrices the server doesn't apply, so their raw positions are 3D world coords with no direct screen mapping.
     * 
     */
    @JsonProperty("screenBbox")
    @JsonPropertyDescription("Screen-space bounding box (in screen pixels) where a draw landed. Computed from the draw's vertex-position range in the shared `_vertexPositions` array. UI draws (`drawType === \"ui\" && targetFbo === 0`) are the only draws that get a usable bbox here today; world draws use model+projection matrices the server doesn't apply, so their raw positions are 3D world coords with no direct screen mapping.")
    private ScreenBbox screenBbox;
    /**
     * Off-diagonal model-matrix entries surfaced for the Composed UI renderer. `m12` / `m21` map to `modelMatrix[8]` / `modelMatrix[1]` in column-major. Non-zero values mean the draw rotates / shears its source rect.
     * 
     */
    @JsonProperty("rotationM12")
    @JsonPropertyDescription("Off-diagonal model-matrix entries surfaced for the Composed UI renderer. `m12` / `m21` map to `modelMatrix[8]` / `modelMatrix[1]` in column-major. Non-zero values mean the draw rotates / shears its source rect.")
    private Double rotationM12;
    @JsonProperty("rotationM21")
    private Double rotationM21;
    /**
     * Per-quad UI decode — one entry per triangle pair). Present only for UI draws (`drawType === "ui"`) that carry indices + uvs + atlasMin/Extents AND have a primary bound texture with valid pixel dimensions. When present, consumers should iterate `composedQuads` for hit-testing and sprite identification — each quad has its own sample/sprite/ screen rect, so a single draw with 50 sprite quads exposes 50 clickable elements instead of one giant collapsed rect.
     * 
     * When absent (no indices, no atlas attributes, etc.), consumers fall back to the per-draw `sampleRect` / `spriteRect` / `screenBbox` fields.
     * 
     */
    @JsonProperty("composedQuads")
    @JsonPropertyDescription("Per-quad UI decode \u2014 one entry per triangle pair). Present only for UI draws (`drawType === \"ui\"`) that carry indices + uvs + atlasMin/Extents AND have a primary bound texture with valid pixel dimensions. When present, consumers should iterate `composedQuads` for hit-testing and sprite identification \u2014 each quad has its own sample/sprite/ screen rect, so a single draw with 50 sprite quads exposes 50 clickable elements instead of one giant collapsed rect.\n\nWhen absent (no indices, no atlas attributes, etc.), consumers fall back to the per-draw `sampleRect` / `spriteRect` / `screenBbox` fields.")
    private List<ComposedQuad> composedQuads = new ArrayList<ComposedQuad>();

    /**
     * Draw call index within the frame (0-based).
     * (Required)
     * 
     */
    @JsonProperty("index")
    public Double getIndex() {
        return index;
    }

    /**
     * GL program / shader ID.
     * (Required)
     * 
     */
    @JsonProperty("shaderId")
    public Double getShaderId() {
        return shaderId;
    }

    /**
     * Total vertices drawn.
     * (Required)
     * 
     */
    @JsonProperty("vertexCount")
    public Double getVertexCount() {
        return vertexCount;
    }

    /**
     * Internal mesh ID (same mesh = same geometry).
     * (Required)
     * 
     */
    @JsonProperty("meshId")
    public Double getMeshId() {
        return meshId;
    }

    /**
     * Which framebuffer this was drawn to (0 = screen).
     * (Required)
     * 
     */
    @JsonProperty("targetFbo")
    public Double getTargetFbo() {
        return targetFbo;
    }

    /**
     * A rectangle on screen.
     * (Required)
     * 
     */
    @JsonProperty("viewport")
    public Rect getViewport() {
        return viewport;
    }

    /**
     * Textures bound during this draw.
     * (Required)
     * 
     */
    @JsonProperty("textures")
    public List<Texture> getTextures() {
        return textures;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("uniforms")
    public UniformValueMap getUniforms() {
        return uniforms;
    }

    /**
     * Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.
     * 
     */
    @JsonProperty("sampleRect")
    public SampleRect getSampleRect() {
        return sampleRect;
    }

    /**
     * EXACT sprite sub-rectangle in atlas pixels — the whole sprite the draw rendered, NOT just the sampled sub-region. Computed from the per-vertex `aTextureUVAtlasMin` + `aTextureUVAtlasExtents` vertex attributes when the RS3 UI shader carries them. Multiplying the UV values by the bound atlas's pixel dimensions reproduces the sprite origin and extent exactly.
     * 
     * When the bound program lacks these attributes (most non-UI shaders), `spriteRect` is `undefined` and consumers fall back to  {@link  SampleRect } . The Live-UI "View in Atlas" cross-link prefers `spriteRect` because it boxes the WHOLE sprite (which is what the user wants to see), not the sub-sample the draw happened to read.
     * 
     */
    @JsonProperty("spriteRect")
    public SpriteRect getSpriteRect() {
        return spriteRect;
    }

    /**
     * Screen-space bounding box (in screen pixels) where a draw landed. Computed from the draw's vertex-position range in the shared `_vertexPositions` array. UI draws (`drawType === "ui" && targetFbo === 0`) are the only draws that get a usable bbox here today; world draws use model+projection matrices the server doesn't apply, so their raw positions are 3D world coords with no direct screen mapping.
     * 
     */
    @JsonProperty("screenBbox")
    public ScreenBbox getScreenBbox() {
        return screenBbox;
    }

    /**
     * Off-diagonal model-matrix entries surfaced for the Composed UI renderer. `m12` / `m21` map to `modelMatrix[8]` / `modelMatrix[1]` in column-major. Non-zero values mean the draw rotates / shears its source rect.
     * 
     */
    @JsonProperty("rotationM12")
    public Double getRotationM12() {
        return rotationM12;
    }

    @JsonProperty("rotationM21")
    public Double getRotationM21() {
        return rotationM21;
    }

    /**
     * Per-quad UI decode — one entry per triangle pair). Present only for UI draws (`drawType === "ui"`) that carry indices + uvs + atlasMin/Extents AND have a primary bound texture with valid pixel dimensions. When present, consumers should iterate `composedQuads` for hit-testing and sprite identification — each quad has its own sample/sprite/ screen rect, so a single draw with 50 sprite quads exposes 50 clickable elements instead of one giant collapsed rect.
     * 
     * When absent (no indices, no atlas attributes, etc.), consumers fall back to the per-draw `sampleRect` / `spriteRect` / `screenBbox` fields.
     * 
     */
    @JsonProperty("composedQuads")
    public List<ComposedQuad> getComposedQuads() {
        return composedQuads;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DrawInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("index");
        sb.append('=');
        sb.append(((this.index == null)?"<null>":this.index));
        sb.append(',');
        sb.append("shaderId");
        sb.append('=');
        sb.append(((this.shaderId == null)?"<null>":this.shaderId));
        sb.append(',');
        sb.append("vertexCount");
        sb.append('=');
        sb.append(((this.vertexCount == null)?"<null>":this.vertexCount));
        sb.append(',');
        sb.append("meshId");
        sb.append('=');
        sb.append(((this.meshId == null)?"<null>":this.meshId));
        sb.append(',');
        sb.append("targetFbo");
        sb.append('=');
        sb.append(((this.targetFbo == null)?"<null>":this.targetFbo));
        sb.append(',');
        sb.append("viewport");
        sb.append('=');
        sb.append(((this.viewport == null)?"<null>":this.viewport));
        sb.append(',');
        sb.append("textures");
        sb.append('=');
        sb.append(((this.textures == null)?"<null>":this.textures));
        sb.append(',');
        sb.append("uniforms");
        sb.append('=');
        sb.append(((this.uniforms == null)?"<null>":this.uniforms));
        sb.append(',');
        sb.append("sampleRect");
        sb.append('=');
        sb.append(((this.sampleRect == null)?"<null>":this.sampleRect));
        sb.append(',');
        sb.append("spriteRect");
        sb.append('=');
        sb.append(((this.spriteRect == null)?"<null>":this.spriteRect));
        sb.append(',');
        sb.append("screenBbox");
        sb.append('=');
        sb.append(((this.screenBbox == null)?"<null>":this.screenBbox));
        sb.append(',');
        sb.append("rotationM12");
        sb.append('=');
        sb.append(((this.rotationM12 == null)?"<null>":this.rotationM12));
        sb.append(',');
        sb.append("rotationM21");
        sb.append('=');
        sb.append(((this.rotationM21 == null)?"<null>":this.rotationM21));
        sb.append(',');
        sb.append("composedQuads");
        sb.append('=');
        sb.append(((this.composedQuads == null)?"<null>":this.composedQuads));
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
        result = ((result* 31)+((this.screenBbox == null)? 0 :this.screenBbox.hashCode()));
        result = ((result* 31)+((this.rotationM12 == null)? 0 :this.rotationM12 .hashCode()));
        result = ((result* 31)+((this.spriteRect == null)? 0 :this.spriteRect.hashCode()));
        result = ((result* 31)+((this.textures == null)? 0 :this.textures.hashCode()));
        result = ((result* 31)+((this.shaderId == null)? 0 :this.shaderId.hashCode()));
        result = ((result* 31)+((this.index == null)? 0 :this.index.hashCode()));
        result = ((result* 31)+((this.sampleRect == null)? 0 :this.sampleRect.hashCode()));
        result = ((result* 31)+((this.uniforms == null)? 0 :this.uniforms.hashCode()));
        result = ((result* 31)+((this.meshId == null)? 0 :this.meshId.hashCode()));
        result = ((result* 31)+((this.vertexCount == null)? 0 :this.vertexCount.hashCode()));
        result = ((result* 31)+((this.viewport == null)? 0 :this.viewport.hashCode()));
        result = ((result* 31)+((this.composedQuads == null)? 0 :this.composedQuads.hashCode()));
        result = ((result* 31)+((this.rotationM21 == null)? 0 :this.rotationM21 .hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DrawInfo) == false) {
            return false;
        }
        DrawInfo rhs = ((DrawInfo) other);
        return (((((((((((((((this.targetFbo == rhs.targetFbo)||((this.targetFbo!= null)&&this.targetFbo.equals(rhs.targetFbo)))&&((this.screenBbox == rhs.screenBbox)||((this.screenBbox!= null)&&this.screenBbox.equals(rhs.screenBbox))))&&((this.rotationM12 == rhs.rotationM12)||((this.rotationM12 != null)&&this.rotationM12 .equals(rhs.rotationM12))))&&((this.spriteRect == rhs.spriteRect)||((this.spriteRect!= null)&&this.spriteRect.equals(rhs.spriteRect))))&&((this.textures == rhs.textures)||((this.textures!= null)&&this.textures.equals(rhs.textures))))&&((this.shaderId == rhs.shaderId)||((this.shaderId!= null)&&this.shaderId.equals(rhs.shaderId))))&&((this.index == rhs.index)||((this.index!= null)&&this.index.equals(rhs.index))))&&((this.sampleRect == rhs.sampleRect)||((this.sampleRect!= null)&&this.sampleRect.equals(rhs.sampleRect))))&&((this.uniforms == rhs.uniforms)||((this.uniforms!= null)&&this.uniforms.equals(rhs.uniforms))))&&((this.meshId == rhs.meshId)||((this.meshId!= null)&&this.meshId.equals(rhs.meshId))))&&((this.vertexCount == rhs.vertexCount)||((this.vertexCount!= null)&&this.vertexCount.equals(rhs.vertexCount))))&&((this.viewport == rhs.viewport)||((this.viewport!= null)&&this.viewport.equals(rhs.viewport))))&&((this.composedQuads == rhs.composedQuads)||((this.composedQuads!= null)&&this.composedQuads.equals(rhs.composedQuads))))&&((this.rotationM21 == rhs.rotationM21)||((this.rotationM21 != null)&&this.rotationM21 .equals(rhs.rotationM21))));
    }

}
