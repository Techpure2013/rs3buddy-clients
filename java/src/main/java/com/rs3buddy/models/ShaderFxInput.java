
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * One custom game-shader FX: replace/patch RS3's own shader, matched by classified type ("water"/"floor"/"foliage"/"animated"/"sky"/"particles"/"main"/"ui"/ "shadow"/"tinted"/"postprocess") or by exact source hash. fragmentSource and/or vertexSource replace that stage; an empty replacement leaves the stock shader.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "matchType",
    "matchHash",
    "fragmentSource",
    "vertexSource",
    "enabled"
})
@Generated("jsonschema2pojo")
public class ShaderFxInput {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    private String id;
    @JsonProperty("matchType")
    private String matchType;
    @JsonProperty("matchHash")
    private String matchHash;
    @JsonProperty("fragmentSource")
    private String fragmentSource;
    @JsonProperty("vertexSource")
    private String vertexSource;
    @JsonProperty("enabled")
    private Boolean enabled;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("matchType")
    public String getMatchType() {
        return matchType;
    }

    @JsonProperty("matchHash")
    public String getMatchHash() {
        return matchHash;
    }

    @JsonProperty("fragmentSource")
    public String getFragmentSource() {
        return fragmentSource;
    }

    @JsonProperty("vertexSource")
    public String getVertexSource() {
        return vertexSource;
    }

    @JsonProperty("enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ShaderFxInput.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("matchType");
        sb.append('=');
        sb.append(((this.matchType == null)?"<null>":this.matchType));
        sb.append(',');
        sb.append("matchHash");
        sb.append('=');
        sb.append(((this.matchHash == null)?"<null>":this.matchHash));
        sb.append(',');
        sb.append("fragmentSource");
        sb.append('=');
        sb.append(((this.fragmentSource == null)?"<null>":this.fragmentSource));
        sb.append(',');
        sb.append("vertexSource");
        sb.append('=');
        sb.append(((this.vertexSource == null)?"<null>":this.vertexSource));
        sb.append(',');
        sb.append("enabled");
        sb.append('=');
        sb.append(((this.enabled == null)?"<null>":this.enabled));
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
        result = ((result* 31)+((this.matchHash == null)? 0 :this.matchHash.hashCode()));
        result = ((result* 31)+((this.fragmentSource == null)? 0 :this.fragmentSource.hashCode()));
        result = ((result* 31)+((this.vertexSource == null)? 0 :this.vertexSource.hashCode()));
        result = ((result* 31)+((this.matchType == null)? 0 :this.matchType.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.enabled == null)? 0 :this.enabled.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ShaderFxInput) == false) {
            return false;
        }
        ShaderFxInput rhs = ((ShaderFxInput) other);
        return (((((((this.matchHash == rhs.matchHash)||((this.matchHash!= null)&&this.matchHash.equals(rhs.matchHash)))&&((this.fragmentSource == rhs.fragmentSource)||((this.fragmentSource!= null)&&this.fragmentSource.equals(rhs.fragmentSource))))&&((this.vertexSource == rhs.vertexSource)||((this.vertexSource!= null)&&this.vertexSource.equals(rhs.vertexSource))))&&((this.matchType == rhs.matchType)||((this.matchType!= null)&&this.matchType.equals(rhs.matchType))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.enabled == rhs.enabled)||((this.enabled!= null)&&this.enabled.equals(rhs.enabled))));
    }

}
