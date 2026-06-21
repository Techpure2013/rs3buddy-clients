# rs3buddy — Lua API reference

Version 0.1.1 · last updated 2026-06-21

## Install / require

The package is the `rs3buddy/` directory (Luau dir-module). Require it and connect — no URL or port:

```lua
local rs3buddy = require("rs3buddy")        -- or require("../rs3buddy") from examples/

local buddy = rs3buddy.connect()            -- auto-discovers the running game
print("connected: " .. buddy:base_url())
```

`connect()` reads the launcher's config (`%APPDATA%\rs3buddy\rs3buddy.json`) to find the running SDK server. The desktop launcher must be running (it injects the native hook into RS3, which brings the server up).

Conventions:
- Top-level methods and reader namespaces both use the colon call form: `buddy:get_player()`, `buddy.chat:read()`.
- Reader-namespace methods take a table of options; positional methods take plain args.
- "No result" (nothing on screen, nothing tracked yet) returns `nil` — it is not an error. Just call again.
- A connection failure (launcher down) raises a tagged error table — catch with `pcall`.
- Color triples are `{ r, g, b }` tables (1-indexed).

```lua
-- Sandboxed hosts with no io/os (e.g. Roblox) can't read the config — pass both:
local buddy = rs3buddy.connect({ base_url = "http://127.0.0.1:1234", request = my_http_fn })
```

## Connection

### rs3buddy.connect(opts?)
Opens a client, auto-discovering the running game.
- **Parameters**
  - `opts` · table (optional) · `{ client_name?, base_url?, request? }`. `client_name` tags requests for debugging; `base_url` targets a specific server; `request` is a custom HTTP function for sandboxed hosts.
- **Returns** an `RS3Buddy` client.

### buddy:base_url()
The discovered server address.
- **Returns** `string`.

## Status & window

### buddy:get_status()
Server health, GL driver, memory, and window in one call. Cheap; safe to call any time.
- **Returns** a table `{ connected, driver = { device, vendor, glVersion }, memory = { usedBytes, totalBytes }, window = { x, y, width, height } }`.

### buddy:get_window()
The game window's position and size in screen pixels.
- **Returns** a table `{ x, y, width, height }`.

### buddy:get_heap()
GL / native heap usage.
- **Returns** a table `{ totalBytes, freeBytes, usedBytes, externalBytes, externalSegments }`.

## Player

### buddy:get_player()
Cheap passive read of the player's world position. No GPU work, no frame capture, no stall — safe to poll every loop iteration.
- **Returns** a table `{ tileX, tileZ, worldX, worldZ }` or `nil`. `worldX`/`worldZ` are RS3 engine units (`tileX * 512`).
- **Notes** Returns `nil` for the first frame or two until the native tap locks on; just call again.

### buddy:update_player()
Alias of `get_player()` — same cheap passive tap, same return.
- **Returns** a table `{ tileX, tileZ, worldX, worldZ }` or `nil`.

### buddy:get_player_name()
Recovers the local player's name and title(s) from the chat input prompt.
- **Returns** a table `{ ok, found, displayName, name, prefix, suffix, title }`. `prefix`/`suffix` are the title text before/after the name.
- **Notes** Opt-in. String fields are empty / `nil` until the prompt has been seen at least once (`found` flips to `true`). The name never appears in the chat feed.

## Chat

### buddy.chat:read(opts?)
Reads the in-game chatbox as structured lines, with per-run and per-glyph colour. The frame/screen capture, glyph recognition, line grouping, and colour sampling run server-side; the reader is stall-free.
- **Parameters**
  - `opts` · table (optional) · `{ x0?, y0?, x1?, y1? }` — region override in window pixels. Omit for the default bottom-left chatbox.
- **Returns** a table `{ ok, font, lineCount, stale, ageMs, lines, locate }`. Each `line` is `{ y, text, color = {r,g,b}, runs = { { text, color } }, glyphs = { { char, x, color } } }`. Lines are ordered top-to-bottom (oldest first); `line.color` is the message colour. Use `runs` to reproduce a multi-colour line and `glyphs` for per-character x + colour.

## Bars

### buddy.bars:read()
Reads the four status orbs (HP, adrenaline, prayer, summoning) in one call. Recognition runs server-side.
- **Returns** a table `{ ok, stale, ageMs, bars }`. `bars` is always the four entries in fixed order; each is `{ name, found, value, max, text, anchor, region }`. `name` is `"hitpoints" | "adrenaline" | "prayer" | "summoning"`; `value`/`max` are `nil` when unreadable / not shown (adrenaline `value` is a percentage with `max = nil`); `anchor` is the icon box `{ x, y, w, h }`; `region` is the digit box `{ x0, y0, x1, y1 }`. Check each entry's `found`.
- **Notes** Opt-in; the orbs must be visible on screen. `stale = false` whenever a fresh capture happened on this call.

## Abilities

### buddy.abilities:read()
Reads the action bar(s): each slot's ability, on-screen and atlas rect, cooldown state, and usability. Recognition runs server-side. Slots are in reading order (rows top-to-bottom, then left-to-right).
- **Returns** a table `{ ok, stale, ageMs, abilities }`. Each slot is `{ name, rect, atlas, activating, onCooldown, cooldownText, cooldownSeconds, usable, color }`. `rect`/`atlas` are `{ x, y, w, h }`; `cooldownText` is e.g. `"5"` or `"1:23"` (`""` when none); `cooldownSeconds` is a number or `nil`; `color` is the per-vertex tint `{ r, g, b }`.
- **Notes** Opt-in; the action bar must be on screen. `usable` is derived from the tint — a slot with `color` ≈ `{51,51,51}` (grey) is unusable. `activating` is the global-cooldown sweep across the whole bar, not a per-ability "fired" signal; watch `onCooldown` / `cooldownSeconds` to know which slot fired.

## Progress

Detects on-screen progress bars — action / skilling progress (fletching, crafting, invention…), Necromancy conjure timers, the adrenaline bar, and so on. Each bar TYPE is identified automatically by its colour signature (a `combo` string) or a friendly `name` you register — there is nothing to train. The reader measures fill % and tracks when a type begins and ends, which makes it ideal for "am I still skilling?" AFK detection or watching timers. Recognition runs server-side. Requires clients v0.1.1+ and the rs3buddy server at engine v0.2.7+.

This client exports the types `ProgressReadResult`, `ProgressBar`, `ProgressGroup`, `ProgressBegan`, and `ProgressEnded`.

### buddy.progress:read(opts?)
Reads every progress bar on screen, grouped by type, with begin/end transitions since the last poll.
- **Parameters**
  - `opts` · table (optional) · `{ name?, combo? }` — pass `{ name = ... }` or `{ combo = ... }` to read just one bar type. Omit to read all of them.
- **Returns** a `ProgressReadResult` table `{ ok, ageMs, count, bars, groups, began, ended }`. `count` is how many bar TYPES are on screen. `bars` is an array of `ProgressBar` — each `{ combo, name, x, y, w, percent, confident }`, where `name` is `nil` until you register one, `percent` is `0..100`, and `confident` flags a clean read. `groups` is the per-TYPE aggregate, an array of `ProgressGroup` — each `{ combo, name, count, stableCount, percents, minPercent, maxPercent, confident }`, where `percents` is the fills high→low and `stableCount` is flicker-proof (e.g. 3 conjure timers read `stableCount = 3` even if one drops a frame). `began` is an array of `ProgressBegan` `{ combo, name }` — bar types that appeared this poll; `ended` is an array of `ProgressEnded` `{ combo, name, maxPercent }` — bar types that went fully gone this poll.
- **Notes** Opt-in; the bar must be visible on screen. A `combo` is the bar's colour signature (e.g. `"154730836:535157007"`) and is stable per bar type, so name it once with `setName` and then read it by name. begin/end is tracked per TYPE — `began` fires when the first bar of a type appears, `ended` fires when every bar of that type is gone — which is what you want for AFK alerts.

```lua
-- Name the skilling bar once, then poll just that type and react when it ends.
buddy.progress:setName("154730836:535157007", "skilling")

local p = buddy.progress:read({ name = "skilling" })
if p and p.count > 0 then
    print(string.format("skilling %d%%", math.floor(p.groups[1].maxPercent)))
end
for _, e in ipairs(p and p.ended or {}) do
    print("stopped: " .. (e.name or e.combo))   -- AFK alert
end
```

### buddy.progress:names()
Returns the registry mapping each colour signature to the friendly name you gave it.
- **Returns** a table `{ ok, names }`, where `names` maps `combo` → `name` (e.g. `{ ["154730836:535157007"] = "skilling" }`).

### buddy.progress:setName(combo, name)
Registers a friendly name for a colour signature so `read` and the groups report it by name. Pass an empty name to remove the mapping.
- **Parameters**
  - `combo` · string · the bar's colour signature.
  - `name` · string · the friendly name; empty removes it.

## Scene

Classified per-frame view of the world plus the camera's view-projection matrix.

A **SceneSnapshot** is `{ timestamp, player, npcs, scenery, floor, water, particles, other, viewProj }`. `player` is `{ world = {x,y,z}, tile = {x,z}, chunk = {x,z}, floor }` or `nil`; the per-kind lists hold **Entity** tables; `viewProj` is 16 numbers (column-major) or `nil`.

An **Entity** is `{ id, drawIndex, kind, world = {x,y,z}, tile = {x,z}, chunk = {x,z}, floor, meshId, shaderId, vertexCount, tag, screen }`. `kind` is `"npc" | "scenery" | "water" | "floor" | "particles" | "player" | "ui" | "unknown"`. `screen` is `{ x, y, depth }` normalized to `[0,1]` (0,0 = bottom-left, matching `gl_FragCoord`), or `nil` when behind the camera; multiply by viewport size for pixels. Match on `tag` (a short stable per-model fingerprint), not `vertexCount`.

### buddy:get_scene()
The whole snapshot.
- **Returns** a `SceneSnapshot` table.

### buddy:get_scene_player()
Just the player object.
- **Returns** the player table `{ world, tile, chunk, floor }` or `nil`.

### buddy:get_npcs(radius?, floor?)
Nearby NPCs.
- **Parameters**
  - `radius` · number (optional) · tile radius. Default 15.
  - `floor` · number (optional) · floor level. Default 0.
- **Returns** an array of Entity tables.

### buddy:get_scenery(radius?, floor?)
Nearby scenery.
- **Parameters**
  - `radius` · number (optional) · tile radius. Default 15.
  - `floor` · number (optional) · floor level. Default 0.
- **Returns** an array of Entity tables.

### buddy:get_water(radius?, floor?)
Nearby water draws.
- **Parameters**
  - `radius` · number (optional) · tile radius. Default 15.
  - `floor` · number (optional) · floor level. Default 0.
- **Returns** an array of Entity tables.

### buddy:get_floor_tiles(radius?, floor?)
Nearby floor tiles.
- **Parameters**
  - `radius` · number (optional) · tile radius. Default 15.
  - `floor` · number (optional) · floor level. Default 0.
- **Returns** an array of Entity tables.

### buddy:get_entity_at(x, z, floor?)
Entities on a specific tile.
- **Parameters**
  - `x` · number · tile X.
  - `z` · number · tile Z.
  - `floor` · number (optional) · floor level. Default 0.
- **Returns** an array of Entity tables on that tile (often empty).

## Drawing (world)

Draw shapes and text anchored to a tile, a world point, the screen, or an entity.

A **Shape** table needs at least `anchor`, `primitive`, `vertices`, `color`:
- `anchor` · table · one of `{ mode="screen", screen={x,y} }`, `{ mode="world", world={x,y,z} }`, `{ mode="tile", tile={x,z,floor, liftY?} }`, `{ mode="entity", entityId, offset={x,y,z} }`.
- `primitive` · string · `"triangles" | "triangle_strip" | "triangle_fan" | "lines" | "line_strip" | "line_loop" | "points"`.
- `vertices` · array of numbers · flat `x,y,z` triplets, local to the anchor.
- `color` · string or array · `"#rrggbb"` / `"#rrggbbaa"`, or a per-vertex array `{ {r,g,b,a}, ... }` for a gradient.
- `thickness` · number (optional) · line width in pixels. Default 1.
- `pointSize` · number (optional) · point size in pixels. Default 4.
- `occludedByWorld` · boolean (optional) · `true` lets world geometry hide it; `false` (default) draws always on top.
- `group` · string (optional) · grouping tag for `clear_shapes(group)`.
- `id` · string (optional) · auto-minted on submit if omitted.

A text **billboard** (`{ kind = "billboard", anchor, text, color, font?, fontSize? }`) and an **image** item (`{ kind = "image", anchor, width, height, ... }`) use the same anchors and are also accepted by `draw_shape` / `draw_scene`.

### buddy:draw_shape(shape)
Submits one shape.
- **Parameters**
  - `shape` · table · a Shape (or billboard / image) as above.
- **Returns** a table `{ id, size }` — the new shape's id and the total shape count.

```lua
buddy:draw_shape({
    anchor = { mode = "tile", tile = { x = 3200, z = 3200, floor = 0 } },
    primitive = "line_loop",
    vertices = { -0.5,0,-0.5, 0.5,0,-0.5, 0.5,0,0.5, -0.5,0,0.5 },
    color = "#00ff00", thickness = 2, occludedByWorld = true,
})
```

### buddy:draw_scene(shapes)
Replaces the entire overlay scene with an array of shapes.
- **Parameters**
  - `shapes` · array of tables · Shapes to draw.
- **Returns** a table `{ size }` — the total shape count.

### buddy:list_shapes()
Lists what is currently drawn.
- **Returns** a table `{ shapes, size }`.

### buddy:remove_shape(id)
Removes one shape by id.
- **Parameters**
  - `id` · string · the shape id.

### buddy:clear_shapes(group?)
Clears drawn shapes.
- **Parameters**
  - `group` · string (optional) · clear only this group; omit to clear everything.
- **Returns** a table `{ cleared, group, size }`.

## UI overlay

Author the HUD as HTML + CSS; the server compiles it into the same widget engine the SDK renders and draws it in-game. Your app owns the state: poll `ui:events()` for clicks, update state, and call `ui:html(...)` again — each call replaces the current UI.

### buddy.ui:html(html, css?)
Renders an HTML + CSS page to the overlay (replaces the current UI). Primary authoring path.
- **Parameters**
  - `html` · string · the markup.
  - `css` · string (optional) · the stylesheet.
- **Returns** a table `{ ok, size }` (`size` = draw-item count).

```lua
buddy.ui:html(
    "<panel anchor='top-right' draggable consume>" ..
    "<span class='title'>Tracker</span>" ..
    "<button id='close' class='danger'>Exit</button></panel>",
    "panel{background:#241d12;padding:12px} .title{color:#f5c54a}"
)
```

### buddy.ui:render(tree)
Renders a raw widget tree instead of HTML.
- **Parameters**
  - `tree` · table · `{ type, props?, children? }` (a `UIWidget`).
- **Returns** a table `{ ok, size }`.

### buddy.ui:clear()
Clears the overlay UI.
- **Returns** a table `{ cleared = true }`.

### buddy.ui:events()
Drains queued interaction events (clicks / close / minimize). Poll this each loop.
- **Returns** a table `{ events }`, where each event is `{ type, id, x, y }`; `type` is `"click" | "close" | "minimize"` and `id` is the widget's id (string or `nil`). Empty when there was no interaction.

### buddy.ui:scaling(opts)
Tunes how big the UI grows on hi-DPI / 4K. Only the given keys are sent.
- **Parameters**
  - `opts` · table · `{ exponent?, scale?, base_height? }`. `exponent` (default 1.5): `1` = constant physical size, `>1` = bigger on 4K. `scale` is an explicit multiplier (overrides the exponent curve). `base_height` is the reference window height (sent to the server as `baseHeight`).
- **Returns** a table `{ ok = true }`.

## Sound

### buddy.sound:play(opts)
Plays developer-supplied audio (wav / mp3 / ogg) through the desktop app's audio host. Only the given keys are sent.
- **Parameters**
  - `opts` · table · `{ file?, bytes?, mime?, volume? }`. Provide either `file` (a path or `file:` / `data:` / `http(s):` URL) or base64 `bytes` (pair with `mime`, defaults server-side to `audio/wav`). `volume` is an optional `0..1` gain.
- **Returns** a table `{ ok = true }`, or `{ ok = false, error = ... }` when no audio host is available.
- **Notes** Requires the desktop audio host; no-op headless.

```lua
buddy.sound:play({ file = "C:/sfx/ding.wav", volume = 0.6 })
-- inline bytes: buddy.sound:play({ bytes = "<base64>", mime = "audio/mp3" })
```

## Fonts

Register and list render / DRAW fonts (used for text billboards) — distinct from the trained-recognition registry.

### buddy:list_fonts()
Lists the registered render fonts.
- **Returns** a table mapping alias → path, e.g. `{ rs3 = "C:/fonts/MyFont.ttf" }`.

### buddy:register_font(name, path)
Registers a render font.
- **Parameters**
  - `name` · string · the alias.
  - `path` · string · a `.ttf` / `.woff2` on the host.

### buddy:unregister_font(name)
Unregisters a render font.
- **Parameters**
  - `name` · string · the alias.

## Sprites

Manage trained sprites — named sprites the recognition system matches.

### buddy:list_trained_sprites()
Lists trained sprites.
- **Returns** an array of `{ name, hash, colorHash, textureId, x, y, width, height, createdAt }`.

### buddy:save_trained_sprite(req)
Saves a trained-sprite record.
- **Parameters**
  - `req` · table · a trained-sprite record (name + hash(es) + rect).

### buddy:delete_trained_sprite(name)
Deletes a trained sprite by name.
- **Parameters**
  - `name` · string · the sprite name.

## Advanced

Low-level surfaces — atlas / recognition, shaders & FX, capture & textures, OCR. Most apps use the high-level readers (chat / bars / abilities) instead. Capture forces a frame readback — not for polling.

### buddy:atlas_sprites()
The atlas sprite record set.
- **Returns** a table `{ sprites }`, each `{ hash, textureId, x, y, w, h, observationCount, lastSeenFrame }`.

### buddy:atlas_sync()
Refreshes the atlas record set from the live frame.

### buddy:atlas_lookup(hash)
Finds an atlas sprite by hash.
- **Parameters**
  - `hash` · number · the sprite hash.
- **Returns** a table `{ hit, record? }`.

### buddy:train_quad(req)
Trains a recognition entry from a drawn quad.
- **Parameters**
  - `req` · table · the train request.

### buddy:recognize_quads(quads)
Recognises an array of quads.
- **Parameters**
  - `quads` · array · quads to match.
- **Returns** the matches.

### buddy:import_sprite_hash(req)
Imports an externally-computed sprite-hash record.
- **Parameters**
  - `req` · table · the sprite-hash record.

### buddy:get_shaders()
Lists loaded GLSL shader programs.
- **Returns** an array of `{ id, vertexSource, fragmentSource, uniforms = { { name, type, location } }, attributes, matchType, kind, fragmentHash, vertexHash }`.

### buddy:add_post_fx(pass)
Registers or replaces a fullscreen post-FX pass.
- **Parameters**
  - `pass` · table · `{ id, fragmentSource, uniforms?, order?, enabled? }`. Samples the previous result via `uScene`; builtins `uScene` / `uResolution` / `uTime` / `uFrame`.
- **Returns** a table `{ id }`.

### buddy:list_post_fx()
All post-FX passes, in render order (ascending `order`).
- **Returns** an array of pass tables.

### buddy:remove_post_fx(id)
Removes a post-FX pass.
- **Parameters**
  - `id` · string · the pass id.
- **Returns** a table `{ removed }`.

### buddy:set_post_fx_enabled(id, enabled)
Enables/disables a post-FX pass without removing it.
- **Parameters**
  - `id` · string · the pass id.
  - `enabled` · boolean · on/off.
- **Returns** a table `{ ok }`.

### buddy:add_shader_fx(o)
Registers or replaces a custom game-shader FX (replaces a stock shader, matched by classified type or exact source hash).
- **Parameters**
  - `o` · table · `{ id, matchType?, matchHash?, fragmentSource?, vertexSource?, enabled? }`.
- **Returns** a table `{ id }`.

### buddy:list_shader_fx()
All active custom game-shader FX.
- **Returns** an array of FX tables.

### buddy:remove_shader_fx(id)
Removes a shader FX (that shader reverts to stock).
- **Parameters**
  - `id` · string · the FX id.
- **Returns** a table `{ removed }`.

### buddy:set_shader_fx_enabled(id, enabled)
Enables/disables a shader FX without removing it.
- **Parameters**
  - `id` · string · the FX id.
  - `enabled` · boolean · on/off.
- **Returns** a table `{ ok }`.

### buddy:capture_frame(opts?)
Captures the current frame's draw list. Heavy — forces a frame readback; not for polling.
- **Parameters**
  - `opts` · table (optional) · `{ include_mesh?, include_texture_pixels?, shader_id?, mesh_id?, target_fbo? }`. The id fields filter the returned draws.
- **Returns** a table `{ draws, drawCount }`; each draw is `{ index, shaderId, vertexCount, meshId, targetFbo, viewport = {x,y,width,height}, textures = { {slot,id,width,height} }, ... }`.

### buddy:get_textures()
Lists captured textures.
- **Returns** an array of `{ id, width, height, format, source }` (`source` is `"snapshot"` or `"frame-harvest"`).

### buddy:read_texture(id, x?, y?, w?, h?)
Reads raw pixels from a texture (optionally a sub-rect).
- **Parameters**
  - `id` · number · the texture id.
  - `x`, `y`, `w`, `h` · number (optional) · sub-rect; each defaults to 0.

### buddy:screen_capture()
Captures the current screen.

### buddy:get_captured_textures()
Lists the captured-texture cache.

### buddy:refresh_captured_textures()
Refreshes the captured-texture cache from the live frame.

### buddy:captured_textures_stats()
Captured-texture cache stats.
- **Returns** a table `{ entryCount, totalDiskBytes, oldestTouchAgeMs, capacityBytes, cacheDir, refreshInFlight }`.

### buddy:clear_captured_textures()
Clears the captured-texture cache.

### buddy:get_captured_texture(id)
Gets one captured texture by id.
- **Parameters**
  - `id` · number · the texture id.

### buddy:get_glyphs_on_screen(opts?)
Finds glyph-like quads on screen.
- **Parameters**
  - `opts` · table (optional) · numeric filters `{ minW?, minH?, maxW?, maxH?, alphaThreshold? }`.
- **Returns** a table `{ width, height, count, glyphs }`.

### buddy:glyph_at_point(x, y, opts?)
Reads the glyph at a screen point.
- **Parameters**
  - `x` · number · screen X.
  - `y` · number · screen Y.
  - `opts` · table (optional) · numeric options merged into the request.

### buddy:match_text(req)
Recognises text matching a request spec (general OCR, separate from the structured readers).
- **Parameters**
  - `req` · table · the request spec.

### buddy:match_region(req)
Recognises the text in a given region.
- **Parameters**
  - `req` · table · region + options.
