"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.RS3Buddy = void 0;
const transport_1 = require("./transport");
function qs(params) {
    const p = new URLSearchParams();
    for (const [k, v] of Object.entries(params))
        if (v !== undefined)
            p.set(k, String(v));
    const s = p.toString();
    return s ? `?${s}` : "";
}
class RS3Buddy {
    t;
    constructor(opts = {}) {
        this.t = new transport_1.Transport(opts);
    }
    /**
     * Connect to the running SDK server. With no options, auto-discovers the
     * server from the launcher's config (rs3buddy.json under %APPDATA%\rs3buddy,
     * then $RS3BUDDY_CONFIG / the cwd); the launcher must be running. Pass
     * { baseUrl } only to target a specific server.
     */
    static connect(opts = {}) {
        return new RS3Buddy(opts);
    }
    get baseUrl() { return this.t.baseUrl; }
    // ── Player (rides the native passive tap; both hit GET /api/player) ──
    /** Acquire the player position (cold call may take a frame or two). */
    getPlayer() { return this.t.request("GET", "/api/player"); }
    /** Cheap refresh; null if not currently tracked → call getPlayer() again. */
    updatePlayer() { return this.t.request("GET", "/api/player"); }
    /**
     * The local player's name + title(s), recovered from the chat input prompt
     * (opt-in; the name never appears in the chat feed itself). `found` is false
     * until the prompt has been seen at least once.
     */
    getPlayerName() { return this.t.request("GET", "/api/player/name"); }
    // ── Status / window / heap ──
    getStatus() { return this.t.request("GET", "/api/status"); }
    getWindow() { return this.t.request("GET", "/api/window"); }
    getHeap() { return this.t.request("GET", "/api/heap"); }
    // ── Capture / shaders / textures ──
    captureFrame(opts = {}) {
        return this.t.request("GET", "/api/frame" + qs({
            mesh: opts.includeMesh ? "true" : undefined,
            pixels: opts.includeTexturePixels ? "true" : undefined,
            shaderId: opts.filter?.shaderId,
            meshId: opts.filter?.meshId,
            targetFbo: opts.filter?.targetFbo,
        }));
    }
    getShaders() { return this.t.request("GET", "/api/shaders"); }
    getTextures() { return this.t.request("GET", "/api/textures"); }
    getCapturedTextures() { return this.t.request("GET", "/api/captured-textures"); }
    refreshCapturedTextures() { return this.t.request("POST", "/api/captured-textures/refresh"); }
    capturedTexturesStats() { return this.t.request("GET", "/api/captured-textures/stats"); }
    clearCapturedTextures() { return this.t.request("DELETE", "/api/captured-textures"); }
    getCapturedTexture(id) { return this.t.request("GET", `/api/captured-texture/${id}`); }
    readTexture(id, x = 0, y = 0, w = 0, h = 0) {
        return this.t.request("GET", `/api/texture/${id}` + qs({ x, y, w, h }));
    }
    screenCapture() { return this.t.request("GET", "/api/screen-capture"); }
    getGlyphsOnScreen(opts = {}) {
        return this.t.request("GET", "/api/glyphs-on-screen" + qs(opts));
    }
    glyphAtPoint(x, y, opts = {}) {
        return this.t.request("POST", "/api/glyph-at-point", { x, y, ...opts });
    }
    // ── Scene ──
    getScene() { return this.t.request("GET", "/api/scene"); }
    getScenePlayer() { return this.t.request("GET", "/api/scene/player"); }
    getNpcs(radius, floor) { return this.t.request("GET", "/api/scene/npcs" + qs({ radius, floor })); }
    getScenery(radius, floor) { return this.t.request("GET", "/api/scene/scenery" + qs({ radius, floor })); }
    getFloorTiles(radius, floor) { return this.t.request("GET", "/api/scene/floor_tiles" + qs({ radius, floor })); }
    getWater(radius, floor) { return this.t.request("GET", "/api/scene/water" + qs({ radius, floor })); }
    getEntityAt(x, z, floor) { return this.t.request("GET", "/api/scene/at" + qs({ x, z, floor })); }
    // ── Draw / overlay ──
    drawShape(shape) { return this.t.request("POST", "/api/draw/shape", shape); }
    drawScene(shapes) { return this.t.request("POST", "/api/draw/scene", shapes); }
    listShapes() { return this.t.request("GET", "/api/draw"); }
    removeShape(id) { return this.t.request("DELETE", `/api/draw/${id}`); }
    clearShapes(group) { return this.t.request("DELETE", "/api/draw" + qs({ group })); }
    // ── Fonts ──
    /** List the registered render/DRAW fonts (alias -> .ttf), NOT the recognition registry. */
    listFonts() { return this.t.request("GET", "/api/fonts/registered"); }
    registerFont(name, p) { return this.t.request("POST", "/api/fonts/register", { name, path: p }); }
    unregisterFont(name) { return this.t.request("DELETE", `/api/fonts/${name}`); }
    // ── Sprites ──
    listTrainedSprites() { return this.t.request("GET", "/api/sprites/trained"); }
    saveTrainedSprite(req) { return this.t.request("POST", "/api/sprites/trained", req); }
    deleteTrainedSprite(name) { return this.t.request("DELETE", `/api/sprites/trained/${name}`); }
    // ── Atlas ──
    importSpriteHash(req) { return this.t.request("POST", "/api/atlas/import-spritehash", req); }
    trainQuad(req) { return this.t.request("POST", "/api/atlas/train-quad", req); }
    atlasSync() { return this.t.request("POST", "/api/atlas/sync"); }
    atlasSprites() { return this.t.request("GET", "/api/atlas/sprites"); }
    atlasLookup(hash) { return this.t.request("GET", `/api/atlas/lookup/${hash}`); }
    recognizeQuads(quads) { return this.t.request("POST", "/api/atlas/recognize-quads", { quads }); }
    // ── OCR ──
    matchText(req) { return this.t.request("POST", "/api/match/text", req); }
    matchRegion(req) { return this.t.request("POST", "/api/match/region", req); }
    // ── Post-FX (fullscreen post-processing passes) ──
    /** Register or replace a fullscreen post-FX pass; returns its id. */
    addPostFx(pass) { return this.t.request("POST", "/api/shaders/postfx", pass); }
    /** All post-FX passes, in render order (ascending `order`). */
    listPostFx() { return this.t.request("GET", "/api/shaders/postfx"); }
    /** Remove a post-FX pass. */
    removePostFx(id) { return this.t.request("DELETE", `/api/shaders/postfx/${id}`); }
    /** Enable/disable a post-FX pass without removing it. */
    setPostFxEnabled(id, enabled) { return this.t.request("POST", `/api/shaders/postfx/${id}/enabled`, { enabled }); }
    // ── Custom game-shader FX (replace RS3's own shaders, by type or hash) ──
    /** Register or replace a custom game-shader FX; returns its id. */
    addShaderFx(o) { return this.t.request("POST", "/api/shaders/fx", o); }
    /** All active custom game-shader FX. */
    listShaderFx() { return this.t.request("GET", "/api/shaders/fx"); }
    /** Remove a shader FX (that shader reverts to stock). */
    removeShaderFx(id) { return this.t.request("DELETE", `/api/shaders/fx/${id}`); }
    /** Enable/disable a shader FX without removing it. */
    setShaderFxEnabled(id, enabled) { return this.t.request("POST", `/api/shaders/fx/${id}/enabled`, { enabled }); }
    // ── Chat ──
    /**
     * Read the in-game chatbox as structured lines + per-glyph colour.
     * Thin wrapper over GET /api/chat; all the heavy work (frame + screen
     * capture, glyph recognition, line grouping, colour sampling) runs
     * server-side. Pass a region override to read a non-default box.
     */
    chat = {
        read: (opts = {}) => this.t.request("GET", "/api/chat" + qs({
            x0: opts.x0, y0: opts.y0, x1: opts.x1, y1: opts.y1,
        })),
    };
}
exports.RS3Buddy = RS3Buddy;
