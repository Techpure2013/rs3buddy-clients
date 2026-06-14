
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "world",
    "tile",
    "chunk",
    "floor"
})
@Generated("jsonschema2pojo")
public class PlayerInfo {

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
     * 
     * (Required)
     * 
     */
    @JsonProperty("floor")
    private Double floor;

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
     * 
     * (Required)
     * 
     */
    @JsonProperty("floor")
    public Double getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PlayerInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        result = ((result* 31)+((this.tile == null)? 0 :this.tile.hashCode()));
        result = ((result* 31)+((this.chunk == null)? 0 :this.chunk.hashCode()));
        result = ((result* 31)+((this.world == null)? 0 :this.world.hashCode()));
        result = ((result* 31)+((this.floor == null)? 0 :this.floor.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlayerInfo) == false) {
            return false;
        }
        PlayerInfo rhs = ((PlayerInfo) other);
        return (((((this.tile == rhs.tile)||((this.tile!= null)&&this.tile.equals(rhs.tile)))&&((this.chunk == rhs.chunk)||((this.chunk!= null)&&this.chunk.equals(rhs.chunk))))&&((this.world == rhs.world)||((this.world!= null)&&this.world.equals(rhs.world))))&&((this.floor == rhs.floor)||((this.floor!= null)&&this.floor.equals(rhs.floor))));
    }

}
