# rs3buddy client contract

Every language client (TS, Python, Lua, Java) implements the SAME behavior over the
existing HTTP/JSON server. The full route->function map lives in
`docs/plans/2026-05-31-multi-language-client-sdks-plan.md`. This file is the
behavioral contract.

## Base URL / port resolution
1. Explicit base URL passed to the client constructor/init wins.
2. Else read `rs3buddy.json` ({"port": <number>}) - search order:
   a. path in env var `RS3BUDDY_CONFIG`, b. current working directory,
   c. the launcher's known config dir.
3. Base URL = `http://127.0.0.1:<port>`.

## Connection
- Reuse ONE keep-alive HTTP connection across all calls (interval polling must not
  open a socket per call).
- Send header `X-Client-Name: <client-id>` when the caller provides one.

## Functions
- Plain free functions / methods returning typed models (generated from the schema).
- `getPlayer()` -> GET /api/player - acquire; may take a frame or two cold.
- `updatePlayer()` -> GET /api/player - cheap refresh; returns null if not currently tracked.

## Results vs errors
- A "no result" (e.g. no player on screen) -> null / None / nil. NOT an error.
- HTTP non-2xx -> `RS3BuddyError` (carries status code + the server's JSON error message).
- Server unreachable / game crashed (socket drops) -> `RS3BuddyConnectionError`.
