
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Info about a loaded shader program.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "vertexSource",
    "fragmentSource",
    "uniforms",
    "attributes",
    "matchType",
    "kind",
    "fragmentHash",
    "vertexHash"
})
@Generated("jsonschema2pojo")
public class ShaderInfo {

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
    @JsonProperty("vertexSource")
    private String vertexSource;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("fragmentSource")
    private String fragmentSource;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("uniforms")
    private List<Uniform> uniforms = new ArrayList<Uniform>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("attributes")
    private List<Attribute> attributes = new ArrayList<Attribute>();
    /**
     * Classified shader class for `addShaderFx({ matchType })` — "animated"/"floor"/"water"/…
     * (Required)
     * 
     */
    @JsonProperty("matchType")
    @JsonPropertyDescription("Classified shader class for `addShaderFx({ matchType })` \u2014 \"animated\"/\"floor\"/\"water\"/\u2026")
    private String matchType;
    /**
     * Friendly kind (npc/scenery/floor/water/particles/ui/unknown).
     * (Required)
     * 
     */
    @JsonProperty("kind")
    @JsonPropertyDescription("Friendly kind (npc/scenery/floor/water/particles/ui/unknown).")
    private String kind;
    /**
     * FNV-1a hash of fragmentSource → pass as `addShaderFx({ matchHash })` to target THIS fragment.
     * (Required)
     * 
     */
    @JsonProperty("fragmentHash")
    @JsonPropertyDescription("FNV-1a hash of fragmentSource \u2192 pass as `addShaderFx({ matchHash })` to target THIS fragment.")
    private String fragmentHash;
    /**
     * FNV-1a hash of vertexSource (to target the vertex stage).
     * (Required)
     * 
     */
    @JsonProperty("vertexHash")
    @JsonPropertyDescription("FNV-1a hash of vertexSource (to target the vertex stage).")
    private String vertexHash;

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
    @JsonProperty("vertexSource")
    public String getVertexSource() {
        return vertexSource;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("fragmentSource")
    public String getFragmentSource() {
        return fragmentSource;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("uniforms")
    public List<Uniform> getUniforms() {
        return uniforms;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("attributes")
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Classified shader class for `addShaderFx({ matchType })` — "animated"/"floor"/"water"/…
     * (Required)
     * 
     */
    @JsonProperty("matchType")
    public String getMatchType() {
        return matchType;
    }

    /**
     * Friendly kind (npc/scenery/floor/water/particles/ui/unknown).
     * (Required)
     * 
     */
    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    /**
     * FNV-1a hash of fragmentSource → pass as `addShaderFx({ matchHash })` to target THIS fragment.
     * (Required)
     * 
     */
    @JsonProperty("fragmentHash")
    public String getFragmentHash() {
        return fragmentHash;
    }

    /**
     * FNV-1a hash of vertexSource (to target the vertex stage).
     * (Required)
     * 
     */
    @JsonProperty("vertexHash")
    public String getVertexHash() {
        return vertexHash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ShaderInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("vertexSource");
        sb.append('=');
        sb.append(((this.vertexSource == null)?"<null>":this.vertexSource));
        sb.append(',');
        sb.append("fragmentSource");
        sb.append('=');
        sb.append(((this.fragmentSource == null)?"<null>":this.fragmentSource));
        sb.append(',');
        sb.append("uniforms");
        sb.append('=');
        sb.append(((this.uniforms == null)?"<null>":this.uniforms));
        sb.append(',');
        sb.append("attributes");
        sb.append('=');
        sb.append(((this.attributes == null)?"<null>":this.attributes));
        sb.append(',');
        sb.append("matchType");
        sb.append('=');
        sb.append(((this.matchType == null)?"<null>":this.matchType));
        sb.append(',');
        sb.append("kind");
        sb.append('=');
        sb.append(((this.kind == null)?"<null>":this.kind));
        sb.append(',');
        sb.append("fragmentHash");
        sb.append('=');
        sb.append(((this.fragmentHash == null)?"<null>":this.fragmentHash));
        sb.append(',');
        sb.append("vertexHash");
        sb.append('=');
        sb.append(((this.vertexHash == null)?"<null>":this.vertexHash));
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
        result = ((result* 31)+((this.fragmentSource == null)? 0 :this.fragmentSource.hashCode()));
        result = ((result* 31)+((this.vertexSource == null)? 0 :this.vertexSource.hashCode()));
        result = ((result* 31)+((this.fragmentHash == null)? 0 :this.fragmentHash.hashCode()));
        result = ((result* 31)+((this.matchType == null)? 0 :this.matchType.hashCode()));
        result = ((result* 31)+((this.kind == null)? 0 :this.kind.hashCode()));
        result = ((result* 31)+((this.uniforms == null)? 0 :this.uniforms.hashCode()));
        result = ((result* 31)+((this.attributes == null)? 0 :this.attributes.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.vertexHash == null)? 0 :this.vertexHash.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ShaderInfo) == false) {
            return false;
        }
        ShaderInfo rhs = ((ShaderInfo) other);
        return ((((((((((this.fragmentSource == rhs.fragmentSource)||((this.fragmentSource!= null)&&this.fragmentSource.equals(rhs.fragmentSource)))&&((this.vertexSource == rhs.vertexSource)||((this.vertexSource!= null)&&this.vertexSource.equals(rhs.vertexSource))))&&((this.fragmentHash == rhs.fragmentHash)||((this.fragmentHash!= null)&&this.fragmentHash.equals(rhs.fragmentHash))))&&((this.matchType == rhs.matchType)||((this.matchType!= null)&&this.matchType.equals(rhs.matchType))))&&((this.kind == rhs.kind)||((this.kind!= null)&&this.kind.equals(rhs.kind))))&&((this.uniforms == rhs.uniforms)||((this.uniforms!= null)&&this.uniforms.equals(rhs.uniforms))))&&((this.attributes == rhs.attributes)||((this.attributes!= null)&&this.attributes.equals(rhs.attributes))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.vertexHash == rhs.vertexHash)||((this.vertexHash!= null)&&this.vertexHash.equals(rhs.vertexHash))));
    }

}
