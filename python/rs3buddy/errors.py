"""Typed error hierarchy for the rs3buddy client."""
from __future__ import annotations
from typing import Any


class RS3BuddyError(Exception):
    """Server returned a non-2xx HTTP status."""

    def __init__(self, status: int, message: str, body: Any = None) -> None:
        super().__init__(message)
        self.status = status
        self.body = body


class RS3BuddyConnectionError(Exception):
    """Could not reach the server (down, wrong port, or game crashed)."""

    def __init__(self, message: str, cause: BaseException | None = None) -> None:
        super().__init__(message)
        self.cause = cause
