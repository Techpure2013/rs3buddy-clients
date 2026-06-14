
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * One post-processing pass: a GLSL fragment shader applied fullscreen. Samples the previous result via `uScene` (sampler2D) at `gl_FragCoord.xy / uResolution`. Builtins available: `uScene`, `uResolution`, `uTime`, `uFrame`. `order` sets its slot in the chain (ascending).
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "fragmentSource",
    "uniforms",
    "order",
    "enabled"
})
@Generated("jsonschema2pojo")
public class PostFxPassInput {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    private String id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("fragmentSource")
    private String fragmentSource;
    @JsonProperty("uniforms")
    private UniformValueMap uniforms;
    @JsonProperty("order")
    private Double order;
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

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("fragmentSource")
    public String getFragmentSource() {
        return fragmentSource;
    }

    @JsonProperty("uniforms")
    public UniformValueMap getUniforms() {
        return uniforms;
    }

    @JsonProperty("order")
    public Double getOrder() {
        return order;
    }

    @JsonProperty("enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PostFxPassInput.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("fragmentSource");
        sb.append('=');
        sb.append(((this.fragmentSource == null)?"<null>":this.fragmentSource));
        sb.append(',');
        sb.append("uniforms");
        sb.append('=');
        sb.append(((this.uniforms == null)?"<null>":this.uniforms));
        sb.append(',');
        sb.append("order");
        sb.append('=');
        sb.append(((this.order == null)?"<null>":this.order));
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
        result = ((result* 31)+((this.fragmentSource == null)? 0 :this.fragmentSource.hashCode()));
        result = ((result* 31)+((this.uniforms == null)? 0 :this.uniforms.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.enabled == null)? 0 :this.enabled.hashCode()));
        result = ((result* 31)+((this.order == null)? 0 :this.order.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PostFxPassInput) == false) {
            return false;
        }
        PostFxPassInput rhs = ((PostFxPassInput) other);
        return ((((((this.fragmentSource == rhs.fragmentSource)||((this.fragmentSource!= null)&&this.fragmentSource.equals(rhs.fragmentSource)))&&((this.uniforms == rhs.uniforms)||((this.uniforms!= null)&&this.uniforms.equals(rhs.uniforms))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.enabled == rhs.enabled)||((this.enabled!= null)&&this.enabled.equals(rhs.enabled))))&&((this.order == rhs.order)||((this.order!= null)&&this.order.equals(rhs.order))));
    }

}
