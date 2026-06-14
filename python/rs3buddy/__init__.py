"""rs3buddy — typed Python client for the rs3buddy HTTP server.

Behavior contract: ../CONTRACT.md
"""
from . import models as models
from .client import RS3Buddy
from .errors import RS3BuddyError, RS3BuddyConnectionError

__all__ = ["RS3Buddy", "RS3BuddyError", "RS3BuddyConnectionError", "models"]
