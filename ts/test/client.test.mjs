import http from "node:http";
import test from "node:test";
import assert from "node:assert/strict";
import { RS3Buddy } from "../dist/index.js";

function mockServer(handler) {
  return new Promise((resolve) => {
    const srv = http.createServer(handler);
    srv.listen(0, "127.0.0.1", () => resolve(srv));
  });
}
const portOf = (srv) => srv.address().port;

test("getPlayer returns the Position the server sends", async () => {
  const srv = await mockServer((req, res) => {
    assert.equal(req.url, "/api/player");
    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify({ tileX: 3200, tileZ: 3200, floor: 0, worldX: 1638400, worldZ: 1638400 }));
  });
  try {
    const buddy = new RS3Buddy({ baseUrl: `http://127.0.0.1:${portOf(srv)}` });
    const p = await buddy.getPlayer();
    assert.equal(p.tileX, 3200);
    assert.equal(p.floor, 0);
  } finally { srv.close(); }
});

test("updatePlayer returns null when no player (server sends null)", async () => {
  const srv = await mockServer((req, res) => {
    res.setHeader("Content-Type", "application/json");
    res.end("null");
  });
  try {
    const buddy = new RS3Buddy({ baseUrl: `http://127.0.0.1:${portOf(srv)}` });
    assert.equal(await buddy.updatePlayer(), null);
  } finally { srv.close(); }
});

test("captureFrame encodes filter options as query params", async () => {
  let seenUrl = "";
  const srv = await mockServer((req, res) => {
    seenUrl = req.url;
    res.setHeader("Content-Type", "application/json");
    res.end(JSON.stringify({ draws: [], timestamp: 1, drawCount: 0 }));
  });
  try {
    const buddy = new RS3Buddy({ baseUrl: `http://127.0.0.1:${portOf(srv)}` });
    await buddy.captureFrame({ includeMesh: true, filter: { targetFbo: 0 } });
    assert.match(seenUrl, /^\/api\/frame\?/);
    assert.match(seenUrl, /mesh=true/);
    assert.match(seenUrl, /targetFbo=0/);
  } finally { srv.close(); }
});

test("getEntityAt sends x,z,floor", async () => {
  let seenUrl = "";
  const srv = await mockServer((req, res) => { seenUrl = req.url; res.end("{}"); });
  try {
    const buddy = new RS3Buddy({ baseUrl: `http://127.0.0.1:${portOf(srv)}` });
    await buddy.getEntityAt(10, 20, 1);
    assert.match(seenUrl, /x=10/); assert.match(seenUrl, /z=20/); assert.match(seenUrl, /floor=1/);
  } finally { srv.close(); }
});
