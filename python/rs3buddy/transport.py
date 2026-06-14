"""HTTP transport: keep-alive connection reuse + rs3buddy.json port resolution."""
from __future__ import annotations

import json
import os
from http.client import HTTPConnection
from typing import Any
from urllib.parse import urlparse

from .errors import RS3BuddyError, RS3BuddyConnectionError


def _read_config() -> dict[str, Any] | None:
    candidates = []
    env = os.environ.get("RS3BUDDY_CONFIG")
    if env:
        candidates.append(env)
    # Well-known per-user path the launcher writes on serve() (USERPROFILE fallback).
    app_data = os.environ.get("APPDATA") or os.environ.get("USERPROFILE")
    if app_data:
        candidates.append(os.path.join(app_data, "rs3buddy", "rs3buddy.json"))
    candidates.append(os.path.join(os.getcwd(), "rs3buddy.json"))
    for path in candidates:
        try:
            with open(path, "r", encoding="utf-8") as fh:
                data = json.load(fh)
            if isinstance(data, dict):
                return data
        except (OSError, ValueError):
            continue
    return None


def resolve_base_url(base_url: str | None = None) -> str:
    """Explicit base_url wins, else read the port from rs3buddy.json."""
    if base_url:
        return base_url.rstrip("/")
    cfg = _read_config()
    if cfg is not None:
        port = cfg.get("port")
        if isinstance(port, int):
            return f"http://127.0.0.1:{port}"
    raise RS3BuddyConnectionError(
        "No base_url given and rs3buddy.json not found "
        "(set RS3BUDDY_CONFIG, run from the launcher dir, or pass base_url=...)."
    )


class Transport:
    """One HTTPConnection reused across calls = keep-alive (no socket per call)."""

    def __init__(
        self,
        base_url: str | None = None,
        client_name: str | None = None,
        timeout: float = 5.0,
    ) -> None:
        self.base_url = resolve_base_url(base_url)
        parsed = urlparse(self.base_url)
        self._host = parsed.hostname or "127.0.0.1"
        self._port = parsed.port or 80
        self._client_name = client_name
        self._timeout = timeout
        self._conn: HTTPConnection | None = None

    def _connection(self) -> HTTPConnection:
        if self._conn is None:
            self._conn = HTTPConnection(self._host, self._port, timeout=self._timeout)
        return self._conn

    def request(self, method: str, route: str, body: Any = None) -> Any:
        payload = None if body is None else json.dumps(body)
        headers = {"Accept": "application/json"}
        if payload is not None:
            headers["Content-Type"] = "application/json"
        if self._client_name:
            headers["X-Client-Name"] = self._client_name

        conn = self._connection()
        try:
            conn.request(method, route, body=payload, headers=headers)
            resp = conn.getresponse()
            raw = resp.read().decode("utf-8")
        except OSError as exc:
            # Reset the connection so the next call reconnects.
            self._conn = None
            raise RS3BuddyConnectionError(
                f"request to {route} failed: {exc}", exc
            ) from exc

        parsed: Any = None
        if raw:
            try:
                parsed = json.loads(raw)
            except ValueError:
                parsed = raw

        if not (200 <= resp.status < 300):
            message = (
                parsed["error"]
                if isinstance(parsed, dict) and isinstance(parsed.get("error"), str)
                else f"HTTP {resp.status}"
            )
            raise RS3BuddyError(resp.status, message, parsed)
        return parsed

    def close(self) -> None:
        if self._conn is not None:
            self._conn.close()
            self._conn = None
