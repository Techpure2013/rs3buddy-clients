# rs3buddy — Troubleshooting

Version 0.1.1 · last updated 2026-06-21

**`connect()` throws / can't reach the server.**
The launcher must be running and have injected the game. Start the launcher, make
sure RS3 (NXT) is open, then run your app. `connect()` reads
`%APPDATA%\rs3buddy\rs3buddy.json` (falling back to `%USERPROFILE%`); if it's missing,
the launcher hasn't started the engine yet.

**A read returns `null` / `None` / `nil`.**
Normal when there's nothing to read (the thing isn't on screen, or isn't tracked
yet). Call again — recognition needs the target actually visible.

**Chat / bars / abilities come back empty.**
The engine reads what's drawn, so the game must be rendering (focused, or with
background rendering enabled) and the chatbox / orbs / action bar must be on screen.

**The overlay doesn't show, or vanishes.**
Each `ui.html(...)` **replaces** the overlay — if your loop stops calling it, it
clears. Keep calling `ui.html` (or `ui.render`), and check the panel's `anchor`
isn't off-screen.

**Lua on a sandboxed host (no `io`/`os`, e.g. Roblox).**
Auto-discovery can't read the config file. Pass an explicit base URL + an HTTP
request function:
`rs3buddy.connect({ base_url = "http://127.0.0.1:<port>", request = my_http_fn })`.

**Java: `com.rs3buddy:rs3buddy-client:0.1.1` won't resolve.**
There is no published Maven artifact. Build it locally (`gradle jar` in `java/`, or
`includeBuild`) — see [java/README.md](../java/README.md).

**Sound won't play / an image tooltip is blank.**
These are engine features — make sure you're on a current engine (the launcher
auto-updates on restart).
