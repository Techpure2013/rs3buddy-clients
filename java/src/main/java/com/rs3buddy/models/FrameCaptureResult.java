
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Result of GET /api/frame — one captured frame's draw calls. The SDK's internal `Frame` (which carries a `dispose()` function) is deliberately not exposed; the wire form is just the serializable draw list + metadata.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "draws",
    "drawCount"
})
@Generated("jsonschema2pojo")
public class FrameCaptureResult {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("draws")
    private List<DrawInfo> draws = new ArrayList<DrawInfo>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("drawCount")
    private Double drawCount;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("draws")
    public List<DrawInfo> getDraws() {
        return draws;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("drawCount")
    public Double getDrawCount() {
        return drawCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FrameCaptureResult.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("draws");
        sb.append('=');
        sb.append(((this.draws == null)?"<null>":this.draws));
        sb.append(',');
        sb.append("drawCount");
        sb.append('=');
        sb.append(((this.drawCount == null)?"<null>":this.drawCount));
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
        result = ((result* 31)+((this.draws == null)? 0 :this.draws.hashCode()));
        result = ((result* 31)+((this.drawCount == null)? 0 :this.drawCount.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FrameCaptureResult) == false) {
            return false;
        }
        FrameCaptureResult rhs = ((FrameCaptureResult) other);
        return (((this.draws == rhs.draws)||((this.draws!= null)&&this.draws.equals(rhs.draws)))&&((this.drawCount == rhs.drawCount)||((this.drawCount!= null)&&this.drawCount.equals(rhs.drawCount))));
    }

}
