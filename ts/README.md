# @rs3buddy/client (TypeScript)

Typed HTTP client for the rs3buddy server. Behavior contract: `../CONTRACT.md`.

## Install (standalone dev, from git)

    npm install <git-url>#path:clients/ts

## Local development of this package

Run `npm install` inside `clients/ts/` once so the editor's TypeScript server can
resolve `@types/node` (the source uses Node globals: `Buffer`, `URL`, `http`).
Without it you'll see `Cannot find name 'Buffer'/'URL'` and implicit-`any` errors.

## Use

    import { RS3Buddy } from "@rs3buddy/client";
    const buddy = new RS3Buddy();            // reads rs3buddy.json, or pass { baseUrl }
    const p = await buddy.getPlayer();       // Position | null
    setInterval(async () => {
      const cur = (await buddy.updatePlayer()) ?? (await buddy.getPlayer());
    }, 250);

## UI overlay

Author your HUD as HTML + CSS and POST it; the server compiles it to the same
widget engine the SDK renders (clicks / drag / scaling all work). Your app owns
the state: poll `ui.events()` for clicks (each event carries the clicked
widget's `id`) and re-render by calling `ui.html()` again — only when it changed.

    const buddy = RS3Buddy.connect();
    await buddy.ui.html(
      "<panel anchor='top-right' draggable consume>" +
      "  <button id='hello'>Hello</button>" +
      "</panel>",
      "panel{background:#241d12;padding:12px}",
    );

    // event loop: react to clicks, re-render, repeat
    for (;;) {
      const { events } = await buddy.ui.events();   // [{ type, id, x, y }]
      for (const e of events) {
        if (e.id === "hello") console.log("clicked!");
      }
      // await buddy.ui.html(nextHtml, css);  // re-render when state changes
      await new Promise((r) => setTimeout(r, 300));
    }

Other methods: `ui.render(tree)` (raw `{ type, props, children }`),
`ui.clear()`, and `ui.scaling({ exponent, scale, baseHeight })` for hi-DPI / 4K.

See `examples/hud.ts` for a complete draggable, minimizable tracker HUD.

## Sound

Play a sound through the desktop app — either a host-side file path or inline
base64 bytes (+ mime), with an optional `volume` (0..1). Requires the desktop
audio host.

    await buddy.sound.play({ file: "C:/sounds/ping.wav", volume: 0.8 });
    // or inline:
    await buddy.sound.play({ bytes: base64Wav, mime: "audio/wav" });
