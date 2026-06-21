# rs3buddy — Glossary

Version 0.1.1 · last updated 2026-06-21

- **Launcher** — the desktop app you run. Injects the hook, starts the engine, and writes the config clients auto-discover.
- **Hook** — the native library inside the game process that records GPU draws + the mouse position. No memory reads, no input injection.
- **Engine** — the local HTTP server that turns draws into readable data and renders your overlays back into the game.
- **Client** — the per-language wrapper (TS / Python / Lua / Java) you install; gives you `connect()` and the methods.
- **`connect()` / auto-discovery** — the client reads the launcher's config file to find the server port, so you pass no URL.
- **Reader** — a high-level recognition method: `chat.read`, `bars.read`, `abilities.read`.
- **Orbs / bars** — the HP / prayer / adrenaline / summoning indicators.
- **Scene** — the 3D world state (objects, players, tiles) the engine exposes.
- **UI overlay** — your in-game HUD, authored as HTML/CSS via `ui.html` and drawn by the engine. Each call replaces the overlay.
- **Draw layer** — in-world shapes (markers / tiles / lines) anchored to world positions, via `drawShape` / `drawScene`.
- **Cursor tooltip** — a desktop window (text or image) that follows the OS mouse anywhere on screen.
- **Atlas** — the texture-atlas + sprite-recognition layer the readers are built on (advanced).
- **Post-FX / shader FX** — fullscreen effect passes / stock-shader replacements (advanced).
- **Opt-in** — data gathered only when you ask for it (player detection, the local player's name).
- **`stale` / `ageMs`** — on a read result: whether cached recognition was reused, and how old it is.
