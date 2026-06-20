
package com.rs3buddy.models;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "position",
    "drawInfo",
    "captureOptions",
    "shaderInfo",
    "textureInfo",
    "sceneSnapshot",
    "entity",
    "playerInfo",
    "tileCoord",
    "worldPos",
    "chunkCoord",
    "entityKind",
    "chatReadResult",
    "barsReadResult",
    "abilitiesReadResult",
    "drawItem",
    "postFxPassInput",
    "shaderFxInput",
    "playerNameResult",
    "frameCaptureResult",
    "uiWidget"
})
@Generated("jsonschema2pojo")
public class RS3buddyApi {

    /**
     * A world position in tile coordinates.
     * (Required)
     * 
     */
    @JsonProperty("position")
    @JsonPropertyDescription("A world position in tile coordinates.")
    private Position position;
    /**
     * Simplified draw call info — one thing the GPU drew this frame.
     * (Required)
     * 
     */
    @JsonProperty("drawInfo")
    @JsonPropertyDescription("Simplified draw call info \u2014 one thing the GPU drew this frame.")
    private DrawInfo drawInfo;
    /**
     * Options for captureFrame().
     * (Required)
     * 
     */
    @JsonProperty("captureOptions")
    @JsonPropertyDescription("Options for captureFrame().")
    private CaptureOptions captureOptions;
    /**
     * Info about a loaded shader program.
     * (Required)
     * 
     */
    @JsonProperty("shaderInfo")
    @JsonPropertyDescription("Info about a loaded shader program.")
    private ShaderInfo shaderInfo;
    /**
     * Info about a loaded texture.
     * (Required)
     * 
     */
    @JsonProperty("textureInfo")
    @JsonPropertyDescription("Info about a loaded texture.")
    private TextureInfo textureInfo;
    /**
     * A full scene snapshot for one frame.
     * (Required)
     * 
     */
    @JsonProperty("sceneSnapshot")
    @JsonPropertyDescription("A full scene snapshot for one frame.")
    private SceneSnapshot sceneSnapshot;
    /**
     * One thing in the scene this frame.
     * (Required)
     * 
     */
    @JsonProperty("entity")
    @JsonPropertyDescription("One thing in the scene this frame.")
    private Entity entity;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("playerInfo")
    private PlayerInfo playerInfo;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tileCoord")
    private TileCoord tileCoord;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("worldPos")
    private WorldPos worldPos;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("chunkCoord")
    private ChunkCoord chunkCoord;
    /**
     * What kind of thing an Entity represents.
     * (Required)
     * 
     */
    @JsonProperty("entityKind")
    @JsonPropertyDescription("What kind of thing an Entity represents.")
    private Entity.EntityKind entityKind;
    /**
     * Result of reading the chatbox (GET /api/chat).
     * (Required)
     * 
     */
    @JsonProperty("chatReadResult")
    @JsonPropertyDescription("Result of reading the chatbox (GET /api/chat).")
    private ChatReadResult chatReadResult;
    /**
     * Result of reading the status bars (GET /api/bars). `bars` is always the four bars in a fixed order (hitpoints, adrenaline, prayer, summoning); check each bar's `found` / `value`.
     * (Required)
     * 
     */
    @JsonProperty("barsReadResult")
    @JsonPropertyDescription("Result of reading the status bars (GET /api/bars). `bars` is always the four bars in a fixed order (hitpoints, adrenaline, prayer, summoning); check each bar's `found` / `value`.")
    private BarsReadResult barsReadResult;
    /**
     * Result of reading the action bar(s) (GET /api/abilities). `abilities` is in reading order — rows top-to-bottom, then left-to-right.
     * (Required)
     * 
     */
    @JsonProperty("abilitiesReadResult")
    @JsonPropertyDescription("Result of reading the action bar(s) (GET /api/abilities). `abilities` is in reading order \u2014 rows top-to-bottom, then left-to-right.")
    private AbilitiesReadResult abilitiesReadResult;
    /**
     * A single submittable overlay item — geometry Shape, text Billboard, or image sprite. Clean wire form of the SDK's `DrawItem` (uses the wire  {@link  ImageItem }  above). This is what `drawShape` accepts and `drawScene` accepts an array of.
     * (Required)
     * 
     */
    @JsonProperty("drawItem")
    @JsonPropertyDescription("A single submittable overlay item \u2014 geometry Shape, text Billboard, or image sprite. Clean wire form of the SDK's `DrawItem` (uses the wire  {@link  ImageItem }  above). This is what `drawShape` accepts and `drawScene` accepts an array of.")
    private Object drawItem;
    /**
     * One post-processing pass: a GLSL fragment shader applied fullscreen. Samples the previous result via `uScene` (sampler2D) at `gl_FragCoord.xy / uResolution`. Builtins available: `uScene`, `uResolution`, `uTime`, `uFrame`. `order` sets its slot in the chain (ascending).
     * (Required)
     * 
     */
    @JsonProperty("postFxPassInput")
    @JsonPropertyDescription("One post-processing pass: a GLSL fragment shader applied fullscreen. Samples the previous result via `uScene` (sampler2D) at `gl_FragCoord.xy / uResolution`. Builtins available: `uScene`, `uResolution`, `uTime`, `uFrame`. `order` sets its slot in the chain (ascending).")
    private PostFxPassInput postFxPassInput;
    /**
     * One custom game-shader FX: replace/patch RS3's own shader, matched by classified type ("water"/"floor"/"foliage"/"animated"/"sky"/"particles"/"main"/"ui"/ "shadow"/"tinted"/"postprocess") or by exact source hash. fragmentSource and/or vertexSource replace that stage; an empty replacement leaves the stock shader.
     * (Required)
     * 
     */
    @JsonProperty("shaderFxInput")
    @JsonPropertyDescription("One custom game-shader FX: replace/patch RS3's own shader, matched by classified type (\"water\"/\"floor\"/\"foliage\"/\"animated\"/\"sky\"/\"particles\"/\"main\"/\"ui\"/ \"shadow\"/\"tinted\"/\"postprocess\") or by exact source hash. fragmentSource and/or vertexSource replace that stage; an empty replacement leaves the stock shader.")
    private ShaderFxInput shaderFxInput;
    /**
     * Result of GET /api/player/name — the local player's name + title(s), recovered from the chat input prompt (opt-in; the name never appears in the chat feed). When `found` is false the name fields are empty strings. `prefix`/`suffix` are the title text before/after the name; `title` is whichever is present.
     * (Required)
     * 
     */
    @JsonProperty("playerNameResult")
    @JsonPropertyDescription("Result of GET /api/player/name \u2014 the local player's name + title(s), recovered from the chat input prompt (opt-in; the name never appears in the chat feed). When `found` is false the name fields are empty strings. `prefix`/`suffix` are the title text before/after the name; `title` is whichever is present.")
    private PlayerNameResult playerNameResult;
    /**
     * Result of GET /api/frame — one captured frame's draw calls. The SDK's internal `Frame` (which carries a `dispose()` function) is deliberately not exposed; the wire form is just the serializable draw list + metadata.
     * (Required)
     * 
     */
    @JsonProperty("frameCaptureResult")
    @JsonPropertyDescription("Result of GET /api/frame \u2014 one captured frame's draw calls. The SDK's internal `Frame` (which carries a `dispose()` function) is deliberately not exposed; the wire form is just the serializable draw list + metadata.")
    private FrameCaptureResult frameCaptureResult;
    /**
     * A widget node on the wire: a built-in `type`, optional `props`, and child widgets. (Bare-text children are a TS authoring nicety; the wire model lists widget children only — use a `label` for text.)
     * (Required)
     * 
     */
    @JsonProperty("uiWidget")
    @JsonPropertyDescription("A widget node on the wire: a built-in `type`, optional `props`, and child widgets. (Bare-text children are a TS authoring nicety; the wire model lists widget children only \u2014 use a `label` for text.)")
    private UIWidget uiWidget;

    /**
     * A world position in tile coordinates.
     * (Required)
     * 
     */
    @JsonProperty("position")
    public Position getPosition() {
        return position;
    }

    /**
     * Simplified draw call info — one thing the GPU drew this frame.
     * (Required)
     * 
     */
    @JsonProperty("drawInfo")
    public DrawInfo getDrawInfo() {
        return drawInfo;
    }

    /**
     * Options for captureFrame().
     * (Required)
     * 
     */
    @JsonProperty("captureOptions")
    public CaptureOptions getCaptureOptions() {
        return captureOptions;
    }

    /**
     * Info about a loaded shader program.
     * (Required)
     * 
     */
    @JsonProperty("shaderInfo")
    public ShaderInfo getShaderInfo() {
        return shaderInfo;
    }

    /**
     * Info about a loaded texture.
     * (Required)
     * 
     */
    @JsonProperty("textureInfo")
    public TextureInfo getTextureInfo() {
        return textureInfo;
    }

    /**
     * A full scene snapshot for one frame.
     * (Required)
     * 
     */
    @JsonProperty("sceneSnapshot")
    public SceneSnapshot getSceneSnapshot() {
        return sceneSnapshot;
    }

    /**
     * One thing in the scene this frame.
     * (Required)
     * 
     */
    @JsonProperty("entity")
    public Entity getEntity() {
        return entity;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("playerInfo")
    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tileCoord")
    public TileCoord getTileCoord() {
        return tileCoord;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("worldPos")
    public WorldPos getWorldPos() {
        return worldPos;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("chunkCoord")
    public ChunkCoord getChunkCoord() {
        return chunkCoord;
    }

    /**
     * What kind of thing an Entity represents.
     * (Required)
     * 
     */
    @JsonProperty("entityKind")
    public Entity.EntityKind getEntityKind() {
        return entityKind;
    }

    /**
     * Result of reading the chatbox (GET /api/chat).
     * (Required)
     * 
     */
    @JsonProperty("chatReadResult")
    public ChatReadResult getChatReadResult() {
        return chatReadResult;
    }

    /**
     * Result of reading the status bars (GET /api/bars). `bars` is always the four bars in a fixed order (hitpoints, adrenaline, prayer, summoning); check each bar's `found` / `value`.
     * (Required)
     * 
     */
    @JsonProperty("barsReadResult")
    public BarsReadResult getBarsReadResult() {
        return barsReadResult;
    }

    /**
     * Result of reading the action bar(s) (GET /api/abilities). `abilities` is in reading order — rows top-to-bottom, then left-to-right.
     * (Required)
     * 
     */
    @JsonProperty("abilitiesReadResult")
    public AbilitiesReadResult getAbilitiesReadResult() {
        return abilitiesReadResult;
    }

    /**
     * A single submittable overlay item — geometry Shape, text Billboard, or image sprite. Clean wire form of the SDK's `DrawItem` (uses the wire  {@link  ImageItem }  above). This is what `drawShape` accepts and `drawScene` accepts an array of.
     * (Required)
     * 
     */
    @JsonProperty("drawItem")
    public Object getDrawItem() {
        return drawItem;
    }

    /**
     * One post-processing pass: a GLSL fragment shader applied fullscreen. Samples the previous result via `uScene` (sampler2D) at `gl_FragCoord.xy / uResolution`. Builtins available: `uScene`, `uResolution`, `uTime`, `uFrame`. `order` sets its slot in the chain (ascending).
     * (Required)
     * 
     */
    @JsonProperty("postFxPassInput")
    public PostFxPassInput getPostFxPassInput() {
        return postFxPassInput;
    }

    /**
     * One custom game-shader FX: replace/patch RS3's own shader, matched by classified type ("water"/"floor"/"foliage"/"animated"/"sky"/"particles"/"main"/"ui"/ "shadow"/"tinted"/"postprocess") or by exact source hash. fragmentSource and/or vertexSource replace that stage; an empty replacement leaves the stock shader.
     * (Required)
     * 
     */
    @JsonProperty("shaderFxInput")
    public ShaderFxInput getShaderFxInput() {
        return shaderFxInput;
    }

    /**
     * Result of GET /api/player/name — the local player's name + title(s), recovered from the chat input prompt (opt-in; the name never appears in the chat feed). When `found` is false the name fields are empty strings. `prefix`/`suffix` are the title text before/after the name; `title` is whichever is present.
     * (Required)
     * 
     */
    @JsonProperty("playerNameResult")
    public PlayerNameResult getPlayerNameResult() {
        return playerNameResult;
    }

    /**
     * Result of GET /api/frame — one captured frame's draw calls. The SDK's internal `Frame` (which carries a `dispose()` function) is deliberately not exposed; the wire form is just the serializable draw list + metadata.
     * (Required)
     * 
     */
    @JsonProperty("frameCaptureResult")
    public FrameCaptureResult getFrameCaptureResult() {
        return frameCaptureResult;
    }

    /**
     * A widget node on the wire: a built-in `type`, optional `props`, and child widgets. (Bare-text children are a TS authoring nicety; the wire model lists widget children only — use a `label` for text.)
     * (Required)
     * 
     */
    @JsonProperty("uiWidget")
    public UIWidget getUiWidget() {
        return uiWidget;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RS3buddyApi.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("position");
        sb.append('=');
        sb.append(((this.position == null)?"<null>":this.position));
        sb.append(',');
        sb.append("drawInfo");
        sb.append('=');
        sb.append(((this.drawInfo == null)?"<null>":this.drawInfo));
        sb.append(',');
        sb.append("captureOptions");
        sb.append('=');
        sb.append(((this.captureOptions == null)?"<null>":this.captureOptions));
        sb.append(',');
        sb.append("shaderInfo");
        sb.append('=');
        sb.append(((this.shaderInfo == null)?"<null>":this.shaderInfo));
        sb.append(',');
        sb.append("textureInfo");
        sb.append('=');
        sb.append(((this.textureInfo == null)?"<null>":this.textureInfo));
        sb.append(',');
        sb.append("sceneSnapshot");
        sb.append('=');
        sb.append(((this.sceneSnapshot == null)?"<null>":this.sceneSnapshot));
        sb.append(',');
        sb.append("entity");
        sb.append('=');
        sb.append(((this.entity == null)?"<null>":this.entity));
        sb.append(',');
        sb.append("playerInfo");
        sb.append('=');
        sb.append(((this.playerInfo == null)?"<null>":this.playerInfo));
        sb.append(',');
        sb.append("tileCoord");
        sb.append('=');
        sb.append(((this.tileCoord == null)?"<null>":this.tileCoord));
        sb.append(',');
        sb.append("worldPos");
        sb.append('=');
        sb.append(((this.worldPos == null)?"<null>":this.worldPos));
        sb.append(',');
        sb.append("chunkCoord");
        sb.append('=');
        sb.append(((this.chunkCoord == null)?"<null>":this.chunkCoord));
        sb.append(',');
        sb.append("entityKind");
        sb.append('=');
        sb.append(((this.entityKind == null)?"<null>":this.entityKind));
        sb.append(',');
        sb.append("chatReadResult");
        sb.append('=');
        sb.append(((this.chatReadResult == null)?"<null>":this.chatReadResult));
        sb.append(',');
        sb.append("barsReadResult");
        sb.append('=');
        sb.append(((this.barsReadResult == null)?"<null>":this.barsReadResult));
        sb.append(',');
        sb.append("abilitiesReadResult");
        sb.append('=');
        sb.append(((this.abilitiesReadResult == null)?"<null>":this.abilitiesReadResult));
        sb.append(',');
        sb.append("drawItem");
        sb.append('=');
        sb.append(((this.drawItem == null)?"<null>":this.drawItem));
        sb.append(',');
        sb.append("postFxPassInput");
        sb.append('=');
        sb.append(((this.postFxPassInput == null)?"<null>":this.postFxPassInput));
        sb.append(',');
        sb.append("shaderFxInput");
        sb.append('=');
        sb.append(((this.shaderFxInput == null)?"<null>":this.shaderFxInput));
        sb.append(',');
        sb.append("playerNameResult");
        sb.append('=');
        sb.append(((this.playerNameResult == null)?"<null>":this.playerNameResult));
        sb.append(',');
        sb.append("frameCaptureResult");
        sb.append('=');
        sb.append(((this.frameCaptureResult == null)?"<null>":this.frameCaptureResult));
        sb.append(',');
        sb.append("uiWidget");
        sb.append('=');
        sb.append(((this.uiWidget == null)?"<null>":this.uiWidget));
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
        result = ((result* 31)+((this.entityKind == null)? 0 :this.entityKind.hashCode()));
        result = ((result* 31)+((this.chatReadResult == null)? 0 :this.chatReadResult.hashCode()));
        result = ((result* 31)+((this.drawInfo == null)? 0 :this.drawInfo.hashCode()));
        result = ((result* 31)+((this.textureInfo == null)? 0 :this.textureInfo.hashCode()));
        result = ((result* 31)+((this.shaderFxInput == null)? 0 :this.shaderFxInput.hashCode()));
        result = ((result* 31)+((this.drawItem == null)? 0 :this.drawItem.hashCode()));
        result = ((result* 31)+((this.playerInfo == null)? 0 :this.playerInfo.hashCode()));
        result = ((result* 31)+((this.worldPos == null)? 0 :this.worldPos.hashCode()));
        result = ((result* 31)+((this.chunkCoord == null)? 0 :this.chunkCoord.hashCode()));
        result = ((result* 31)+((this.barsReadResult == null)? 0 :this.barsReadResult.hashCode()));
        result = ((result* 31)+((this.frameCaptureResult == null)? 0 :this.frameCaptureResult.hashCode()));
        result = ((result* 31)+((this.playerNameResult == null)? 0 :this.playerNameResult.hashCode()));
        result = ((result* 31)+((this.postFxPassInput == null)? 0 :this.postFxPassInput.hashCode()));
        result = ((result* 31)+((this.position == null)? 0 :this.position.hashCode()));
        result = ((result* 31)+((this.captureOptions == null)? 0 :this.captureOptions.hashCode()));
        result = ((result* 31)+((this.tileCoord == null)? 0 :this.tileCoord.hashCode()));
        result = ((result* 31)+((this.uiWidget == null)? 0 :this.uiWidget.hashCode()));
        result = ((result* 31)+((this.shaderInfo == null)? 0 :this.shaderInfo.hashCode()));
        result = ((result* 31)+((this.sceneSnapshot == null)? 0 :this.sceneSnapshot.hashCode()));
        result = ((result* 31)+((this.entity == null)? 0 :this.entity.hashCode()));
        result = ((result* 31)+((this.abilitiesReadResult == null)? 0 :this.abilitiesReadResult.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RS3buddyApi) == false) {
            return false;
        }
        RS3buddyApi rhs = ((RS3buddyApi) other);
        return ((((((((((((((((((((((this.entityKind == rhs.entityKind)||((this.entityKind!= null)&&this.entityKind.equals(rhs.entityKind)))&&((this.chatReadResult == rhs.chatReadResult)||((this.chatReadResult!= null)&&this.chatReadResult.equals(rhs.chatReadResult))))&&((this.drawInfo == rhs.drawInfo)||((this.drawInfo!= null)&&this.drawInfo.equals(rhs.drawInfo))))&&((this.textureInfo == rhs.textureInfo)||((this.textureInfo!= null)&&this.textureInfo.equals(rhs.textureInfo))))&&((this.shaderFxInput == rhs.shaderFxInput)||((this.shaderFxInput!= null)&&this.shaderFxInput.equals(rhs.shaderFxInput))))&&((this.drawItem == rhs.drawItem)||((this.drawItem!= null)&&this.drawItem.equals(rhs.drawItem))))&&((this.playerInfo == rhs.playerInfo)||((this.playerInfo!= null)&&this.playerInfo.equals(rhs.playerInfo))))&&((this.worldPos == rhs.worldPos)||((this.worldPos!= null)&&this.worldPos.equals(rhs.worldPos))))&&((this.chunkCoord == rhs.chunkCoord)||((this.chunkCoord!= null)&&this.chunkCoord.equals(rhs.chunkCoord))))&&((this.barsReadResult == rhs.barsReadResult)||((this.barsReadResult!= null)&&this.barsReadResult.equals(rhs.barsReadResult))))&&((this.frameCaptureResult == rhs.frameCaptureResult)||((this.frameCaptureResult!= null)&&this.frameCaptureResult.equals(rhs.frameCaptureResult))))&&((this.playerNameResult == rhs.playerNameResult)||((this.playerNameResult!= null)&&this.playerNameResult.equals(rhs.playerNameResult))))&&((this.postFxPassInput == rhs.postFxPassInput)||((this.postFxPassInput!= null)&&this.postFxPassInput.equals(rhs.postFxPassInput))))&&((this.position == rhs.position)||((this.position!= null)&&this.position.equals(rhs.position))))&&((this.captureOptions == rhs.captureOptions)||((this.captureOptions!= null)&&this.captureOptions.equals(rhs.captureOptions))))&&((this.tileCoord == rhs.tileCoord)||((this.tileCoord!= null)&&this.tileCoord.equals(rhs.tileCoord))))&&((this.uiWidget == rhs.uiWidget)||((this.uiWidget!= null)&&this.uiWidget.equals(rhs.uiWidget))))&&((this.shaderInfo == rhs.shaderInfo)||((this.shaderInfo!= null)&&this.shaderInfo.equals(rhs.shaderInfo))))&&((this.sceneSnapshot == rhs.sceneSnapshot)||((this.sceneSnapshot!= null)&&this.sceneSnapshot.equals(rhs.sceneSnapshot))))&&((this.entity == rhs.entity)||((this.entity!= null)&&this.entity.equals(rhs.entity))))&&((this.abilitiesReadResult == rhs.abilitiesReadResult)||((this.abilitiesReadResult!= null)&&this.abilitiesReadResult.equals(rhs.abilitiesReadResult))));
    }

}
