# RS3 Buddy — Developer Guide

A complete, function-first reference for the RS3 Buddy clients. Build in-game overlays and
read game state from **any language** — each client (TypeScript, Python, Lua, Java) exposes the
same capabilities through idiomatic, typed methods such as `buddy.chat.read()`, `buddy.bars.read()`,
`buddy.getPlayer()`, `buddy.drawShape(...)`, `buddy.ui.html(...)`, and `buddy.sound.play(...)`.

You never touch a socket or a URL. `connect()` finds the running game for you, and every method
is a thin, typed call. This guide documents **every** method: its name in all four languages, its
parameters, the exact shape it returns, and a usage snippet.

> The overlay reads the game's GPU draws only — it never reads game memory or injects input
> (mouse input is *read and selectively consumed*, never synthesized). Player detection, the
> player's name, and all reads are **opt-in per method** — nothing is gathered passively.

### How to read this guide

- **Method names.** TypeScript & Java are camelCase (`getPlayer`); Python & Lua are snake_case
  (`get_player`). The reader namespaces (`chat`, `bars`, `abilities`, `ui`, `sound`) use a dot in
  TS / Python / Java (`buddy.ui.html(...)`) and a **colon** in Lua (`buddy.ui:html(...)`);
  **top-level Lua methods also use a colon** (`buddy:get_player()`).
- **Imports.** TS: `import { RS3Buddy } from "@rs3buddy/client"`. Python: `from rs3buddy import RS3Buddy`.
  Lua: `local rs3buddy = require("rs3buddy")`. Java: `import com.rs3buddy.RS3Buddy;`.
- **Return shapes** in this guide are the **live shapes** observed from the running server. A
  field shown as `int|null` is a number or `null`/`None`/`nil` depending on language.
- **"No result"** (e.g. nothing on screen) returns `null` / `None` / `nil` — it is **not** an error.

### Table of contents

1. [Getting started](#1-getting-started) · 2. [Status & window](#2-status--window) ·
3. [Player](#3-player) · 4. [Chat](#4-chat) · 5. [Bars](#5-bars) · 6. [Abilities](#6-abilities) ·
7. [Scene](#7-scene) · 8. [Drawing in the world](#8-drawing-in-the-world) ·
9. [UI overlay](#9-ui-overlay) · 10. [Sound](#10-sound) · 11. [Fonts](#11-fonts) ·
12. [Sprites](#12-sprites) · 13. [Atlas / recognition (advanced)](#13-atlas--recognition-advanced) ·
14. [Shaders & FX (advanced)](#14-shaders--fx-advanced) ·
15. [Capture & textures (advanced)](#15-capture--textures-advanced) · 16. [OCR (advanced)](#16-ocr-advanced) ·
17. [Function reference](#17-function-reference)

---

## 1. Getting started

The desktop **launcher must be running**. It injects the native hook into the RS3 client, which
brings the SDK server up and writes a small config file under `%APPDATA%\rs3buddy\rs3buddy.json`.
Each client's `connect()` reads that config and wires itself to the running game automatically —
no URL, no port, no setup.

### `connect()` — open a client

| | name | parameters | returns |
|---|---|---|---|
| **TS** | `RS3Buddy.connect(opts?)` | `opts?: { clientName?, timeoutMs?, baseUrl? }` | `RS3Buddy` |
| **Python** | `RS3Buddy.connect(...)` | `client_name?: str`, `timeout?: float`, `base_url?: str` | `RS3Buddy` |
| **Lua** | `rs3buddy.connect(opts?)` | `opts?: { client_name?, base_url?, request? }` | `RS3Buddy` |
| **Java** | `RS3Buddy.connect()` / `connect(clientName)` | `clientName?: String` | `RS3Buddy` |

With no arguments, `connect()` auto-discovers the running game. Pass `clientName` /
`client_name` to tag your requests (useful for debugging). `baseUrl` / `base_url` is only for
targeting a specific server.

```ts
// TypeScript — npm install @rs3buddy/client
import { RS3Buddy, RS3BuddyConnectionError } from "@rs3buddy/client";

const buddy = RS3Buddy.connect();                 // auto-discovers the running game
console.log("connected:", buddy.baseUrl);
// Optional: RS3Buddy.connect({ clientName: "my-app", timeoutMs: 15000 })
```
```python
# Python — pip install rs3buddy
from rs3buddy import RS3Buddy, RS3BuddyConnectionError

buddy = RS3Buddy.connect()                         # auto-discovers the running game
print("connected:", buddy.base_url)
# Optional: RS3Buddy.connect(client_name="my-app", timeout=15.0)
```
```lua
-- Lua / Luau — require the package directory
local rs3buddy = require("rs3buddy")

local buddy = rs3buddy.connect()                   -- auto-discovers the running game
print("connected: " .. buddy:base_url())
-- Optional: rs3buddy.connect({ client_name = "my-app" })
```
```java
// Java (JDK 11+) — depends on the rs3buddy-client jar (+ Jackson)
import com.rs3buddy.RS3Buddy;
import com.rs3buddy.RS3BuddyConnectionException;

RS3Buddy buddy = RS3Buddy.connect();               // auto-discovers the running game
System.out.println("connected: " + buddy.baseUrl());
// Optional: RS3Buddy.connect("my-app")            // tags requests with a client name
```

### `baseUrl` — the discovered server address

| | name | returns |
|---|---|---|
| **TS** | `buddy.baseUrl` (getter) | `string` |
| **Python** | `buddy.base_url` (property) | `str` |
| **Lua** | `buddy:base_url()` | `string` |
| **Java** | `buddy.baseUrl()` | `String` |

### Errors & conventions

- **No result.** A call with nothing to return (no player on screen, nothing tracked yet) returns
  `null` / `None` / `nil`. This is normal — just call again.
- **Connection error.** If the server can't be reached (launcher not running, game crashed), the
  call raises:
  - **TS / Python:** `RS3BuddyConnectionError`
  - **Java:** `RS3BuddyConnectionException`
  - **Lua:** a tagged error table (catch with `pcall`)
- **Server error.** A server-side failure (non-2xx) raises `RS3BuddyError` (TS / Python) instead.

> **Lua in a sandbox.** On sandboxed hosts with no `io`/`os` (e.g. Roblox), auto-discovery can't
> read the config — pass an explicit base URL and an HTTP request function:
> `rs3buddy.connect({ base_url = "http://127.0.0.1:1234", request = my_http_fn })`.

---

## 2. Status & window

Cheap metadata about the server, the GL driver, and the game window. Safe to call any time.

### `getStatus()`

Server health + driver + memory + window, in one call.

**Returns**

```jsonc
{
  "connected": true,
  "driver":  { "device": "...", "vendor": "...", "glVersion": "..." },
  "memory":  { "usedBytes": 0, "totalBytes": 0 },
  "window":  { "x": 0, "y": 0, "width": 0, "height": 0 }
}
```

| | name |
|---|---|
| **TS** | `buddy.getStatus()` |
| **Python** | `buddy.get_status()` |
| **Lua** | `buddy:get_status()` |
| **Java** | `buddy.getStatus()` |

### `getWindow()`

The game window's position & size in screen pixels.

**Returns** — `{ x, y, width, height }`

| | name |
|---|---|
| **TS** | `buddy.getWindow()` |
| **Python** | `buddy.get_window()` |
| **Lua** | `buddy:get_window()` |
| **Java** | `buddy.getWindow()` |

### `getHeap()`

GL / native heap usage.

**Returns** — `{ totalBytes, freeBytes, usedBytes, externalBytes, externalSegments }`

| | name |
|---|---|
| **TS** | `buddy.getHeap()` |
| **Python** | `buddy.get_heap()` |
| **Lua** | `buddy:get_heap()` |
| **Java** | `buddy.getHeap()` |

```ts
const st = await buddy.getStatus();
console.log(st.driver.glVersion, st.window.width, "x", st.window.height);

const win = await buddy.getWindow();   // { x, y, width, height }
```
```python
st = buddy.get_status()
print(st["driver"]["glVersion"], st["window"]["width"], "x", st["window"]["height"])

win = buddy.get_window()               # { x, y, width, height }
```

---

## 3. Player

The player's world position. There are two ways to read it; **use the cheap passive read** and
poll it freely.

### `getPlayer()` / `updatePlayer()` — cheap passive read (poll freely)

Both call the same cheap passive tap. **No GPU work, no frame capture, no stall** — call it every
loop iteration. It returns `null` for the first frame or two until the native tap locks on, so if
you get `null`, just call again.

**Parameters** — none.

**Returns** — a position **or** `null`:

```jsonc
{ "tileX": 0, "tileZ": 0, "worldX": 0, "worldZ": 0 }   // or null / None / nil
```

`worldX` / `worldZ` are RS3 engine units (`tileX * 512`).

| | name | returns |
|---|---|---|
| **TS** | `buddy.getPlayer()` / `buddy.updatePlayer()` | `Position \| null` |
| **Python** | `buddy.get_player()` / `buddy.update_player()` | `Position \| None` |
| **Lua** | `buddy:get_player()` / `buddy:update_player()` | `Position?` |
| **Java** | `buddy.getPlayer()` / `buddy.updatePlayer()` | `Position` (null until tracked) |

> There is also a **forced-detection** variant that runs a GPU occlusion-mesh capture to lock the
> player. It exists for a cold first-acquire, but it is **heavy — do NOT poll it.** For continuous
> position, always use the cheap `getPlayer()` above.

```ts
const pos = await buddy.getPlayer();             // { tileX, tileZ, worldX, worldZ } | null
if (pos) console.log("tile", pos.tileX, pos.tileZ);
```
```python
pos = buddy.get_player()                          # { tileX, tileZ, worldX, worldZ } | None
if pos:
    print("tile", pos["tileX"], pos["tileZ"])
```
```lua
local pos = buddy:get_player()                    -- { tileX, tileZ, worldX, worldZ } | nil
if pos then print("tile", pos.tileX, pos.tileZ) end
```
```java
Position pos = buddy.getPlayer();                 // null until tracked
if (pos != null) System.out.println("tile " + pos.getTileX() + ", " + pos.getTileZ());
```

### `getPlayerName()` — the local player's name (opt-in)

Recovers the local player's name + title(s) from the **chat input prompt** (opt-in). The name
**never** appears in the chat feed. All string fields are empty / `null` until the prompt has been
seen at least once (`found` flips to `true`).

**Parameters** — none.

**Returns**

```jsonc
{
  "ok": true,
  "found": false,
  "displayName": null,
  "name": null,
  "prefix": null,    // title text before the name
  "suffix": null,    // title text after the name
  "title": null      // whichever title is present
}
```

| | name |
|---|---|
| **TS** | `buddy.getPlayerName()` |
| **Python** | `buddy.get_player_name()` |
| **Lua** | `buddy:get_player_name()` |
| **Java** | `buddy.getPlayerName()` |

```ts
const me = await buddy.getPlayerName();
if (me.found) console.log(me.displayName);
```
```python
me = buddy.get_player_name()
if me["found"]:
    print(me["displayName"])
```

---

## 4. Chat

Read the in-game chatbox as structured lines, with per-run and per-glyph colour. The heavy work
(frame + screen capture, glyph recognition, line grouping, colour sampling) runs server-side; the
reader is stall-free.

### `chat.read(opts?)`

**Parameters** — optional region override (window pixels). Omit to use the default bottom-left
chatbox.

| name | type | default | meaning |
|---|---|---|---|
| `x0` | `int` | auto | left edge of the region to scan |
| `y0` | `int` | auto | top edge |
| `x1` | `int` | auto | right edge |
| `y1` | `int` | auto | bottom edge |

**Returns**

```jsonc
{
  "ok": true,
  "font": "16pt",
  "lineCount": 1,
  "lines": [
    {
      "y": 0,
      "text": "You pick the target's pocket.",
      "color": [255, 255, 255],            // the message (last-glyph) colour, [r,g,b]
      "runs":  [ { "text": "...", "color": [255, 255, 255] } ],  // same-colour segments
      "glyphs":[ { "char": "Y", "x": 0, "color": [255, 255, 255] } ]
    }
  ]
}
```

Lines are ordered top-to-bottom (oldest first). Each `line.color` is the message colour (clan
green, public white, …). Use `runs` to reproduce a multi-colour line (clan tag / sender / body
each in its own colour), and `glyphs` for per-character x + colour.

| | name | params |
|---|---|---|
| **TS** | `buddy.chat.read(opts?)` | `{ x0?, y0?, x1?, y1? }` |
| **Python** | `buddy.chat.read(...)` | `x0=None, y0=None, x1=None, y1=None` |
| **Lua** | `buddy.chat:read(opts?)` | `{ x0?, y0?, x1?, y1? }` |
| **Java** | `buddy.chat.read()` / `read(x0,y0,x1,y1)` | `Integer x0,y0,x1,y1` |

```ts
const chat = await buddy.chat.read();
for (const line of chat.lines) console.log(line.color, line.text);
```
```python
chat = buddy.chat.read()
for line in chat["lines"]:
    print(line["color"], line["text"])
```
```lua
local chat = buddy.chat:read()
for _, line in ipairs(chat.lines or {}) do
    local c = line.color or { 0, 0, 0 }
    print(string.format("(%s,%s,%s) %s", c[1], c[2], c[3], tostring(line.text)))
end
```
```java
ChatReadResult chat = buddy.chat.read();
for (ChatLine line : chat.getLines()) {
    System.out.println(line.getColor() + " " + line.getText());  // color is [r, g, b]
}
```

---

## 5. Bars

Read the four status bars / orbs (HP, adrenaline, prayer, summoning) — each bar's current value,
its max (when shown), the raw text, and the located anchor + scanned region. Recognition runs
server-side.

### `bars.read()`

**Parameters** — none.

**Returns** — `bars` is always the four bars in a fixed order; check each entry's `found`:

```jsonc
{
  "ok": true,
  "stale": false,        // false whenever a fresh capture happened on this call
  "ageMs": 0,            // age of the cached glyph set this read used
  "bars": [
    {
      "name": "hitpoints",          // | "adrenaline" | "prayer" | "summoning"
      "found": true,
      "value": 10018,               // int | null (null when found but unreadable)
      "max": 10200,                 // int | null (null when only one number shown)
      "text": "10,018/10,200",
      "anchor": { "x": 0, "y": 0, "w": 0, "h": 0 },     // | null when not found
      "region": { "x0": 0, "y0": 0, "x1": 0, "y1": 0 }  // | null when not found
    },
    {
      "name": "adrenaline",
      "found": true,
      "value": 97,
      "max": null,
      "text": "97%",
      "anchor": null,
      "region": null
    }
    // ... prayer, summoning
  ]
}
```

`value` is a percentage for adrenaline (`max` is `null`). `anchor` is where the bar's icon was
located; `region` is the pixel box the digits were read from.

| | name |
|---|---|
| **TS** | `buddy.bars.read()` |
| **Python** | `buddy.bars.read()` |
| **Lua** | `buddy.bars:read()` |
| **Java** | `buddy.bars.read()` |

```ts
const { bars } = await buddy.bars.read();
const hp = bars.find((b) => b.name === "hitpoints");
console.log(`${hp?.value} / ${hp?.max}`);
```
```python
res = buddy.bars.read()
hp = next(b for b in res["bars"] if b["name"] == "hitpoints")
print(hp["value"], "/", hp["max"])
```
```lua
local res = buddy.bars:read()
for _, b in ipairs(res.bars) do
    if b.name == "hitpoints" then
        print(tostring(b.value) .. "/" .. tostring(b.max))
    end
end
```
```java
BarsReadResult bars = buddy.bars.read();
for (BarValue b : bars.getBars()) {
    if ("hitpoints".equals(b.getName())) {
        System.out.println(b.getValue() + " / " + b.getMax());
    }
}
```

---

## 6. Abilities

Read the action bar(s): each slot's ability, its on-screen + atlas rect, cooldown state, and
whether it's usable. Recognition runs server-side. Slots are in reading order (rows top-to-bottom,
then left-to-right).

### `abilities.read()`

**Parameters** — none.

**Returns**

```jsonc
{
  "ok": true,
  "stale": false,
  "ageMs": 0,
  "abilities": [
    {
      "name": "provoke",                       // also e.g. "buff:freedom", "anticipation"
      "rect":  { "x": 0, "y": 0, "w": 0, "h": 0 },   // on-screen slot
      "atlas": { "x": 0, "y": 0, "w": 0, "h": 0 },   // sprite location in the atlas
      "activating": false,   // global-cooldown flash sweep — NOT per-ability (see note)
      "onCooldown": false,
      "cooldownText": "",     // e.g. "5" or "1:23"; "" when none
      "cooldownSeconds": null,// int | null
      "usable": true,         // false when greyed out (depends on tint capture)
      "color": [51, 51, 51]   // per-vertex tint [r,g,b]; a grey triple ⇒ unusable
    }
  ]
}
```

> `activating` flashes across the **whole bar** on any cast (it's the global-cooldown sweep), so
> it is **not** a reliable per-ability "fired" signal. To know which slot actually fired, watch
> `onCooldown` / `cooldownSeconds` instead.

`usable` is derived from the per-vertex tint: a slot with `color` ≈ `[51,51,51]` (grey) is
unusable.

| | name |
|---|---|
| **TS** | `buddy.abilities.read()` |
| **Python** | `buddy.abilities.read()` |
| **Lua** | `buddy.abilities:read()` |
| **Java** | `buddy.abilities.read()` |

```ts
const { abilities } = await buddy.abilities.read();
for (const a of abilities) {
  console.log(a.name, a.usable ? "ready" : "—", a.onCooldown ? a.cooldownText : "");
}
```
```python
res = buddy.abilities.read()
for a in res["abilities"]:
    print(a["name"], "ready" if a["usable"] else "—",
          a["cooldownText"] if a["onCooldown"] else "")
```
```lua
local res = buddy.abilities:read()
for _, a in ipairs(res.abilities) do
    print(a.name, a.usable and "ready" or "—", a.onCooldown and a.cooldownText or "")
end
```
```java
AbilitiesReadResult res = buddy.abilities.read();
for (AbilitySlot a : res.getAbilities()) {
    System.out.println(a.getName() + " " + (a.getUsable() ? "ready" : "—"));
}
```

---

## 7. Scene

Classified per-frame view of the world: the player, NPCs, scenery, floor tiles, water, particles,
plus the camera's view-projection matrix. Use `getScene()` for the whole snapshot, or the
per-kind helpers for just one list.

### The scene object

`getScene()` returns a **SceneSnapshot**:

```jsonc
{
  "timestamp": 0,
  "player":  { "world": {"x","y","z"}, "tile": {"x","z"}, "chunk": {"x","z"}, "floor": 0 }, // or null
  "npcs":     [ /* Entity[] */ ],
  "scenery":  [ /* Entity[] */ ],
  "floor":    [ /* Entity[] */ ],
  "water":    [ /* Entity[] */ ],
  "particles":[ /* Entity[] */ ],
  "other":    [ /* Entity[] */ ],
  "viewProj": [ /* 16 numbers, column-major */ ]   // or null
}
```

Each entry in the per-kind lists is an **Entity**:

```jsonc
{
  "id": "...",            // stable-ish identity, hashed from (meshId, shaderId)
  "drawIndex": 0,         // unique draw within the frame
  "kind": "npc",          // "npc" | "scenery" | "water" | "floor" | "particles" | "player" | "ui" | "unknown"
  "world": { "x": 0, "y": 0, "z": 0 },
  "tile":  { "x": 0, "z": 0 },
  "chunk": { "x": 0, "z": 0 },
  "floor": 0,
  "meshId": 0,
  "shaderId": 0,
  "vertexCount": 0,
  "tag": "k3p9z",         // short stable per-model fingerprint (match on this, not vertexCount)
  "screen": { "x": 0, "y": 0, "depth": 0 }   // normalized [0,1]; null when behind camera
}
```

`screen.x/y` are normalized `[0,1]` (0,0 = bottom-left, matching `gl_FragCoord`); multiply by your
viewport size to get pixels. To project any world point yourself:
`clip = viewProj · [x,y,z,1]; screenXY = clip.xy / clip.w · 0.5 + 0.5`.

### Methods

| capability | params | returns |
|---|---|---|
| whole snapshot | — | `SceneSnapshot` |
| player only | — | the player object **or** `null` |
| npcs | `radius? = 15`, `floor? = 0` | `Entity[]` |
| scenery | `radius? = 15`, `floor? = 0` | `Entity[]` |
| water | `radius? = 15`, `floor? = 0` | `Entity[]` |
| floor tiles | `radius? = 15`, `floor? = 0` | `Entity[]` |
| entity at tile | `x`, `z`, `floor? = 0` | `Entity[]` on that tile (e.g. `[]`) |

| | snapshot | player | npcs | scenery | water | floor tiles | entity at |
|---|---|---|---|---|---|---|---|
| **TS** | `getScene()` | `getScenePlayer()` | `getNpcs(r,floor)` | `getScenery(r,floor)` | `getWater(r,floor)` | `getFloorTiles(r,floor)` | `getEntityAt(x,z,floor)` |
| **Python** | `get_scene()` | `get_scene_player()` | `get_npcs(r,floor)` | `get_scenery(r,floor)` | `get_water(r,floor)` | `get_floor_tiles(r,floor)` | `get_entity_at(x,z,floor)` |
| **Lua** | `buddy:get_scene()` | `buddy:get_scene_player()` | `buddy:get_npcs(r,floor)` | `buddy:get_scenery(r,floor)` | `buddy:get_water(r,floor)` | `buddy:get_floor_tiles(r,floor)` | `buddy:get_entity_at(x,z,floor)` |
| **Java** | `getScene()` | `getScenePlayer()` | `getNpcs(r,floor)` | `getScenery(r,floor)` | `getWater(r,floor)` | `getFloorTiles(r,floor)` | `getEntityAt(x,z,floor)` |

```ts
const scene = await buddy.getScene();              // { player, npcs, scenery, ... }
for (const npc of scene.npcs) console.log(npc.tag, npc.tile);

const nearby = await buddy.getNpcs(15, 0);          // Entity[]
const here = await buddy.getEntityAt(3200, 3200, 0); // Entity[] on that tile (often [])
```
```python
scene = buddy.get_scene()                           # { player, npcs, scenery, ... }
for npc in scene["npcs"]:
    print(npc["tag"], npc["tile"])

nearby = buddy.get_npcs(15, 0)                       # Entity[]
here = buddy.get_entity_at(3200, 3200, 0)           # Entity[]
```
```lua
local scene = buddy:get_scene()                     -- { player, npcs, scenery, ... }
for _, npc in ipairs(scene.npcs or {}) do
    print(npc.tag, npc.tile.x, npc.tile.z)
end

local nearby = buddy:get_npcs(15, 0)                -- Entity[]
```
```java
SceneSnapshot scene = buddy.getScene();             // player / npcs / scenery / ...
JsonNode npcs = buddy.getNpcs(15, 0);               // Entity[] as JSON
JsonNode here = buddy.getEntityAt(3200, 3200, 0);   // Entity[]
```

---

## 8. Drawing in the world

Draw shapes and text anchored to a 3D tile, a world point, the screen, or an entity. Submit one
item with `drawShape(shape)`, replace the entire overlay scene with `drawScene([...])`, list what's
drawn, remove one by id, or clear a group.

### The Shape object

A shape is a plain object. At minimum you need `anchor`, `primitive`, `vertices`, and `color`.

| field | type | meaning |
|---|---|---|
| `anchor` | object | where the shape lives — see below |
| `primitive` | string | `"triangles"` · `"triangle_strip"` · `"triangle_fan"` · `"lines"` · `"line_strip"` · `"line_loop"` · `"points"` |
| `vertices` | `number[]` | flat `x,y,z` triplets (length a multiple of 3), **local to the anchor** |
| `color` | string \| array | `"#rrggbb"` / `"#rrggbbaa"`, **or** a per-vertex array `[[r,g,b,a],...]` for a gradient |
| `thickness` | `number` | line width in **pixels** (for line primitives). Default 1 |
| `pointSize` | `number` | point size in pixels (for `points`). Default 4 |
| `occludedByWorld` | `bool` | `true` = world geometry can hide it; `false` (default) = always on top |
| `group` | `string` | grouping tag; `clearShapes(group)` removes all shapes in a group |
| `id` | `string` | optional; auto-minted on submit if omitted |

**Anchor** (`anchor.mode` selects one):

```jsonc
{ "mode": "screen", "screen": { "x": 0, "y": 0 } }          // screen pixels
{ "mode": "world",  "world":  { "x": 0, "y": 0, "z": 0 } }  // engine units
{ "mode": "tile",   "tile":   { "x": 0, "z": 0, "floor": 0 } } // tile coords (+ optional liftY)
{ "mode": "entity", "entityId": "...", "offset": { "x":0,"y":0,"z":0 } } // follow an entity
```

### `drawShape(shape)` — submit one

**Returns** — `{ "id": "...", "size": 1 }` (`id` of the added shape, `size` = total shapes now).

**Example A — screen-space outlined rectangle (`line_loop`):**

```ts
await buddy.drawShape({
  anchor: { mode: "screen", screen: { x: 40, y: 40 } },
  primitive: "line_loop",
  vertices: [0,0,0,  200,0,0,  200,80,0,  0,80,0],   // 4 corners, local to (40,40)
  color: "#00ff00",
  thickness: 2,
});
```

**Example B — a filled triangle (`triangles`) with a per-vertex gradient:**

```ts
await buddy.drawShape({
  anchor: { mode: "screen", screen: { x: 300, y: 120 } },
  primitive: "triangles",
  vertices: [0,0,0,  120,0,0,  60,100,0],
  color: [[255,0,0,255], [0,255,0,255], [0,0,255,255]],  // one color per vertex
});
```

**Example C — a tile-anchored ground highlight (`line_loop`, occluded by world):**

```ts
await buddy.drawShape({
  anchor: { mode: "tile", tile: { x: 3200, z: 3200, floor: 0 } },
  primitive: "line_loop",
  vertices: [-0.5,0,-0.5,  0.5,0,-0.5,  0.5,0,0.5,  -0.5,0,0.5],  // 1-tile ring
  color: "#00ff00",
  thickness: 2,
  occludedByWorld: true,
});
```

### `drawScene(shapes)` — replace the whole overlay scene

Pass an array of shapes; it **replaces** everything currently drawn.

**Returns** — `{ "size": <count> }`.

```ts
await buddy.drawScene([
  { anchor: { mode: "tile", tile: { x: 3200, z: 3200, floor: 0 } },
    primitive: "line_loop",
    vertices: [-0.5,0,-0.5,  0.5,0,-0.5,  0.5,0,0.5,  -0.5,0,0.5],
    color: "#00ff00", thickness: 2, occludedByWorld: true },
]);
```

### `listShapes()` / `removeShape(id)` / `clearShapes(group?)`

| capability | params | returns |
|---|---|---|
| list | — | `{ "shapes": [ ... ], "size": <count> }` |
| remove one | `id: string` | removes that shape |
| clear | `group? : string` | `{ "cleared": true, "group": <string\|null>, "size": <count> }` |

`clearShapes()` with no argument clears everything; pass a `group` to clear just that group.

### Names in all four languages

| capability | TS | Python | Lua | Java |
|---|---|---|---|---|
| draw one | `drawShape(shape)` | `draw_shape(shape)` | `buddy:draw_shape(shape)` | `drawShape(shape)` |
| draw scene | `drawScene(shapes)` | `draw_scene(shapes)` | `buddy:draw_scene(shapes)` | `drawScene(shapes)` |
| list | `listShapes()` | `list_shapes()` | `buddy:list_shapes()` | `listShapes()` |
| remove one | `removeShape(id)` | `remove_shape(id)` | `buddy:remove_shape(id)` | `removeShape(id)` |
| clear | `clearShapes(group?)` | `clear_shapes(group)` | `buddy:clear_shapes(group)` | `clearShapes(group)` |

```python
buddy.draw_shape({
    "anchor": {"mode": "tile", "tile": {"x": 3200, "z": 3200, "floor": 0}},
    "primitive": "line_loop",
    "vertices": [-0.5,0,-0.5, 0.5,0,-0.5, 0.5,0,0.5, -0.5,0,0.5],
    "color": "#00ff00", "thickness": 2, "occludedByWorld": True,
})
buddy.clear_shapes()
```
```lua
buddy:draw_shape({
    anchor = { mode = "tile", tile = { x = 3200, z = 3200, floor = 0 } },
    primitive = "line_loop",
    vertices = { -0.5,0,-0.5, 0.5,0,-0.5, 0.5,0,0.5, -0.5,0,0.5 },
    color = "#00ff00", thickness = 2, occludedByWorld = true,
})
buddy:clear_shapes()
```
```java
Map<String,Object> tile = Map.of("mode","tile",
    "tile", Map.of("x",3200,"z",3200,"floor",0));
Map<String,Object> ring = Map.of(
    "anchor", tile, "primitive", "line_loop",
    "vertices", List.of(-0.5,0,-0.5, 0.5,0,-0.5, 0.5,0,0.5, -0.5,0,0.5),
    "color", "#00ff00", "thickness", 2, "occludedByWorld", true);
buddy.drawShape(ring);
buddy.clearShapes(null);
```

> **Billboards / images.** `drawShape` / `drawScene` also accept a text **billboard**
> (`{ kind: "billboard", anchor, text, color, font?, fontSize? }`) and an **image** item
> (`{ kind: "image", anchor, width, height, ... }`). Same anchors as a Shape.

---

## 9. UI overlay

Author your HUD as **HTML + CSS** and hand it to `ui.html(html, css)`. The server compiles it into
the same widget engine the SDK renders, draws it in-game, and auto-scales it for the display. Your
app owns the state: poll `ui.events()` for clicks, update your state, and call `ui.html(...)` again
to re-render — **each call replaces the current UI**.

### Methods

| method | params | returns |
|---|---|---|
| `ui.html(html, css?)` | `html: string`, `css?: string` | `{ "ok": true, "size": <draw-item count> }` |
| `ui.render(tree)` | `tree: { type, props, children:[...] }` | `{ "ok": true, "size": <count> }` |
| `ui.clear()` | — | `{ "cleared": true }` |
| `ui.events()` | — | `{ "events": [ { type, id, x, y } ] }` (empty when no interaction) |
| `ui.scaling(opts)` | see below | `{ "ok": true }` |

**`ui.events()`** drains queued interaction events. Each event:

```jsonc
{ "type": "click",     // | "close" | "minimize"
  "id": "start",        // the widget's id (string | null)
  "x": 0, "y": 0 }
```

**`ui.scaling(opts)`** tunes how big the UI grows on hi-DPI / 4K:

| key | type | default | meaning |
|---|---|---|---|
| `exponent` | `number` | `1.5` | `1` = constant physical size; `>1` = bigger on 4K |
| `scale` | `number` | — | explicit multiplier (overrides the exponent curve) |
| `baseHeight` | `number` | — | reference window height the scaling is normalised against |

(Python uses `base_height`; Lua uses `base_height`; both map to the server's `baseHeight`.)

| | html | render | clear | events | scaling |
|---|---|---|---|---|---|
| **TS** | `buddy.ui.html(html, css?)` | `buddy.ui.render(tree)` | `buddy.ui.clear()` | `buddy.ui.events()` | `buddy.ui.scaling(opts)` |
| **Python** | `buddy.ui.html(html, css="")` | `buddy.ui.render(tree)` | `buddy.ui.clear()` | `buddy.ui.events()` | `buddy.ui.scaling(exponent=, scale=, base_height=)` |
| **Lua** | `buddy.ui:html(html, css?)` | `buddy.ui:render(tree)` | `buddy.ui:clear()` | `buddy.ui:events()` | `buddy.ui:scaling(opts)` |
| **Java** | `buddy.ui.html(html, css)` | `buddy.ui.render(tree)` | `buddy.ui.clear()` | `buddy.ui.events()` | `buddy.ui.scaling(exponent, scale, baseHeight)` |

### Supported HTML tags

The HTML parser maps these tags to widgets (unknown tags become a column container, so a page never
hard-fails):

| tag(s) | becomes |
|---|---|
| `panel` | panel (rounded container / HUD root) |
| `div`, `section`, `article`, `main`, `column`, `col`, `vstack` | column (vertical flex) |
| `row`, `header`, `footer`, `nav`, `hstack` | row (horizontal flex) |
| `span`, `p`, `label`, `text`, `h1`–`h4`, `small`, `b`, `strong` | text label (`h1`–`h4` set a larger font size) |
| `button`, `a`, `btn` | button |
| `progress`, `gauge`, `meter`, `bar` | gauge / stat bar (`value`, `max`, `min`, `vertical` attrs) |
| `img`, `image` | image (`src` attr) |
| `hr`, `divider` | divider |
| `badge` | badge |

**Special attributes:** `id` (so a widget's clicks surface in `ui.events()`); `draggable` on a
panel lets the user move it (the camera stays put); `consume` makes a widget block clicks from
reaching the game; `anchor` (e.g. `top-right`) pins a root; `variant` on a button
(`primary`/`secondary`/`good`/`danger`/`ghost`/`plain`); `icon` (`close`/`minimize`/`maximize`);
`action="minimize"` / `action="close"` are **built-in** behaviors (an icon button defaults its
icon to its action).

### Supported CSS

Selectors: **tag**, **`.class`**, **`#id`**, **descendant** (`.row button`), and inline
`style="..."` (inline wins). Properties that map:

| CSS | effect |
|---|---|
| `background` (incl. `linear-gradient(...)`), `background-color`, `background-image: url(...)` | panel/element background (gradient → two-stop) |
| `color` | text colour |
| `fill` | gauge fill colour |
| `border` (`1px solid #rrggbb`), `border-color`, `border-width` | outline |
| `border-radius` | corner radius |
| `box-shadow` | drop shadow |
| `padding`, `margin`, `gap` | spacing |
| `display: flex` (+ `flex-direction`) | flips a container to a flex row/column |
| `justify-content` | main-axis distribution (`flex-start`/`center`/`flex-end`/`space-between`/…) |
| `align-items` | cross-axis alignment (`flex-start`/`center`/`flex-end`/`stretch`) |
| `width`, `height` | size (number px, `auto`, or `%` → flex) |
| `flex` | `flex: 1` makes the element grow |
| `font-size`, `font-family` | text font |
| `opacity` | element opacity |

Unknown CSS is ignored. Buttons fire clicks carrying their `id` through `ui.events()`.

### The stall-free HUD loop

Poll the cheap `getPlayer()`, drain `ui.events()`, and only re-POST the UI when its markup actually
changes. (`ui.render(tree)` exists if you'd rather build a raw widget tree
`{ type, props, children:[...] }` instead of HTML.)

```ts
// TypeScript — npx ts-node hud.ts
import { RS3Buddy, RS3BuddyConnectionError, type Position } from "@rs3buddy/client";

const CSS = [
  "panel{background:#241d12;border:1px solid #b0904a;border-radius:10px;padding:12px;gap:8px;width:220px;align-items:stretch}",
  "header{display:flex;justify-content:space-between;align-items:center}",
  ".title{color:#f5c54a;font-size:16px}",
  ".row{display:flex;justify-content:space-between;gap:12px}",
  ".k{color:#c9bfa6}.v{color:#fff}.accent{color:#8fd1ff}",
  ".btns{display:flex;gap:8px}.btns button{flex:1}",
  ".good{background:#3f7a3f}.danger{background:#b03b30}",
].join("\n");
const sleep = (ms: number) => new Promise<void>((r) => setTimeout(r, ms));

async function main(): Promise<void> {
  const buddy = RS3Buddy.connect({ clientName: "hud-example" });
  let running = false, ticks = 0, tileX = 0, tileZ = 0, located = false, lastHtml = "";

  for (;;) {
    let pos: Position | null = null;
    try { pos = await buddy.getPlayer(); }                 // cheap passive read — safe to poll
    catch (e) {
      if (e instanceof RS3BuddyConnectionError) { console.error("launcher not running?"); return; }
    }
    if (pos?.tileX !== undefined) { tileX = pos.tileX; tileZ = pos.tileZ; located = true; }

    const resp = await buddy.ui.events().catch(() => null);   // drain clicks
    for (const e of resp?.events ?? []) {
      if (e.id === "close") { await buddy.ui.clear(); return; }
      if (e.id === "start") running = !running;
    }
    if (running) ticks++;

    const tile = located ? `${tileX}, ${tileZ}` : "—";
    const html =
      "<panel anchor='top-right' draggable consume>" +
        "<header><span class='title'>Tracker</span>" +
          "<button id='close' icon='close' variant='plain'></button></header><hr/>" +
        `<div class='row'><span class='k'>Tile</span><span class='accent'>${tile}</span></div>` +
        `<div class='row'><span class='k'>Ticks</span><span class='v'>${ticks}</span></div><hr/>` +
        "<div class='btns'>" +
          `<button id='start' class='${running ? "" : "good"}'>${running ? "Pause" : "Start"}</button>` +
          "<button id='close' class='danger'>Exit</button></div>" +
      "</panel>";

    if (html !== lastHtml) { await buddy.ui.html(html, CSS); lastHtml = html; }  // re-POST only on change
    await sleep(300);
  }
}
void main();
```
```python
# Python — python hud.py
import time
from rs3buddy import RS3Buddy, RS3BuddyConnectionError

CSS = "\n".join([
    "panel{background:#241d12;border:1px solid #b0904a;border-radius:10px;padding:12px;gap:8px;width:220px;align-items:stretch}",
    "header{display:flex;justify-content:space-between;align-items:center}",
    ".title{color:#f5c54a;font-size:16px}",
    ".row{display:flex;justify-content:space-between;gap:12px}",
    ".k{color:#c9bfa6}.v{color:#fff}.accent{color:#8fd1ff}",
    ".btns{display:flex;gap:8px}.btns button{flex:1}",
    ".good{background:#3f7a3f}.danger{background:#b03b30}",
])

def main() -> None:
    buddy = RS3Buddy.connect(client_name="hud-example")
    running, ticks, tile_x, tile_z, located, last_html = False, 0, 0, 0, False, ""

    while True:
        try:
            pos = buddy.get_player()                       # cheap passive read — safe to poll
        except RS3BuddyConnectionError:
            print("launcher not running?"); return
        if pos and pos.get("tileX") is not None:
            tile_x, tile_z, located = int(pos["tileX"]), int(pos["tileZ"]), True

        resp = buddy.ui.events()                           # drain clicks
        for e in (resp.get("events") if resp else None) or []:
            if e.get("id") == "close":
                buddy.ui.clear(); return
            if e.get("id") == "start":
                running = not running
        if running:
            ticks += 1

        tile = f"{tile_x}, {tile_z}" if located else "—"
        html = (
            "<panel anchor='top-right' draggable consume>"
            "<header><span class='title'>Tracker</span>"
            "<button id='close' icon='close' variant='plain'></button></header><hr/>"
            f"<div class='row'><span class='k'>Tile</span><span class='accent'>{tile}</span></div>"
            f"<div class='row'><span class='k'>Ticks</span><span class='v'>{ticks}</span></div><hr/>"
            "<div class='btns'>"
            f"<button id='start' class='{'' if running else 'good'}'>{'Pause' if running else 'Start'}</button>"
            "<button id='close' class='danger'>Exit</button></div>"
            "</panel>"
        )
        if html != last_html:                              # re-POST only on change
            buddy.ui.html(html, CSS); last_html = html
        time.sleep(0.3)

if __name__ == "__main__":
    main()
```
```lua
-- Lua / Luau — lune run hud.luau
local rs3buddy = require("rs3buddy")
local buddy = rs3buddy.connect({ client_name = "hud-example" })

local CSS = table.concat({
    "panel{background:#241d12;border:1px solid #b0904a;border-radius:10px;padding:12px;gap:8px;width:220px;align-items:stretch}",
    "header{display:flex;justify-content:space-between;align-items:center}",
    ".title{color:#f5c54a;font-size:16px}",
    ".row{display:flex;justify-content:space-between;gap:12px}",
    ".k{color:#c9bfa6}.v{color:#fff}.accent{color:#8fd1ff}",
    ".btns{display:flex;gap:8px}.btns button{flex:1}",
    ".good{background:#3f7a3f}.danger{background:#b03b30}",
}, "\n")

local running, ticks, tileX, tileZ, located, lastHtml = false, 0, 0, 0, false, ""

while true do
    local ok, pos = pcall(function() return buddy:get_player() end)  -- cheap passive read
    if ok and type(pos) == "table" and pos.tileX ~= nil then
        tileX, tileZ, located = pos.tileX, pos.tileZ, true
    end

    local resp = buddy.ui:events()                         -- drain clicks
    local events = if type(resp) == "table" then resp.events else nil
    if type(events) == "table" then
        for _, e in ipairs(events) do
            local id = if type(e) == "table" then e.id else nil
            if id == "close" then
                buddy.ui:clear(); return
            elseif id == "start" then
                running = not running
            end
        end
    end
    if running then ticks += 1 end

    local tile = if located then (tostring(tileX) .. ", " .. tostring(tileZ)) else "—"
    local startClass = if running then "" else "good"
    local startLabel = if running then "Pause" else "Start"
    local html = "<panel anchor='top-right' draggable consume>"
        .. "<header><span class='title'>Tracker</span>"
        .. "<button id='close' icon='close' variant='plain'></button></header><hr/>"
        .. "<div class='row'><span class='k'>Tile</span><span class='accent'>" .. tile .. "</span></div>"
        .. "<div class='row'><span class='k'>Ticks</span><span class='v'>" .. tostring(ticks) .. "</span></div><hr/>"
        .. "<div class='btns'>"
        .. "<button id='start' class='" .. startClass .. "'>" .. startLabel .. "</button>"
        .. "<button id='close' class='danger'>Exit</button></div>"
        .. "</panel>"

    if html ~= lastHtml then                               -- re-POST only on change
        buddy.ui:html(html, CSS); lastHtml = html
    end
    if (_G :: any).task then (_G :: any).task.wait(0.3) end -- host timer
end
```
```java
// Java — compile + run against the client jar (+ Jackson)
import com.rs3buddy.RS3Buddy;
import com.rs3buddy.models.Position;
import com.fasterxml.jackson.databind.JsonNode;

public final class Hud {
    private static final String CSS = String.join("\n",
        "panel{background:#241d12;border:1px solid #b0904a;border-radius:10px;padding:12px;gap:8px;width:220px;align-items:stretch}",
        "header{display:flex;justify-content:space-between;align-items:center}",
        ".title{color:#f5c54a;font-size:16px}",
        ".row{display:flex;justify-content:space-between;gap:12px}",
        ".k{color:#c9bfa6}.v{color:#fff}.accent{color:#8fd1ff}",
        ".btns{display:flex;gap:8px}.btns button{flex:1}",
        ".good{background:#3f7a3f}.danger{background:#b03b30}");

    public static void main(String[] args) throws InterruptedException {
        RS3Buddy buddy = RS3Buddy.connect("hud-example");
        boolean running = false, located = false;
        int ticks = 0, tileX = 0, tileZ = 0;
        String lastHtml = "";

        while (true) {
            Position pos = null;
            try { pos = buddy.getPlayer(); }                 // cheap passive read — safe to poll
            catch (RuntimeException ignore) { /* not tracked yet */ }
            if (pos != null && pos.getTileX() != null) {
                tileX = pos.getTileX().intValue();
                tileZ = pos.getTileZ().intValue();
                located = true;
            }

            JsonNode resp = buddy.ui.events();               // drain clicks
            JsonNode events = (resp == null) ? null : resp.get("events");
            if (events != null) {
                for (JsonNode e : events) {
                    switch (e.path("id").asText("")) {
                        case "close": buddy.ui.clear(); return;
                        case "start": running = !running; break;
                        default: break;
                    }
                }
            }
            if (running) ticks++;

            String tile = located ? (tileX + ", " + tileZ) : "—";
            String html = "<panel anchor='top-right' draggable consume>"
                + "<header><span class='title'>Tracker</span>"
                + "<button id='close' icon='close' variant='plain'></button></header><hr/>"
                + "<div class='row'><span class='k'>Tile</span><span class='accent'>" + tile + "</span></div>"
                + "<div class='row'><span class='k'>Ticks</span><span class='v'>" + ticks + "</span></div><hr/>"
                + "<div class='btns'>"
                + "<button id='start' class='" + (running ? "" : "good") + "'>" + (running ? "Pause" : "Start") + "</button>"
                + "<button id='close' class='danger'>Exit</button></div>"
                + "</panel>";

            if (!html.equals(lastHtml)) {                   // re-POST only on change
                buddy.ui.html(html, CSS); lastHtml = html;
            }
            Thread.sleep(300);
        }
    }
}
```

---

## 10. Sound

Play developer-supplied audio (wav / mp3 / ogg) host-side through the desktop app. Provide
**either** a host-side `file` path (or a `file:` / `data:` / `http(s):` URL) **or** inline base64
`bytes` with a `mime` type; `volume` is an optional `0..1` gain. Requires the desktop audio host
(no-op headless).

### `sound.play(opts)`

| key | type | default | meaning |
|---|---|---|---|
| `file` | `string` | — | path or `file:`/`data:`/`http(s):` URL (use instead of `bytes`) |
| `bytes` | `string` | — | base64 audio, played inline (pair with `mime`) |
| `mime` | `string` | `audio/wav` | MIME type for inline `bytes` |
| `volume` | `number` | host default | `0..1` gain |

**Returns** — `{ "ok": true }` (`{ "ok": false, "error": ... }` when no audio host is available).

| | name |
|---|---|
| **TS** | `buddy.sound.play(opts)` |
| **Python** | `buddy.sound.play(file=, bytes=, mime=, volume=)` |
| **Lua** | `buddy.sound:play(opts)` |
| **Java** | `buddy.sound.play(file, volume)` / `buddy.sound.playBytes(base64, mime, volume)` |

```ts
await buddy.sound.play({ file: "C:/sfx/ding.wav", volume: 0.6 });
// inline bytes: await buddy.sound.play({ bytes: "<base64>", mime: "audio/mp3" });
```
```python
buddy.sound.play(file="C:/sfx/ding.wav", volume=0.6)
# inline bytes: buddy.sound.play(bytes="<base64>", mime="audio/mp3")
```
```lua
buddy.sound:play({ file = "C:/sfx/ding.wav", volume = 0.6 })
-- inline bytes: buddy.sound:play({ bytes = "<base64>", mime = "audio/mp3" })
```
```java
buddy.sound.play("C:/sfx/ding.wav", 0.6);            // volume may be null for host default
// inline bytes: buddy.sound.playBytes("<base64>", "audio/mp3", null);
```

---

## 11. Fonts

Register and list **render / DRAW fonts** — the fonts used when drawing text billboards. These are
distinct from the trained-recognition registry (see note below).

### `listFonts()`

**Returns** — an alias → path map of registered render fonts:

```jsonc
{ "rs3": "C:/fonts/MyFont.ttf" }
```

### `registerFont(name, path)` / `unregisterFont(name)`

| capability | params |
|---|---|
| register | `name: string`, `path: string` (a `.ttf` / `.woff2` on the host) |
| unregister | `name: string` |

| | list | register | unregister |
|---|---|---|---|
| **TS** | `listFonts()` | `registerFont(name, path)` | `unregisterFont(name)` |
| **Python** | `list_fonts()` | `register_font(name, path)` | `unregister_font(name)` |
| **Lua** | `buddy:list_fonts()` | `buddy:register_font(name, path)` | `buddy:unregister_font(name)` |
| **Java** | `listFonts()` | `registerFont(name, path)` | `unregisterFont(name)` |

```ts
await buddy.registerFont("myfont", "C:/fonts/MyFont.ttf");
const fonts = await buddy.listFonts();               // { myfont: "C:/fonts/MyFont.ttf", ... }
```
```python
buddy.register_font("myfont", "C:/fonts/MyFont.ttf")
fonts = buddy.list_fonts()
```

> **Recognition fonts (different list).** There is also a *trained-recognition* font list that
> returns `{ "fonts": [ { name, glyphCount, lineHeight } ] }`. It describes the glyph sets the
> chat / bars / abilities readers recognise, not the render fonts above.

---

## 12. Sprites

Manage **trained sprites** — named sprites the recognition system matches.

### `listTrainedSprites()`

**Returns** — an array of trained sprites:

```jsonc
[
  { "name": "...", "hash": 0, "colorHash": 0, "textureId": 0,
    "x": 0, "y": 0, "width": 0, "height": 0, "createdAt": 0 }
]
```

### `saveTrainedSprite(req)` / `deleteTrainedSprite(name)`

| capability | params |
|---|---|
| save | `req` — a trained-sprite record (name + hash(es) + rect) |
| delete | `name: string` |

| | list | save | delete |
|---|---|---|---|
| **TS** | `listTrainedSprites()` | `saveTrainedSprite(req)` | `deleteTrainedSprite(name)` |
| **Python** | `list_trained_sprites()` | `save_trained_sprite(req)` | `delete_trained_sprite(name)` |
| **Lua** | `buddy:list_trained_sprites()` | `buddy:save_trained_sprite(req)` | `buddy:delete_trained_sprite(name)` |
| **Java** | `listTrainedSprites()` | `saveTrainedSprite(req)` | `deleteTrainedSprite(name)` |

```ts
const sprites = await buddy.listTrainedSprites();    // [{ name, hash, colorHash, ... }]
```
```python
sprites = buddy.list_trained_sprites()
```

---

## 13. Atlas / recognition (advanced)

Low-level texture-atlas + sprite-recognition surface. Most apps never need this — use the high-level
readers (chat / bars / abilities) instead. Documented for completeness; one representative snippet
plus the name mapping.

### Methods

| capability | params | returns / purpose |
|---|---|---|
| atlas sprites | — | `{ "sprites": [ { hash, textureId, x, y, w, h, observationCount, lastSeenFrame } ] }` |
| atlas sync | — | refresh the atlas record set from the live frame |
| atlas lookup | `hash: number` | `{ "hit": bool, "record"?: {...} }` — find a sprite by hash |
| train quad | `req` | train a recognition entry from a drawn quad |
| recognize quads | `quads: array` | recognise an array of quads (returns matches) |
| import sprite-hash | `req` | import an externally-computed sprite hash record |

| capability | TS | Python | Lua | Java |
|---|---|---|---|---|
| atlas sprites | `atlasSprites()` | `atlas_sprites()` | `buddy:atlas_sprites()` | `atlasSprites()` |
| atlas sync | `atlasSync()` | `atlas_sync()` | `buddy:atlas_sync()` | `atlasSync()` |
| atlas lookup | `atlasLookup(hash)` | `atlas_lookup(hash)` | `buddy:atlas_lookup(hash)` | `atlasLookup(hash)` |
| train quad | `trainQuad(req)` | `train_quad(req)` | `buddy:train_quad(req)` | `trainQuad(req)` |
| recognize quads | `recognizeQuads(quads)` | `recognize_quads(quads)` | `buddy:recognize_quads(quads)` | `recognizeQuads(quads)` |
| import sprite-hash | `importSpriteHash(req)` | `import_sprite_hash(req)` | `buddy:import_sprite_hash(req)` | `importSpriteHash(req)` |

```ts
const atlas = await buddy.atlasSprites();            // { sprites: [...] }
const found = await buddy.atlasLookup(123456);        // { hit, record? }
```

---

## 14. Shaders & FX (advanced)

Inspect loaded GLSL shaders, and register fullscreen **post-FX** passes or replace RS3's own
**game-shader FX**. Advanced — keep concise; the FX list endpoints return arrays of passes.

### `getShaders()`

**Returns** — an array of shader programs:

```jsonc
[
  { "id": 0,
    "vertexSource": "/* GLSL */",
    "fragmentSource": "/* GLSL */",
    "uniforms": [ { "name": "...", "type": "...", "location": 0 } ] }
]
```

### Post-FX (fullscreen passes) & game-shader FX

A **post-FX pass** is `{ id, fragmentSource, uniforms?, order?, enabled? }` (samples the previous
result via `uScene`; builtins `uScene`/`uResolution`/`uTime`/`uFrame`). A **shader FX** is
`{ id, matchType?, matchHash?, fragmentSource?, vertexSource?, enabled? }` (replaces a stock shader,
matched by classified type or exact source hash).

| capability | params | returns |
|---|---|---|
| get shaders | — | `ShaderInfo[]` (above) |
| add post-FX | `pass` | `{ "id": "..." }` |
| list post-FX | — | `[]` (array of passes, ascending `order`) |
| remove post-FX | `id` | `{ "removed": bool }` |
| enable post-FX | `id`, `enabled` | `{ "ok": bool }` |
| add shader FX | `o` | `{ "id": "..." }` |
| list shader FX | — | `[]` (array of passes) |
| remove shader FX | `id` | `{ "removed": bool }` |
| enable shader FX | `id`, `enabled` | `{ "ok": bool }` |

| capability | TS | Python | Lua | Java |
|---|---|---|---|---|
| get shaders | `getShaders()` | `get_shaders()` | `buddy:get_shaders()` | `getShaders()` |
| add post-FX | `addPostFx(pass)` | `add_post_fx(pass)` | `buddy:add_post_fx(pass)` | `addPostFx(pass)` |
| list post-FX | `listPostFx()` | `list_post_fx()` | `buddy:list_post_fx()` | `listPostFx()` |
| remove post-FX | `removePostFx(id)` | `remove_post_fx(id)` | `buddy:remove_post_fx(id)` | `removePostFx(id)` |
| enable post-FX | `setPostFxEnabled(id, on)` | `set_post_fx_enabled(id, on)` | `buddy:set_post_fx_enabled(id, on)` | `setPostFxEnabled(id, on)` |
| add shader FX | `addShaderFx(o)` | `add_shader_fx(o)` | `buddy:add_shader_fx(o)` | `addShaderFx(o)` |
| list shader FX | `listShaderFx()` | `list_shader_fx()` | `buddy:list_shader_fx()` | `listShaderFx()` |
| remove shader FX | `removeShaderFx(id)` | `remove_shader_fx(id)` | `buddy:remove_shader_fx(id)` | `removeShaderFx(id)` |
| enable shader FX | `setShaderFxEnabled(id, on)` | `set_shader_fx_enabled(id, on)` | `buddy:set_shader_fx_enabled(id, on)` | `setShaderFxEnabled(id, on)` |

> A shape's per-shape `shader` field (see [§8](#8-drawing-in-the-world)) reports compile status via a
> compile-status read shaped `{ "ok": bool, "log": "..." }`; on a compile error the shape falls back
> to the built-in shader so a typo never breaks your overlay.

```ts
const shaders = await buddy.getShaders();            // ShaderInfo[]
await buddy.addPostFx({
  id: "grayscale",
  fragmentSource: "/* sample uScene, output luminance */",
  order: 0,
});
const passes = await buddy.listPostFx();             // []  (array of passes)
```

---

## 15. Capture & textures (advanced)

Heavy GPU capture + raw texture / glyph access. **Not for polling** — a capture forces a frame
readback. Use the high-level readers for normal work. Advanced; one snippet + the name mapping.

### Representative shapes

- **`captureFrame(opts?)`** → `{ "draws": [ { index, shaderId, vertexCount, meshId, targetFbo,
  viewport:{x,y,width,height}, textures:[{slot,id,width,height}], ... } ] }`. Options:
  `includeMesh`, `includeTexturePixels`, and a `filter` (`shaderId` / `meshId` / `targetFbo` /
  `drawType`).
- **`getTextures()`** → `[ { id, width, height, format: "R8"|"0x8C4F"|..., source: "snapshot" } ]`.
- **`capturedTexturesStats()`** → `{ entryCount, totalDiskBytes, oldestTouchAgeMs, capacityBytes,
  cacheDir, refreshInFlight }`.
- **`getGlyphsOnScreen(opts)`** → `{ width, height, count, glyphs: [] }`. Options: `minW`, `minH`,
  `maxW`, `maxH`, `alphaThreshold`.

### Methods

| capability | params | TS | Python | Lua | Java |
|---|---|---|---|---|---|
| capture frame | `opts?` | `captureFrame(opts?)` | `capture_frame(...)` | `buddy:capture_frame(opts?)` | `captureFrame(...)` |
| list textures | — | `getTextures()` | `get_textures()` | `buddy:get_textures()` | `getTextures()` |
| read texture | `id, x?, y?, w?, h?` | `readTexture(id,x,y,w,h)` | `read_texture(id,x,y,w,h)` | `buddy:read_texture(id,x,y,w,h)` | `readTexture(id,x,y,w,h)` |
| screen capture | — | `screenCapture()` | `screen_capture()` | `buddy:screen_capture()` | `screenCapture()` |
| captured textures | — | `getCapturedTextures()` | `get_captured_textures()` | `buddy:get_captured_textures()` | `getCapturedTextures()` |
| refresh captured | — | `refreshCapturedTextures()` | `refresh_captured_textures()` | `buddy:refresh_captured_textures()` | `refreshCapturedTextures()` |
| captured stats | — | `capturedTexturesStats()` | `captured_textures_stats()` | `buddy:captured_textures_stats()` | `capturedTexturesStats()` |
| clear captured | — | `clearCapturedTextures()` | `clear_captured_textures()` | `buddy:clear_captured_textures()` | `clearCapturedTextures()` |
| get one captured | `id` | `getCapturedTexture(id)` | `get_captured_texture(id)` | `buddy:get_captured_texture(id)` | `getCapturedTexture(id)` |
| glyphs on screen | `opts` | `getGlyphsOnScreen(opts)` | `get_glyphs_on_screen(**opts)` | `buddy:get_glyphs_on_screen(opts)` | `getGlyphsOnScreen(opts)` |
| glyph at point | `x, y, opts?` | `glyphAtPoint(x,y,opts)` | `glyph_at_point(x,y,**opts)` | `buddy:glyph_at_point(x,y,opts)` | `glyphAtPoint(x,y,opts)` |

```ts
const frame = await buddy.captureFrame();            // { draws: [...] }  (heavy — not for polling)
const texs = await buddy.getTextures();               // [{ id, width, height, format, source }]
const glyphs = await buddy.getGlyphsOnScreen({ minW: 4, minH: 6 });  // { width, height, count, glyphs }
```

---

## 16. OCR (advanced)

General text-recognition helpers for arbitrary screen regions, separate from the structured
chat / bars / abilities readers. Advanced; document from the signatures.

| capability | params | purpose | TS | Python | Lua | Java |
|---|---|---|---|---|---|---|
| match text | `req` | recognise text matching a request spec | `matchText(req)` | `match_text(req)` | `buddy:match_text(req)` | `matchText(req)` |
| match region | `req` | recognise the text in a given region | `matchRegion(req)` | `match_region(req)` | `buddy:match_region(req)` | `matchRegion(req)` |

```ts
const out = await buddy.matchText({ /* request spec */ });
const region = await buddy.matchRegion({ /* region + options */ });
```

---

## 17. Function reference

Every capability and its method name in all four clients. Lua reader namespaces use a colon
(`buddy.ui:html(...)`), as do top-level Lua methods (`buddy:get_player()`).

| Capability | TypeScript | Python | Lua | Java |
|---|---|---|---|---|
| **Connect** | `RS3Buddy.connect()` | `RS3Buddy.connect()` | `rs3buddy.connect()` | `RS3Buddy.connect()` |
| Base URL | `buddy.baseUrl` | `buddy.base_url` | `buddy:base_url()` | `buddy.baseUrl()` |
| **Status** | `buddy.getStatus()` | `buddy.get_status()` | `buddy:get_status()` | `buddy.getStatus()` |
| Window | `buddy.getWindow()` | `buddy.get_window()` | `buddy:get_window()` | `buddy.getWindow()` |
| Heap | `buddy.getHeap()` | `buddy.get_heap()` | `buddy:get_heap()` | `buddy.getHeap()` |
| **Player** — position | `buddy.getPlayer()` | `buddy.get_player()` | `buddy:get_player()` | `buddy.getPlayer()` |
| Player — refresh | `buddy.updatePlayer()` | `buddy.update_player()` | `buddy:update_player()` | `buddy.updatePlayer()` |
| Player — name | `buddy.getPlayerName()` | `buddy.get_player_name()` | `buddy:get_player_name()` | `buddy.getPlayerName()` |
| **Chat** — read | `buddy.chat.read()` | `buddy.chat.read()` | `buddy.chat:read()` | `buddy.chat.read()` |
| **Bars** — read | `buddy.bars.read()` | `buddy.bars.read()` | `buddy.bars:read()` | `buddy.bars.read()` |
| **Abilities** — read | `buddy.abilities.read()` | `buddy.abilities.read()` | `buddy.abilities:read()` | `buddy.abilities.read()` |
| **Scene** — snapshot | `buddy.getScene()` | `buddy.get_scene()` | `buddy:get_scene()` | `buddy.getScene()` |
| Scene — player | `buddy.getScenePlayer()` | `buddy.get_scene_player()` | `buddy:get_scene_player()` | `buddy.getScenePlayer()` |
| Scene — npcs | `buddy.getNpcs(r, floor)` | `buddy.get_npcs(r, floor)` | `buddy:get_npcs(r, floor)` | `buddy.getNpcs(r, floor)` |
| Scene — scenery | `buddy.getScenery(r, floor)` | `buddy.get_scenery(r, floor)` | `buddy:get_scenery(r, floor)` | `buddy.getScenery(r, floor)` |
| Scene — water | `buddy.getWater(r, floor)` | `buddy.get_water(r, floor)` | `buddy:get_water(r, floor)` | `buddy.getWater(r, floor)` |
| Scene — floor tiles | `buddy.getFloorTiles(r, floor)` | `buddy.get_floor_tiles(r, floor)` | `buddy:get_floor_tiles(r, floor)` | `buddy.getFloorTiles(r, floor)` |
| Scene — entity at | `buddy.getEntityAt(x, z, floor)` | `buddy.get_entity_at(x, z, floor)` | `buddy:get_entity_at(x, z, floor)` | `buddy.getEntityAt(x, z, floor)` |
| **Draw** — one shape | `buddy.drawShape(shape)` | `buddy.draw_shape(shape)` | `buddy:draw_shape(shape)` | `buddy.drawShape(shape)` |
| Draw — batch / scene | `buddy.drawScene(shapes)` | `buddy.draw_scene(shapes)` | `buddy:draw_scene(shapes)` | `buddy.drawScene(shapes)` |
| Draw — list | `buddy.listShapes()` | `buddy.list_shapes()` | `buddy:list_shapes()` | `buddy.listShapes()` |
| Draw — remove one | `buddy.removeShape(id)` | `buddy.remove_shape(id)` | `buddy:remove_shape(id)` | `buddy.removeShape(id)` |
| Draw — clear | `buddy.clearShapes(group?)` | `buddy.clear_shapes(group)` | `buddy:clear_shapes(group)` | `buddy.clearShapes(group)` |
| **UI** — HTML page | `buddy.ui.html(html, css?)` | `buddy.ui.html(html, css)` | `buddy.ui:html(html, css?)` | `buddy.ui.html(html, css)` |
| UI — widget tree | `buddy.ui.render(tree)` | `buddy.ui.render(tree)` | `buddy.ui:render(tree)` | `buddy.ui.render(tree)` |
| UI — clear | `buddy.ui.clear()` | `buddy.ui.clear()` | `buddy.ui:clear()` | `buddy.ui.clear()` |
| UI — drain clicks | `buddy.ui.events()` | `buddy.ui.events()` | `buddy.ui:events()` | `buddy.ui.events()` |
| UI — display scaling | `buddy.ui.scaling(opts)` | `buddy.ui.scaling(...)` | `buddy.ui:scaling(opts)` | `buddy.ui.scaling(...)` |
| **Sound** — play | `buddy.sound.play(opts)` | `buddy.sound.play(...)` | `buddy.sound:play(opts)` | `buddy.sound.play(file, vol)` / `playBytes(...)` |
| **Fonts** — list | `buddy.listFonts()` | `buddy.list_fonts()` | `buddy:list_fonts()` | `buddy.listFonts()` |
| Fonts — register | `buddy.registerFont(name, path)` | `buddy.register_font(name, path)` | `buddy:register_font(name, path)` | `buddy.registerFont(name, path)` |
| Fonts — unregister | `buddy.unregisterFont(name)` | `buddy.unregister_font(name)` | `buddy:unregister_font(name)` | `buddy.unregisterFont(name)` |
| **Sprites** — list trained | `buddy.listTrainedSprites()` | `buddy.list_trained_sprites()` | `buddy:list_trained_sprites()` | `buddy.listTrainedSprites()` |
| Sprites — save trained | `buddy.saveTrainedSprite(req)` | `buddy.save_trained_sprite(req)` | `buddy:save_trained_sprite(req)` | `buddy.saveTrainedSprite(req)` |
| Sprites — delete trained | `buddy.deleteTrainedSprite(name)` | `buddy.delete_trained_sprite(name)` | `buddy:delete_trained_sprite(name)` | `buddy.deleteTrainedSprite(name)` |
| **Atlas** — sprites | `buddy.atlasSprites()` | `buddy.atlas_sprites()` | `buddy:atlas_sprites()` | `buddy.atlasSprites()` |
| Atlas — sync | `buddy.atlasSync()` | `buddy.atlas_sync()` | `buddy:atlas_sync()` | `buddy.atlasSync()` |
| Atlas — lookup | `buddy.atlasLookup(hash)` | `buddy.atlas_lookup(hash)` | `buddy:atlas_lookup(hash)` | `buddy.atlasLookup(hash)` |
| Atlas — train quad | `buddy.trainQuad(req)` | `buddy.train_quad(req)` | `buddy:train_quad(req)` | `buddy.trainQuad(req)` |
| Atlas — recognize quads | `buddy.recognizeQuads(quads)` | `buddy.recognize_quads(quads)` | `buddy:recognize_quads(quads)` | `buddy.recognizeQuads(quads)` |
| Atlas — import sprite-hash | `buddy.importSpriteHash(req)` | `buddy.import_sprite_hash(req)` | `buddy:import_sprite_hash(req)` | `buddy.importSpriteHash(req)` |
| **Shaders** — list | `buddy.getShaders()` | `buddy.get_shaders()` | `buddy:get_shaders()` | `buddy.getShaders()` |
| Post-FX — add | `buddy.addPostFx(pass)` | `buddy.add_post_fx(pass)` | `buddy:add_post_fx(pass)` | `buddy.addPostFx(pass)` |
| Post-FX — list | `buddy.listPostFx()` | `buddy.list_post_fx()` | `buddy:list_post_fx()` | `buddy.listPostFx()` |
| Post-FX — remove | `buddy.removePostFx(id)` | `buddy.remove_post_fx(id)` | `buddy:remove_post_fx(id)` | `buddy.removePostFx(id)` |
| Post-FX — enable | `buddy.setPostFxEnabled(id, on)` | `buddy.set_post_fx_enabled(id, on)` | `buddy:set_post_fx_enabled(id, on)` | `buddy.setPostFxEnabled(id, on)` |
| Shader-FX — add | `buddy.addShaderFx(o)` | `buddy.add_shader_fx(o)` | `buddy:add_shader_fx(o)` | `buddy.addShaderFx(o)` |
| Shader-FX — list | `buddy.listShaderFx()` | `buddy.list_shader_fx()` | `buddy:list_shader_fx()` | `buddy.listShaderFx()` |
| Shader-FX — remove | `buddy.removeShaderFx(id)` | `buddy.remove_shader_fx(id)` | `buddy:remove_shader_fx(id)` | `buddy.removeShaderFx(id)` |
| Shader-FX — enable | `buddy.setShaderFxEnabled(id, on)` | `buddy.set_shader_fx_enabled(id, on)` | `buddy:set_shader_fx_enabled(id, on)` | `buddy.setShaderFxEnabled(id, on)` |
| **Capture** — frame | `buddy.captureFrame(opts?)` | `buddy.capture_frame(...)` | `buddy:capture_frame(opts?)` | `buddy.captureFrame(...)` |
| Textures — list | `buddy.getTextures()` | `buddy.get_textures()` | `buddy:get_textures()` | `buddy.getTextures()` |
| Textures — read | `buddy.readTexture(id,x,y,w,h)` | `buddy.read_texture(id,x,y,w,h)` | `buddy:read_texture(id,x,y,w,h)` | `buddy.readTexture(id,x,y,w,h)` |
| Screen — capture | `buddy.screenCapture()` | `buddy.screen_capture()` | `buddy:screen_capture()` | `buddy.screenCapture()` |
| Captured textures — list | `buddy.getCapturedTextures()` | `buddy.get_captured_textures()` | `buddy:get_captured_textures()` | `buddy.getCapturedTextures()` |
| Captured textures — refresh | `buddy.refreshCapturedTextures()` | `buddy.refresh_captured_textures()` | `buddy:refresh_captured_textures()` | `buddy.refreshCapturedTextures()` |
| Captured textures — stats | `buddy.capturedTexturesStats()` | `buddy.captured_textures_stats()` | `buddy:captured_textures_stats()` | `buddy.capturedTexturesStats()` |
| Captured textures — clear | `buddy.clearCapturedTextures()` | `buddy.clear_captured_textures()` | `buddy:clear_captured_textures()` | `buddy.clearCapturedTextures()` |
| Captured textures — get one | `buddy.getCapturedTexture(id)` | `buddy.get_captured_texture(id)` | `buddy:get_captured_texture(id)` | `buddy.getCapturedTexture(id)` |
| Glyphs — on screen | `buddy.getGlyphsOnScreen(opts)` | `buddy.get_glyphs_on_screen(**opts)` | `buddy:get_glyphs_on_screen(opts)` | `buddy.getGlyphsOnScreen(opts)` |
| Glyphs — at point | `buddy.glyphAtPoint(x,y,opts?)` | `buddy.glyph_at_point(x,y,**opts)` | `buddy:glyph_at_point(x,y,opts)` | `buddy.glyphAtPoint(x,y,opts)` |
| **OCR** — match text | `buddy.matchText(req)` | `buddy.match_text(req)` | `buddy:match_text(req)` | `buddy.matchText(req)` |
| OCR — match region | `buddy.matchRegion(req)` | `buddy.match_region(req)` | `buddy:match_region(req)` | `buddy.matchRegion(req)` |

> Every method is typed in TypeScript, Python (TypedDict), Lua (Luau), and Java. The clients are
> thin wrappers — each call maps to one server request — so any language with a typed client is a
> first-class citizen, no HTTP plumbing required.
