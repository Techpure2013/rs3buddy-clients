"""Example: read the RS3 chatbox from Python.

Prerequisite: the rs3buddy launcher must be running — it injects the native hook
into the RS3 client, which starts the SDK server and writes the config that
connect() auto-discovers. With nothing running, the call below raises
RS3BuddyConnectionError.

Run (after `pip install rs3buddy`, or from clients/python with PYTHONPATH=.):
    python examples/read_chat.py
"""

from rs3buddy import RS3Buddy, RS3BuddyConnectionError


def main() -> None:
    # No URL needed: connect() auto-discovers the server from the launcher's
    # config (%APPDATA%\rs3buddy\rs3buddy.json). A cold first read does a frame
    # capture, so give it a generous timeout.
    buddy = RS3Buddy.connect(timeout=15.0)

    try:
        chat = buddy.chat.read()
    except RS3BuddyConnectionError as e:
        print("Could not reach the SDK server.")
        print("Is the launcher running (native injected into RS3)?")
        print(" ", e)
        return

    print(f"{chat['lineCount']} chat lines:\n")
    for line in chat["lines"]:
        # line['color'] is the line's dominant colour: clan green, public white, ...
        print(f"  {line['color']}  {line['text']!r}")


if __name__ == "__main__":
    main()
