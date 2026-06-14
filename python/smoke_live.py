"""Manual: python clients/python/smoke_live.py
Requires a running server + rs3buddy.json, or RS3BUDDY_BASEURL env var.
"""
import os
import sys

sys.path.insert(0, os.path.dirname(__file__))
from rs3buddy import RS3Buddy  # noqa: E402

base = os.environ.get("RS3BUDDY_BASEURL")
buddy = RS3Buddy(base_url=base) if base else RS3Buddy()
print("base_url:", buddy.base_url)
status = buddy.get_status()
print("status.connected:", status.get("connected") if isinstance(status, dict) else status)
p = buddy.get_player()
for _ in range(8):
    if p:
        break
    p = buddy.update_player()
print("player:", p)
buddy.close()
