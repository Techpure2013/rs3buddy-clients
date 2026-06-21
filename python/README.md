# rs3buddy — Python API reference

Version 0.1.0 · last updated 2026-06-21

## Install

```bash
pip install rs3buddy
```

```python
from rs3buddy import RS3Buddy, RS3BuddyError, RS3BuddyConnectionError

buddy = RS3Buddy.connect()   # auto-discovers the running game from the launcher config
```

The desktop launcher must be running: it injects the native hook into RS3, brings the SDK server up, and writes `%APPDATA%\rs3buddy\rs3buddy.json`, which `connect()` reads. Methods are synchronous. A call with nothing to return (no player on screen, nothing tracked) returns `None` — that is not an error; just call again. An unreachable server raises `RS3BuddyConnectionError`; a non-2xx server response raises `RS3BuddyError`.

Return shapes are `TypedDict`s defined in `rs3buddy.models`. Their numeric fields are typed `float` in the schema but carry integer values at runtime.

## Connection

### RS3Buddy.connect(base_url=None, client_name=None, timeout=5.0) -> RS3Buddy
Connect to the running SDK server.
- **Parameters**
  - `base_url` · `str | None` · target a specific server; omit to auto-discover from the launcher config (`rs3buddy.json` under `%APPDATA%/rs3buddy`, then `$RS3BUDDY_CONFIG`, then the cwd).
  - `client_name` · `str | None` · tags your requests (useful for debugging).
  - `timeout` · `float` · per-request timeout in seconds.
- **Returns** `RS3Buddy`.

### base_url -> str
Property holding the discovered (or supplied) server address.
- **Returns** `str`.

### close() -> None
Close the underlying HTTP transport.

## Status & window

### get_status() -> dict
Server health + GL driver + memory + window, in one call. Cheap; safe to call any time.
- **Returns** `dict` — `{ "connected": bool, "driver": { "device", "vendor", "glVersion" }, "memory": { "usedBytes", "totalBytes" }, "window": { "x", "y", "width", "height" } }`.

### get_window() -> dict
The game window's position and size in screen pixels.
- **Returns** `dict` — `{ "x", "y", "width", "height" }`.

### get_heap() -> dict
GL / native heap usage.
- **Returns** `dict` — `{ "totalBytes", "freeBytes", "usedBytes", "externalBytes", "externalSegments" }`.

```python
st = buddy.get_status()
print(st["driver"]["glVersion"], st["window"]["width"], "x", st["window"]["height"])
win = buddy.get_window()   # { "x", "y", "width", "height" }
```

## Player

### get_player() -> Position | None
Acquire the player's world position via the cheap native passive tap. No GPU work, no frame capture, no stall.
- **Returns** `Position | None` — `{ "tileX", "tileZ", "worldX", "worldZ" }`, or `None` until the tap locks on. `worldX`/`worldZ` are RS3 engine units (`tileX * 512`).
- **Notes** Opt-in. Returns `None` for the first frame or two on a cold call — call again. Safe to poll every loop iteration.

### update_player() -> Position | None
Same cheap passive read as `get_player()` (both hit `GET /api/player`); use it as the per-loop refresh.
- **Returns** `Position | None` — `None` when not currently tracked, in which case call `get_player()` again.

### get_player_name() -> PlayerNameResult
Recovers the local player's name + title(s) from the chat input prompt.
- **Returns** `PlayerNameResult` — `{ "ok", "found", "displayName", "name", "prefix"?, "suffix"?, "title"? }`. `prefix`/`suffix` are the title text before/after the name.
- **Notes** Opt-in. The name never appears in the chat feed. String fields are empty until the prompt has been seen once (`found` flips to `True`).

```python
pos = buddy.get_player()
if pos:
    print("tile", pos["tileX"], pos["tileZ"])

me = buddy.get_player_name()
if me["found"]:
    print(me["displayName"])
```

## Chat

### chat.read(x0=None, y0=None, x1=None, y1=None) -> ChatReadResult
Read the in-game chatbox as structured lines, with per-run and per-glyph colour. The heavy work (frame + screen capture, glyph recognition, line grouping, colour sampling) runs server-side; the reader is stall-free.
- **Parameters** (optional region override, window pixels; omit to use the default bottom-left chatbox)
  - `x0` · `int | None` · left edge of the region to scan.
  - `y0` · `int | None` · top edge.
  - `x1` · `int | None` · right edge.
  - `y1` · `int | None` · bottom edge.
- **Returns** `ChatReadResult` — `{ "ok", "font", "lineCount", "stale", "ageMs", "lines": [ChatLine, ...], "locate" }`. Each `ChatLine` is `{ "y", "text", "color": [r,g,b], "runs": [{ "text", "color" }], "glyphs": [{ "char", "x", "color" }] }`. Lines are ordered top-to-bottom (oldest first); `line.color` is the message (last-glyph) colour; `runs` are same-colour segments.

```python
chat = buddy.chat.read()
for line in chat["lines"]:
    print(line["color"], line["text"])
```

## Bars

### bars.read() -> BarsReadResult
Reads the four status bars / orbs (HP, adrenaline, prayer, summoning) in one call. Recognition runs server-side.
- **Returns** `BarsReadResult` — `{ "ok", "stale", "ageMs", "bars": [BarValue, ...] }`. `bars` is always the four bars in a fixed order; check each entry's `found`. Each `BarValue` is `{ "name", "found", "value": int|None, "max": int|None, "text", "anchor": {x,y,w,h}|None, "region": {x0,y0,x1,y1}|None }`. `name` is `"hitpoints" | "adrenaline" | "prayer" | "summoning"`. `value` is a percentage for adrenaline (`max` is `None`); `max` is `None` when only one number is shown. `anchor` is where the icon was located; `region` is the pixel box the digits were read from.
- **Notes** Opt-in; the orbs must be visible on screen. `stale` is `False` whenever a fresh capture happened on this call.

```python
res = buddy.bars.read()
hp = next(b for b in res["bars"] if b["name"] == "hitpoints")
print(hp["value"], "/", hp["max"])
```

## Abilities

### abilities.read() -> AbilitiesReadResult
Reads the action bar(s): each slot's ability, on-screen + atlas rect, cooldown state, and whether it's usable. Recognition runs server-side. Slots are in reading order (rows top-to-bottom, then left-to-right).
- **Returns** `AbilitiesReadResult` — `{ "ok", "stale", "ageMs", "abilities": [AbilitySlot, ...] }`. Each `AbilitySlot` is `{ "name", "rect": {x,y,w,h}, "atlas": {x,y,w,h}, "activating", "onCooldown", "cooldownText", "cooldownSeconds": int|None, "usable", "color": [r,g,b] }`. `name` may be e.g. `"provoke"`, `"anticipation"`, `"buff:freedom"`. `usable` is derived from the per-vertex tint: `color` ≈ `[51,51,51]` (grey) means unusable.
- **Notes** Opt-in; the action bar must be on screen. `activating` flashes across the **whole bar** on any cast (the global-cooldown sweep), so it is not a reliable per-slot "fired" signal — watch `onCooldown` / `cooldownSeconds` instead.

```python
res = buddy.abilities.read()
for a in res["abilities"]:
    print(a["name"], "ready" if a["usable"] else "—",
          a["cooldownText"] if a["onCooldown"] else "")
```

## Scene

A classified per-frame view of the world. Each list entry is an `Entity` (`rs3buddy.models.Entity`): `{ "id", "drawIndex", "kind", "world": {x,y,z}, "tile": {x,z}, "chunk": {x,z}, "floor", "meshId", "shaderId", "vertexCount", "tag", "screen": {x,y,depth}|None }`. `kind` is one of `"player" | "npc" | "scenery" | "floor" | "water" | "particles" | "ui" | "unknown"`. `screen.x/y` are normalized `[0,1]` (0,0 = bottom-left, matching `gl_FragCoord`), `None` when behind the camera; match on `tag` (a stable per-model fingerprint), not `vertexCount`.

### get_scene() -> SceneSnapshot
The whole snapshot.
- **Returns** `SceneSnapshot` — `{ "timestamp", "player": PlayerInfo|None, "npcs": [Entity], "scenery": [...], "floor": [...], "water": [...], "particles": [...], "other": [...], "viewProj": [16 floats]|None }`. `viewProj` is column-major. `PlayerInfo` is `{ "world": {x,y,z}, "tile": {x,z}, "chunk": {x,z}, "floor" }`. To project a world point: `clip = viewProj · [x,y,z,1]; screenXY = clip.xy / clip.w · 0.5 + 0.5`.

### get_scene_player() -> dict | None
The player object only.
- **Returns** the `PlayerInfo` object **or** `None`.

### get_npcs(radius=None, floor=None) -> list[Entity]
NPCs near the player.
- **Parameters** `radius` · `int | None` · tile radius (server default 15). `floor` · `int | None` · floor level (default 0).
- **Returns** `list[Entity]`.

### get_scenery(radius=None, floor=None) -> list[Entity]
Scenery near the player.
- **Parameters** `radius` · `int | None` · tile radius (default 15). `floor` · `int | None` · floor (default 0).
- **Returns** `list[Entity]`.

### get_water(radius=None, floor=None) -> list[Entity]
Water draws near the player.
- **Parameters** `radius` · `int | None` · tile radius (default 15). `floor` · `int | None` · floor (default 0).
- **Returns** `list[Entity]`.

### get_floor_tiles(radius=None, floor=None) -> list[Entity]
Floor tiles near the player.
- **Parameters** `radius` · `int | None` · tile radius (default 15). `floor` · `int | None` · floor (default 0).
- **Returns** `list[Entity]`.

### get_entity_at(x, z, floor=None) -> list[Entity]
Entities standing on a specific tile.
- **Parameters** `x` · `int` · tile X. `z` · `int` · tile Z. `floor` · `int | None` · floor (default 0).
- **Returns** `list[Entity]` on that tile (often `[]`).

```python
scene = buddy.get_scene()
for npc in scene["npcs"]:
    print(npc["tag"], npc["tile"])

nearby = buddy.get_npcs(15, 0)
here = buddy.get_entity_at(3200, 3200, 0)
```

## Drawing (world)

Draw shapes and text anchored to a 3D tile, a world point, the screen, or an entity. A shape is a plain `dict` (`rs3buddy.models.Shape`): minimally `anchor`, `primitive`, `vertices`, `color`. Key fields:

- `anchor` — one of: `{"mode":"screen","screen":{x,y}}` · `{"mode":"world","world":{x,y,z}}` · `{"mode":"tile","tile":{x,z,floor}}` (optional `liftY`) · `{"mode":"entity","entityId":...,"offset":{x,y,z}}`.
- `primitive` — `"triangles" | "triangle_strip" | "triangle_fan" | "lines" | "line_strip" | "line_loop" | "points"`.
- `vertices` — flat `x,y,z` triplets (length a multiple of 3), local to the anchor.
- `color` — `"#rrggbb"` / `"#rrggbbaa"`, or a per-vertex array `[[r,g,b,a], ...]` for a gradient.
- `thickness` (line px, default 1) · `pointSize` (point px, default 4) · `occludedByWorld` (`True` = world geometry can hide it; default `False` = always on top) · `group` (clear tag) · `id` (auto-minted if omitted).

`draw_shape` / `draw_scene` also accept a text billboard (`{"kind":"billboard","anchor","text","color","font"?,"fontSize"?}`) and an image item (`{"kind":"image","anchor","width","height", ...}`), same anchors as a shape.

### draw_shape(shape) -> dict
Submit one shape.
- **Parameters** `shape` · `DrawItem` (`Shape | Billboard | ImageItem`) · the shape to add.
- **Returns** `dict` — `{ "id", "size" }` (`id` of the added shape, `size` = total shapes now).

### draw_scene(shapes) -> dict
Replace the entire overlay scene with a list of shapes.
- **Parameters** `shapes` · `list[DrawItem]` · the full set; **replaces** everything currently drawn.
- **Returns** `dict` — `{ "size": <count> }`.

### list_shapes() -> dict
List what is currently drawn.
- **Returns** `dict` — `{ "shapes": [ ... ], "size": <count> }`.

### remove_shape(id) -> dict
Remove one shape by id.
- **Parameters** `id` · `str` · the shape id.
- **Returns** `dict`.

### clear_shapes(group=None) -> dict
Clear drawn shapes.
- **Parameters** `group` · `str | None` · clear only this group; omit to clear everything.
- **Returns** `dict` — `{ "cleared": True, "group": str|None, "size": <count> }`.

```python
buddy.draw_shape({
    "anchor": {"mode": "tile", "tile": {"x": 3200, "z": 3200, "floor": 0}},
    "primitive": "line_loop",
    "vertices": [-0.5, 0, -0.5,  0.5, 0, -0.5,  0.5, 0, 0.5,  -0.5, 0, 0.5],
    "color": "#00ff00", "thickness": 2, "occludedByWorld": True,
})
buddy.clear_shapes()
```

## UI overlay

Author the HUD as HTML + CSS and hand it to `ui.html(...)`. The server compiles it into the same widget engine the SDK renders and auto-scales it. Your app owns the state: poll `ui.events()` for clicks, update state, and call `ui.html(...)` again — each call replaces the current UI. (See the developer guide for the supported tag/CSS subset.)

### ui.html(html, css="") -> dict
Render an HTML + CSS page to the overlay (replaces the current UI). This is the primary authoring path.
- **Parameters** `html` · `str` · the markup. `css` · `str` · stylesheet (selectors: tag, `.class`, `#id`, descendant, inline `style`).
- **Returns** `dict` — `{ "ok": True, "size": <draw-item count> }`.

### ui.render(tree) -> dict
Render a raw widget tree instead of HTML; compiles to the same engine as `html()`.
- **Parameters** `tree` · `UIWidget` · `{ "type", "props"?, "children"?: [...] }`.
- **Returns** `dict` — `{ "ok": True, "size": <count> }`.

### ui.clear() -> dict
Clear the overlay UI.
- **Returns** `dict` — `{ "cleared": True }`.

### ui.events() -> dict
Drain queued interaction events (clicks / close / minimize). Poll this in your loop.
- **Returns** `dict` — `{ "events": [ { "type", "id", "x", "y" } ] }` (empty list when no interaction). `type` is `"click" | "close" | "minimize"`; `id` is the widget's id (`str | None`).

### ui.scaling(exponent=None, scale=None, base_height=None) -> dict
Tune how big the UI grows on hi-DPI / 4K. Only the given keys are sent.
- **Parameters**
  - `exponent` · `float | None` · `1` = ≈constant physical size; `>1` = bigger on 4K (server default 1.5).
  - `scale` · `float | None` · explicit multiplier (overrides the exponent curve).
  - `base_height` · `int | None` · reference window height the scaling is normalised against (maps to the server's `baseHeight`).
- **Returns** `dict` — `{ "ok": True }`.

```python
resp = buddy.ui.events()
for e in (resp.get("events") if resp else None) or []:
    if e.get("id") == "close":
        buddy.ui.clear()
buddy.ui.html("<panel anchor='top-right'><span>HP</span></panel>",
              "panel{background:#241d12;padding:12px}")
```

## Sound

### sound.play(file=None, bytes=None, mime=None, volume=None) -> dict
Play developer-supplied audio host-side through the desktop app. Provide **either** `file` **or** `bytes`. Only the given keys are sent.
- **Parameters**
  - `file` · `str | None` · a host path or `file:` / `data:` / `http(s):` URL.
  - `bytes` · `str | None` · base64 audio, played inline (pair with `mime`).
  - `mime` · `str | None` · MIME type for inline `bytes` (server default `audio/wav`).
  - `volume` · `float | None` · `0..1` gain.
- **Returns** `dict` — `{ "ok": True }`, or `{ "ok": False, "error": ... }` when no audio host is available.
- **Notes** Requires the desktop audio host (no-op headless).

```python
buddy.sound.play(file="C:/sfx/ding.wav", volume=0.6)
# inline bytes: buddy.sound.play(bytes="<base64>", mime="audio/mp3")
```

## Fonts

Register/list **render (DRAW) fonts** — the fonts used when drawing text billboards. Distinct from the trained-recognition registry the chat/bars/abilities readers use.

### list_fonts() -> dict
List the registered render fonts.
- **Returns** `dict` — an alias → path map, e.g. `{ "rs3": "C:/fonts/MyFont.ttf" }`.

### register_font(name, path) -> dict
Register a render font.
- **Parameters** `name` · `str` · alias. `path` · `str` · a `.ttf` / `.woff2` on the host.
- **Returns** `dict`.

### unregister_font(name) -> dict
Remove a registered render font.
- **Parameters** `name` · `str` · the alias.
- **Returns** `dict`.

```python
buddy.register_font("myfont", "C:/fonts/MyFont.ttf")
fonts = buddy.list_fonts()
```

## Sprites

Manage **trained sprites** — named sprites the recognition system matches.

### list_trained_sprites() -> list
List trained sprites.
- **Returns** `list` — `[ { "name", "hash", "colorHash", "textureId", "x", "y", "width", "height", "createdAt" } ]`.

### save_trained_sprite(req) -> dict
Save (train) a sprite record.
- **Parameters** `req` · `dict` · a trained-sprite record (name + hash(es) + rect).
- **Returns** `dict`.

### delete_trained_sprite(name) -> dict
Delete a trained sprite by name.
- **Parameters** `name` · `str` · the sprite name.
- **Returns** `dict`.

```python
sprites = buddy.list_trained_sprites()   # [{ "name", "hash", "colorHash", ... }]
```

## Advanced

Low-level surfaces. Most apps never need these — prefer the high-level readers. Heavy GPU operations (capture / textures / glyphs) force a frame readback; do not poll them.

### Atlas / recognition

#### atlas_sprites() -> dict
All atlas sprite records from the live frame.
- **Returns** `dict` — `{ "sprites": [ { "hash", "textureId", "x", "y", "w", "h", "observationCount", "lastSeenFrame" } ] }`.

#### atlas_sync() -> dict
Refresh the atlas record set from the live frame.
- **Returns** `dict`.

#### atlas_lookup(hash) -> dict
Find a sprite by hash.
- **Parameters** `hash` · `int` · the sprite hash.
- **Returns** `dict` — `{ "hit": bool, "record"?: {...} }`.

#### train_quad(req) -> dict
Train a recognition entry from a drawn quad.
- **Parameters** `req` · `dict` · the train request.
- **Returns** `dict`.

#### recognize_quads(quads) -> dict
Recognise an array of quads.
- **Parameters** `quads` · `list` · quads to match (sent as `{"quads": quads}`).
- **Returns** `dict` — matches.

#### import_sprite_hash(req) -> dict
Import an externally-computed sprite-hash record.
- **Parameters** `req` · `dict` · the record.
- **Returns** `dict`.

### Shaders & FX

A **post-FX pass** is `{ "id", "fragmentSource", "uniforms"?, "order"?, "enabled"? }` (samples the previous result via `uScene`; builtins `uScene` / `uResolution` / `uTime` / `uFrame`). A **shader FX** is `{ "id", "matchType"?, "matchHash"?, "fragmentSource"?, "vertexSource"?, "enabled"? }` (replaces a stock shader, matched by classified type or exact source hash).

#### get_shaders() -> list[ShaderInfo]
Inspect loaded GLSL shaders.
- **Returns** `list[ShaderInfo]` — each `{ "id", "vertexSource", "fragmentSource", "uniforms": [{name,type,location}], "attributes", "matchType", "kind", "fragmentHash", "vertexHash" }`.

#### add_post_fx(pass_) -> dict
Register or replace a fullscreen post-FX pass.
- **Parameters** `pass_` · `PostFxPassInput` · the pass.
- **Returns** `dict` — `{ "id": ... }`.

#### list_post_fx() -> list[PostFxPassInput]
All post-FX passes, in render order (ascending `order`).
- **Returns** `list[PostFxPassInput]`.

#### remove_post_fx(id) -> dict
Remove a post-FX pass.
- **Parameters** `id` · `str`.
- **Returns** `dict` — `{ "removed": bool }`.

#### set_post_fx_enabled(id, enabled) -> dict
Enable/disable a post-FX pass without removing it.
- **Parameters** `id` · `str`. `enabled` · `bool`.
- **Returns** `dict` — `{ "ok": bool }`.

#### add_shader_fx(o) -> dict
Register or replace a custom game-shader FX.
- **Parameters** `o` · `ShaderFxInput` · the FX.
- **Returns** `dict` — `{ "id": ... }`.

#### list_shader_fx() -> list[ShaderFxInput]
All active custom game-shader FX.
- **Returns** `list[ShaderFxInput]`.

#### remove_shader_fx(id) -> dict
Remove a shader FX (that shader reverts to stock).
- **Parameters** `id` · `str`.
- **Returns** `dict` — `{ "removed": bool }`.

#### set_shader_fx_enabled(id, enabled) -> dict
Enable/disable a shader FX without removing it.
- **Parameters** `id` · `str`. `enabled` · `bool`.
- **Returns** `dict` — `{ "ok": bool }`.

### Capture & textures

#### capture_frame(include_mesh=False, include_texture_pixels=False, shader_id=None, mesh_id=None, target_fbo=None) -> FrameCaptureResult
Capture the current frame's draw list.
- **Parameters**
  - `include_mesh` · `bool` · include mesh data.
  - `include_texture_pixels` · `bool` · include texture pixel data.
  - `shader_id` · `int | None` · filter to one shader.
  - `mesh_id` · `int | None` · filter to one mesh.
  - `target_fbo` · `int | None` · filter to one target FBO.
- **Returns** `FrameCaptureResult` — `{ "draws": [DrawInfo], "drawCount" }`; each `DrawInfo` includes `index, shaderId, vertexCount, meshId, targetFbo, viewport, textures, ...`.
- **Notes** Heavy — forces a frame readback. Not for polling.

#### get_textures() -> list[TextureInfo]
List captured textures.
- **Returns** `list[TextureInfo]` — each `{ "id", "width", "height", "format", "source"? }` (`source` is `"snapshot"` | `"frame-harvest"`).

#### read_texture(id, x=0, y=0, w=0, h=0) -> dict
Read raw pixels from a texture (optionally a sub-rect).
- **Parameters** `id` · `int`. `x`, `y`, `w`, `h` · `int` · sub-rect (0 = full).
- **Returns** `dict`.
- **Notes** Heavy GPU read. Not for polling.

#### screen_capture() -> dict
Capture the current screen.
- **Returns** `dict`.
- **Notes** Heavy. Not for polling.

#### get_captured_textures() -> dict
List the on-disk captured-texture cache.
- **Returns** `dict`.

#### refresh_captured_textures() -> dict
Refresh the captured-texture cache from the live frame.
- **Returns** `dict`.

#### captured_textures_stats() -> dict
Cache statistics.
- **Returns** `dict` — `{ "entryCount", "totalDiskBytes", "oldestTouchAgeMs", "capacityBytes", "cacheDir", "refreshInFlight" }`.

#### clear_captured_textures() -> dict
Clear the captured-texture cache.
- **Returns** `dict`.

#### get_captured_texture(id) -> dict
Fetch one cached captured texture.
- **Parameters** `id` · `int`.
- **Returns** `dict`.

#### get_glyphs_on_screen(**opts) -> dict
Detect glyph-like regions on screen.
- **Parameters** `**opts` · `int` · optional filters `minW`, `minH`, `maxW`, `maxH`, `alphaThreshold`.
- **Returns** `dict` — `{ "width", "height", "count", "glyphs": [] }`.
- **Notes** Heavy GPU read. Not for polling.

#### glyph_at_point(x, y, **opts) -> dict
Detect the glyph at a screen point.
- **Parameters** `x` · `int`. `y` · `int`. `**opts` · `int` · optional detection options.
- **Returns** `dict`.

### OCR

General text recognition for arbitrary screen regions, separate from the structured chat/bars/abilities readers.

#### match_text(req) -> dict
Recognise text matching a request spec.
- **Parameters** `req` · `dict` · the request spec.
- **Returns** `dict`.

#### match_region(req) -> dict
Recognise the text in a given region.
- **Parameters** `req` · `dict` · region + options.
- **Returns** `dict`.

```python
frame = buddy.capture_frame()          # { "draws": [...], "drawCount" } — heavy, not for polling
texs = buddy.get_textures()            # [{ "id", "width", "height", "format", "source" }]
glyphs = buddy.get_glyphs_on_screen(minW=4, minH=6)
```
