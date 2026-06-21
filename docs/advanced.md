# rs3buddy — Advanced features

Version 0.1.0 · last updated 2026-06-21

These are low-level surfaces. **Most apps never need them** — the high-level readers
(`chat` / `bars` / `abilities`) and the UI/draw APIs cover normal work. Reach for
these only when the readers can't express what you need. Per-language method
signatures are in the API reference ([TS](../ts/README.md) · [Python](../python/README.md)
· [Lua](../lua/README.md) · [Java](../java/README.md)); this page is **what each is
for and when to use it**.

## Atlas / recognition

**What:** the raw texture-atlas + sprite-recognition layer the readers are built on
— list atlas sprites, look one up by hash, train a recognition entry from a drawn
quad, recognise quads, import a precomputed sprite hash.

**When:** only to recognise something the built-in readers don't cover (a custom
interface element, a modded sprite), or to build your own recognition set. For
chat / stats / abilities, use the readers — they already do this for you.

Methods: `atlasSprites`, `atlasSync`, `atlasLookup`, `trainQuad`, `recognizeQuads`, `importSpriteHash`.

## Shaders & FX

**What:** inspect loaded GLSL programs (`getShaders`), register fullscreen **post-FX**
passes, or replace RS3's own **game-shader FX**.

**When:** visual effects only — a grayscale / night-vision pass, recolouring a stock
shader. A post-FX pass samples the previous result via `uScene` (builtins
`uScene`/`uResolution`/`uTime`/`uFrame`); a shader FX replaces a stock shader matched
by classified type or source hash. A bad compile falls back to the built-in shader,
so a typo never breaks your overlay. Visual and heavy — not something you poll.

Methods: `getShaders`; `addPostFx` / `listPostFx` / `removePostFx` / `setPostFxEnabled`;
`addShaderFx` / `listShaderFx` / `removeShaderFx` / `setShaderFxEnabled`.

## Capture & textures

**What:** heavy GPU capture — a full frame's draw list (`captureFrame`), the texture
table (`getTextures`), raw texture / glyph pixels, on-screen glyph boxes.

**When:** tooling and debugging (inspecting what the game drew) or building a new
reader. **A capture forces a frame readback — never poll it.** For live data use the
high-level readers, which are cached and cheap.

Methods: `captureFrame`, `getTextures`, `readTexture`, `screenCapture`,
`getCapturedTextures` (+ `refresh` / `stats` / `clear` / `get-one`),
`getGlyphsOnScreen`, `glyphAtPoint`.

## OCR

**What:** general text recognition for an arbitrary screen region (`matchText`,
`matchRegion`), separate from the structured readers.

**When:** reading text the structured readers don't cover (a custom panel, an
interface number). For the chatbox or stat orbs, use `chat.read` / `bars.read` —
they're purpose-built and faster.

Methods: `matchText`, `matchRegion`.
