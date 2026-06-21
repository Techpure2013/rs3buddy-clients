# rs3buddy clients

Version 0.1.1 · last updated 2026-06-21

Client libraries for **rs3buddy** in four languages — thin, typed wrappers over the
local rs3buddy HTTP server. You `connect()` to the running server and call methods
like `chat.read()` to read live game state and draw in-game overlays.

**New here? Start with the [Developer Guide](developer-guide.md)** — requirements,
install, and a 10-line hello world.

## Install

The clients are distributed from this repo (they are not on npm / PyPI / Maven).

| Language | Runtime | Get the client |
|---|---|---|
| TypeScript / JS | Node 18+ | `cd ts && npm install && npm run build`, then `npm install <path>/ts` in your project |
| Python | Python 3.10+ | `pip install "git+https://github.com/Techpure2013/rs3buddy-clients.git#subdirectory=python"` (or `pip install ./python`) |
| Lua / Luau | Lua 5.1+ | `luarocks install luasocket`, then vendor the `lua/rs3buddy` folder |
| Java | JDK 11+ to use, 17 to build · + Jackson | `gradle jar` in `java/` → `build/libs/rs3buddy-client-0.1.1.jar` (or `includeBuild`) — see [java/README.md](java/README.md) |

## Quick taste

Start the launcher (it brings up the local server), then `connect()` — no URL needed —
and read the chatbox:

```ts
import { RS3Buddy } from "@rs3buddy/client";
const buddy = RS3Buddy.connect();
const chat = await buddy.chat.read();
console.log(`${chat.lineCount} chat lines`);
```

The same in Python / Lua / Java is in the [Developer Guide](developer-guide.md).

## Docs

- **[Developer Guide](developer-guide.md)** — from nothing to a running overlay
- **API reference** (every method, per language): [TypeScript](ts/README.md) · [Python](python/README.md) · [Lua](lua/README.md) · [Java](java/README.md)
- [docs/overview.md](docs/overview.md) — how it works (game ↔ hook ↔ engine ↔ your client)
- [docs/examples.md](docs/examples.md) — the stall-free HUD loop, step by step
- [docs/html-overlay.md](docs/html-overlay.md) — UI overlay: HTML tags + CSS
- [docs/advanced.md](docs/advanced.md) — atlas, shaders/FX, capture, OCR
- [docs/glossary.md](docs/glossary.md) · [docs/troubleshooting.md](docs/troubleshooting.md)
- [CONTRACT.md](CONTRACT.md) — the raw HTTP contract (for languages without a client)
