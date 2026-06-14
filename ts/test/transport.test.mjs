import http from "node:http";
import test from "node:test";
import assert from "node:assert/strict";
import { Transport } from "../dist/transport.js";
import { RS3BuddyError, RS3BuddyConnectionError } from "../dist/errors.js";

function mockServer(handler) {
  return new Promise((resolve) => {
    const srv = http.createServer(handler);
    srv.listen(0, "127.0.0.1", () => resolve(srv));
  });
}
const portOf = (srv) => srv.address().port;

test("GET parses JSON and reuses a keep-alive socket", async () => {
  const seen = [];
  const srv = await mockServer((req, res) => {
    seen.push(req.headers["x-client-name"]);
    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify({ ok: true, sock: req.socket.remotePort }));
  });
  try {
    const t = new Transport({ baseUrl: `http://127.0.0.1:${portOf(srv)}`, clientName: "tester" });
    const a = await t.request("GET", "/api/x");
    const b = await t.request("GET", "/api/x");
    assert.equal(a.ok, true);
    assert.equal(seen[0], "tester");          // X-Client-Name sent
    assert.equal(a.sock, b.sock);             // same client socket reused (keep-alive)
  } finally {
    srv.close();
  }
});

test("non-2xx becomes RS3BuddyError with server message", async () => {
  const srv = await mockServer((req, res) => {
    res.statusCode = 400;
    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify({ error: "bad thing" }));
  });
  try {
    const t = new Transport({ baseUrl: `http://127.0.0.1:${portOf(srv)}` });
    await assert.rejects(() => t.request("GET", "/api/x"), (e) => {
      assert.ok(e instanceof RS3BuddyError);
      assert.equal(e.status, 400);
      assert.equal(e.message, "bad thing");
      return true;
    });
  } finally {
    srv.close();
  }
});

test("connection refused becomes RS3BuddyConnectionError", async () => {
  // Port 1 is privileged/closed → ECONNREFUSED.
  const t = new Transport({ baseUrl: "http://127.0.0.1:1" });
  await assert.rejects(() => t.request("GET", "/api/x"), (e) => {
    assert.ok(e instanceof RS3BuddyConnectionError);
    return true;
  });
});
