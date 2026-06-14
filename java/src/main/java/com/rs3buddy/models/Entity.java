
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
 * One thing in the scene this frame.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "drawIndex",
    "kind",
    "world",
    "tile",
    "chunk",
    "floor",
    "meshId",
    "shaderId",
    "vertexCount",
    "tag",
    "screen"
})
@Generated("jsonschema2pojo")
public class Entity {

    /**
     * Stable per-entity identity within a frame. Same physical entity tends to get the same id across frames because it's hashed from (meshId, shaderId). For unique-instance tracking use  {@link  drawIndex } .
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("Stable per-entity identity within a frame. Same physical entity tends to get the same id across frames because it's hashed from (meshId, shaderId). For unique-instance tracking use  {@link  drawIndex } .")
    private String id;
    /**
     * Draw call index — uniquely identifies this draw within the frame.
     * (Required)
     * 
     */
    @JsonProperty("drawIndex")
    @JsonPropertyDescription("Draw call index \u2014 uniquely identifies this draw within the frame.")
    private Double drawIndex;
    /**
     * What kind of thing an Entity represents.
     * (Required)
     * 
     */
    @JsonProperty("kind")
    @JsonPropertyDescription("What kind of thing an Entity represents.")
    private Entity.EntityKind kind;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("world")
    private WorldPos world;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tile")
    private TileCoord tile;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("chunk")
    private ChunkCoord chunk;
    /**
     * Floor level (0–3 typical; see FLOOR_Y_BASE / FLOOR_Y_STEP).
     * (Required)
     * 
     */
    @JsonProperty("floor")
    @JsonPropertyDescription("Floor level (0\u20133 typical; see FLOOR_Y_BASE / FLOOR_Y_STEP).")
    private Double floor;
    /**
     * RS3 mesh ID — same value means same geometry across frames.
     * (Required)
     * 
     */
    @JsonProperty("meshId")
    @JsonPropertyDescription("RS3 mesh ID \u2014 same value means same geometry across frames.")
    private Double meshId;
    /**
     * GL program / shader ID.
     * (Required)
     * 
     */
    @JsonProperty("shaderId")
    @JsonPropertyDescription("GL program / shader ID.")
    private Double shaderId;
    /**
     * Vertex count of this draw (rough size proxy).
     * (Required)
     * 
     */
    @JsonProperty("vertexCount")
    @JsonPropertyDescription("Vertex count of this draw (rough size proxy).")
    private Double vertexCount;
    /**
     * Short, stable per-model fingerprint — a 5-char base36 hash of the mesh identity. Match a specific model with this (e.g. `o.tag === "k3p9z"`) instead of a raw vertexCount, which can run into the millions.
     * (Required)
     * 
     */
    @JsonProperty("tag")
    @JsonPropertyDescription("Short, stable per-model fingerprint \u2014 a 5-char base36 hash of the mesh identity. Match a specific model with this (e.g. `o.tag === \"k3p9z\"`) instead of a raw vertexCount, which can run into the millions.")
    private String tag;
    /**
     * Screen position of this entity's world origin. x/y are normalized [0,1] (0,0 = bottom-left, matching gl_FragCoord); depth is window depth [0,1]. Null when behind the camera or no view-projection was found this frame. Multiply x/y by your viewport size to get pixels.
     * (Required)
     * 
     */
    @JsonProperty("screen")
    @JsonPropertyDescription("Screen position of this entity's world origin. x/y are normalized [0,1] (0,0 = bottom-left, matching gl_FragCoord); depth is window depth [0,1]. Null when behind the camera or no view-projection was found this frame. Multiply x/y by your viewport size to get pixels.")
    private Object screen;

    /**
     * Stable per-entity identity within a frame. Same physical entity tends to get the same id across frames because it's hashed from (meshId, shaderId). For unique-instance tracking use  {@link  drawIndex } .
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Draw call index — uniquely identifies this draw within the frame.
     * (Required)
     * 
     */
    @JsonProperty("drawIndex")
    public Double getDrawIndex() {
        return drawIndex;
    }

    /**
     * What kind of thing an Entity represents.
     * (Required)
     * 
     */
    @JsonProperty("kind")
    public Entity.EntityKind getKind() {
        return kind;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("world")
    public WorldPos getWorld() {
        return world;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tile")
    public TileCoord getTile() {
        return tile;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("chunk")
    public ChunkCoord getChunk() {
        return chunk;
    }

    /**
     * Floor level (0–3 typical; see FLOOR_Y_BASE / FLOOR_Y_STEP).
     * (Required)
     * 
     */
    @JsonProperty("floor")
    public Double getFloor() {
        return floor;
    }

    /**
     * RS3 mesh ID — same value means same geometry across frames.
     * (Required)
     * 
     */
    @JsonProperty("meshId")
    public Double getMeshId() {
        return meshId;
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
     * Vertex count of this draw (rough size proxy).
     * (Required)
     * 
     */
    @JsonProperty("vertexCount")
    public Double getVertexCount() {
        return vertexCount;
    }

    /**
     * Short, stable per-model fingerprint — a 5-char base36 hash of the mesh identity. Match a specific model with this (e.g. `o.tag === "k3p9z"`) instead of a raw vertexCount, which can run into the millions.
     * (Required)
     * 
     */
    @JsonProperty("tag")
    public String getTag() {
        return tag;
    }

    /**
     * Screen position of this entity's world origin. x/y are normalized [0,1] (0,0 = bottom-left, matching gl_FragCoord); depth is window depth [0,1]. Null when behind the camera or no view-projection was found this frame. Multiply x/y by your viewport size to get pixels.
     * (Required)
     * 
     */
    @JsonProperty("screen")
    public Object getScreen() {
        return screen;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Entity.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("drawIndex");
        sb.append('=');
        sb.append(((this.drawIndex == null)?"<null>":this.drawIndex));
        sb.append(',');
        sb.append("kind");
        sb.append('=');
        sb.append(((this.kind == null)?"<null>":this.kind));
        sb.append(',');
        sb.append("world");
        sb.append('=');
        sb.append(((this.world == null)?"<null>":this.world));
        sb.append(',');
        sb.append("tile");
        sb.append('=');
        sb.append(((this.tile == null)?"<null>":this.tile));
        sb.append(',');
        sb.append("chunk");
        sb.append('=');
        sb.append(((this.chunk == null)?"<null>":this.chunk));
        sb.append(',');
        sb.append("floor");
        sb.append('=');
        sb.append(((this.floor == null)?"<null>":this.floor));
        sb.append(',');
        sb.append("meshId");
        sb.append('=');
        sb.append(((this.meshId == null)?"<null>":this.meshId));
        sb.append(',');
        sb.append("shaderId");
        sb.append('=');
        sb.append(((this.shaderId == null)?"<null>":this.shaderId));
        sb.append(',');
        sb.append("vertexCount");
        sb.append('=');
        sb.append(((this.vertexCount == null)?"<null>":this.vertexCount));
        sb.append(',');
        sb.append("tag");
        sb.append('=');
        sb.append(((this.tag == null)?"<null>":this.tag));
        sb.append(',');
        sb.append("screen");
        sb.append('=');
        sb.append(((this.screen == null)?"<null>":this.screen));
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
        result = ((result* 31)+((this.kind == null)? 0 :this.kind.hashCode()));
        result = ((result* 31)+((this.shaderId == null)? 0 :this.shaderId.hashCode()));
        result = ((result* 31)+((this.drawIndex == null)? 0 :this.drawIndex.hashCode()));
        result = ((result* 31)+((this.chunk == null)? 0 :this.chunk.hashCode()));
        result = ((result* 31)+((this.screen == null)? 0 :this.screen.hashCode()));
        result = ((result* 31)+((this.meshId == null)? 0 :this.meshId.hashCode()));
        result = ((result* 31)+((this.vertexCount == null)? 0 :this.vertexCount.hashCode()));
        result = ((result* 31)+((this.world == null)? 0 :this.world.hashCode()));
        result = ((result* 31)+((this.tile == null)? 0 :this.tile.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.tag == null)? 0 :this.tag.hashCode()));
        result = ((result* 31)+((this.floor == null)? 0 :this.floor.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Entity) == false) {
            return false;
        }
        Entity rhs = ((Entity) other);
        return (((((((((((((this.kind == rhs.kind)||((this.kind!= null)&&this.kind.equals(rhs.kind)))&&((this.shaderId == rhs.shaderId)||((this.shaderId!= null)&&this.shaderId.equals(rhs.shaderId))))&&((this.drawIndex == rhs.drawIndex)||((this.drawIndex!= null)&&this.drawIndex.equals(rhs.drawIndex))))&&((this.chunk == rhs.chunk)||((this.chunk!= null)&&this.chunk.equals(rhs.chunk))))&&((this.screen == rhs.screen)||((this.screen!= null)&&this.screen.equals(rhs.screen))))&&((this.meshId == rhs.meshId)||((this.meshId!= null)&&this.meshId.equals(rhs.meshId))))&&((this.vertexCount == rhs.vertexCount)||((this.vertexCount!= null)&&this.vertexCount.equals(rhs.vertexCount))))&&((this.world == rhs.world)||((this.world!= null)&&this.world.equals(rhs.world))))&&((this.tile == rhs.tile)||((this.tile!= null)&&this.tile.equals(rhs.tile))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.tag == rhs.tag)||((this.tag!= null)&&this.tag.equals(rhs.tag))))&&((this.floor == rhs.floor)||((this.floor!= null)&&this.floor.equals(rhs.floor))));
    }


    /**
     * What kind of thing an Entity represents.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum EntityKind {

        PLAYER("player"),
        NPC("npc"),
        SCENERY("scenery"),
        FLOOR("floor"),
        WATER("water"),
        PARTICLES("particles"),
        UI("ui"),
        UNKNOWN("unknown");
        private final String value;
        private final static Map<String, Entity.EntityKind> CONSTANTS = new HashMap<String, Entity.EntityKind>();

        static {
            for (Entity.EntityKind c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        EntityKind(String value) {
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
        public static Entity.EntityKind fromValue(String value) {
            Entity.EntityKind constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
