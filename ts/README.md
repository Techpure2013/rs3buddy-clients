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
