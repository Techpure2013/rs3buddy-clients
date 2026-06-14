package com.rs3buddy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CoverageTest {
    static final String[] EXPECTED = {
        "getPlayer", "updatePlayer", "getStatus", "getWindow", "getHeap",
        "captureFrame", "getShaders", "getTextures", "getCapturedTextures",
        "refreshCapturedTextures", "capturedTexturesStats", "clearCapturedTextures",
        "getCapturedTexture", "readTexture", "screenCapture", "getGlyphsOnScreen", "glyphAtPoint",
        "getScene", "getScenePlayer", "getNpcs", "getScenery", "getFloorTiles", "getWater", "getEntityAt",
        "drawShape", "drawScene", "listShapes", "removeShape", "clearShapes",
        "listFonts", "registerFont", "unregisterFont",
        "listTrainedSprites", "saveTrainedSprite", "deleteTrainedSprite",
        "importSpriteHash", "trainQuad", "atlasSync", "atlasSprites", "atlasLookup", "recognizeQuads",
        "matchText", "matchRegion",
    };

    @Test
    void exposesEveryRouteMethod() {
        Set<String> methods = Arrays.stream(RS3Buddy.class.getMethods())
            .map(java.lang.reflect.Method::getName).collect(Collectors.toSet());
        for (String name : EXPECTED) {
            assertTrue(methods.contains(name), "missing method: " + name);
        }
    }
}
