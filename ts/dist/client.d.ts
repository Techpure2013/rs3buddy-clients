import { TransportOptions } from "./transport";
import type { Position, CaptureOptions, ShaderInfo, TextureInfo, SceneSnapshot, ChatReadResult, DrawItem, PostFxPassInput, ShaderFxInput, PlayerNameResult, FrameCaptureResult } from "./models";
/** Optional chatbox region override (window px). */
export interface ChatReadOptions {
    x0?: number;
    y0?: number;
    x1?: number;
    y1?: number;
}
export declare class RS3Buddy {
    private readonly t;
    constructor(opts?: TransportOptions);
    /**
     * Connect to the running SDK server. With no options, auto-discovers the
     * server from the launcher's config (rs3buddy.json under %APPDATA%\rs3buddy,
     * then $RS3BUDDY_CONFIG / the cwd); the launcher must be running. Pass
     * { baseUrl } only to target a specific server.
     */
    static connect(opts?: TransportOptions): RS3Buddy;
    get baseUrl(): string;
    /** Acquire the player position (cold call may take a frame or two). */
    getPlayer(): Promise<Position | null>;
    /** Cheap refresh; null if not currently tracked → call getPlayer() again. */
    updatePlayer(): Promise<Position | null>;
    /**
     * The local player's name + title(s), recovered from the chat input prompt
     * (opt-in; the name never appears in the chat feed itself). `found` is false
     * until the prompt has been seen at least once.
     */
    getPlayerName(): Promise<PlayerNameResult>;
    getStatus(): Promise<unknown>;
    getWindow(): Promise<unknown>;
    getHeap(): Promise<unknown>;
    captureFrame(opts?: CaptureOptions): Promise<FrameCaptureResult>;
    getShaders(): Promise<ShaderInfo[]>;
    getTextures(): Promise<TextureInfo[]>;
    getCapturedTextures(): Promise<unknown>;
    refreshCapturedTextures(): Promise<unknown>;
    capturedTexturesStats(): Promise<unknown>;
    clearCapturedTextures(): Promise<unknown>;
    getCapturedTexture(id: number): Promise<unknown>;
    readTexture(id: number, x?: number, y?: number, w?: number, h?: number): Promise<unknown>;
    screenCapture(): Promise<unknown>;
    getGlyphsOnScreen(opts?: Record<string, number>): Promise<unknown>;
    glyphAtPoint(x: number, y: number, opts?: Record<string, number>): Promise<unknown>;
    getScene(): Promise<SceneSnapshot>;
    getScenePlayer(): Promise<unknown>;
    getNpcs(radius?: number, floor?: number): Promise<unknown>;
    getScenery(radius?: number, floor?: number): Promise<unknown>;
    getFloorTiles(radius?: number, floor?: number): Promise<unknown>;
    getWater(radius?: number, floor?: number): Promise<unknown>;
    getEntityAt(x: number, z: number, floor?: number): Promise<unknown>;
    drawShape(shape: DrawItem): Promise<unknown>;
    drawScene(shapes: DrawItem[]): Promise<unknown>;
    listShapes(): Promise<unknown>;
    removeShape(id: string): Promise<unknown>;
    clearShapes(group?: string): Promise<unknown>;
    /** List the registered render/DRAW fonts (alias -> .ttf), NOT the recognition registry. */
    listFonts(): Promise<unknown>;
    registerFont(name: string, p: string): Promise<unknown>;
    unregisterFont(name: string): Promise<unknown>;
    listTrainedSprites(): Promise<unknown>;
    saveTrainedSprite(req: unknown): Promise<unknown>;
    deleteTrainedSprite(name: string): Promise<unknown>;
    importSpriteHash(req: unknown): Promise<unknown>;
    trainQuad(req: unknown): Promise<unknown>;
    atlasSync(): Promise<unknown>;
    atlasSprites(): Promise<unknown>;
    atlasLookup(hash: number): Promise<unknown>;
    recognizeQuads(quads: unknown[]): Promise<unknown>;
    matchText(req: unknown): Promise<unknown>;
    matchRegion(req: unknown): Promise<unknown>;
    /** Register or replace a fullscreen post-FX pass; returns its id. */
    addPostFx(pass: PostFxPassInput): Promise<{
        id: string;
    }>;
    /** All post-FX passes, in render order (ascending `order`). */
    listPostFx(): Promise<PostFxPassInput[]>;
    /** Remove a post-FX pass. */
    removePostFx(id: string): Promise<{
        removed: boolean;
    }>;
    /** Enable/disable a post-FX pass without removing it. */
    setPostFxEnabled(id: string, enabled: boolean): Promise<{
        ok: boolean;
    }>;
    /** Register or replace a custom game-shader FX; returns its id. */
    addShaderFx(o: ShaderFxInput): Promise<{
        id: string;
    }>;
    /** All active custom game-shader FX. */
    listShaderFx(): Promise<ShaderFxInput[]>;
    /** Remove a shader FX (that shader reverts to stock). */
    removeShaderFx(id: string): Promise<{
        removed: boolean;
    }>;
    /** Enable/disable a shader FX without removing it. */
    setShaderFxEnabled(id: string, enabled: boolean): Promise<{
        ok: boolean;
    }>;
    /**
     * Read the in-game chatbox as structured lines + per-glyph colour.
     * Thin wrapper over GET /api/chat; all the heavy work (frame + screen
     * capture, glyph recognition, line grouping, colour sampling) runs
     * server-side. Pass a region override to read a non-default box.
     */
    readonly chat: {
        read: (opts?: ChatReadOptions) => Promise<ChatReadResult>;
    };
}
//# sourceMappingURL=client.d.ts.map