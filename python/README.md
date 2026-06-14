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
