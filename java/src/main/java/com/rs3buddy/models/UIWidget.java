
package com.rs3buddy.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * A widget node on the wire: a built-in `type`, optional `props`, and child widgets. (Bare-text children are a TS authoring nicety; the wire model lists widget children only — use a `label` for text.)
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "props",
    "children"
})
@Generated("jsonschema2pojo")
public class UIWidget {

    /**
     * Built-in widget kinds the engine lays out + flattens directly.
     * (Required)
     * 
     */
    @JsonProperty("type")
    @JsonPropertyDescription("Built-in widget kinds the engine lays out + flattens directly.")
    private UIWidget.WidgetType type;
    /**
     * Style + content props for a widget (JSON-serializable subset of `Props`).
     * 
     */
    @JsonProperty("props")
    @JsonPropertyDescription("Style + content props for a widget (JSON-serializable subset of `Props`).")
    private UIProps props;
    @JsonProperty("children")
    private List<UIWidget> children = new ArrayList<UIWidget>();

    /**
     * Built-in widget kinds the engine lays out + flattens directly.
     * (Required)
     * 
     */
    @JsonProperty("type")
    public UIWidget.WidgetType getType() {
        return type;
    }

    /**
     * Style + content props for a widget (JSON-serializable subset of `Props`).
     * 
     */
    @JsonProperty("props")
    public UIProps getProps() {
        return props;
    }

    @JsonProperty("children")
    public List<UIWidget> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(UIWidget.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("props");
        sb.append('=');
        sb.append(((this.props == null)?"<null>":this.props));
        sb.append(',');
        sb.append("children");
        sb.append('=');
        sb.append(((this.children == null)?"<null>":this.children));
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
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.children == null)? 0 :this.children.hashCode()));
        result = ((result* 31)+((this.props == null)? 0 :this.props.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UIWidget) == false) {
            return false;
        }
        UIWidget rhs = ((UIWidget) other);
        return ((((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type)))&&((this.children == rhs.children)||((this.children!= null)&&this.children.equals(rhs.children))))&&((this.props == rhs.props)||((this.props!= null)&&this.props.equals(rhs.props))));
    }


    /**
     * Built-in widget kinds the engine lays out + flattens directly.
     * 
     */
    @Generated("jsonschema2pojo")
    public enum WidgetType {

        PANEL("panel"),
        ROW("row"),
        COLUMN("column"),
        STACK("stack"),
        GRID("grid"),
        SPACER("spacer"),
        LABEL("label"),
        GAUGE("gauge"),
        IMAGE("image"),
        DIVIDER("divider"),
        BADGE("badge"),
        WORLD_LABEL("worldLabel"),
        WORLD_MARKER("worldMarker"),
        TILE("tile");
        private final String value;
        private final static Map<String, UIWidget.WidgetType> CONSTANTS = new HashMap<String, UIWidget.WidgetType>();

        static {
            for (UIWidget.WidgetType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WidgetType(String value) {
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
        public static UIWidget.WidgetType fromValue(String value) {
            UIWidget.WidgetType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
