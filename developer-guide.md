# RS3 Buddy — Developer Guide

Version 0.1.0 · last updated 2026-06-21

rs3buddy is a local SDK for building RuneScape 3 overlays and tools. Your app
`connect()`s to a server the launcher runs, reads live game state (chat, stats,
abilities, the 3D scene), and draws in-game UI — all over `localhost`. It reads the
game's GPU draws and the mouse only; it never reads game memory or injects input.

This guide takes you from nothing to a running overlay. For every method, see your
language's **API reference**; for deeper topics, the **docs/** folder (links at the end).

## Requirements

- **Windows**, with **RuneScape 3 (NXT client)** running.
- The **rs3buddy launcher** running — it injects the hook, starts the local server,
  and writes the config your client auto-discovers (`%APPDATA%\rs3buddy\rs3buddy.json`),
  so `connect()` needs no URL.
- Your language's runtime, and the client for it. **The clients are distributed from
  this repo** (`rs3buddy-clients`) — they are not on npm / PyPI / Maven. Install:

| Language | Runtime | Install the client |
|---|---|---|
| TypeScript / JS | Node 18+ | `cd ts && npm install && npm run build`, then `npm install <path>/rs3buddy-clients/ts` in your project |
| Python | Python 3.10+ | `pip install "git+https://github.com/Techpure2013/rs3buddy-clients.git#subdirectory=python"` (or `pip install ./python`) |
| Lua / Luau | Lua 5.1+ | `luarocks install luasocket`, then vendor the `lua/rs3buddy` folder |
| Java | JDK 11+ to use, JDK 17 to build · + Jackson | `gradle jar` in `java/` → `build/libs/rs3buddy-client-0.1.0.jar` (or `includeBuild`) — see [java/README.md](java/README.md) |

## Hello world

A HUD is a loop: **read state → draw → repeat**. This connects, reads your prayer
points, and shows them in a small overlay top-right. Pick your language:

<details>
<summary><b>TypeScript</b></summary>

```ts
// run: npx tsx hello.ts   (with the game + launcher open)
import { RS3Buddy } from "@rs3buddy/client";

const buddy = RS3Buddy.connect();                  // finds the running game via the launcher config
const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));

for (;;) {                                          // a HUD is just: read → draw → repeat
  const { bars } = await buddy.bars.read();         // the four orbs (HP / prayer / adren / summoning)
  const prayer = bars.find((b) => b.name === "prayer");
  const text = prayer?.found ? `Prayer: ${prayer.value}` : "Prayer: —";

  // ui.html REPLACES the whole overlay each call. anchor pins it to a corner.
  await buddy.ui.html(`<panel anchor='top-right'><span>${text}</span></panel>`);

  await sleep(600);                                 // poll a few times a second
}
```

</details>

<details>
<summary><b>Python</b></summary>

```python
# run: python hello.py   (with the game + launcher open)
import time
from rs3buddy import RS3Buddy

buddy = RS3Buddy.connect()                          # finds the running game via the launcher config

while True:                                          # read → draw → repeat
    res = buddy.bars.read()                          # the four orbs
    prayer = next((b for b in res["bars"] if b["name"] == "prayer"), None)
    text = f"Prayer: {prayer['value']}" if prayer and prayer["found"] else "Prayer: —"

    # ui.html REPLACES the whole overlay each call.
    buddy.ui.html(f"<panel anchor='top-right'><span>{text}</span></panel>")
    time.sleep(0.6)
```

</details>

<details>
<summary><b>Lua</b></summary>

```lua
-- requires the rs3buddy package + luasocket (for sleep)
local rs3buddy = require("rs3buddy")
local socket = require("socket")

local buddy = rs3buddy.connect()                    -- finds the running game via the launcher config

while true do                                        -- read → draw → repeat
  local res = buddy.bars:read()                      -- the four orbs (note: ':' call style)
  local text = "Prayer: —"
  for _, b in ipairs(res.bars) do
    if b.name == "prayer" and b.found then text = "Prayer: " .. tostring(b.value) end
  end

  -- ui.html REPLACES the whole overlay each call.
  buddy.ui:html("<panel anchor='top-right'><span>" .. text .. "</span></panel>")
  socket.sleep(0.6)
end
```

</details>

<details>
<summary><b>Java</b></summary>

```java
// needs rs3buddy-client + Jackson on the classpath (see java/README.md)
import com.rs3buddy.RS3Buddy;

public class Hello {
  public static void main(String[] args) throws Exception {
    RS3Buddy buddy = RS3Buddy.connect();            // finds the running game via the launcher config
    while (true) {                                   // read → draw → repeat
      var bars = buddy.bars.read();                  // the four orbs
      String text = "Prayer: —";
      for (var b : bars.getBars())
        if ("prayer".equals(b.getName())) text = "Prayer: " + b.getValue();

      // ui.html REPLACES the whole overlay each call (css is the 2nd arg).
      buddy.ui.html("<panel anchor='top-right'><span>" + text + "</span></panel>", "");
      Thread.sleep(600);
    }
  }
}
```

</details>

Run it with the game + launcher open: a small `Prayer: N` panel appears top-right.
That's the whole shape of an overlay — everything else is more reads and richer UI.

## Conventions

- **No result yet?** A read with nothing to return (nothing on screen, not tracked
  yet) gives `null` / `None` / `nil`. Normal — just call again.
- **Errors.** If the server is unreachable (launcher closed, game gone) the client
  raises a connection error — `RS3BuddyConnectionError` (TS / Python),
  `RS3BuddyConnectionException` (Java), or a tagged error table in Lua. Server-side
  failures raise `RS3BuddyError` (TS / Python).
- **Opt-in reads.** Some data (player detection, the local player's name) is gathered
  only when you call for it — nothing is collected passively.

## Where to go next

- **API reference** for your language — every method, with signatures, params and returns:
  [TypeScript](ts/README.md) · [Python](python/README.md) · [Lua](lua/README.md) · [Java](java/README.md)
- [docs/overview.md](docs/overview.md) — how it works (game ↔ hook ↔ engine ↔ your client)
- [docs/examples.md](docs/examples.md) — the stall-free HUD loop, explained step by step
- [docs/html-overlay.md](docs/html-overlay.md) — UI overlay: supported HTML tags + CSS
- [docs/advanced.md](docs/advanced.md) — atlas, shaders/FX, capture, OCR (and when to use them)
- [docs/glossary.md](docs/glossary.md) · [docs/troubleshooting.md](docs/troubleshooting.md)
- [CONTRACT.md](CONTRACT.md) — the raw HTTP contract (for languages without a client)
