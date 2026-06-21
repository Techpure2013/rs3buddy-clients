import { TransportOptions } from "./transport";
import type { Position, CaptureOptions, ShaderInfo, TextureInfo, SceneSnapshot, ChatReadResult, BarsReadResult, AbilitiesReadResult, ProgressReadResult, DrawItem, PostFxPassInput, ShaderFxInput, PlayerNameResult, FrameCaptureResult } from "./models";
/** Optional chatbox region override (window px). */
export interface ChatReadOptions {
    x0?: number;
    y0?: number;
    x1?: number;
    y1?: number;
}
/** Optionally read just one bar type (by friendly name or colour-signature combo). */
export interface ProgressReadOptions {
    /** Friendly name you registered (e.g. "skilling", "conjure"). */
    name?: string;
    /** Raw colour-signature combo key. */
    combo?: string;
}
/** A queued UI interaction event (click / close / minimize). */
export interface UIEvent {
    type: string;
    id: string;
    x: number;
    y: number;
}
/** Response of GET /api/ui/events. */
export interface UIEvents {
    events: UIEvent[];
}
/** Auto display-scaling options (how big the UI is on hi-DPI / 4K). */
export interface UIScalingOptions {
    /** 1 = proportional (≈constant physical size); >1 = bigger on 4K (default 1.5). */
    exponent?: number;
    /** Explicit scale multiplier (overrides the exponent curve). */
    scale?: number;
    /** Reference window height the scaling is normalised against. */
    baseHeight?: number;
}
/** Sound playback options: a host-side file path OR base64 bytes (+ mime). */
export interface SoundPlayOptions {
    /** Path to an audio file on the host (or a file:/data:/http(s): URL). */
    file?: string;
    /** Base64-encoded audio, played inline; pair with `mime`. */
    bytes?: string;
    /** MIME type for inline `bytes` (default "audio/wav"). */
    mime?: string;
    /** Playback volume 0..1 (default host volume). */
    volume?: number;
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
    /**
     * Read the four status bars: each bar's current `value`, `max` (when the bar
     * shows current/max), `found`, and the located `anchor` + scanned `region`.
     * Thin wrapper over GET /api/bars; recognition runs server-side.
     */
    readonly bars: {
        read: () => Promise<BarsReadResult>;
    };
    /**
     * Read the action bar(s): each slot's `name`, `rect`, cooldown
     * (`onCooldown` / `cooldownText` / `cooldownSeconds`) and `usable` state.
     * Thin wrapper over GET /api/abilities; recognition runs server-side.
     */
    readonly abilities: {
        read: () => Promise<AbilitiesReadResult>;
    };
    /**
     * Detect on-screen progress bars (action progress, conjure timers, skilling,
     * adrenaline, …). Each bar TYPE is identified by its colour signature
     * (`combo`, or a friendly `name` you registered) — no training. Returns the
     * raw `bars`, a per-type aggregate (`groups`, with a flicker-proof
     * `stableCount` + each fill %), and per-type `began` / `ended` events. Pass
     * `{ name }` or `{ combo }` to read just one bar type. GET /api/progress.
     */
    readonly progress: {
        read: (opts?: ProgressReadOptions) => Promise<ProgressReadResult>;
        /** The combo → friendly-name registry. GET /api/progress/names. */
        names: () => Promise<{
            ok: boolean;
            names: Record<string, string>;
        }>;
        /** Name a bar combo so you can read it by name (empty name removes it). POST /api/progress/name. */
        setName: (combo: string, name: string) => Promise<{
            ok: boolean;
            names: Record<string, string>;
        }>;
    };
    /**
     * Overlay UI. Author the HUD as HTML + CSS and POST it; the server compiles it
     * to the same widget engine the SDK renders (clicks / drag / scaling all work).
     * Your app owns the state: poll `events()` for clicks (each event carries the
     * clicked widget's `id`) and re-render by calling `html()` again.
     */
    readonly ui: {
        /** Render an HTML + CSS "page" to the overlay (replaces the current UI). POST /api/ui/html. */
        html: (html: string, css?: string) => Promise<unknown>;
        /** Render a raw widget tree (`{ type, props, children }`) to the overlay. POST /api/ui. */
        render: (tree: unknown) => Promise<unknown>;
        /** Clear the overlay UI. DELETE /api/ui. */
        clear: () => Promise<unknown>;
        /** Drain queued interaction events (clicks / close / minimize); each `{ type, id, x, y }`. GET /api/ui/events. */
        events: () => Promise<UIEvents>;
        /** Configure auto display-scaling on hi-DPI / 4K. POST /api/ui/scaling. */
        scaling: (opts: UIScalingOptions) => Promise<unknown>;
    };
    /**
     * Play a sound through the desktop app. Pass either a host-side `file` path
     * (or file:/data:/http(s): URL) OR inline base64 `bytes` (+ `mime`); `volume`
     * is 0..1. Thin wrapper over POST /api/sound. Requires the desktop audio host.
     */
    readonly sound: {
        play: (opts: SoundPlayOptions) => Promise<unknown>;
    };
}
//# sourceMappingURL=client.d.ts.map