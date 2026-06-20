# rs3buddy (Lua / Luau)

Typed HTTP client for the rs3buddy server. Behavior contract: `../CONTRACT.md`.

The client is the **`rs3buddy/`** package — a `--!strict` Luau module that runs in
any Lua host (standard Lua 5.1/5.4 + LuaJIT via LuaSocket, Roblox, or any host
that injects its own HTTP function). Pluggable transport, vendored JSON, no build
step.

## Quick start

    local rs3buddy = require("rs3buddy")
    local buddy = rs3buddy.connect()        -- auto-discovers the running server
    local chat = buddy.chat:read()
    print(chat.lineCount .. " chat lines")

`connect()` with no arguments auto-discovers the server: it reads the port the
launcher wrote to `%APPDATA%\rs3buddy\rs3buddy.json` (USERPROFILE fallback) and
connects to `http://127.0.0.1:<port>`. The launcher must be running (native
injected into RS3 → SDK server up). See `examples/read_chat.luau`.

Pass an explicit `base_url` to target a specific server, or an injected `request`
function for hosts without LuaSocket:

    local buddy = rs3buddy.connect({ base_url = "http://127.0.0.1:1234" })

**Performance — reuse the client.** The SDK server shares the game's render
thread, so a cold TCP connect can stall ~2 s. The default LuaSocket transport
holds one persistent keep-alive socket and reuses it — a warm request is
~0.3 ms. Create one client and keep it; do **not** make a new client per call.

## Sandboxed hosts (Roblox, ...) — inject a transport

Auto-discovery needs `io`/`os`; sandboxed hosts have neither, so `connect()` with
no `base_url` returns no config there. On those hosts pass **both** an explicit
`base_url` and a `request` function — it must return `(status, body_string)`:

    local buddy = rs3buddy.connect({
      base_url = "http://127.0.0.1:1234",
      request  = function(method, url, body, headers)
        return my_http(method, url, body, headers)  -- e.g. Roblox HttpService
      end,
    })
    local p = buddy:get_player()

Options: `{ base_url = string?, request = fn?, client_name = string? }`. The
default LuaSocket transport is used only when no `request` is given.

## Dependencies (standard Lua / LuaJIT)

The default transport uses **LuaSocket**: `luarocks install luasocket`. JSON is
vendored (`rs3buddy/json.luau`), so no JSON rock is required.

On Linux/macOS that one `luarocks` line is all you need. On **Windows without
Visual Studio**, build LuaSocket with the LLVM toolchain (clang-cl) against a
LuaBinaries `lua54.dll` — no VS / no MSVC required:

    :: 1. import lib straight from the DLL
    llvm-readobj --coff-exports lua54.dll          :: collect the lua_* / luaL_* names
    ::   write lua54.def:  first line  "LIBRARY lua54.dll"  then "EXPORTS" then the names
    llvm-lib /def:lua54.def /machine:x64 /out:lua54.lib

    :: 2. socket/core.dll  (/MT static CRT -> no vcruntime dependency)
    clang-cl /O2 /MT /LD /I<lua headers> ^
      luasocket.c timeout.c buffer.c io.c auxiliar.c options.c inet.c tcp.c udp.c ^
      except.c select.c wsocket.c compat.c ^
      /Fe:socket\core.dll /link lua54.lib ws2_32.lib /EXPORT:luaopen_socket_core

Two gotchas that cost real time, documented so you don't hit them: the `.def`
**must** carry a `LIBRARY lua54.dll` line (without it the import lib leaves the
DLL name blank and the module fails to load with "specified module could not be
found"), and link `/MT` (static CRT) so the DLL doesn't depend on a separately
installed MSVC runtime.

## UI overlay

Author a HUD as HTML + CSS and POST it; the server compiles it to the same widget
engine the SDK renders (clicks / drag / scaling all work). Your code owns the
state: poll `buddy.ui:events()` for clicks (each event carries the clicked
widget's `id`) and re-render by calling `buddy.ui:html(...)` again.

    local buddy = rs3buddy.connect()
    buddy.ui:html(
      "<panel anchor='top-right' draggable consume>"
        .. "<span class='title'>Hello</span>"
        .. "<button id='close' class='danger'>Exit</button></panel>",
      "panel{background:#241d12;padding:12px;gap:8px}"
        .. ".title{color:#f5c54a}.danger{background:#b03b30}"
    )
    -- ... later, in your loop:
    for _, e in ipairs(buddy.ui:events().events) do
      if e.id == "close" then buddy.ui:clear() end
    end

`buddy.ui` methods (all colon-called):

- `ui:html(html, css?)` — render an HTML + CSS page (POST `/api/ui/html`). The
  primary authoring path; replaces the current overlay.
- `ui:render(tree)` — render a raw widget tree `{ type, props, children }` (POST
  `/api/ui`). Same engine as `ui:html`.
- `ui:clear()` — clear the overlay (DELETE `/api/ui`).
- `ui:events()` — drain queued interaction events (GET `/api/ui/events`); returns
  `{ events = { { type, id, x, y }, ... } }`.
- `ui:scaling({ exponent?, scale?, base_height? })` — configure hi-DPI / 4K
  display-scaling (POST `/api/ui/scaling`); only the given keys are sent
  (`base_height` maps to the server's `baseHeight`).

A full, runnable HUD (state + poll loop + re-render) is in
**`examples/hud.luau`** — the Luau port of the Java `Hud` example.

## Sound

Play a developer-supplied clip through the desktop app's audio host:

    buddy.sound:play({ file = "C:/sounds/ping.wav" })   -- path or file:/data:/http(s): URL
    buddy.sound:play({ bytes = b64, mime = "audio/wav", volume = 0.5 })  -- base64 audio

`sound:play(opts)` (POST `/api/sound`) takes `{ file?, bytes?, mime?, volume? }` —
provide **either** `file` **or** base64 `bytes` (with optional `mime`, default
`audio/wav`); `volume` is an optional 0..1 gain. Only the given keys are sent.
Returns `{ ok = true }`, or `{ ok = false, error = ... }` when no audio host is
available (sound requires the desktop app).

## Typing

- **Luau:** `rs3buddy/types.luau` — native `export type` defs, `--!strict` clean.
- **Standard Lua (LuaLS):** `rs3buddy/types.luacats.lua` — LuaCATS `---@class`
  annotations; point your editor at this dir (see `.luarc.json`).

Both are generated from the server schema and committed — no build step.
