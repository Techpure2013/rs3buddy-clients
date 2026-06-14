
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * A full scene snapshot for one frame.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "timestamp",
    "player",
    "npcs",
    "scenery",
    "floor",
    "water",
    "particles",
    "other",
    "viewProj"
})
@Generated("jsonschema2pojo")
public class SceneSnapshot {

    /**
     * Frame timestamp (ms).
     * (Required)
     * 
     */
    @JsonProperty("timestamp")
    @JsonPropertyDescription("Frame timestamp (ms).")
    private Double timestamp;
    /**
     * The local player (from C++ isTinted detection), or null.
     * (Required)
     * 
     */
    @JsonProperty("player")
    @JsonPropertyDescription("The local player (from C++ isTinted detection), or null.")
    private Object player;
    /**
     * Living animated characters (excluding the player).
     * (Required)
     * 
     */
    @JsonProperty("npcs")
    @JsonPropertyDescription("Living animated characters (excluding the player).")
    private List<Entity> npcs = new ArrayList<Entity>();
    /**
     * Static world objects (trees, doors, buildings).
     * (Required)
     * 
     */
    @JsonProperty("scenery")
    @JsonPropertyDescription("Static world objects (trees, doors, buildings).")
    private List<Entity> scenery = new ArrayList<Entity>();
    /**
     * Floor / terrain tile draws.
     * (Required)
     * 
     */
    @JsonProperty("floor")
    @JsonPropertyDescription("Floor / terrain tile draws.")
    private List<Entity> floor = new ArrayList<Entity>();
    /**
     * Water surface draws.
     * (Required)
     * 
     */
    @JsonProperty("water")
    @JsonPropertyDescription("Water surface draws.")
    private List<Entity> water = new ArrayList<Entity>();
    /**
     * Particle / effect draws.
     * (Required)
     * 
     */
    @JsonProperty("particles")
    @JsonPropertyDescription("Particle / effect draws.")
    private List<Entity> particles = new ArrayList<Entity>();
    /**
     * Anything that didn't fit a clean kind.
     * (Required)
     * 
     */
    @JsonProperty("other")
    @JsonPropertyDescription("Anything that didn't fit a clean kind.")
    private List<Entity> other = new ArrayList<Entity>();
    /**
     * The frame's combined view-projection (camera) matrix, column-major, or null if no 3D draw exposed one. Project any world point yourself: clip = viewProj · [x,y,z,1]; screenXY = clip.xy / clip.w · 0.5 + 0.5.
     * (Required)
     * 
     */
    @JsonProperty("viewProj")
    @JsonPropertyDescription("The frame's combined view-projection (camera) matrix, column-major, or null if no 3D draw exposed one. Project any world point yourself: clip = viewProj \u00b7 [x,y,z,1]; screenXY = clip.xy / clip.w \u00b7 0.5 + 0.5.")
    private Object viewProj;

    /**
     * Frame timestamp (ms).
     * (Required)
     * 
     */
    @JsonProperty("timestamp")
    public Double getTimestamp() {
        return timestamp;
    }

    /**
     * The local player (from C++ isTinted detection), or null.
     * (Required)
     * 
     */
    @JsonProperty("player")
    public Object getPlayer() {
        return player;
    }

    /**
     * Living animated characters (excluding the player).
     * (Required)
     * 
     */
    @JsonProperty("npcs")
    public List<Entity> getNpcs() {
        return npcs;
    }

    /**
     * Static world objects (trees, doors, buildings).
     * (Required)
     * 
     */
    @JsonProperty("scenery")
    public List<Entity> getScenery() {
        return scenery;
    }

    /**
     * Floor / terrain tile draws.
     * (Required)
     * 
     */
    @JsonProperty("floor")
    public List<Entity> getFloor() {
        return floor;
    }

    /**
     * Water surface draws.
     * (Required)
     * 
     */
    @JsonProperty("water")
    public List<Entity> getWater() {
        return water;
    }

    /**
     * Particle / effect draws.
     * (Required)
     * 
     */
    @JsonProperty("particles")
    public List<Entity> getParticles() {
        return particles;
    }

    /**
     * Anything that didn't fit a clean kind.
     * (Required)
     * 
     */
    @JsonProperty("other")
    public List<Entity> getOther() {
        return other;
    }

    /**
     * The frame's combined view-projection (camera) matrix, column-major, or null if no 3D draw exposed one. Project any world point yourself: clip = viewProj · [x,y,z,1]; screenXY = clip.xy / clip.w · 0.5 + 0.5.
     * (Required)
     * 
     */
    @JsonProperty("viewProj")
    public Object getViewProj() {
        return viewProj;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SceneSnapshot.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("timestamp");
        sb.append('=');
        sb.append(((this.timestamp == null)?"<null>":this.timestamp));
        sb.append(',');
        sb.append("player");
        sb.append('=');
        sb.append(((this.player == null)?"<null>":this.player));
        sb.append(',');
        sb.append("npcs");
        sb.append('=');
        sb.append(((this.npcs == null)?"<null>":this.npcs));
        sb.append(',');
        sb.append("scenery");
        sb.append('=');
        sb.append(((this.scenery == null)?"<null>":this.scenery));
        sb.append(',');
        sb.append("floor");
        sb.append('=');
        sb.append(((this.floor == null)?"<null>":this.floor));
        sb.append(',');
        sb.append("water");
        sb.append('=');
        sb.append(((this.water == null)?"<null>":this.water));
        sb.append(',');
        sb.append("particles");
        sb.append('=');
        sb.append(((this.particles == null)?"<null>":this.particles));
        sb.append(',');
        sb.append("other");
        sb.append('=');
        sb.append(((this.other == null)?"<null>":this.other));
        sb.append(',');
        sb.append("viewProj");
        sb.append('=');
        sb.append(((this.viewProj == null)?"<null>":this.viewProj));
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
        result = ((result* 31)+((this.npcs == null)? 0 :this.npcs.hashCode()));
        result = ((result* 31)+((this.other == null)? 0 :this.other.hashCode()));
        result = ((result* 31)+((this.viewProj == null)? 0 :this.viewProj.hashCode()));
        result = ((result* 31)+((this.scenery == null)? 0 :this.scenery.hashCode()));
        result = ((result* 31)+((this.floor == null)? 0 :this.floor.hashCode()));
        result = ((result* 31)+((this.particles == null)? 0 :this.particles.hashCode()));
        result = ((result* 31)+((this.water == null)? 0 :this.water.hashCode()));
        result = ((result* 31)+((this.timestamp == null)? 0 :this.timestamp.hashCode()));
        result = ((result* 31)+((this.player == null)? 0 :this.player.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SceneSnapshot) == false) {
            return false;
        }
        SceneSnapshot rhs = ((SceneSnapshot) other);
        return ((((((((((this.npcs == rhs.npcs)||((this.npcs!= null)&&this.npcs.equals(rhs.npcs)))&&((this.other == rhs.other)||((this.other!= null)&&this.other.equals(rhs.other))))&&((this.viewProj == rhs.viewProj)||((this.viewProj!= null)&&this.viewProj.equals(rhs.viewProj))))&&((this.scenery == rhs.scenery)||((this.scenery!= null)&&this.scenery.equals(rhs.scenery))))&&((this.floor == rhs.floor)||((this.floor!= null)&&this.floor.equals(rhs.floor))))&&((this.particles == rhs.particles)||((this.particles!= null)&&this.particles.equals(rhs.particles))))&&((this.water == rhs.water)||((this.water!= null)&&this.water.equals(rhs.water))))&&((this.timestamp == rhs.timestamp)||((this.timestamp!= null)&&this.timestamp.equals(rhs.timestamp))))&&((this.player == rhs.player)||((this.player!= null)&&this.player.equals(rhs.player))));
    }

}
