
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * One UI quad decoded from a draw's index buffer. RS3 UI draws batch many sprite quads into a single GPU draw (each quad is two triangles = 6 indices). The legacy per-draw `sampleRect` / `spriteRect` / `screenBbox` collapse all quads into one bounding rect, which loses per-sprite resolution. `composedQuads` walks the index buffer in groups of 6 and emits one entry per quad with its own atlas + screen rects.
 * 
 * Coordinate spaces:   - `sampleRect`  — atlas pixels (sub-region the quad sampled)   - `spriteRect`  — atlas pixels (WHOLE sprite the quad belongs to)   - `screen.x/y/w/h` — screen pixels (top-left + extents of the quad)   - `m12` / `m21` — shear/rotation off-diagonals
 * 
 * Each quad's `topleft` vertex carries the atlas-min/extents/UV-origin (those attributes are constant across all 4 vertices of one quad in RS3's UI shader). Degenerate quads (zero extent or NaN-poisoned) are skipped at decode time so consumers never see a w==0 entry.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sampleRect",
    "spriteRect",
    "screen",
    "m12",
    "m21",
    "whitesprite",
    "color",
    "_colourOff",
    "_topleft"
})
@Generated("jsonschema2pojo")
public class ComposedQuad {

    /**
     * Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.
     * (Required)
     * 
     */
    @JsonProperty("sampleRect")
    @JsonPropertyDescription("Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.")
    private SampleRect sampleRect;
    /**
     * Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.
     * (Required)
     * 
     */
    @JsonProperty("spriteRect")
    @JsonPropertyDescription("Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.")
    private SampleRect spriteRect;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("screen")
    private Screen screen;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m12")
    private Double m12;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m21")
    private Double m21;
    /**
     * True when the shader hardcodes a solid white fill (reference whitesprite sentinel: sample origin < -60000). Consumers render a plain rect instead of sampling the atlas.
     * 
     */
    @JsonProperty("whitesprite")
    @JsonPropertyDescription("True when the shader hardcodes a solid white fill (reference whitesprite sentinel: sample origin < -60000). Consumers render a plain rect instead of sampling the atlas.")
    private Boolean whitesprite;
    /**
     * Per-vertex aVertexColour RGBA in [0,1] (the glyph/sprite tint). RS3 multiplies white UI/text pixels by this colour. Undefined when the program lacks aVertexColour or it wasn't captured.
     * 
     */
    @JsonProperty("color")
    @JsonPropertyDescription("Per-vertex aVertexColour RGBA in [0,1] (the glyph/sprite tint). RS3 multiplies white UI/text pixels by this colour. Undefined when the program lacks aVertexColour or it wasn't captured.")
    private List<Double> color = new ArrayList<Double>();
    /**
     * DEBUG (chat-colour trace): the draw's colour-pool base offset (`server.colourDataOffset`, vec4 units) and this quad's top-left vertex index. The tint is read at `pool[(_colourOff + _topleft)]`. Temporary — remove once the colour read is verified.
     * 
     */
    @JsonProperty("_colourOff")
    @JsonPropertyDescription("DEBUG (chat-colour trace): the draw's colour-pool base offset (`server.colourDataOffset`, vec4 units) and this quad's top-left vertex index. The tint is read at `pool[(_colourOff + _topleft)]`. Temporary \u2014 remove once the colour read is verified.")
    private Double colourOff;
    @JsonProperty("_topleft")
    private Double topleft;

    /**
     * Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.
     * (Required)
     * 
     */
    @JsonProperty("sampleRect")
    public SampleRect getSampleRect() {
        return sampleRect;
    }

    /**
     * Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.
     * (Required)
     * 
     */
    @JsonProperty("spriteRect")
    public SampleRect getSpriteRect() {
        return spriteRect;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("screen")
    public Screen getScreen() {
        return screen;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m12")
    public Double getM12() {
        return m12;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m21")
    public Double getM21() {
        return m21;
    }

    /**
     * True when the shader hardcodes a solid white fill (reference whitesprite sentinel: sample origin < -60000). Consumers render a plain rect instead of sampling the atlas.
     * 
     */
    @JsonProperty("whitesprite")
    public Boolean getWhitesprite() {
        return whitesprite;
    }

    /**
     * Per-vertex aVertexColour RGBA in [0,1] (the glyph/sprite tint). RS3 multiplies white UI/text pixels by this colour. Undefined when the program lacks aVertexColour or it wasn't captured.
     * 
     */
    @JsonProperty("color")
    public List<Double> getColor() {
        return color;
    }

    /**
     * DEBUG (chat-colour trace): the draw's colour-pool base offset (`server.colourDataOffset`, vec4 units) and this quad's top-left vertex index. The tint is read at `pool[(_colourOff + _topleft)]`. Temporary — remove once the colour read is verified.
     * 
     */
    @JsonProperty("_colourOff")
    public Double getColourOff() {
        return colourOff;
    }

    @JsonProperty("_topleft")
    public Double getTopleft() {
        return topleft;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ComposedQuad.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sampleRect");
        sb.append('=');
        sb.append(((this.sampleRect == null)?"<null>":this.sampleRect));
        sb.append(',');
        sb.append("spriteRect");
        sb.append('=');
        sb.append(((this.spriteRect == null)?"<null>":this.spriteRect));
        sb.append(',');
        sb.append("screen");
        sb.append('=');
        sb.append(((this.screen == null)?"<null>":this.screen));
        sb.append(',');
        sb.append("m12");
        sb.append('=');
        sb.append(((this.m12 == null)?"<null>":this.m12));
        sb.append(',');
        sb.append("m21");
        sb.append('=');
        sb.append(((this.m21 == null)?"<null>":this.m21));
        sb.append(',');
        sb.append("whitesprite");
        sb.append('=');
        sb.append(((this.whitesprite == null)?"<null>":this.whitesprite));
        sb.append(',');
        sb.append("color");
        sb.append('=');
        sb.append(((this.color == null)?"<null>":this.color));
        sb.append(',');
        sb.append("colourOff");
        sb.append('=');
        sb.append(((this.colourOff == null)?"<null>":this.colourOff));
        sb.append(',');
        sb.append("topleft");
        sb.append('=');
        sb.append(((this.topleft == null)?"<null>":this.topleft));
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
        result = ((result* 31)+((this.spriteRect == null)? 0 :this.spriteRect.hashCode()));
        result = ((result* 31)+((this.color == null)? 0 :this.color.hashCode()));
        result = ((result* 31)+((this.topleft == null)? 0 :this.topleft.hashCode()));
        result = ((result* 31)+((this.sampleRect == null)? 0 :this.sampleRect.hashCode()));
        result = ((result* 31)+((this.screen == null)? 0 :this.screen.hashCode()));
        result = ((result* 31)+((this.colourOff == null)? 0 :this.colourOff.hashCode()));
        result = ((result* 31)+((this.m21 == null)? 0 :this.m21 .hashCode()));
        result = ((result* 31)+((this.m12 == null)? 0 :this.m12 .hashCode()));
        result = ((result* 31)+((this.whitesprite == null)? 0 :this.whitesprite.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ComposedQuad) == false) {
            return false;
        }
        ComposedQuad rhs = ((ComposedQuad) other);
        return ((((((((((this.spriteRect == rhs.spriteRect)||((this.spriteRect!= null)&&this.spriteRect.equals(rhs.spriteRect)))&&((this.color == rhs.color)||((this.color!= null)&&this.color.equals(rhs.color))))&&((this.topleft == rhs.topleft)||((this.topleft!= null)&&this.topleft.equals(rhs.topleft))))&&((this.sampleRect == rhs.sampleRect)||((this.sampleRect!= null)&&this.sampleRect.equals(rhs.sampleRect))))&&((this.screen == rhs.screen)||((this.screen!= null)&&this.screen.equals(rhs.screen))))&&((this.colourOff == rhs.colourOff)||((this.colourOff!= null)&&this.colourOff.equals(rhs.colourOff))))&&((this.m21 == rhs.m21)||((this.m21 != null)&&this.m21 .equals(rhs.m21))))&&((this.m12 == rhs.m12)||((this.m12 != null)&&this.m12 .equals(rhs.m12))))&&((this.whitesprite == rhs.whitesprite)||((this.whitesprite!= null)&&this.whitesprite.equals(rhs.whitesprite))));
    }

}
