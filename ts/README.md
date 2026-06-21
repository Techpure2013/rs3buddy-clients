# rs3buddy — TypeScript API reference

Version 0.1.0 · last updated 2026-06-21

## Install

```ts
// npm install @rs3buddy/client
import { RS3Buddy, RS3BuddyConnectionError } from "@rs3buddy/client";

const buddy = RS3Buddy.connect();   // auto-discovers the running game
console.log("connected:", buddy.baseUrl);
```

The desktop launcher must be running: it injects the native hook, brings the SDK server up, and writes `%APPDATA%\rs3buddy\rs3buddy.json`. `connect()` reads that config and wires itself to the running game — no URL, no port.

All methods are async (return a `Promise`) unless noted (`baseUrl` is a getter). A call with nothing to return (no player on screen, nothing tracked) resolves to `null`, not an error. A server-side non-2xx raises `RS3BuddyError`; an unreachable server raises `RS3BuddyConnectionError`.

## Connection

### RS3Buddy.connect(opts?: TransportOptions): RS3Buddy
Open a client. With no options, auto-discovers the server from the launcher config; the launcher must be running.
- **Parameters**
  - `opts` · `TransportOptions` · optional `{ baseUrl?, clientName?, timeoutMs? }`. `baseUrl` (string) overrides the discovered config and targets a specific server. `clientName` (string) is sent as the `X-Client-Name` header on every request (handy for debugging). `timeoutMs` (number) is the per-request timeout; default `5000`.
- **Returns** `RS3Buddy` — a connected client.

### buddy.baseUrl: string
The discovered server address (getter, not a Promise).
- **Returns** `string` — e.g. `http://127.0.0.1:<port>`.

## Status & window

### buddy.getStatus(): Promise<unknown>
Server health + GL driver + memory + window, in one call. Cheap; safe to call any time.
- **Returns** `Promise<unknown>` — `{ connected: boolean, driver: { device, vendor, glVersion }, memory: { usedBytes, totalBytes }, window: { x, y, width, height } }`.

### buddy.getWindow(): Promise<unknown>
The game window's position and size in screen pixels.
- **Returns** `Promise<unknown>` — `{ x, y, width, height }`.

### buddy.getHeap(): Promise<unknown>
GL / native heap usage.
- **Returns** `Promise<unknown>` — `{ totalBytes, freeBytes, usedBytes, externalBytes, externalSegments }`.

## Player

### buddy.getPlayer(): Promise<Position | null>
Cheap passive read of the player's world position — no GPU work, no frame capture, no stall. Safe to poll every loop.
- **Returns** `Promise<Position | null>` — `{ tileX, tileZ, worldX, worldZ }` (`worldX = tileX * 512`), or `null` for the first frame or two until the native tap locks on. On `null`, call again.
- **Notes** Opt-in. There is also a forced-detection variant that runs a GPU occlusion-mesh capture for a cold first-acquire — it is heavy; do not poll it. Use this method for continuous position.

### buddy.updatePlayer(): Promise<Position | null>
Alias of `getPlayer()` — same cheap passive tap (both hit `GET /api/player`).
- **Returns** `Promise<Position | null>` — same as `getPlayer()`; `null` when not currently tracked → call again.

### buddy.getPlayerName(): Promise<PlayerNameResult>
Recovers the local player's name + title(s) from the chat input prompt. The name never appears in the chat feed.
- **Returns** `Promise<PlayerNameResult>` — `{ ok, found, displayName, name, prefix?, suffix?, title? }`. `prefix`/`suffix` are the title text before/after the name; `title` is whichever is present. String fields are empty until `found` flips to `true`.
- **Notes** Opt-in; `found` stays `false` until the chat input prompt has been seen at least once.

## Chat

### buddy.chat.read(opts?: ChatReadOptions): Promise<ChatReadResult>
Reads the in-game chatbox as structured lines, with per-run and per-glyph colour. Stall-free; the heavy work (capture, glyph recognition, line grouping, colour sampling) runs server-side.
- **Parameters**
  - `opts` · `ChatReadOptions` · optional region override in window px: `{ x0?, y0?, x1?, y1? }`. Omit to use the default bottom-left chatbox.
- **Returns** `Promise<ChatReadResult>` — `{ ok, font, lineCount, stale, ageMs, lines: ChatLine[], locate }`. Each `ChatLine` is `{ y, text, color, runs: ChatRun[], glyphs: ChatGlyph[] }`. `color` is the message (last-glyph) colour as `[r, g, b]`; `runs` are contiguous same-colour segments (clan tag / sender / body); `glyphs` give per-character `{ char, x, color }`. Lines are ordered top-to-bottom (oldest first).
- **Notes** Opt-in; the chatbox must be on screen.

## Bars

### buddy.bars.read(): Promise<BarsResult>
Reads the four status bars / orbs (HP, adrenaline, prayer, summoning) in one call. Recognition runs server-side.
- **Returns** `Promise<BarsReadResult>` — `{ ok, stale, ageMs, bars: BarValue[] }`. `bars` is always the four bars in fixed order (`hitpoints`, `adrenaline`, `prayer`, `summoning`); check each entry's `found`. Each `BarValue` is `{ name, found, value, max, text, anchor, region }` — `value` is `number | null` (a percentage for adrenaline, whose `max` is `null`), `max` is `number | null` (only set when the bar shows current/max), `anchor`/`region` are window-px rects or `null` when not found.
- **Notes** Opt-in; the orbs must be visible on screen. `stale` is `false` whenever a fresh capture happened on this call.

## Abilities

### buddy.abilities.read(): Promise<AbilitiesResult>
Reads the action bar(s): each slot's ability, on-screen + atlas rect, cooldown state, and whether it's usable. Recognition runs server-side.
- **Returns** `Promise<AbilitiesReadResult>` — `{ ok, stale, ageMs, abilities: AbilitySlot[] }`, in reading order (rows top-to-bottom, then left-to-right). Each `AbilitySlot` is `{ name, rect, atlas, activating, onCooldown, cooldownText, cooldownSeconds, usable, color }`. `name` omits the `ability:` prefix; `cooldownSeconds` is `number | null`; `usable` is derived from the per-vertex tint (`color` ≈ `[51,51,51]` grey ⇒ unusable).
- **Notes** Opt-in; the action bar must be on screen. `activating` is the global-cooldown flash sweep across the whole bar, not a per-ability "fired" signal — watch `onCooldown` / `cooldownSeconds` to tell which slot fired. `usable` depends on correct tint capture.

## Scene

### buddy.getScene(): Promise<SceneSnapshot>
Classified per-frame view of the world plus the camera matrix.
- **Returns** `Promise<SceneSnapshot>` — `{ timestamp, player, npcs, scenery, floor, water, particles, other, viewProj }`. `player` is a `PlayerInfo` (`{ world, tile, chunk, floor }`) or `null`. The per-kind lists are `Entity[]`. `viewProj` is 16 numbers (column-major) or `null`. Each `Entity` is `{ id, drawIndex, kind, world, tile, chunk, floor, meshId, shaderId, vertexCount, tag, screen }`; `screen` is `{ x, y, depth }` normalized `[0,1]` (0,0 = bottom-left) or `null` when behind the camera. Match a specific model on `tag` (a stable 5-char fingerprint), not `vertexCount`.

### buddy.getScenePlayer(): Promise<unknown>
The scene's player object only.
- **Returns** `Promise<unknown>` — a `PlayerInfo` (`{ world, tile, chunk, floor }`) or `null`.

### buddy.getNpcs(radius?: number, floor?: number): Promise<unknown>
Living animated characters near the player (excluding the player).
- **Parameters**
  - `radius` · `number` · optional search radius in tiles. Default `15`.
  - `floor` · `number` · optional floor index. Default `0`.
- **Returns** `Promise<unknown>` — `Entity[]`.

### buddy.getScenery(radius?: number, floor?: number): Promise<unknown>
Static world objects (trees, doors, buildings) near the player.
- **Parameters**
  - `radius` · `number` · optional radius in tiles. Default `15`.
  - `floor` · `number` · optional floor index. Default `0`.
- **Returns** `Promise<unknown>` — `Entity[]`.

### buddy.getWater(radius?: number, floor?: number): Promise<unknown>
Water-surface draws near the player.
- **Parameters**
  - `radius` · `number` · optional radius in tiles. Default `15`.
  - `floor` · `number` · optional floor index. Default `0`.
- **Returns** `Promise<unknown>` — `Entity[]`.

### buddy.getFloorTiles(radius?: number, floor?: number): Promise<unknown>
Floor / terrain tile draws near the player.
- **Parameters**
  - `radius` · `number` · optional radius in tiles. Default `15`.
  - `floor` · `number` · optional floor index. Default `0`.
- **Returns** `Promise<unknown>` — `Entity[]`.

### buddy.getEntityAt(x: number, z: number, floor?: number): Promise<unknown>
Entities standing on a specific tile.
- **Parameters**
  - `x` · `number` · absolute tile X.
  - `z` · `number` · absolute tile Z.
  - `floor` · `number` · optional floor index. Default `0`.
- **Returns** `Promise<unknown>` — `Entity[]` on that tile (often `[]`).

## Drawing (world)

Shapes are plain `DrawItem` objects (`Shape | Billboard | ImageItem`). A geometry `Shape` needs at minimum `anchor`, `primitive`, `vertices`, and `color`. The `anchor` is `{ mode: "tile" | "world" | "screen" | "entity", ... }`; `vertices` is a flat `x,y,z` array local to the anchor; `color` is a `Color` (e.g. `"#rrggbb[aa]"`, named, or `[r,g,b(,a)]`) or a per-vertex `Color[]` for gradients.

### buddy.drawShape(shape: DrawItem): Promise<unknown>
Submit one overlay item (geometry shape, text billboard, or image).
- **Parameters**
  - `shape` · `DrawItem` · the item to draw. `Shape` supports `primitive`, `vertices`, `color`, `thickness` (px line width, default 1), `pointSize` (px, default 4), `occludedByWorld` (default `false` = always on top), `blend`, `group`, `id`, plus advanced `shader`/`uniforms`/`conformToGround`. A `Billboard` (`kind: "billboard"`) carries `text`, `font?`, `fontSize?`, alignment, etc. An `ImageItem` (`kind: "image"`) carries `width`, `height`, and an optional tint.
- **Returns** `Promise<unknown>` — `{ id, size }`: the added shape's id and the total shape count.

### buddy.drawScene(shapes: DrawItem[]): Promise<unknown>
Replace the entire overlay scene with an array of items.
- **Parameters**
  - `shapes` · `DrawItem[]` · the full set of items; replaces everything currently drawn.
- **Returns** `Promise<unknown>` — `{ size }`: the new total shape count.

### buddy.listShapes(): Promise<unknown>
List the currently drawn overlay items.
- **Returns** `Promise<unknown>` — `{ shapes: DrawItem[], size }`.

### buddy.removeShape(id: string): Promise<unknown>
Remove one drawn shape by id.
- **Parameters**
  - `id` · `string` · the id returned by `drawShape`.
- **Returns** `Promise<unknown>` — removal acknowledgement.

### buddy.clearShapes(group?: string): Promise<unknown>
Clear drawn shapes — all of them, or just one group.
- **Parameters**
  - `group` · `string` · optional grouping tag. Omit to clear everything.
- **Returns** `Promise<unknown>` — `{ cleared: true, group, size }`.

## UI overlay

Author the HUD as HTML + CSS and POST it; the server compiles it to the same widget engine the SDK renders (clicks / drag / scaling all work). Your app owns the state: poll `events()` for clicks and call `html()` again to re-render — each call replaces the current UI.

### buddy.ui.html(html: string, css?: string): Promise<unknown>
Render an HTML + CSS "page" to the overlay (replaces the current UI).
- **Parameters**
  - `html` · `string` · the markup. Tags map to widgets (`panel`, `div`/`row`/`column`, `span`/`p`/`h1`–`h4`, `button`, `progress`/`gauge`, `img`, `hr`, `badge`, …; unknown tags become a column so a page never hard-fails). Special attrs: `id`, `draggable`, `consume`, `anchor`, `variant`, `icon`, `action`.
  - `css` · `string` · optional stylesheet. Supports tag / `.class` / `#id` / descendant selectors and inline `style="..."`; common box, flex, colour, font, and gradient properties map. Defaults to `""`.
- **Returns** `Promise<unknown>` — `{ ok: true, size }` (draw-item count).

### buddy.ui.render(tree: unknown): Promise<unknown>
Render a raw widget tree instead of HTML.
- **Parameters**
  - `tree` · `unknown` · a `UIWidget` tree `{ type, props?, children? }` (see `WidgetType` / `UIProps`).
- **Returns** `Promise<unknown>` — `{ ok: true, size }`.

### buddy.ui.clear(): Promise<unknown>
Clear the overlay UI.
- **Returns** `Promise<unknown>` — `{ cleared: true }`.

### buddy.ui.events(): Promise<UIEvents>
Drain queued interaction events (clicks / close / minimize).
- **Returns** `Promise<UIEvents>` — `{ events: UIEvent[] }`; each `UIEvent` is `{ type, id, x, y }` (`type` is `"click"` | `"close"` | `"minimize"`, `id` is the widget's id). Empty when there was no interaction.

### buddy.ui.scaling(opts: UIScalingOptions): Promise<unknown>
Tune how big the UI grows on hi-DPI / 4K.
- **Parameters**
  - `opts` · `UIScalingOptions` · `{ exponent?, scale?, baseHeight? }`. `exponent` (default `1.5`): `1` = constant physical size, `>1` = bigger on 4K. `scale` is an explicit multiplier (overrides the exponent curve). `baseHeight` is the reference window height the scaling is normalised against.
- **Returns** `Promise<unknown>` — `{ ok: true }`.

## Sound

### buddy.sound.play(opts: SoundPlayOptions): Promise<unknown>
Play developer-supplied audio (wav / mp3 / ogg) host-side through the desktop app.
- **Parameters**
  - `opts` · `SoundPlayOptions` · `{ file?, bytes?, mime?, volume? }`. Provide either `file` (a host path or `file:`/`data:`/`http(s):` URL) or inline base64 `bytes` paired with `mime` (default `"audio/wav"`). `volume` is `0..1` gain (default host volume).
- **Returns** `Promise<unknown>` — `{ ok: true }`, or `{ ok: false, error }` when no audio host is available.
- **Notes** Requires the desktop audio host (no-op headless).

## Fonts

These are render / DRAW fonts used by text billboards — distinct from the trained-recognition registry.

### buddy.listFonts(): Promise<unknown>
List the registered render fonts.
- **Returns** `Promise<unknown>` — an alias → path map, e.g. `{ "rs3": "C:/fonts/MyFont.ttf" }`.

### buddy.registerFont(name: string, p: string): Promise<unknown>
Register a render font from a host-side font file.
- **Parameters**
  - `name` · `string` · the alias to register.
  - `p` · `string` · path to a `.ttf` / `.woff2` on the host.
- **Returns** `Promise<unknown>` — registration acknowledgement.

### buddy.unregisterFont(name: string): Promise<unknown>
Unregister a render font.
- **Parameters**
  - `name` · `string` · the alias to remove.
- **Returns** `Promise<unknown>` — removal acknowledgement.

## Sprites

Manage trained sprites — named sprites the recognition system matches.

### buddy.listTrainedSprites(): Promise<unknown>
List the trained sprites.
- **Returns** `Promise<unknown>` — `[{ name, hash, colorHash, textureId, x, y, width, height, createdAt }]`.

### buddy.saveTrainedSprite(req: unknown): Promise<unknown>
Save / train a sprite record.
- **Parameters**
  - `req` · `unknown` · a trained-sprite record (name + hash(es) + rect).
- **Returns** `Promise<unknown>` — save acknowledgement.

### buddy.deleteTrainedSprite(name: string): Promise<unknown>
Delete a trained sprite by name.
- **Parameters**
  - `name` · `string` · the sprite name.
- **Returns** `Promise<unknown>` — deletion acknowledgement.

## Advanced

Low-level surfaces — most apps use the high-level readers (chat / bars / abilities) instead. Capture methods force a frame readback; do not poll them.

### buddy.atlasSprites(): Promise<unknown>
List recognised sprites in the live texture atlas.
- **Returns** `Promise<unknown>` — `{ sprites: [{ hash, textureId, x, y, w, h, observationCount, lastSeenFrame }] }`.

### buddy.atlasSync(): Promise<unknown>
Refresh the atlas record set from the live frame.
- **Returns** `Promise<unknown>` — sync acknowledgement.

### buddy.atlasLookup(hash: number): Promise<unknown>
Find a sprite in the atlas by hash.
- **Parameters**
  - `hash` · `number` · the sprite hash.
- **Returns** `Promise<unknown>` — `{ hit: boolean, record? }`.

### buddy.trainQuad(req: unknown): Promise<unknown>
Train a recognition entry from a drawn quad.
- **Parameters**
  - `req` · `unknown` · the train-quad request.
- **Returns** `Promise<unknown>` — training result.

### buddy.recognizeQuads(quads: unknown[]): Promise<unknown>
Recognise an array of quads against the atlas.
- **Parameters**
  - `quads` · `unknown[]` · the quads to recognise.
- **Returns** `Promise<unknown>` — the matches.

### buddy.importSpriteHash(req: unknown): Promise<unknown>
Import an externally-computed sprite-hash record.
- **Parameters**
  - `req` · `unknown` · the sprite-hash record.
- **Returns** `Promise<unknown>` — import acknowledgement.

### buddy.getShaders(): Promise<ShaderInfo[]>
List the loaded GLSL shader programs.
- **Returns** `Promise<ShaderInfo[]>` — each `{ id, vertexSource, fragmentSource, uniforms[], attributes[], matchType, kind, fragmentHash, vertexHash }`. `matchType` / `fragmentHash` feed `addShaderFx`.

### buddy.addPostFx(pass: PostFxPassInput): Promise<{ id: string }>
Register or replace a fullscreen post-FX pass (GLSL fragment applied fullscreen, sampling the previous result via `uScene`).
- **Parameters**
  - `pass` · `PostFxPassInput` · `{ id, fragmentSource, uniforms?, order?, enabled? }`. Builtins in scope: `uScene`, `uResolution`, `uTime`, `uFrame`. `order` sets its slot in the chain (ascending).
- **Returns** `Promise<{ id: string }>` — the pass id.

### buddy.listPostFx(): Promise<PostFxPassInput[]>
List the post-FX passes in render order.
- **Returns** `Promise<PostFxPassInput[]>` — passes ascending by `order`.

### buddy.removePostFx(id: string): Promise<{ removed: boolean }>
Remove a post-FX pass.
- **Parameters**
  - `id` · `string` · the pass id.
- **Returns** `Promise<{ removed: boolean }>`.

### buddy.setPostFxEnabled(id: string, enabled: boolean): Promise<{ ok: boolean }>
Enable / disable a post-FX pass without removing it.
- **Parameters**
  - `id` · `string` · the pass id.
  - `enabled` · `boolean` · new enabled state.
- **Returns** `Promise<{ ok: boolean }>`.

### buddy.addShaderFx(o: ShaderFxInput): Promise<{ id: string }>
Register or replace a custom game-shader FX (patches one of RS3's own shaders, matched by classified type or exact source hash).
- **Parameters**
  - `o` · `ShaderFxInput` · `{ id, matchType?, matchHash?, fragmentSource?, vertexSource?, enabled? }`. `fragmentSource`/`vertexSource` replace that stage; an empty replacement leaves the stock shader.
- **Returns** `Promise<{ id: string }>` — the FX id.

### buddy.listShaderFx(): Promise<ShaderFxInput[]>
List all active custom game-shader FX.
- **Returns** `Promise<ShaderFxInput[]>`.

### buddy.removeShaderFx(id: string): Promise<{ removed: boolean }>
Remove a shader FX (that shader reverts to stock).
- **Parameters**
  - `id` · `string` · the FX id.
- **Returns** `Promise<{ removed: boolean }>`.

### buddy.setShaderFxEnabled(id: string, enabled: boolean): Promise<{ ok: boolean }>
Enable / disable a shader FX without removing it.
- **Parameters**
  - `id` · `string` · the FX id.
  - `enabled` · `boolean` · new enabled state.
- **Returns** `Promise<{ ok: boolean }>`.

### buddy.captureFrame(opts?: CaptureOptions): Promise<FrameCaptureResult>
Capture one frame's draw calls.
- **Parameters**
  - `opts` · `CaptureOptions` · optional `{ includeMesh?, includeTexturePixels?, filter?, detectPlayer? }`. `filter` is `{ shaderId?, meshId?, targetFbo?, drawType? }`.
- **Returns** `Promise<FrameCaptureResult>` — `{ draws: DrawInfo[], drawCount }`.
- **Notes** Heavy — forces a frame readback; not for polling.

### buddy.getTextures(): Promise<TextureInfo[]>
List the loaded textures.
- **Returns** `Promise<TextureInfo[]>` — each `{ id, width, height, format, source? }`.

### buddy.readTexture(id: number, x?: number, y?: number, w?: number, h?: number): Promise<unknown>
Read raw pixels from a texture (optionally a sub-rect).
- **Parameters**
  - `id` · `number` · the texture id.
  - `x` · `number` · optional sub-rect left. Default `0`.
  - `y` · `number` · optional sub-rect top. Default `0`.
  - `w` · `number` · optional sub-rect width. Default `0` (full).
  - `h` · `number` · optional sub-rect height. Default `0` (full).
- **Returns** `Promise<unknown>` — the texture pixel data.
- **Notes** Heavy GPU read; not for polling.

### buddy.screenCapture(): Promise<unknown>
Capture the current screen.
- **Returns** `Promise<unknown>` — the screen capture payload.
- **Notes** Heavy; not for polling.

### buddy.getCapturedTextures(): Promise<unknown>
List the cached captured textures.
- **Returns** `Promise<unknown>` — the captured-texture set.

### buddy.refreshCapturedTextures(): Promise<unknown>
Refresh the captured-texture cache from the live frame.
- **Returns** `Promise<unknown>` — refresh acknowledgement.

### buddy.capturedTexturesStats(): Promise<unknown>
Stats about the captured-texture cache.
- **Returns** `Promise<unknown>` — `{ entryCount, totalDiskBytes, oldestTouchAgeMs, capacityBytes, cacheDir, refreshInFlight }`.

### buddy.clearCapturedTextures(): Promise<unknown>
Clear the captured-texture cache.
- **Returns** `Promise<unknown>` — clear acknowledgement.

### buddy.getCapturedTexture(id: number): Promise<unknown>
Fetch one captured texture by id.
- **Parameters**
  - `id` · `number` · the captured-texture id.
- **Returns** `Promise<unknown>` — the captured texture.

### buddy.getGlyphsOnScreen(opts?: Record<string, number>): Promise<unknown>
Detect candidate glyph rects across the screen.
- **Parameters**
  - `opts` · `Record<string, number>` · optional filters `{ minW?, minH?, maxW?, maxH?, alphaThreshold? }`. Defaults to `{}`.
- **Returns** `Promise<unknown>` — `{ width, height, count, glyphs: [] }`.
- **Notes** Heavy capture; not for polling.

### buddy.glyphAtPoint(x: number, y: number, opts?: Record<string, number>): Promise<unknown>
Recognise the glyph at a screen point.
- **Parameters**
  - `x` · `number` · screen x (window px).
  - `y` · `number` · screen y (window px).
  - `opts` · `Record<string, number>` · optional recognition tuning. Defaults to `{}`.
- **Returns** `Promise<unknown>` — the recognised glyph (or none).

### buddy.matchText(req: unknown): Promise<unknown>
OCR — recognise text matching a request spec.
- **Parameters**
  - `req` · `unknown` · the match-text request spec.
- **Returns** `Promise<unknown>` — the recognition result.

### buddy.matchRegion(req: unknown): Promise<unknown>
OCR — recognise the text in a given region.
- **Parameters**
  - `req` · `unknown` · the region + options.
- **Returns** `Promise<unknown>` — the recognition result.
