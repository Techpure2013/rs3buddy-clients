# rs3buddy — Examples

Version 0.1.0 · last updated 2026-06-21

A working HUD that tracks your tile, counts ticks, and has Start/Pause + Exit
buttons — and the **stall-free loop** every overlay should use. (For a bare
minimum, the [developer guide](../developer-guide.md) has a 10-line hello world.)

## The stall-free HUD loop

A HUD is a loop: **read state → build UI → draw → repeat.** Done naively that
re-sends the whole overlay every tick and can stall the game's render. The pattern
below stays cheap — passive reads, and it re-POSTs only when the markup actually
changes.

### Step by step (what each part does, and whether it's required)

1. **`connect()` once, before the loop.** *Required.* Auto-discovers the running game.
2. **Read the state you need — prefer the cheap passive reads, and poll them.**
   `getPlayer()` costs nothing extra, so it's safe every tick. *Required in some form
   — this is your data.* Guard it so a "not tracked yet" result (`null`/`None`/`nil`)
   or a connection error doesn't crash the loop.
3. **Drain clicks with `ui.events()`** and react (each event carries the widget's
   `id`). *Required only if your UI has buttons.* It returns an empty list when
   nothing was clicked.
4. **Build the HTML string** from your current state. *Required.*
5. **Re-POST only when the HTML changed** — keep the last string and compare.
   *Recommended, not required:* skipping it just re-sends an identical overlay every
   tick (more work, same result). This one check is what makes the loop "stall-free."
6. **Sleep a short interval** (300 ms here). *Required* — never busy-loop.
7. **On a `close` event, `ui.clear()` then exit.** *Good practice* — so the overlay
   doesn't linger after your app stops.

The CSS is passed with each `ui.html(html, css)` call; it's an ordinary stylesheet
(supported tags + properties: [html-overlay.md](html-overlay.md)). The panel uses
`draggable` (user can move it) and `consume` (clicks don't fall through to the game).
Buttons carry an `id` so their clicks return through `ui.events()`.

### Full example — pick your language

<details>
<summary><b>TypeScript</b></summary>

```ts
// npx tsx hud.ts
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
  const buddy = RS3Buddy.connect({ clientName: "hud-example" });   // 1. connect once
  let running = false, ticks = 0, tileX = 0, tileZ = 0, located = false, lastHtml = "";

  for (;;) {
    let pos: Position | null = null;
    try { pos = await buddy.getPlayer(); }                 // 2. cheap passive read — safe to poll
    catch (e) {
      if (e instanceof RS3BuddyConnectionError) { console.error("launcher not running?"); return; }
    }
    if (pos?.tileX !== undefined) { tileX = pos.tileX; tileZ = pos.tileZ; located = true; }

    const resp = await buddy.ui.events().catch(() => null);   // 3. drain clicks
    for (const e of resp?.events ?? []) {
      if (e.id === "close") { await buddy.ui.clear(); return; } // 7. clean up + exit
      if (e.id === "start") running = !running;
    }
    if (running) ticks++;

    const tile = located ? `${tileX}, ${tileZ}` : "—";
    const html =                                            // 4. build markup from state
      "<panel anchor='top-right' draggable consume>" +
        "<header><span class='title'>Tracker</span>" +
          "<button id='close' icon='close' variant='plain'></button></header><hr/>" +
        `<div class='row'><span class='k'>Tile</span><span class='accent'>${tile}</span></div>` +
        `<div class='row'><span class='k'>Ticks</span><span class='v'>${ticks}</span></div><hr/>` +
        "<div class='btns'>" +
          `<button id='start' class='${running ? "" : "good"}'>${running ? "Pause" : "Start"}</button>` +
          "<button id='close' class='danger'>Exit</button></div>" +
      "</panel>";

    if (html !== lastHtml) { await buddy.ui.html(html, CSS); lastHtml = html; } // 5. re-POST only on change
    await sleep(300);                                       // 6. don't busy-loop
  }
}
void main();
```

</details>

<details>
<summary><b>Python</b></summary>

```python
# python hud.py
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
    buddy = RS3Buddy.connect(client_name="hud-example")     # 1. connect once
    running, ticks, tile_x, tile_z, located, last_html = False, 0, 0, 0, False, ""

    while True:
        try:
            pos = buddy.get_player()                        # 2. cheap passive read — safe to poll
        except RS3BuddyConnectionError:
            print("launcher not running?"); return
        if pos and pos.get("tileX") is not None:
            tile_x, tile_z, located = int(pos["tileX"]), int(pos["tileZ"]), True

        resp = buddy.ui.events()                            # 3. drain clicks
        for e in (resp.get("events") if resp else None) or []:
            if e.get("id") == "close":
                buddy.ui.clear(); return                     # 7. clean up + exit
            if e.get("id") == "start":
                running = not running
        if running:
            ticks += 1

        tile = f"{tile_x}, {tile_z}" if located else "—"
        html = (                                            # 4. build markup from state
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
        if html != last_html:                               # 5. re-POST only on change
            buddy.ui.html(html, CSS); last_html = html
        time.sleep(0.3)                                     # 6. don't busy-loop

if __name__ == "__main__":
    main()
```

</details>

<details>
<summary><b>Lua / Luau</b></summary>

```lua
-- lune run hud.luau
local rs3buddy = require("rs3buddy")
local buddy = rs3buddy.connect({ client_name = "hud-example" })   -- 1. connect once

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
    local ok, pos = pcall(function() return buddy:get_player() end)  -- 2. cheap passive read (pcall guards errors)
    if ok and type(pos) == "table" and pos.tileX ~= nil then
        tileX, tileZ, located = pos.tileX, pos.tileZ, true
    end

    local resp = buddy.ui:events()                          -- 3. drain clicks
    local events = if type(resp) == "table" then resp.events else nil
    if type(events) == "table" then
        for _, e in ipairs(events) do
            local id = if type(e) == "table" then e.id else nil
            if id == "close" then
                buddy.ui:clear(); return                     -- 7. clean up + exit
            elseif id == "start" then
                running = not running
            end
        end
    end
    if running then ticks += 1 end

    local tile = if located then (tostring(tileX) .. ", " .. tostring(tileZ)) else "—"
    local startClass = if running then "" else "good"
    local startLabel = if running then "Pause" else "Start"
    local html = "<panel anchor='top-right' draggable consume>"   -- 4. build markup from state
        .. "<header><span class='title'>Tracker</span>"
        .. "<button id='close' icon='close' variant='plain'></button></header><hr/>"
        .. "<div class='row'><span class='k'>Tile</span><span class='accent'>" .. tile .. "</span></div>"
        .. "<div class='row'><span class='k'>Ticks</span><span class='v'>" .. tostring(ticks) .. "</span></div><hr/>"
        .. "<div class='btns'>"
        .. "<button id='start' class='" .. startClass .. "'>" .. startLabel .. "</button>"
        .. "<button id='close' class='danger'>Exit</button></div>"
        .. "</panel>"

    if html ~= lastHtml then                                -- 5. re-POST only on change
        buddy.ui:html(html, CSS); lastHtml = html
    end
    if (_G :: any).task then (_G :: any).task.wait(0.3) end -- 6. host timer (don't busy-loop)
end
```

</details>

<details>
<summary><b>Java</b></summary>

```java
// compile + run against the client jar (+ Jackson) — see java/README.md
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
        RS3Buddy buddy = RS3Buddy.connect("hud-example");   // 1. connect once
        boolean running = false, located = false;
        int ticks = 0, tileX = 0, tileZ = 0;
        String lastHtml = "";

        while (true) {
            Position pos = null;
            try { pos = buddy.getPlayer(); }                 // 2. cheap passive read — safe to poll
            catch (RuntimeException ignore) { /* not tracked yet */ }
            if (pos != null && pos.getTileX() != null) {
                tileX = pos.getTileX().intValue();
                tileZ = pos.getTileZ().intValue();
                located = true;
            }

            JsonNode resp = buddy.ui.events();               // 3. drain clicks
            JsonNode events = (resp == null) ? null : resp.get("events");
            if (events != null) {
                for (JsonNode e : events) {
                    switch (e.path("id").asText("")) {
                        case "close": buddy.ui.clear(); return;  // 7. clean up + exit
                        case "start": running = !running; break;
                        default: break;
                    }
                }
            }
            if (running) ticks++;

            String tile = located ? (tileX + ", " + tileZ) : "—";
            String html = "<panel anchor='top-right' draggable consume>"  // 4. build markup from state
                + "<header><span class='title'>Tracker</span>"
                + "<button id='close' icon='close' variant='plain'></button></header><hr/>"
                + "<div class='row'><span class='k'>Tile</span><span class='accent'>" + tile + "</span></div>"
                + "<div class='row'><span class='k'>Ticks</span><span class='v'>" + ticks + "</span></div><hr/>"
                + "<div class='btns'>"
                + "<button id='start' class='" + (running ? "" : "good") + "'>" + (running ? "Pause" : "Start") + "</button>"
                + "<button id='close' class='danger'>Exit</button></div>"
                + "</panel>";

            if (!html.equals(lastHtml)) {                   // 5. re-POST only on change
                buddy.ui.html(html, CSS); lastHtml = html;
            }
            Thread.sleep(300);                              // 6. don't busy-loop
        }
    }
}
```

</details>

From here, swap `getPlayer()` for any reader (`chat.read`, `bars.read`,
`abilities.read`, `getScene`) and add rows/buttons — the loop shape stays the same.
See your language's API reference for every method.
