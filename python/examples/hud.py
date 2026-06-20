"""Example: a draggable "Tracker" HUD authored from Python.

The HUD is plain HTML + CSS POSTed to the overlay; Python owns the state and
re-renders. Buttons report clicks via buddy.ui.events() (each event carries the
clicked widget's id).

STALL-FREE: get_player() hits GET /api/player, which does the CHEAP passive read
(no GPU, no frame capture) — safe to poll every loop. (Only ?detect=1 forces the
heavy capture-based detection; this example never does that.)

Prerequisite: the rs3buddy launcher must be running — it injects the native hook
into the RS3 client, which starts the SDK server and writes the config that
connect() auto-discovers. With nothing running, the call below raises
RS3BuddyConnectionError.

Run (after `pip install rs3buddy`, or from clients/python with PYTHONPATH=.):
    python examples/hud.py
"""

import time

from rs3buddy import RS3Buddy, RS3BuddyConnectionError

CSS = "\n".join([
    "panel{background:#241d12;border:1px solid #b0904a;border-radius:10px;padding:12px;gap:8px;width:220px;align-items:stretch}",
    "header{display:flex;justify-content:space-between;align-items:center}",
    ".title{color:#f5c54a;font-size:16px}",
    ".ctrl{display:flex;gap:6px}",
    ".row{display:flex;justify-content:space-between;gap:12px}",
    ".k{color:#c9bfa6}.v{color:#ffffff}.accent{color:#8fd1ff}",
    ".btns{display:flex;gap:8px}.btns button{flex:1}",
    ".good{background:#3f7a3f}.danger{background:#b03b30}.info{background:#2f5a7a}",
])


def main() -> None:
    buddy = RS3Buddy.connect(client_name="hud-example")
    print("connected:", buddy.base_url)

    running = False
    minimized = False
    located = False
    ticks = 0
    tile_x = 0
    tile_z = 0
    start_x = None
    start_z = None
    last_html = ""

    while True:
        # cheap passive read (GET /api/player) — safe to poll
        try:
            pos = buddy.get_player()
        except RS3BuddyConnectionError:
            pos = None  # not tracked yet
        if pos and pos.get("tileX") is not None and pos.get("tileZ") is not None:
            tile_x = int(pos["tileX"])
            tile_z = int(pos["tileZ"])
            located = True

        resp = buddy.ui.events()
        events = resp.get("events") if resp else None
        if events:
            for e in events:
                eid = e.get("id", "")
                if eid == "close":
                    buddy.ui.clear()
                    return
                elif eid == "min":
                    minimized = not minimized
                elif eid == "start":
                    running = not running
                    if running:
                        start_x, start_z = tile_x, tile_z
                elif eid == "reset":
                    ticks = 0
                    start_x, start_z = tile_x, tile_z
        if running and not minimized:
            ticks += 1

        dist = (abs(tile_x - start_x) + abs(tile_z - start_z)) if start_x is not None else 0
        secs = int(ticks * 0.6) % 60
        mins = int(ticks * 0.6) // 60
        tile = f"{tile_x}, {tile_z}" if located else "—"

        if minimized:
            html = (
                "<panel anchor='top-right' draggable consume>"
                "<header><span class='title'>Tracker</span><span class='ctrl'>"
                "<button id='min' icon='maximize' variant='plain'></button>"
                "<button id='close' icon='close' variant='plain'></button></span></header>"
                "</panel>"
            )
        else:
            html = (
                "<panel anchor='top-right' draggable consume>"
                "<header><span class='title'>Tracker</span><span class='ctrl'>"
                "<button id='min' icon='minimize' variant='plain'></button>"
                "<button id='close' icon='close' variant='plain'></button></span></header>"
                "<hr/>"
                f"<div class='row'><span class='k'>Tile</span><span class='accent'>{tile}</span></div>"
                f"<div class='row'><span class='k'>Distance</span><span class='v'>{dist} tiles</span></div>"
                f"<div class='row'><span class='k'>Time</span><span class='v'>{mins}:{secs:02d}</span></div>"
                f"<div class='row'><span class='k'>Ticks</span><span class='v'>{ticks}</span></div>"
                "<hr/>"
                "<div class='btns'>"
                f"<button id='start' class='{'' if running else 'good'}'>{'Pause' if running else 'Start'}</button>"
                "<button id='reset' class='danger'>Reset</button>"
                "</div>"
                "<div class='btns'>"
                "<button id='min' class='info'>Minimize</button>"
                "<button id='close' class='danger'>Exit</button>"
                "</div>"
                "</panel>"
            )

        # re-POST only when the HUD actually changed (avoids needless work)
        if html != last_html:
            buddy.ui.html(html, CSS)
            last_html = html
        time.sleep(0.3)


if __name__ == "__main__":
    main()
