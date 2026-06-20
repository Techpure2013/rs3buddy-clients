package com.rs3buddy;

import com.fasterxml.jackson.databind.JsonNode;
import com.rs3buddy.models.Position;

/**
 * The "Tracker" HUD as a Java app — authored with HTML + CSS over the HTTP client.
 * Java owns the state and re-renders; buttons report clicks via buddy.ui.events().
 *
 * STALL-FREE: getPlayer() hits GET /api/player, which does the CHEAP passive read
 * (no GPU, no frame capture) — safe to poll. (Only ?detect=1 forces the heavy
 * capture-based detection; don't poll that.)
 *
 * Build + run: see ReadChat.java for the classpath details.
 */
public final class Hud {
    private static final String CSS = String.join("\n",
        "panel{background:#241d12;border:1px solid #b0904a;border-radius:10px;padding:12px;gap:8px;width:220px;align-items:stretch}",
        "header{display:flex;justify-content:space-between;align-items:center}",
        ".title{color:#f5c54a;font-size:16px}",
        ".ctrl{display:flex;gap:6px}",
        ".row{display:flex;justify-content:space-between;gap:12px}",
        ".k{color:#c9bfa6}.v{color:#ffffff}.accent{color:#8fd1ff}",
        ".btns{display:flex;gap:8px}.btns button{flex:1}",
        ".good{background:#3f7a3f}.danger{background:#b03b30}.info{background:#2f5a7a}");

    public static void main(String[] args) throws InterruptedException {
        RS3Buddy buddy = RS3Buddy.connect("hud-example");
        System.out.println("connected: " + buddy.baseUrl());

        boolean running = false, minimized = false, located = false;
        int ticks = 0, tileX = 0, tileZ = 0;
        Integer startX = null, startZ = null;
        String lastHtml = "";

        while (true) {
            // cheap passive read (GET /api/player -> buddy.player()) — safe to poll
            Position pos = null;
            try { pos = buddy.getPlayer(); } catch (RuntimeException ignore) { /* not tracked yet */ }
            if (pos != null && pos.getTileX() != null && pos.getTileZ() != null) {
                tileX = pos.getTileX().intValue();
                tileZ = pos.getTileZ().intValue();
                located = true;
            }

            JsonNode resp = buddy.ui.events();
            JsonNode events = (resp == null) ? null : resp.get("events");
            if (events != null) {
                for (JsonNode e : events) {
                    switch (e.path("id").asText("")) {
                        case "close": buddy.ui.clear(); return;
                        case "min":   minimized = !minimized; break;
                        case "start": running = !running; if (running) { startX = tileX; startZ = tileZ; } break;
                        case "reset": ticks = 0; startX = tileX; startZ = tileZ; break;
                        default: break;
                    }
                }
            }
            if (running && !minimized) ticks++;

            int dist = (startX != null) ? Math.abs(tileX - startX) + Math.abs(tileZ - startZ) : 0;
            int secs = (int) (ticks * 0.6) % 60, mins = (int) (ticks * 0.6) / 60;
            String tile = located ? (tileX + ", " + tileZ) : "—";

            String html;
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
                     +   "<div class='row'><span class='k'>Tile</span><span class='accent'>" + tile + "</span></div>"
                     +   "<div class='row'><span class='k'>Distance</span><span class='v'>" + dist + " tiles</span></div>"
                     +   "<div class='row'><span class='k'>Time</span><span class='v'>" + mins + ":" + (secs < 10 ? "0" : "") + secs + "</span></div>"
                     +   "<div class='row'><span class='k'>Ticks</span><span class='v'>" + ticks + "</span></div>"
                     +   "<hr/>"
                     +   "<div class='btns'>"
                     +     "<button id='start' class='" + (running ? "" : "good") + "'>" + (running ? "Pause" : "Start") + "</button>"
                     +     "<button id='reset' class='danger'>Reset</button>"
                     +   "</div>"
                     +   "<div class='btns'>"
                     +     "<button id='min' class='info'>Minimize</button>"
                     +     "<button id='close' class='danger'>Exit</button>"
                     +   "</div>"
                     + "</panel>";
            }

            // re-POST only when the HUD actually changed (avoids needless work)
            if (!html.equals(lastHtml)) {
                buddy.ui.html(html, CSS);
                lastHtml = html;
            }
            Thread.sleep(300);
        }
    }
}
