# rs3buddy — Overview

Version 0.1.1 · last updated 2026-06-21

## What it is

rs3buddy is a local SDK for building RuneScape 3 overlays and tools. Your program
talks to a small HTTP server on `localhost`, reads live game state, and draws UI
back into the game.

## How it fits together

```
RuneScape 3 (NXT) ──GPU draws──▶ native hook ──▶ rs3buddy engine ──HTTP──▶ your app
                                      ▲                                   (TS / Python /
                          launcher injects the hook                       Lua / Java client)
                          and runs the engine
```

- **Launcher** — you run it. It injects the hook into the game, starts the engine,
  and writes a config file (`%APPDATA%\rs3buddy\rs3buddy.json`) your client
  auto-discovers, so `connect()` needs no URL.
- **Hook** — a native library inside the game process. It records the GPU draw
  calls the game makes and the mouse position. It does **not** read game memory and
  does **not** send input.
- **Engine** — turns those draws into readable data (chat, stat orbs, abilities,
  the 3D scene) with on-screen recognition, serves it over HTTP, and renders your
  overlays back into the game.
- **Client** — the thin per-language wrapper you install. `connect()` finds the
  server; methods like `bars.read()` fetch data.

## What it can read

All of it comes from what's drawn on screen:

- Chat lines, the stat orbs (HP / prayer / adrenaline / summoning), the action bar,
  progress bars (skilling / crafting actions, Necromancy conjure timers, the
  adrenaline bar), the 3D scene (objects, players, tiles), and recognised
  fonts / sprites.
- The mouse position, for cursor-following overlays and tooltips.

Never the keyboard, never game memory, never injected input.

## What it can draw

- In-world shapes (markers, tiles, lines) anchored to game-world positions.
- An HTML/CSS UI overlay (panels, buttons, gauges) that scales to the display.
- A desktop cursor tooltip — text or image — that follows the mouse anywhere on screen.

## Safety model

- Reads only the game's GPU draws and the mouse. No memory reading, no input
  injection, no automation of gameplay.
- Some reads are **opt-in** (player detection, the local player's name) and are
  gathered only when your app asks for them.

Next: the [developer guide](../developer-guide.md) to get running, then your
language's API reference for every method.
