/**
 * Example: the "Tracker" HUD as a TypeScript app — authored with HTML + CSS
 * over the HTTP client. Your code owns the state and re-renders; buttons report
 * clicks via buddy.ui.events().
 *
 * STALL-FREE: getPlayer() hits GET /api/player, which does the CHEAP passive read
 * (no GPU, no frame capture) — safe to poll every loop. (Only ?detect=1 forces the
 * heavy capture-based detection; don't poll that.)
 *
 * Prerequisite: the rs3buddy launcher must be running — it injects the native
 * hook into the RS3 client, which starts the SDK server and writes the config
 * that connect() auto-discovers. With nothing running, connect()/the first call
 * rejects with RS3BuddyConnectionError.
 *
 * Run (after `npm install @rs3buddy/client`):
 *   npx ts-node examples/hud.ts      // or compile, then `node`
 */
import {
  RS3Buddy,
  RS3BuddyConnectionError,
  type Position,
} from "@rs3buddy/client";

const CSS = [
  "panel{background:#241d12;border:1px solid #b0904a;border-radius:10px;padding:12px;gap:8px;width:220px;align-items:stretch}",
  "header{display:flex;justify-content:space-between;align-items:center}",
  ".title{color:#f5c54a;font-size:16px}",
  ".ctrl{display:flex;gap:6px}",
  ".row{display:flex;justify-content:space-between;gap:12px}",
  ".k{color:#c9bfa6}.v{color:#ffffff}.accent{color:#8fd1ff}",
  ".btns{display:flex;gap:8px}.btns button{flex:1}",
  ".good{background:#3f7a3f}.danger{background:#b03b30}.info{background:#2f5a7a}",
].join("\n");

const sleep = (ms: number): Promise<void> => new Promise((r) => setTimeout(r, ms));

async function main(): Promise<void> {
  // No URL needed: connect() auto-discovers the server from the launcher's config
  // (%APPDATA%\rs3buddy\rs3buddy.json). A cold first read may take a frame or two.
  const buddy = RS3Buddy.connect({ clientName: "hud-example", timeoutMs: 15000 });
  console.log("connected:", buddy.baseUrl);

  let running = false, minimized = false, located = false;
  let ticks = 0, tileX = 0, tileZ = 0;
  let startX: number | null = null, startZ: number | null = null;
  let lastHtml = "";

  for (;;) {
    // cheap passive read (GET /api/player) — safe to poll
    let pos: Position | null = null;
    try {
      pos = await buddy.getPlayer();
    } catch (e) {
      if (e instanceof RS3BuddyConnectionError) {
        console.error("Could not reach the SDK server — is the launcher running?");
        console.error(" ", e.message);
        return;
      }
      // any other error: treat as "not tracked yet" and keep going
    }
    if (pos !== null && pos.tileX !== undefined && pos.tileZ !== undefined) {
      tileX = pos.tileX;
      tileZ = pos.tileZ;
      located = true;
    }

    // drain queued button clicks
    const resp = await buddy.ui.events().catch(() => null);
    for (const e of resp?.events ?? []) {
      switch (e.id) {
        case "close": await buddy.ui.clear(); return;
        case "min":   minimized = !minimized; break;
        case "start": running = !running; if (running) { startX = tileX; startZ = tileZ; } break;
        case "reset": ticks = 0; startX = tileX; startZ = tileZ; break;
        default: break;
      }
    }
    if (running && !minimized) ticks++;

    const dist = startX !== null && startZ !== null
      ? Math.abs(tileX - startX) + Math.abs(tileZ - startZ) : 0;
    const secs = Math.floor(ticks * 0.6) % 60, mins = Math.floor(Math.floor(ticks * 0.6) / 60);
    const tile = located ? `${tileX}, ${tileZ}` : "—";

    let html: string;
    if (minimized) {
      html = "<panel anchor='top-right' draggable consume>"
           +   "<header><span class='title'>Tracker</span><span class='ctrl'>"
           +     "<button id='min' icon='maximize' variant='plain'></button>"
           +     "<button id='close' icon='close' variant='plain'></button></span></header>"
           + "</panel>";
    } else {
      html = "<panel anchor='top-right' draggable consume>"
           +   "<header><span class='title'>Tracker</span><span class='ctrl'>"
           +     "<button id='min' icon='minimize' variant='plain'></button>"
           +     "<button id='close' icon='close' variant='plain'></button></span></header>"
           +   "<hr/>"
           +   `<div class='row'><span class='k'>Tile</span><span class='accent'>${tile}</span></div>`
           +   `<div class='row'><span class='k'>Distance</span><span class='v'>${dist} tiles</span></div>`
           +   `<div class='row'><span class='k'>Time</span><span class='v'>${mins}:${secs < 10 ? "0" : ""}${secs}</span></div>`
           +   `<div class='row'><span class='k'>Ticks</span><span class='v'>${ticks}</span></div>`
           +   "<hr/>"
           +   "<div class='btns'>"
           +     `<button id='start' class='${running ? "" : "good"}'>${running ? "Pause" : "Start"}</button>`
           +     "<button id='reset' class='danger'>Reset</button>"
           +   "</div>"
           +   "<div class='btns'>"
           +     "<button id='min' class='info'>Minimize</button>"
           +     "<button id='close' class='danger'>Exit</button>"
           +   "</div>"
           + "</panel>";
    }

    // re-POST only when the HUD actually changed (avoids needless work)
    if (html !== lastHtml) {
      await buddy.ui.html(html, CSS);
      lastHtml = html;
    }
    await sleep(300);
  }
}

void main();
