# rs3buddy (Python)

Typed HTTP client for the rs3buddy server. Behavior contract: `../CONTRACT.md`.

## Install (standalone dev, from git)

    pip install "git+<git-url>#subdirectory=clients/python"

Types ship in the package (`py.typed` + generated `models.py`) — Pyright/mypy and
editor autocomplete work with no build step.

## Use

    from rs3buddy import RS3Buddy
    buddy = RS3Buddy()                 # reads rs3buddy.json, or RS3Buddy(base_url=...)
    p = buddy.get_player()             # Position | None
    while True:
        cur = buddy.update_player() or buddy.get_player()

## UI overlay

Author a HUD as HTML + CSS and POST it; it compiles to the same widget engine
the SDK renders, so drag / clicks / scaling all work. Your app owns the state:
poll `buddy.ui.events()` for clicks (each event carries the clicked widget's
`id`) and re-render by calling `buddy.ui.html(...)` again.

    buddy.ui.html(
        "<panel anchor='top-right' draggable consume>"
        "<button id='hello'>Hello</button>"
        "</panel>",
        "panel{background:#241d12;padding:12px}",
    )

    for e in buddy.ui.events().get("events", []):
        if e["id"] == "hello":
            print("clicked!")

    # buddy.ui.render(tree)                       # raw widget tree instead of HTML
    # buddy.ui.scaling(exponent=1.5)              # tune hi-DPI / 4K sizing
    # buddy.ui.clear()                            # remove the overlay

A full, runnable HUD (live player tile + timer + buttons) is in
[`examples/hud.py`](examples/hud.py).

## Sound

Play a sound through the desktop app's audio host — by file path/URL, or by
base64 bytes + MIME. `volume` is an optional 0..1 gain.

    buddy.sound.play(file=r"C:\sounds\ping.wav")
    # buddy.sound.play(bytes=b64, mime="audio/wav", volume=0.5)
