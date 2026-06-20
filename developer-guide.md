# RS3 Buddy — Developer Guide

Build in-game overlays and read game state from **any language**. Each language ships a
typed client (TypeScript, Python, Lua, Java) that exposes the same capabilities through
idiomatic methods — `buddy.ui.html(...)`, `buddy.sound.play(...)`, `buddy.chat.read()`,
`buddy.bars.read()`, `buddy.getPlayer()`, `buddy.drawShape(...)`, and so on. You never touch
a socket or a URL: `connect()` finds the running game for you, and every method is a thin,
typed call.

> The overlay reads the game's GPU draws only — it never reads game memory or injects
> input (mouse input is *read and selectively consumed*, never synthesized).

---

## 1. Getting started

The desktop **launcher must be running**. It injects the native hook into the RS3 client,
which brings the SDK server up and writes a small config file under
`%APPDATA%\rs3buddy\rs3buddy.json`. Each client's `connect()` reads that config and wires
itself to the running game automatically — no URL, no port, no setup. With nothing running,
the first call raises a **connection error** (see below).

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

**Conventions**
- A "no result" (e.g. no player on screen) returns `null` / `None` / `nil` — not an error.
- **Errors.** If the server can't be reached (launcher not running, game crashed), the call
  raises a connection error: `RS3BuddyConnectionError` (TS / Python),
  `RS3BuddyConnectionException` (Java), or a tagged error table (Lua, caught with `pcall`).
  A server-side failure (non-2xx) raises `RS3BuddyError` (TS / Python) instead.
- **Method-name shape:** TypeScript & Java are camelCase (`getPlayer`); Python & Lua are
  snake_case (`get_player`). The reader namespaces (`chat`, `bars`, `abilities`, `ui`,
  `sound`) use a dot in TS / Python / Java (`buddy.ui.html(...)`) and a colon in Lua
  (`buddy.ui:html(...)`); top-level Lua methods also use a colon (`buddy:get_player()`).

> **Lua in a sandbox.** On sandboxed hosts with no `io`/`os` (e.g. Roblox), auto-discovery
> can't read the config — pass an explicit base URL and an HTTP request function:
> `rs3buddy.connect({ base_url = "http://127.0.0.1:1234", request = my_http_fn })`.

---

## 2. UI overlay

Author your HUD as **HTML + CSS** and hand it to `ui.html(html, css)`. The server compiles it
into the same widget engine the SDK renders, draws it in-game, and auto-scales it for the
display. Your app owns the state: poll `ui.events()` for clicks (each event carries the
clicked widget's `id`), update your state, and call `ui.html(...)` again to re-render —
calling it replaces the current UI.

Other UI methods:
- `ui.render(tree)` — render a raw widget tree `{ type, props, children }` instead of HTML.
- `ui.clear()` — remove the overlay.
- `ui.events()` — drain queued interaction events; each is `{ type, id, x, y }`.
- `ui.scaling({ exponent })` — tune how big the UI grows on hi-DPI / 4K (`exponent` `1.0` =
  constant physical size; `>1` bigger on 4K, **default 1.5**; also `scale`, `baseHeight`).

The HUDs below are **stall-free polling loops**: `getPlayer()` does the cheap passive read
(no GPU, no frame capture), so it's safe to call every iteration; the UI is only re-POSTed
when its markup actually changes. Buttons carry an `id`, so clicks come back through
`ui.events()` tagged with it.

```ts
// TypeScript — run: npx ts-node hud.ts
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
    try { pos = await buddy.getPlayer(); }                // cheap passive read — safe to poll
    catch (e) {
      if (e instanceof RS3BuddyConnectionError) { console.error("launcher not running?"); return; }
    }
    if (pos?.tileX !== undefined) { tileX = pos.tileX; tileZ = pos.tileZ; located = true; }

    const resp = await buddy.ui.events().catch(() => null);
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
# Python — run: python hud.py
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

        resp = buddy.ui.events()
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
-- Lua / Luau — run: lune run hud.luau
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

    local resp = buddy.ui:events()
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

    if html ~= lastHtml then                              -- re-POST only on change
        buddy.ui:html(html, CSS); lastHtml = html
    end
    -- sleep ~0.3s with your host's timer (lune task.wait, luasocket socket.sleep, ...)
    if (_G :: any).task then (_G :: any).task.wait(0.3) end
end
```
```java
// Java — build the client jar, then compile + run against it (+ Jackson on the classpath)
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

            JsonNode resp = buddy.ui.events();
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

**Widgets & props (for `ui.render(tree)` and as the CSS targets for `ui.html`):** `panel`
(rounded container / HUD root), `row` / `column` (flex), `stack`, `grid`, `spacer`, `label`,
`button`, `gauge` (stat bar), `image`, `divider`, `badge`, `accordion`, `worldLabel`,
`worldMarker`, `tile`. Common props — layout: `width`/`height` (number | `"auto"` | `"flex"`),
`pad`, `margin`, `gap`, `align`, `justify`; visual: `bg`, `color`, `fill`, `track`, `outline`,
`radius`, `opacity`, `shadow`, `font`, `fontSize` (colors are `#rgb` / `#rrggbb` / `#rrggbbaa`);
root placement: `anchor` (`top-left`…`bottom-right` or `{ x, y }`). Buttons take `variant`
(`primary` · `secondary` · `good` · `danger` · `ghost` · `plain`), a built-in `icon`
(`close` · `minimize` · `maximize`), and an `id` so their clicks surface in `ui.events()`.
`draggable` on a panel lets the user move it (the camera stays put); `consume` makes a widget
block clicks from reaching the game.

---

## 3. Sound

Play developer-supplied audio host-side (wav / mp3 / ogg) through the desktop app. Provide
**either** a host-side `file` path (or a `file:` / `data:` / `http(s):` URL) **or** inline
base64 `bytes` with a `mime` type; `volume` is an optional `0..1` gain. Requires the desktop
audio host.

```ts
// TypeScript — options object: { file?, bytes?, mime?, volume? }
await buddy.sound.play({ file: "C:/sfx/ding.wav", volume: 0.6 });
// inline bytes: await buddy.sound.play({ bytes: "<base64>", mime: "audio/mp3" });
```
```python
# Python — keyword args
buddy.sound.play(file="C:/sfx/ding.wav", volume=0.6)
# inline bytes: buddy.sound.play(bytes="<base64>", mime="audio/mp3")
```
```lua
-- Lua — options table (colon call)
buddy.sound:play({ file = "C:/sfx/ding.wav", volume = 0.6 })
-- inline bytes: buddy.sound:play({ bytes = "<base64>", mime = "audio/mp3" })
```
```java
// Java — play(file, volume); playBytes(base64, mime, volume) for inline audio
buddy.sound.play("C:/sfx/ding.wav", 0.6);            // volume may be null for host default
buddy.sound.play("C:/sfx/ding.wav", null);
// inline bytes: buddy.sound.playBytes("<base64>", "audio/mp3", null);
```

---

## 4. Reading game state

All reads are **cheap and safe to poll every loop** — the heavy work runs server-side and is
cached per frame. `null` / `None` / `nil` (or a `found: false` entry) means "nothing on
screen right now".

- `chat.read()` → `{ ok, lineCount, lines: [...] }`. Each line: `text`, `color` `[r, g, b]`
  (the message/channel colour — clan green, public white, …), `runs` (contiguous same-colour
  segments for a multi-colour line), and per-glyph `glyphs`.
- `bars.read()` → `{ ok, bars: [...] }` for HP / adrenaline / prayer / summoning. Each bar:
  `name` (`hitpoints` / `adrenaline` / `prayer` / `summoning`), `found`, `value`, `max`,
  `text`.
- `abilities.read()` → `{ ok, abilities: [...] }` — the action bar. Each slot: `name`, `rect`,
  `usable`, `onCooldown`, `cooldownText`, `cooldownSeconds`.
- `getPlayer()` / `get_player()` → player world position: `{ tileX, tileZ, worldX, worldZ }`,
  or `null` until the player is tracked. This is the **cheap passive read** — poll it freely.
  (The heavy, forced capture-based detection is a separate opt-in, not used by these calls;
  don't poll that.) Related: `getPlayerName()` / `get_player_name()` (opt-in; recovered from
  the chat input prompt).
- Scene: `getScene()` / `get_scene()` returns a full snapshot (`player`, `npcs`, `scenery`,
  `floor`, `water`, …); `getNpcs(radius, floor)` and `getScenery(radius, floor)` fetch just
  those lists.

```ts
// TypeScript
const { bars } = await buddy.bars.read();
const hp = bars.find((b) => b.name === "hitpoints");
console.log(`${hp?.value} / ${hp?.max}`);

const chat = await buddy.chat.read();
for (const line of chat.lines) console.log(line.color, line.text);

const pos = await buddy.getPlayer();                 // { tileX, tileZ, worldX, worldZ } | null
if (pos) console.log("tile", pos.tileX, pos.tileZ);

const scene = await buddy.getScene();                // { player, npcs, scenery, ... }
```
```python
# Python
res = buddy.bars.read()
hp = next(b for b in res["bars"] if b["name"] == "hitpoints")
print(hp["value"], "/", hp["max"])

chat = buddy.chat.read()
for line in chat["lines"]:
    print(line["color"], line["text"])

pos = buddy.get_player()                              # { tileX, tileZ, worldX, worldZ } | None
if pos:
    print("tile", pos["tileX"], pos["tileZ"])

scene = buddy.get_scene()                             # { player, npcs, scenery, ... }
```
```lua
-- Lua
local res = buddy.bars:read()
for _, b in ipairs(res.bars) do
    if b.name == "hitpoints" then
        print(tostring(b.value) .. "/" .. tostring(b.max))
    end
end

local chat = buddy.chat:read()
for _, line in ipairs(chat.lines or {}) do
    local c = line.color or { 0, 0, 0 }
    print(string.format("(%s,%s,%s)", c[1], c[2], c[3]) .. " " .. tostring(line.text))
end

local pos = buddy:get_player()                        -- { tileX, tileZ, worldX, worldZ } | nil
if pos then print("tile", pos.tileX, pos.tileZ) end

local scene = buddy:get_scene()                       -- { player, npcs, scenery, ... }
```
```java
// Java — bars.read() / chat.read() return typed results; getPlayer() returns Position
BarsReadResult bars = buddy.bars.read();
for (BarValue b : bars.getBars()) {
    if ("hitpoints".equals(b.getName())) {
        System.out.println(b.getValue() + " / " + b.getMax());
    }
}

ChatReadResult chat = buddy.chat.read();
for (ChatLine line : chat.getLines()) {
    System.out.println(line.getColor() + " " + line.getText());  // color is [r, g, b]
}

Position pos = buddy.getPlayer();                     // null until tracked
if (pos != null) System.out.println("tile " + pos.getTileX() + ", " + pos.getTileZ());

SceneSnapshot scene = buddy.getScene();               // player / npcs / scenery / ...
```

---

## 5. Drawing in the world

Beyond the UI, you can draw shapes and text anchored to a 3D tile, a world point, the screen,
or an entity. Use `drawShape(shape)` for one item, or `drawScene([...])` for a batch.

A draw item is an object with an `anchor` (`{ mode: "tile", tile: { x, z, floor } }`,
`"world"`, `"screen"`, or `"entity"`), a `primitive` (`triangles` / `lines` / `line_loop` /
`points` / …), a flat `vertices` array, and a `color` (a `#rrggbb(aa)` string or `[r, g, b]`).
Optional fields include `id`, `thickness`, `occludedByWorld`, `group`, and `conformToGround`.
A `billboard` item (`{ kind: "billboard", anchor, text }`) draws 3D-anchored text. Clear with
`clearShapes()` / `clear_shapes()`, or remove one with `removeShape(id)`.

```ts
// TypeScript — outline a ground tile + label it
await buddy.drawScene([
  { anchor: { mode: "tile", tile: { x: 3200, z: 3200, floor: 0 } },
    primitive: "line_loop",
    vertices: [-0.5, 0, -0.5,  0.5, 0, -0.5,  0.5, 0, 0.5,  -0.5, 0, 0.5],
    color: "#00ff00", thickness: 2, occludedByWorld: true },
  { kind: "billboard", anchor: { mode: "tile", tile: { x: 3200, z: 3200, floor: 0 } },
    text: "Here", color: "#ffff00" },
]);
```
```python
# Python
buddy.draw_scene([
    {"anchor": {"mode": "tile", "tile": {"x": 3200, "z": 3200, "floor": 0}},
     "primitive": "line_loop",
     "vertices": [-0.5, 0, -0.5,  0.5, 0, -0.5,  0.5, 0, 0.5,  -0.5, 0, 0.5],
     "color": "#00ff00", "thickness": 2, "occludedByWorld": True},
    {"kind": "billboard", "anchor": {"mode": "tile", "tile": {"x": 3200, "z": 3200, "floor": 0}},
     "text": "Here", "color": "#ffff00"},
])
```
```lua
-- Lua
buddy:draw_scene({
    { anchor = { mode = "tile", tile = { x = 3200, z = 3200, floor = 0 } },
      primitive = "line_loop",
      vertices = { -0.5, 0, -0.5, 0.5, 0, -0.5, 0.5, 0, 0.5, -0.5, 0, 0.5 },
      color = "#00ff00", thickness = 2, occludedByWorld = true },
    { kind = "billboard", anchor = { mode = "tile", tile = { x = 3200, z = 3200, floor = 0 } },
      text = "Here", color = "#ffff00" },
})
```
```java
// Java — drawShape(Object) / drawScene(List<Object>); build maps/POJOs and pass them in
Map<String, Object> tile = Map.of("mode", "tile",
    "tile", Map.of("x", 3200, "z", 3200, "floor", 0));
Map<String, Object> ring = Map.of(
    "anchor", tile, "primitive", "line_loop",
    "vertices", List.of(-0.5, 0, -0.5,  0.5, 0, -0.5,  0.5, 0, 0.5,  -0.5, 0, 0.5),
    "color", "#00ff00", "thickness", 2, "occludedByWorld", true);
Map<String, Object> label = Map.of(
    "kind", "billboard", "anchor", tile, "text", "Here", "color", "#ffff00");
buddy.drawScene(List.of(ring, label));
```

---

## 6. Function reference

Each capability and its method name in all four clients. (Lua reader namespaces use a colon —
`buddy.ui:html(...)` — as do top-level Lua methods: `buddy:get_player()`.)

| Capability | TypeScript | Python | Lua | Java |
|------------|------------|--------|-----|------|
| Connect | `RS3Buddy.connect()` | `RS3Buddy.connect()` | `rs3buddy.connect()` | `RS3Buddy.connect()` |
| Base URL | `buddy.baseUrl` | `buddy.base_url` | `buddy:base_url()` | `buddy.baseUrl()` |
| **UI** — HTML page | `buddy.ui.html(html, css)` | `buddy.ui.html(html, css)` | `buddy.ui:html(html, css)` | `buddy.ui.html(html, css)` |
| UI — widget tree | `buddy.ui.render(tree)` | `buddy.ui.render(tree)` | `buddy.ui:render(tree)` | `buddy.ui.render(tree)` |
| UI — clear | `buddy.ui.clear()` | `buddy.ui.clear()` | `buddy.ui:clear()` | `buddy.ui.clear()` |
| UI — drain clicks | `buddy.ui.events()` | `buddy.ui.events()` | `buddy.ui:events()` | `buddy.ui.events()` |
| UI — display scaling | `buddy.ui.scaling(opts)` | `buddy.ui.scaling(...)` | `buddy.ui:scaling(opts)` | `buddy.ui.scaling(...)` |
| **Sound** — play | `buddy.sound.play(opts)` | `buddy.sound.play(...)` | `buddy.sound:play(opts)` | `buddy.sound.play(file, vol)` / `playBytes(...)` |
| **Chat** — read | `buddy.chat.read()` | `buddy.chat.read()` | `buddy.chat:read()` | `buddy.chat.read()` |
| **Bars** — read | `buddy.bars.read()` | `buddy.bars.read()` | `buddy.bars:read()` | `buddy.bars.read()` |
| **Abilities** — read | `buddy.abilities.read()` | `buddy.abilities.read()` | `buddy.abilities:read()` | `buddy.abilities.read()` |
| **Player** — position | `buddy.getPlayer()` | `buddy.get_player()` | `buddy:get_player()` | `buddy.getPlayer()` |
| Player — refresh | `buddy.updatePlayer()` | `buddy.update_player()` | `buddy:update_player()` | `buddy.updatePlayer()` |
| Player — name | `buddy.getPlayerName()` | `buddy.get_player_name()` | `buddy:get_player_name()` | `buddy.getPlayerName()` |
| **Scene** — snapshot | `buddy.getScene()` | `buddy.get_scene()` | `buddy:get_scene()` | `buddy.getScene()` |
| Scene — npcs | `buddy.getNpcs(r, floor)` | `buddy.get_npcs(r, floor)` | `buddy:get_npcs(r, floor)` | `buddy.getNpcs(r, floor)` |
| Scene — scenery | `buddy.getScenery(r, floor)` | `buddy.get_scenery(r, floor)` | `buddy:get_scenery(r, floor)` | `buddy.getScenery(r, floor)` |
| Scene — entity at | `buddy.getEntityAt(x, z, floor)` | `buddy.get_entity_at(x, z, floor)` | `buddy:get_entity_at(x, z, floor)` | `buddy.getEntityAt(x, z, floor)` |
| **Draw** — one shape | `buddy.drawShape(shape)` | `buddy.draw_shape(shape)` | `buddy:draw_shape(shape)` | `buddy.drawShape(shape)` |
| Draw — batch | `buddy.drawScene(shapes)` | `buddy.draw_scene(shapes)` | `buddy:draw_scene(shapes)` | `buddy.drawScene(shapes)` |
| Draw — list | `buddy.listShapes()` | `buddy.list_shapes()` | `buddy:list_shapes()` | `buddy.listShapes()` |
| Draw — remove one | `buddy.removeShape(id)` | `buddy.remove_shape(id)` | `buddy:remove_shape(id)` | `buddy.removeShape(id)` |
| Draw — clear | `buddy.clearShapes(group?)` | `buddy.clear_shapes(group)` | `buddy:clear_shapes(group)` | `buddy.clearShapes(group)` |
| **Status** | `buddy.getStatus()` | `buddy.get_status()` | `buddy:get_status()` | `buddy.getStatus()` |
| Window | `buddy.getWindow()` | `buddy.get_window()` | `buddy:get_window()` | `buddy.getWindow()` |

> All methods are typed in TypeScript, Python (TypedDict), Lua (Luau), and Java. The clients
> are thin wrappers — every call maps to one server request — so any language with a typed
> client is a first-class citizen, no HTTP plumbing required.
