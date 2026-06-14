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

## Typing

- **Luau:** `rs3buddy/types.luau` — native `export type` defs, `--!strict` clean.
- **Standard Lua (LuaLS):** `rs3buddy/types.luacats.lua` — LuaCATS `---@class`
  annotations; point your editor at this dir (see `.luarc.json`).

Both are generated from the server schema and committed — no build step.
