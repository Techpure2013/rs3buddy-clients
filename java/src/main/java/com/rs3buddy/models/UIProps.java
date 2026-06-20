
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
 * Style + content props for a widget (JSON-serializable subset of `Props`).
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "width",
    "height",
    "pad",
    "margin",
    "gap",
    "bg",
    "color",
    "fill",
    "track",
    "outline",
    "outlineWidth",
    "radius",
    "opacity",
    "shadow",
    "blend",
    "animation",
    "align",
    "justify",
    "font",
    "fontSize",
    "text",
    "value",
    "max",
    "min",
    "vertical",
    "src",
    "tint",
    "anchor",
    "tile",
    "world",
    "id",
    "group"
})
@Generated("jsonschema2pojo")
public class UIProps {

    @JsonProperty("width")
    private Object width;
    @JsonProperty("height")
    private Object height;
    /**
     * Padding/margin: one value (all sides), [vertical, horizontal], or [top,right,bottom,left].
     * 
     */
    @JsonProperty("pad")
    @JsonPropertyDescription("Padding/margin: one value (all sides), [vertical, horizontal], or [top,right,bottom,left].")
    private Object pad;
    /**
     * Padding/margin: one value (all sides), [vertical, horizontal], or [top,right,bottom,left].
     * 
     */
    @JsonProperty("margin")
    @JsonPropertyDescription("Padding/margin: one value (all sides), [vertical, horizontal], or [top,right,bottom,left].")
    private Object margin;
    @JsonProperty("gap")
    private Double gap;
    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("bg")
    @JsonPropertyDescription("Color \u2014 accepts:   \u2022 `\"#rrggbb\"` or `\"#rrggbbaa\"` hex   \u2022 `\"rgb(r,g,b)\"` or `\"rgba(r,g,b,a)\"` (a in 0\u20131)   \u2022 `[r, g, b]` or `[r, g, b, a]` tuples (0\u2013255)   \u2022 Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.")
    private Object bg;
    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("color")
    @JsonPropertyDescription("Color \u2014 accepts:   \u2022 `\"#rrggbb\"` or `\"#rrggbbaa\"` hex   \u2022 `\"rgb(r,g,b)\"` or `\"rgba(r,g,b,a)\"` (a in 0\u20131)   \u2022 `[r, g, b]` or `[r, g, b, a]` tuples (0\u2013255)   \u2022 Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.")
    private Object color;
    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("fill")
    @JsonPropertyDescription("Color \u2014 accepts:   \u2022 `\"#rrggbb\"` or `\"#rrggbbaa\"` hex   \u2022 `\"rgb(r,g,b)\"` or `\"rgba(r,g,b,a)\"` (a in 0\u20131)   \u2022 `[r, g, b]` or `[r, g, b, a]` tuples (0\u2013255)   \u2022 Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.")
    private Object fill;
    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("track")
    @JsonPropertyDescription("Color \u2014 accepts:   \u2022 `\"#rrggbb\"` or `\"#rrggbbaa\"` hex   \u2022 `\"rgb(r,g,b)\"` or `\"rgba(r,g,b,a)\"` (a in 0\u20131)   \u2022 `[r, g, b]` or `[r, g, b, a]` tuples (0\u2013255)   \u2022 Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.")
    private Object track;
    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("outline")
    @JsonPropertyDescription("Color \u2014 accepts:   \u2022 `\"#rrggbb\"` or `\"#rrggbbaa\"` hex   \u2022 `\"rgb(r,g,b)\"` or `\"rgba(r,g,b,a)\"` (a in 0\u20131)   \u2022 `[r, g, b]` or `[r, g, b, a]` tuples (0\u2013255)   \u2022 Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.")
    private Object outline;
    @JsonProperty("outlineWidth")
    private Double outlineWidth;
    @JsonProperty("radius")
    private Double radius;
    @JsonProperty("opacity")
    private Double opacity;
    @JsonProperty("shadow")
    private Object shadow;
    /**
     * Blending mode for compositing the shape against the screen.
     * 
     */
    @JsonProperty("blend")
    @JsonPropertyDescription("Blending mode for compositing the shape against the screen.")
    private UIProps.BlendMode blend;
    @JsonProperty("animation")
    private AnimationSpec animation;
    /**
     * Cross-axis alignment of a container's children.
     * 
     */
    @JsonProperty("align")
    @JsonPropertyDescription("Cross-axis alignment of a container's children.")
    private UIProps.Align align;
    /**
     * Main-axis distribution of a container's children.
     * 
     */
    @JsonProperty("justify")
    @JsonPropertyDescription("Main-axis distribution of a container's children.")
    private UIProps.Justify justify;
    @JsonProperty("font")
    private String font;
    @JsonProperty("fontSize")
    private Double fontSize;
    @JsonProperty("text")
    private Object text;
    @JsonProperty("value")
    private Double value;
    @JsonProperty("max")
    private Double max;
    @JsonProperty("min")
    private Double min;
    @JsonProperty("vertical")
    private Boolean vertical;
    /**
     * Image source (a path or URL on the wire; bytes are TS-only).
     * 
     */
    @JsonProperty("src")
    @JsonPropertyDescription("Image source (a path or URL on the wire; bytes are TS-only).")
    private String src;
    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("tint")
    @JsonPropertyDescription("Color \u2014 accepts:   \u2022 `\"#rrggbb\"` or `\"#rrggbbaa\"` hex   \u2022 `\"rgb(r,g,b)\"` or `\"rgba(r,g,b,a)\"` (a in 0\u20131)   \u2022 `[r, g, b]` or `[r, g, b, a]` tuples (0\u2013255)   \u2022 Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.")
    private Object tint;
    @JsonProperty("anchor")
    private Object anchor;
    /**
     * Tile anchor for world-space widgets.
     * 
     */
    @JsonProperty("tile")
    @JsonPropertyDescription("Tile anchor for world-space widgets.")
    private UITile tile;
    /**
     * World-space (engine units) anchor.
     * 
     */
    @JsonProperty("world")
    @JsonPropertyDescription("World-space (engine units) anchor.")
    private UIWorld world;
    @JsonProperty("id")
    private String id;
    @JsonProperty("group")
    private String group;

    @JsonProperty("width")
    public Object getWidth() {
        return width;
    }

    @JsonProperty("height")
    public Object getHeight() {
        return height;
    }

    /**
     * Padding/margin: one value (all sides), [vertical, horizontal], or [top,right,bottom,left].
     * 
     */
    @JsonProperty("pad")
    public Object getPad() {
        return pad;
    }

    /**
     * Padding/margin: one value (all sides), [vertical, horizontal], or [top,right,bottom,left].
     * 
     */
    @JsonProperty("margin")
    public Object getMargin() {
        return margin;
    }

    @JsonProperty("gap")
    public Double getGap() {
        return gap;
    }

    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("bg")
    public Object getBg() {
        return bg;
    }

    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("color")
    public Object getColor() {
        return color;
    }

    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("fill")
    public Object getFill() {
        return fill;
    }

    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("track")
    public Object getTrack() {
        return track;
    }

    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("outline")
    public Object getOutline() {
        return outline;
    }

    @JsonProperty("outlineWidth")
    public Double getOutlineWidth() {
        return outlineWidth;
    }

    @JsonProperty("radius")
    public Double getRadius() {
        return radius;
    }

    @JsonProperty("opacity")
    public Double getOpacity() {
        return opacity;
    }

    @JsonProperty("shadow")
    public Object getShadow() {
        return shadow;
    }

    /**
     * Blending mode for compositing the shape against the screen.
     * 
     */
    @JsonProperty("blend")
    public UIProps.BlendMode getBlend() {
        return blend;
    }

    @JsonProperty("animation")
    public AnimationSpec getAnimation() {
        return animation;
    }

    /**
     * Cross-axis alignment of a container's children.
     * 
     */
    @JsonProperty("align")
    public UIProps.Align getAlign() {
        return align;
    }

    /**
     * Main-axis distribution of a container's children.
     * 
     */
    @JsonProperty("justify")
    public UIProps.Justify getJustify() {
        return justify;
    }

    @JsonProperty("font")
    public String getFont() {
        return font;
    }

    @JsonProperty("fontSize")
    public Double getFontSize() {
        return fontSize;
    }

    @JsonProperty("text")
    public Object getText() {
        return text;
    }

    @JsonProperty("value")
    public Double getValue() {
        return value;
    }

    @JsonProperty("max")
    public Double getMax() {
        return max;
    }

    @JsonProperty("min")
    public Double getMin() {
        return min;
    }

    @JsonProperty("vertical")
    public Boolean getVertical() {
        return vertical;
    }

    /**
     * Image source (a path or URL on the wire; bytes are TS-only).
     * 
     */
    @JsonProperty("src")
    public String getSrc() {
        return src;
    }

    /**
     * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
     * 
     */
    @JsonProperty("tint")
    public Object getTint() {
        return tint;
    }

    @JsonProperty("anchor")
    public Object getAnchor() {
        return anchor;
    }

    /**
     * Tile anchor for world-space widgets.
     * 
     */
    @JsonProperty("tile")
    public UITile getTile() {
        return tile;
    }

    /**
     * World-space (engine units) anchor.
     * 
     */
    @JsonProperty("world")
    public UIWorld getWorld() {
        return world;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("group")
    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(UIProps.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("width");
        sb.append('=');
        sb.append(((this.width == null)?"<null>":this.width));
        sb.append(',');
        sb.append("height");
        sb.append('=');
        sb.append(((this.height == null)?"<null>":this.height));
        sb.append(',');
        sb.append("pad");
        sb.append('=');
        sb.append(((this.pad == null)?"<null>":this.pad));
        sb.append(',');
        sb.append("margin");
        sb.append('=');
        sb.append(((this.margin == null)?"<null>":this.margin));
        sb.append(',');
        sb.append("gap");
        sb.append('=');
        sb.append(((this.gap == null)?"<null>":this.gap));
        sb.append(',');
        sb.append("bg");
        sb.append('=');
        sb.append(((this.bg == null)?"<null>":this.bg));
        sb.append(',');
        sb.append("color");
        sb.append('=');
        sb.append(((this.color == null)?"<null>":this.color));
        sb.append(',');
        sb.append("fill");
        sb.append('=');
        sb.append(((this.fill == null)?"<null>":this.fill));
        sb.append(',');
        sb.append("track");
        sb.append('=');
        sb.append(((this.track == null)?"<null>":this.track));
        sb.append(',');
        sb.append("outline");
        sb.append('=');
        sb.append(((this.outline == null)?"<null>":this.outline));
        sb.append(',');
        sb.append("outlineWidth");
        sb.append('=');
        sb.append(((this.outlineWidth == null)?"<null>":this.outlineWidth));
        sb.append(',');
        sb.append("radius");
        sb.append('=');
        sb.append(((this.radius == null)?"<null>":this.radius));
        sb.append(',');
        sb.append("opacity");
        sb.append('=');
        sb.append(((this.opacity == null)?"<null>":this.opacity));
        sb.append(',');
        sb.append("shadow");
        sb.append('=');
        sb.append(((this.shadow == null)?"<null>":this.shadow));
        sb.append(',');
        sb.append("blend");
        sb.append('=');
        sb.append(((this.blend == null)?"<null>":this.blend));
        sb.append(',');
        sb.append("animation");
        sb.append('=');
        sb.append(((this.animation == null)?"<null>":this.animation));
        sb.append(',');
        sb.append("align");
        sb.append('=');
        sb.append(((this.align == null)?"<null>":this.align));
        sb.append(',');
        sb.append("justify");
        sb.append('=');
        sb.append(((this.justify == null)?"<null>":this.justify));
        sb.append(',');
        sb.append("font");
        sb.append('=');
        sb.append(((this.font == null)?"<null>":this.font));
        sb.append(',');
        sb.append("fontSize");
        sb.append('=');
        sb.append(((this.fontSize == null)?"<null>":this.fontSize));
        sb.append(',');
        sb.append("text");
        sb.append('=');
        sb.append(((this.text == null)?"<null>":this.text));
        sb.append(',');
        sb.append("value");
        sb.append('=');
        sb.append(((this.value == null)?"<null>":this.value));
        sb.append(',');
        sb.append("max");
        sb.append('=');
        sb.append(((this.max == null)?"<null>":this.max));
        sb.append(',');
        sb.append("min");
        sb.append('=');
        sb.append(((this.min == null)?"<null>":this.min));
        sb.append(',');
        sb.append("vertical");
        sb.append('=');
        sb.append(((this.vertical == null)?"<null>":this.vertical));
        sb.append(',');
        sb.append("src");
        sb.append('=');
        sb.append(((this.src == null)?"<null>":this.src));
        sb.append(',');
        sb.append("tint");
        sb.append('=');
        sb.append(((this.tint == null)?"<null>":this.tint));
        sb.append(',');
        sb.append("anchor");
        sb.append('=');
        sb.append(((this.anchor == null)?"<null>":this.anchor));
        sb.append(',');
        sb.append("tile");
        sb.append('=');
        sb.append(((this.tile == null)?"<null>":this.tile));
        sb.append(',');
        sb.append("world");
        sb.append('=');
        sb.append(((this.world == null)?"<null>":this.world));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("group");
        sb.append('=');
        sb.append(((this.group == null)?"<null>":this.group));
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
        result = ((result* 31)+((this.shadow == null)? 0 :this.shadow.hashCode()));
        result = ((result* 31)+((this.bg == null)? 0 :this.bg.hashCode()));
        result = ((result* 31)+((this.blend == null)? 0 :this.blend.hashCode()));
        result = ((result* 31)+((this.vertical == null)? 0 :this.vertical.hashCode()));
        result = ((result* 31)+((this.align == null)? 0 :this.align.hashCode()));
        result = ((result* 31)+((this.tint == null)? 0 :this.tint.hashCode()));
        result = ((result* 31)+((this.pad == null)? 0 :this.pad.hashCode()));
        result = ((result* 31)+((this.outline == null)? 0 :this.outline.hashCode()));
        result = ((result* 31)+((this.min == null)? 0 :this.min.hashCode()));
        result = ((result* 31)+((this.world == null)? 0 :this.world.hashCode()));
        result = ((result* 31)+((this.justify == null)? 0 :this.justify.hashCode()));
        result = ((result* 31)+((this.gap == null)? 0 :this.gap.hashCode()));
        result = ((result* 31)+((this.tile == null)? 0 :this.tile.hashCode()));
        result = ((result* 31)+((this.text == null)? 0 :this.text.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.track == null)? 0 :this.track.hashCode()));
        result = ((result* 31)+((this.radius == null)? 0 :this.radius.hashCode()));
        result = ((result* 31)+((this.value == null)? 0 :this.value.hashCode()));
        result = ((result* 31)+((this.height == null)? 0 :this.height.hashCode()));
        result = ((result* 31)+((this.group == null)? 0 :this.group.hashCode()));
        result = ((result* 31)+((this.margin == null)? 0 :this.margin.hashCode()));
        result = ((result* 31)+((this.max == null)? 0 :this.max.hashCode()));
        result = ((result* 31)+((this.src == null)? 0 :this.src.hashCode()));
        result = ((result* 31)+((this.fill == null)? 0 :this.fill.hashCode()));
        result = ((result* 31)+((this.animation == null)? 0 :this.animation.hashCode()));
        result = ((result* 31)+((this.outlineWidth == null)? 0 :this.outlineWidth.hashCode()));
        result = ((result* 31)+((this.anchor == null)? 0 :this.anchor.hashCode()));
        result = ((result* 31)+((this.width == null)? 0 :this.width.hashCode()));
        result = ((result* 31)+((this.fontSize == null)? 0 :this.fontSize.hashCode()));
        result = ((result* 31)+((this.opacity == null)? 0 :this.opacity.hashCode()));
        result = ((result* 31)+((this.font == null)? 0 :this.font.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UIProps) == false) {
            return false;
        }
        UIProps rhs = ((UIProps) other);
        return (((((((((((((((((((((((((((((((((this.color == rhs.color)||((this.color!= null)&&this.color.equals(rhs.color)))&&((this.shadow == rhs.shadow)||((this.shadow!= null)&&this.shadow.equals(rhs.shadow))))&&((this.bg == rhs.bg)||((this.bg!= null)&&this.bg.equals(rhs.bg))))&&((this.blend == rhs.blend)||((this.blend!= null)&&this.blend.equals(rhs.blend))))&&((this.vertical == rhs.vertical)||((this.vertical!= null)&&this.vertical.equals(rhs.vertical))))&&((this.align == rhs.align)||((this.align!= null)&&this.align.equals(rhs.align))))&&((this.tint == rhs.tint)||((this.tint!= null)&&this.tint.equals(rhs.tint))))&&((this.pad == rhs.pad)||((this.pad!= null)&&this.pad.equals(rhs.pad))))&&((this.outline == rhs.outline)||((this.outline!= null)&&this.outline.equals(rhs.outline))))&&((this.min == rhs.min)||((this.min!= null)&&this.min.equals(rhs.min))))&&((this.world == rhs.world)||((this.world!= null)&&this.world.equals(rhs.world))))&&((this.justify == rhs.justify)||((this.justify!= null)&&this.justify.equals(rhs.justify))))&&((this.gap == rhs.gap)||((this.gap!= null)&&this.gap.equals(rhs.gap))))&&((this.tile == rhs.tile)||((this.tile!= null)&&this.tile.equals(rhs.tile))))&&((this.text == rhs.text)||((this.text!= null)&&this.text.equals(rhs.text))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.track == rhs.track)||((this.track!= null)&&this.track.equals(rhs.track))))&&((this.radius == rhs.radius)||((this.radius!= null)&&this.radius.equals(rhs.radius))))&&((this.value == rhs.value)||((this.value!= null)&&this.value.equals(rhs.value))))&&((this.height == rhs.height)||((this.height!= null)&&this.height.equals(rhs.height))))&&((this.group == rhs.group)||((this.group!= null)&&this.group.equals(rhs.group))))&&((this.margin == rhs.margin)||((this.margin!= null)&&this.margin.equals(rhs.margin))))&&((this.max == rhs.max)||((this.max!= null)&&this.max.equals(rhs.max))))&&((this.src == rhs.src)||((this.src!= null)&&this.src.equals(rhs.src))))&&((this.fill == rhs.fill)||((this.fill!= null)&&this.fill.equals(rhs.fill))))&&((this.animation == rhs.animation)||((this.animation!= null)&&this.animation.equals(rhs.animation))))&&((this.outlineWidth == rhs.outlineWidth)||((this.outlineWidth!= null)&&this.outlineWidth.equals(rhs.outlineWidth))))&&((this.anchor == rhs.anchor)||((this.anchor!= null)&&this.anchor.equals(rhs.anchor))))&&((this.width == rhs.width)||((this.width!= null)&&this.width.equals(rhs.width))))&&((this.fontSize == rhs.fontSize)||((this.fontSize!= null)&&this.fontSize.equals(rhs.fontSize))))&&((this.opacity == rhs.opacity)||((this.opacity!= null)&&this.opacity.equals(rhs.opacity))))&&((this.font == rhs.font)||((this.font!= null)&&this.font.equals(rhs.font))));
    }


    /**
     * Cross-axis alignment of a container's children.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum Align {

        START("start"),
        CENTER("center"),
        END("end"),
        STRETCH("stretch");
        private final String value;
        private final static Map<String, UIProps.Align> CONSTANTS = new HashMap<String, UIProps.Align>();

        static {
            for (UIProps.Align c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Align(String value) {
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
        public static UIProps.Align fromValue(String value) {
            UIProps.Align constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Blending mode for compositing the shape against the screen.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum BlendMode {

        NORMAL("normal"),
        ADDITIVE("additive"),
        MULTIPLY("multiply");
        private final String value;
        private final static Map<String, UIProps.BlendMode> CONSTANTS = new HashMap<String, UIProps.BlendMode>();

        static {
            for (UIProps.BlendMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        BlendMode(String value) {
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
        public static UIProps.BlendMode fromValue(String value) {
            UIProps.BlendMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Main-axis distribution of a container's children.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum Justify {

        START("start"),
        CENTER("center"),
        END("end"),
        BETWEEN("between"),
        AROUND("around"),
        EVENLY("evenly");
        private final String value;
        private final static Map<String, UIProps.Justify> CONSTANTS = new HashMap<String, UIProps.Justify>();

        static {
            for (UIProps.Justify c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Justify(String value) {
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
        public static UIProps.Justify fromValue(String value) {
            UIProps.Justify constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
