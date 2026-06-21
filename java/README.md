# rs3buddy — Java API reference

Version 0.1.0 · last updated 2026-06-21

## Install

- **JDK 11+** (the client uses only `java.net.http.HttpClient` and `Path.of`, both Java 11). The project itself builds with a **JDK 17** toolchain.
- **Get the jar.** There is no published Maven Central artifact. Build it from the client source at `C:\Users\Techp\Desktop\rs3buddy-clients\java`:
  - **Source build (recommended):** in your own Gradle project add `includeBuild("path/to/rs3buddy-clients/java")` to `settings.gradle`, then depend on it: `implementation "com.rs3buddy:rs3buddy-client:0.1.0"`.
  - **Plain jar:** run `gradle jar` in `rs3buddy-clients/java` → produces `build/libs/rs3buddy-client-0.1.0.jar`. Put that on your classpath alongside the three Jackson jars.
- **Dependency:** Jackson `com.fasterxml.jackson.core:jackson-databind:2.17.1` (pulls in `jackson-core` + `jackson-annotations`). The source build resolves this transitively; for a plain-jar setup add all three Jackson jars (from your Gradle cache) to the classpath.

```java
import com.rs3buddy.RS3Buddy;

RS3Buddy buddy = RS3Buddy.connect();   // auto-discovers the running game via the launcher config
```

> The launcher must be running. `connect()` reads `%APPDATA%\rs3buddy\rs3buddy.json` (falling back to `%USERPROFILE%`) for the server port. Reads are opt-in per method; nothing is gathered passively.

Return shapes typed as `JsonNode` are returned as parsed Jackson trees (the client does not bind them to a model class). Numeric model fields are boxed (`Double`, `Boolean`) and may be `null`.

## Connection

### static RS3Buddy RS3Buddy.connect()
Connect to the running SDK server, auto-discovering its port from the launcher config.
- **Returns** `RS3Buddy` — a connected client.
- **Overloads**
  - `RS3Buddy.connect()` — no client name.
  - `RS3Buddy.connect(String clientName)` — tags every request with an `X-Client-Name` header.
- **Notes** Throws `RS3BuddyConnectionException` if `rs3buddy.json` can't be found, read, or parsed (launcher not running). To target a server directly, bypass `connect()` and use the constructor.

### RS3Buddy(String baseUrl)
Construct a client against an explicit server URL (skips auto-discovery).
- **Parameters**
  - `baseUrl` · `String` · e.g. `"http://127.0.0.1:4400"`.
- **Returns** `RS3Buddy`.
- **Overloads**
  - `new RS3Buddy(String baseUrl)`
  - `new RS3Buddy(String baseUrl, String clientName)` — also sets the `X-Client-Name` header.

### String baseUrl()
The discovered (or supplied) server address.
- **Returns** `String` — e.g. `"http://127.0.0.1:4400"`.

### class RS3BuddyConnectionException extends RuntimeException
Thrown when the server cannot be reached (down, wrong port, or game crashed).

### class RS3BuddyException extends RuntimeException
Thrown when the server returns a non-2xx HTTP status.
- **Notes** Carries `int status()` (HTTP status code) and `String body()` (raw response body, may be JSON, or null).

## Status & window

### JsonNode getStatus()
Server health + GL driver + memory + window, in one call.
- **Returns** `JsonNode` — `{ connected, driver:{device,vendor,glVersion}, memory:{usedBytes,totalBytes}, window:{x,y,width,height} }`.

### JsonNode getWindow()
The game window's position and size in screen pixels.
- **Returns** `JsonNode` — `{ x, y, width, height }`.

### JsonNode getHeap()
GL / native heap usage.
- **Returns** `JsonNode` — `{ totalBytes, freeBytes, usedBytes, externalBytes, externalSegments }`.

## Player

### Position getPlayer()
Acquire the player's world position via the cheap passive native tap. No GPU work, no frame capture, no stall — safe to poll every loop.
- **Returns** `Position` — `{ tileX, tileZ, worldX, worldZ }` (all `Double`); `null` until the tap locks on (call again). `worldX`/`worldZ` are RS3 engine units (`tileX * 512`). Getters: `getTileX()`, `getTileZ()`, `getWorldX()`, `getWorldZ()`, each `Double`.
- **Notes** A heavier forced-detection variant (GPU occlusion-mesh capture) exists server-side for a cold first-acquire, but is not exposed as a separate Java method — `getPlayer()` is the read you poll.

### Position updatePlayer()
Cheap refresh of the player position; identical passive tap as `getPlayer()`.
- **Returns** `Position` — or `null` if not currently tracked (then call `getPlayer()` again).

### PlayerNameResult getPlayerName()
Recover the local player's name + title(s) from the chat input prompt (opt-in).
- **Returns** `PlayerNameResult` — getters: `getOk()` (`Boolean`), `getFound()` (`Boolean`), `getDisplayName()`, `getName()`, `getPrefix()`, `getSuffix()`, `getTitle()` (each `String`). String fields are empty/`null` until `getFound()` is `true`.
- **Notes** Opt-in. The name never appears in the chat feed; it comes only from the input prompt, which must have been seen at least once.

## Chat

### ChatReadResult chat.read()
Read the in-game chatbox as structured lines, with per-run and per-glyph colour.
- **Returns** `ChatReadResult` — getters: `getOk()` (`Boolean`), `getFont()` (`String`, e.g. `"16pt"`; `null` if nothing recognised), `getLineCount()` (`Double`), `getStale()` (`Boolean`), `getAgeMs()` (`Double`), `getLines()` (`List<ChatLine>`), `getLocate()` (`ChatLocate`). Each `ChatLine` has `getY()` (`Double`), `getText()` (`String`), `getColor()` (`List<Double>`, the message colour `[r,g,b]`), `getRuns()` (`List<ChatRun>`, same-colour segments), `getGlyphs()` (`List<ChatGlyph>`, per-char x + colour). Lines are ordered top-to-bottom (oldest first).
- **Overloads**
  - `chat.read()` — default bottom-left chatbox region.
  - `chat.read(Integer x0, Integer y0, Integer x1, Integer y1)` — override the scan region in window pixels.
- **Notes** Opt-in. Heavy work (capture, glyph recognition, line grouping, colour sampling) runs server-side; the call is stall-free.

## Bars

### BarsReadResult bars.read()
Read the four status orbs (HP, adrenaline, prayer, summoning) in one call.
- **Returns** `BarsReadResult` — getters: `getOk()` (`Boolean`), `getStale()` (`Boolean`; `false` when a fresh capture happened on this call), `getAgeMs()` (`Double`), `getBars()` (`List<BarValue>`, always the four bars in fixed order). Each `BarValue` has `getName()` (`BarValue.BarName` enum: `HITPOINTS`/`ADRENALINE`/`PRAYER`/`SUMMONING`), `getFound()` (`Boolean`), `getValue()` (`Double`; a percentage for adrenaline; `null` when found-but-unreadable), `getMax()` (`Double`; `null` when only one number shown), `getText()` (`String`, e.g. `"10,018/10,200"`), `getAnchor()` (`Object`; icon rect or `null`), `getRegion()` (`Object`; scanned pixel box or `null`).
- **Notes** Opt-in; the orbs must be visible on screen.

## Abilities

### AbilitiesReadResult abilities.read()
Read the action bar(s): each slot's ability, rects, cooldown state, and usability. Slots are in reading order (rows top-to-bottom, then left-to-right).
- **Returns** `AbilitiesReadResult` — getters: `getOk()` (`Boolean`), `getStale()` (`Boolean`), `getAgeMs()` (`Double`), `getAbilities()` (`List<AbilitySlot>`). Each `AbilitySlot` has `getName()` (`String`, ability id without the `ability:` prefix, e.g. `"anticipation"`), `getRect()` (`AbilityRect`, on-screen slot), `getAtlas()` (`AbilityRect`, sprite location in the atlas), `getActivating()` (`Boolean`), `getOnCooldown()` (`Boolean`), `getCooldownText()` (`String`, e.g. `"5"` or `"1:23"`; `""` when none), `getCooldownSeconds()` (`Double`; `null` when not on CD/unreadable), `getUsable()` (`Boolean`), `getColor()` (`List<Double>`, per-vertex tint `[r,g,b]`; a grey triple ⇒ unusable).
- **Notes** Opt-in. `getActivating()` flashes across the whole bar on any cast (the global-cooldown sweep) — it is not a reliable per-slot "fired" signal; watch `getOnCooldown()` / `getCooldownSeconds()` instead. `getUsable()` is derived from the tint capture.

## Scene

### SceneSnapshot getScene()
Classified per-frame view of the world (player, NPCs, scenery, floor tiles, water, particles) plus the camera view-projection matrix.
- **Returns** `SceneSnapshot` — fields: `timestamp`, `player` (or `null`), `npcs`, `scenery`, `floor`, `water`, `particles`, `other` (each an Entity list), `viewProj` (16 numbers, column-major, or `null`). Each Entity carries `id`, `drawIndex`, `kind`, `world{x,y,z}`, `tile{x,z}`, `chunk{x,z}`, `floor`, `meshId`, `shaderId`, `vertexCount`, `tag` (short stable per-model fingerprint — match on this, not `vertexCount`), `screen{x,y,depth}` (normalized `[0,1]`, `0,0` = bottom-left; `null` when behind camera).

### JsonNode getScenePlayer()
Just the player object from the current scene snapshot.
- **Returns** `JsonNode` — the player object, or `null`.

### JsonNode getNpcs(Integer radius, Integer floor)
Nearby NPC entities.
- **Parameters**
  - `radius` · `Integer` · tile radius (default 15 when null).
  - `floor` · `Integer` · floor level (default 0 when null).
- **Returns** `JsonNode` — an Entity array.

### JsonNode getScenery(Integer radius, Integer floor)
Nearby scenery entities.
- **Parameters**
  - `radius` · `Integer` · tile radius (default 15 when null).
  - `floor` · `Integer` · floor level (default 0 when null).
- **Returns** `JsonNode` — an Entity array.

### JsonNode getWater(Integer radius, Integer floor)
Nearby water entities.
- **Parameters**
  - `radius` · `Integer` · tile radius (default 15 when null).
  - `floor` · `Integer` · floor level (default 0 when null).
- **Returns** `JsonNode` — an Entity array.

### JsonNode getFloorTiles(Integer radius, Integer floor)
Nearby floor tiles.
- **Parameters**
  - `radius` · `Integer` · tile radius (default 15 when null).
  - `floor` · `Integer` · floor level (default 0 when null).
- **Returns** `JsonNode` — an Entity array.

### JsonNode getEntityAt(int x, int z, Integer floor)
Entities standing on a specific tile.
- **Parameters**
  - `x` · `int` · tile x.
  - `z` · `int` · tile z.
  - `floor` · `Integer` · floor level (default 0 when null).
- **Returns** `JsonNode` — an Entity array for that tile (often empty `[]`).

## Drawing (world)

A shape is a plain object (build with a `Map`/`List` or any Jackson-serializable type). Minimum fields: `anchor`, `primitive`, `vertices`, `color`. Optional: `thickness` (px, line primitives), `pointSize` (px, points), `occludedByWorld` (`false` default = always on top), `group`, `id`. `anchor.mode` is one of `screen` / `world` / `tile` / `entity`. `primitive` is one of `triangles` / `triangle_strip` / `triangle_fan` / `lines` / `line_strip` / `line_loop` / `points`. `vertices` is a flat `x,y,z` list local to the anchor. `color` is `"#rrggbb"` / `"#rrggbbaa"` or a per-vertex array for a gradient. `drawShape`/`drawScene` also accept a text `billboard` item and an `image` item.

```java
import java.util.List;
import java.util.Map;

Map<String,Object> ring = Map.of(
    "anchor", Map.of("mode","tile", "tile", Map.of("x",3200,"z",3200,"floor",0)),
    "primitive", "line_loop",
    "vertices", List.of(-0.5,0,-0.5, 0.5,0,-0.5, 0.5,0,0.5, -0.5,0,0.5),
    "color", "#00ff00", "thickness", 2, "occludedByWorld", true);
buddy.drawShape(ring);
```

### JsonNode drawShape(Object shape)
Submit one shape to the overlay.
- **Parameters**
  - `shape` · `Object` · a Jackson-serializable shape object.
- **Returns** `JsonNode` — `{ id, size }` (`id` of the added shape, `size` = total shapes now).

### JsonNode drawScene(List<Object> shapes)
Replace the entire overlay scene with the given shapes.
- **Parameters**
  - `shapes` · `List<Object>` · the full shape list (replaces everything currently drawn).
- **Returns** `JsonNode` — `{ size }` (count after replace).

### JsonNode listShapes()
List everything currently drawn.
- **Returns** `JsonNode` — `{ shapes:[...], size }`.

### JsonNode removeShape(String id)
Remove one shape by id.
- **Parameters**
  - `id` · `String` · the shape id.
- **Returns** `JsonNode` — the removal result.

### JsonNode clearShapes(String group)
Clear drawn shapes.
- **Parameters**
  - `group` · `String` · clear just this group; pass `null` to clear everything.
- **Returns** `JsonNode` — `{ cleared, group, size }`.

## UI overlay

Author the HUD as HTML + CSS and POST it; the server compiles it to the same widget engine the SDK renders (clicks / drag / scaling work). Each `html`/`render` call replaces the current UI. Your app owns state: poll `ui.events()` for clicks and re-render on change. Supported tags include `panel`, `div`/`row`/`column`, text (`span`/`p`/`h1`–`h4`), `button`, `progress`/`gauge`, `img`, `hr`, `badge`; special attrs include `id`, `draggable`, `consume`, `anchor`, `variant`, `icon`, `action`. CSS supports tag/`.class`/`#id`/descendant selectors and inline `style`.

### JsonNode ui.html(String html, String css)
Render an HTML + CSS "page" to the overlay (replaces the current UI).
- **Parameters**
  - `html` · `String` · the markup.
  - `css` · `String` · the stylesheet; pass `null` (treated as `""`) for none.
- **Returns** `JsonNode` — `{ ok, size }` (draw-item count).

### JsonNode ui.render(Object tree)
Render a raw widget tree instead of HTML.
- **Parameters**
  - `tree` · `Object` · `{ type, props, children:[...] }`.
- **Returns** `JsonNode` — `{ ok, size }`.

### JsonNode ui.clear()
Clear the overlay UI.
- **Returns** `JsonNode` — `{ cleared:true }`.

### JsonNode ui.events()
Drain queued interaction events.
- **Returns** `JsonNode` — `{ events:[ { type, id, x, y } ] }`, empty when no interaction. `type` is `click` / `close` / `minimize`; `id` is the widget id (string or null).

### JsonNode ui.scaling(Double exponent, Double scale, Double baseHeight)
Tune how big the UI grows on hi-DPI / 4K. Null args are omitted from the request.
- **Parameters**
  - `exponent` · `Double` · `1` = constant physical size; `>1` = bigger on 4K (default 1.5).
  - `scale` · `Double` · explicit multiplier (overrides the exponent curve).
  - `baseHeight` · `Double` · reference window height the scaling normalises against.
- **Returns** `JsonNode` — `{ ok:true }`.

## Sound

Play developer-supplied audio host-side through the desktop app. `volume` is `0..1`; pass `null` for host default. Requires the desktop audio host (no-op headless).

### JsonNode sound.play(String file, Double volume)
Play an audio file on the host.
- **Parameters**
  - `file` · `String` · a path or a `file:` / `data:` / `http(s):` URL.
  - `volume` · `Double` · `0..1` gain; `null` = host default.
- **Returns** `JsonNode` — `{ ok:true }` (`{ ok:false, error }` when no audio host).

### JsonNode sound.playBytes(String base64, String mime, Double volume)
Play inline base64-encoded audio.
- **Parameters**
  - `base64` · `String` · the audio payload.
  - `mime` · `String` · its MIME type, e.g. `"audio/wav"`.
  - `volume` · `Double` · `0..1` gain; `null` = host default.
- **Returns** `JsonNode` — `{ ok:true }` (or `{ ok:false, error }`).

## Fonts

Render/DRAW fonts (used when drawing text billboards) — distinct from the trained-recognition registry.

### JsonNode listFonts()
List the registered render fonts.
- **Returns** `JsonNode` — an alias → path map, e.g. `{ "rs3": "C:/fonts/MyFont.ttf" }`.

### JsonNode registerFont(String name, String path)
Register a render font.
- **Parameters**
  - `name` · `String` · alias.
  - `path` · `String` · a `.ttf` / `.woff2` on the host.
- **Returns** `JsonNode` — the registration result.

### JsonNode unregisterFont(String name)
Unregister a render font.
- **Parameters**
  - `name` · `String` · the alias to remove.
- **Returns** `JsonNode` — the result.

## Sprites

Trained sprites — named sprites the recognition system matches.

### JsonNode listTrainedSprites()
List trained sprites.
- **Returns** `JsonNode` — an array of `{ name, hash, colorHash, textureId, x, y, width, height, createdAt }`.

### JsonNode saveTrainedSprite(Object req)
Save a trained-sprite record.
- **Parameters**
  - `req` · `Object` · a trained-sprite record (name + hash(es) + rect).
- **Returns** `JsonNode` — the save result.

### JsonNode deleteTrainedSprite(String name)
Delete a trained sprite by name.
- **Parameters**
  - `name` · `String` · the sprite name.
- **Returns** `JsonNode` — the delete result.

## Advanced

Low-level atlas / shader / capture / OCR surface. Most apps never need this — use the high-level readers (chat / bars / abilities) instead. Capture forces a frame readback — not for polling.

### Atlas / recognition

### JsonNode atlasSprites()
All atlas sprite records from the live frame.
- **Returns** `JsonNode` — `{ sprites:[ { hash, textureId, x, y, w, h, observationCount, lastSeenFrame } ] }`.

### JsonNode atlasSync()
Refresh the atlas record set from the live frame.
- **Returns** `JsonNode` — the sync result.

### JsonNode atlasLookup(long hash)
Find a sprite by hash.
- **Parameters**
  - `hash` · `long` · the sprite hash.
- **Returns** `JsonNode` — `{ hit, record? }`.

### JsonNode trainQuad(Object req)
Train a recognition entry from a drawn quad.
- **Parameters**
  - `req` · `Object` · the train request.
- **Returns** `JsonNode` — the train result.

### JsonNode recognizeQuads(List<Object> quads)
Recognise an array of quads.
- **Parameters**
  - `quads` · `List<Object>` · the quads to match (sent as `{ quads:[...] }`).
- **Returns** `JsonNode` — the matches.

### JsonNode importSpriteHash(Object req)
Import an externally-computed sprite-hash record.
- **Parameters**
  - `req` · `Object` · the sprite-hash record.
- **Returns** `JsonNode` — the import result.

### Shaders & FX

A **post-FX pass** is `{ id, fragmentSource, uniforms?, order?, enabled? }` (samples the previous result via `uScene`; builtins `uScene`/`uResolution`/`uTime`/`uFrame`). A **shader FX** is `{ id, matchType?, matchHash?, fragmentSource?, vertexSource?, enabled? }` (replaces a stock shader, matched by classified type or exact source hash).

### ShaderInfo[] getShaders()
List loaded GLSL shader programs.
- **Returns** `ShaderInfo[]` — each `{ id, vertexSource, fragmentSource, uniforms:[ { name, type, location } ] }`.

### JsonNode addPostFx(PostFxPassInput pass)
Register or replace a fullscreen post-FX pass.
- **Parameters**
  - `pass` · `PostFxPassInput` · the pass definition.
- **Returns** `JsonNode` — `{ id }`.

### PostFxPassInput[] listPostFx()
All post-FX passes, in render order (ascending `order`).
- **Returns** `PostFxPassInput[]`.

### JsonNode removePostFx(String id)
Remove a post-FX pass.
- **Parameters**
  - `id` · `String` · the pass id.
- **Returns** `JsonNode` — `{ removed }`.

### JsonNode setPostFxEnabled(String id, boolean enabled)
Enable/disable a post-FX pass without removing it.
- **Parameters**
  - `id` · `String` · the pass id.
  - `enabled` · `boolean` · on/off.
- **Returns** `JsonNode` — `{ ok }`.

### JsonNode addShaderFx(ShaderFxInput o)
Register or replace a custom game-shader FX (replaces an RS3 shader by type or hash).
- **Parameters**
  - `o` · `ShaderFxInput` · the FX definition.
- **Returns** `JsonNode` — `{ id }`.

### ShaderFxInput[] listShaderFx()
All active custom game-shader FX.
- **Returns** `ShaderFxInput[]`.

### JsonNode removeShaderFx(String id)
Remove a shader FX (that shader reverts to stock).
- **Parameters**
  - `id` · `String` · the FX id.
- **Returns** `JsonNode` — `{ removed }`.

### JsonNode setShaderFxEnabled(String id, boolean enabled)
Enable/disable a shader FX without removing it.
- **Parameters**
  - `id` · `String` · the FX id.
  - `enabled` · `boolean` · on/off.
- **Returns** `JsonNode` — `{ ok }`.

### Capture & textures

### FrameCaptureResult captureFrame()
Capture the current frame's draw list. Heavy — forces a frame readback; not for polling.
- **Returns** `FrameCaptureResult` — `{ draws:[ { index, shaderId, vertexCount, meshId, targetFbo, viewport:{x,y,width,height}, textures:[{slot,id,width,height}], ... } ] }`.
- **Overloads**
  - `captureFrame()`
  - `captureFrame(boolean includeMesh, boolean includeTexturePixels, Integer shaderId, Integer meshId, Integer targetFbo)` — include mesh data / texture pixels and filter by `shaderId` / `meshId` / `targetFbo` (null = no filter).

### TextureInfo[] getTextures()
List captured textures.
- **Returns** `TextureInfo[]` — each `{ id, width, height, format ("R8"|"0x8C4F"|...), source ("snapshot") }`.

### JsonNode readTexture(int id, int x, int y, int w, int h)
Read raw pixels from a texture region.
- **Parameters**
  - `id` · `int` · texture id.
  - `x`, `y`, `w`, `h` · `int` · the region.
- **Returns** `JsonNode` — the pixel data.
- **Overloads**
  - `readTexture(int id)` — reads the whole texture (region `0,0,0,0`).
  - `readTexture(int id, int x, int y, int w, int h)`

### JsonNode screenCapture()
Capture the full screen.
- **Returns** `JsonNode` — the capture result.

### JsonNode getCapturedTextures()
List the captured-texture cache.
- **Returns** `JsonNode`.

### JsonNode refreshCapturedTextures()
Refresh the captured-texture cache from the live frame.
- **Returns** `JsonNode`.

### JsonNode capturedTexturesStats()
Captured-texture cache statistics.
- **Returns** `JsonNode` — `{ entryCount, totalDiskBytes, oldestTouchAgeMs, capacityBytes, cacheDir, refreshInFlight }`.

### JsonNode clearCapturedTextures()
Clear the captured-texture cache.
- **Returns** `JsonNode`.

### JsonNode getCapturedTexture(int id)
Get one captured texture by id.
- **Parameters**
  - `id` · `int` · texture id.
- **Returns** `JsonNode`.

### JsonNode getGlyphsOnScreen(Map<String,Object> opts)
All glyph-like blobs detected on screen.
- **Parameters**
  - `opts` · `Map<String,Object>` · optional filters `minW`, `minH`, `maxW`, `maxH`, `alphaThreshold` (null = none).
- **Returns** `JsonNode` — `{ width, height, count, glyphs:[] }`.

### JsonNode glyphAtPoint(int x, int y, Map<String,Object> opts)
The glyph blob at a screen point.
- **Parameters**
  - `x`, `y` · `int` · the point.
  - `opts` · `Map<String,Object>` · optional filters (merged into the request; null = none).
- **Returns** `JsonNode` — the glyph at that point.

### OCR

### JsonNode matchText(Object req)
Recognise text matching a request spec, anywhere on screen.
- **Parameters**
  - `req` · `Object` · the request spec.
- **Returns** `JsonNode` — the matches.

### JsonNode matchRegion(Object req)
Recognise the text in a given region.
- **Parameters**
  - `req` · `Object` · region + options.
- **Returns** `JsonNode` — the recognised text.
