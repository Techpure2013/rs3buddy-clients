import test from "node:test";
import assert from "node:assert/strict";
import { RS3Buddy } from "../dist/index.js";

const EXPECTED = [
  "getPlayer","updatePlayer","getStatus","getWindow","getHeap",
  "captureFrame","getShaders","getTextures","getCapturedTextures",
  "refreshCapturedTextures","capturedTexturesStats","clearCapturedTextures",
  "getCapturedTexture","readTexture","screenCapture","getGlyphsOnScreen","glyphAtPoint",
  "getScene","getScenePlayer","getNpcs","getScenery","getFloorTiles","getWater","getEntityAt",
  "drawShape","drawScene","listShapes","removeShape","clearShapes",
  "listFonts","registerFont","unregisterFont",
  "listTrainedSprites","saveTrainedSprite","deleteTrainedSprite",
  "importSpriteHash","trainQuad","atlasSync","atlasSprites","atlasLookup","recognizeQuads",
  "matchText","matchRegion",
];

test("client exposes every route-table function", () => {
  const buddy = new RS3Buddy({ baseUrl: "http://127.0.0.1:1" });
  for (const name of EXPECTED) {
    assert.equal(typeof buddy[name], "function", `missing method: ${name}`);
  }
});
