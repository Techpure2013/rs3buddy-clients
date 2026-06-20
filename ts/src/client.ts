import { Transport, TransportOptions } from "./transport";
import type {
  Position, CaptureOptions, ShaderInfo, TextureInfo,
  SceneSnapshot, ChatReadResult, BarsReadResult, AbilitiesReadResult,
  DrawItem, PostFxPassInput, ShaderFxInput, PlayerNameResult, FrameCaptureResult,
} from "./models";

function qs(params: Record<string, string | number | undefined>): string {
  const p = new URLSearchParams();
  for (const [k, v] of Object.entries(params)) if (v !== undefined) p.set(k, String(v));
  const s = p.toString();
  return s ? `?${s}` : "";
}

/** Optional chatbox region override (window px). */
export interface ChatReadOptions {
  x0?: number;
  y0?: number;
  x1?: number;
  y1?: number;
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

export class RS3Buddy {
  private readonly t: Transport;
  constructor(opts: TransportOptions = {}) {
    this.t = new Transport(opts);
  }

  /**
   * Connect to the running SDK server. With no options, auto-discovers the
   * server from the launcher's config (rs3buddy.json under %APPDATA%\rs3buddy,
   * then $RS3BUDDY_CONFIG / the cwd); the launcher must be running. Pass
   * { baseUrl } only to target a specific server.
   */
  static connect(opts: TransportOptions = {}): RS3Buddy {
    return new RS3Buddy(opts);
  }

  get baseUrl(): string { return this.t.baseUrl; }

  // ── Player (rides the native passive tap; both hit GET /api/player) ──
  /** Acquire the player position (cold call may take a frame or two). */
  getPlayer(): Promise<Position | null> { return this.t.request("GET", "/api/player"); }
  /** Cheap refresh; null if not currently tracked → call getPlayer() again. */
  updatePlayer(): Promise<Position | null> { return this.t.request("GET", "/api/player"); }
  /**
   * The local player's name + title(s), recovered from the chat input prompt
   * (opt-in; the name never appears in the chat feed itself). `found` is false
   * until the prompt has been seen at least once.
   */
  getPlayerName(): Promise<PlayerNameResult> { return this.t.request("GET", "/api/player/name"); }

  // ── Status / window / heap ──
  getStatus(): Promise<unknown> { return this.t.request("GET", "/api/status"); }
  getWindow(): Promise<unknown> { return this.t.request("GET", "/api/window"); }
  getHeap(): Promise<unknown> { return this.t.request("GET", "/api/heap"); }

  // ── Capture / shaders / textures ──
  captureFrame(opts: CaptureOptions = {}): Promise<FrameCaptureResult> {
    return this.t.request("GET", "/api/frame" + qs({
      mesh: opts.includeMesh ? "true" : undefined,
      pixels: opts.includeTexturePixels ? "true" : undefined,
      shaderId: opts.filter?.shaderId,
      meshId: opts.filter?.meshId,
      targetFbo: opts.filter?.targetFbo,
    }));
  }
  getShaders(): Promise<ShaderInfo[]> { return this.t.request("GET", "/api/shaders"); }
  getTextures(): Promise<TextureInfo[]> { return this.t.request("GET", "/api/textures"); }
  getCapturedTextures(): Promise<unknown> { return this.t.request("GET", "/api/captured-textures"); }
  refreshCapturedTextures(): Promise<unknown> { return this.t.request("POST", "/api/captured-textures/refresh"); }
  capturedTexturesStats(): Promise<unknown> { return this.t.request("GET", "/api/captured-textures/stats"); }
  clearCapturedTextures(): Promise<unknown> { return this.t.request("DELETE", "/api/captured-textures"); }
  getCapturedTexture(id: number): Promise<unknown> { return this.t.request("GET", `/api/captured-texture/${id}`); }
  readTexture(id: number, x = 0, y = 0, w = 0, h = 0): Promise<unknown> {
    return this.t.request("GET", `/api/texture/${id}` + qs({ x, y, w, h }));
  }
  screenCapture(): Promise<unknown> { return this.t.request("GET", "/api/screen-capture"); }
  getGlyphsOnScreen(opts: Record<string, number> = {}): Promise<unknown> {
    return this.t.request("GET", "/api/glyphs-on-screen" + qs(opts));
  }
  glyphAtPoint(x: number, y: number, opts: Record<string, number> = {}): Promise<unknown> {
    return this.t.request("POST", "/api/glyph-at-point", { x, y, ...opts });
  }

  // ── Scene ──
  getScene(): Promise<SceneSnapshot> { return this.t.request("GET", "/api/scene"); }
  getScenePlayer(): Promise<unknown> { return this.t.request("GET", "/api/scene/player"); }
  getNpcs(radius?: number, floor?: number): Promise<unknown> { return this.t.request("GET", "/api/scene/npcs" + qs({ radius, floor })); }
  getScenery(radius?: number, floor?: number): Promise<unknown> { return this.t.request("GET", "/api/scene/scenery" + qs({ radius, floor })); }
  getFloorTiles(radius?: number, floor?: number): Promise<unknown> { return this.t.request("GET", "/api/scene/floor_tiles" + qs({ radius, floor })); }
  getWater(radius?: number, floor?: number): Promise<unknown> { return this.t.request("GET", "/api/scene/water" + qs({ radius, floor })); }
  getEntityAt(x: number, z: number, floor?: number): Promise<unknown> { return this.t.request("GET", "/api/scene/at" + qs({ x, z, floor })); }

  // ── Draw / overlay ──
  drawShape(shape: DrawItem): Promise<unknown> { return this.t.request("POST", "/api/draw/shape", shape); }
  drawScene(shapes: DrawItem[]): Promise<unknown> { return this.t.request("POST", "/api/draw/scene", shapes); }
  listShapes(): Promise<unknown> { return this.t.request("GET", "/api/draw"); }
  removeShape(id: string): Promise<unknown> { return this.t.request("DELETE", `/api/draw/${id}`); }
  clearShapes(group?: string): Promise<unknown> { return this.t.request("DELETE", "/api/draw" + qs({ group })); }

  // ── Fonts ──
  /** List the registered render/DRAW fonts (alias -> .ttf), NOT the recognition registry. */
  listFonts(): Promise<unknown> { return this.t.request("GET", "/api/fonts/registered"); }
  registerFont(name: string, p: string): Promise<unknown> { return this.t.request("POST", "/api/fonts/register", { name, path: p }); }
  unregisterFont(name: string): Promise<unknown> { return this.t.request("DELETE", `/api/fonts/${name}`); }

  // ── Sprites ──
  listTrainedSprites(): Promise<unknown> { return this.t.request("GET", "/api/sprites/trained"); }
  saveTrainedSprite(req: unknown): Promise<unknown> { return this.t.request("POST", "/api/sprites/trained", req); }
  deleteTrainedSprite(name: string): Promise<unknown> { return this.t.request("DELETE", `/api/sprites/trained/${name}`); }

  // ── Atlas ──
  importSpriteHash(req: unknown): Promise<unknown> { return this.t.request("POST", "/api/atlas/import-spritehash", req); }
  trainQuad(req: unknown): Promise<unknown> { return this.t.request("POST", "/api/atlas/train-quad", req); }
  atlasSync(): Promise<unknown> { return this.t.request("POST", "/api/atlas/sync"); }
  atlasSprites(): Promise<unknown> { return this.t.request("GET", "/api/atlas/sprites"); }
  atlasLookup(hash: number): Promise<unknown> { return this.t.request("GET", `/api/atlas/lookup/${hash}`); }
  recognizeQuads(quads: unknown[]): Promise<unknown> { return this.t.request("POST", "/api/atlas/recognize-quads", { quads }); }

  // ── OCR ──
  matchText(req: unknown): Promise<unknown> { return this.t.request("POST", "/api/match/text", req); }
  matchRegion(req: unknown): Promise<unknown> { return this.t.request("POST", "/api/match/region", req); }

  // ── Post-FX (fullscreen post-processing passes) ──
  /** Register or replace a fullscreen post-FX pass; returns its id. */
  addPostFx(pass: PostFxPassInput): Promise<{ id: string }> { return this.t.request("POST", "/api/shaders/postfx", pass); }
  /** All post-FX passes, in render order (ascending `order`). */
  listPostFx(): Promise<PostFxPassInput[]> { return this.t.request("GET", "/api/shaders/postfx"); }
  /** Remove a post-FX pass. */
  removePostFx(id: string): Promise<{ removed: boolean }> { return this.t.request("DELETE", `/api/shaders/postfx/${id}`); }
  /** Enable/disable a post-FX pass without removing it. */
  setPostFxEnabled(id: string, enabled: boolean): Promise<{ ok: boolean }> { return this.t.request("POST", `/api/shaders/postfx/${id}/enabled`, { enabled }); }

  // ── Custom game-shader FX (replace RS3's own shaders, by type or hash) ──
  /** Register or replace a custom game-shader FX; returns its id. */
  addShaderFx(o: ShaderFxInput): Promise<{ id: string }> { return this.t.request("POST", "/api/shaders/fx", o); }
  /** All active custom game-shader FX. */
  listShaderFx(): Promise<ShaderFxInput[]> { return this.t.request("GET", "/api/shaders/fx"); }
  /** Remove a shader FX (that shader reverts to stock). */
  removeShaderFx(id: string): Promise<{ removed: boolean }> { return this.t.request("DELETE", `/api/shaders/fx/${id}`); }
  /** Enable/disable a shader FX without removing it. */
  setShaderFxEnabled(id: string, enabled: boolean): Promise<{ ok: boolean }> { return this.t.request("POST", `/api/shaders/fx/${id}/enabled`, { enabled }); }

  // ── Chat ──
  /**
   * Read the in-game chatbox as structured lines + per-glyph colour.
   * Thin wrapper over GET /api/chat; all the heavy work (frame + screen
   * capture, glyph recognition, line grouping, colour sampling) runs
   * server-side. Pass a region override to read a non-default box.
   */
  readonly chat = {
    read: (opts: ChatReadOptions = {}): Promise<ChatReadResult> =>
      this.t.request("GET", "/api/chat" + qs({
        x0: opts.x0, y0: opts.y0, x1: opts.x1, y1: opts.y1,
      })),
  };

  // ── Status bars (HP / adrenaline / prayer / summoning) ──
  /**
   * Read the four status bars: each bar's current `value`, `max` (when the bar
   * shows current/max), `found`, and the located `anchor` + scanned `region`.
   * Thin wrapper over GET /api/bars; recognition runs server-side.
   */
  readonly bars = {
    read: (): Promise<BarsReadResult> => this.t.request("GET", "/api/bars"),
  };

  // ── Ability bars (action bar slots) ──
  /**
   * Read the action bar(s): each slot's `name`, `rect`, cooldown
   * (`onCooldown` / `cooldownText` / `cooldownSeconds`) and `usable` state.
   * Thin wrapper over GET /api/abilities; recognition runs server-side.
   */
  readonly abilities = {
    read: (): Promise<AbilitiesReadResult> => this.t.request("GET", "/api/abilities"),
  };

  // ── Overlay UI ──
  /**
   * Overlay UI. Author the HUD as HTML + CSS and POST it; the server compiles it
   * to the same widget engine the SDK renders (clicks / drag / scaling all work).
   * Your app owns the state: poll `events()` for clicks (each event carries the
   * clicked widget's `id`) and re-render by calling `html()` again.
   */
  readonly ui = {
    /** Render an HTML + CSS "page" to the overlay (replaces the current UI). POST /api/ui/html. */
    html: (html: string, css?: string): Promise<unknown> =>
      this.t.request("POST", "/api/ui/html", { html, css: css ?? "" }),
    /** Render a raw widget tree (`{ type, props, children }`) to the overlay. POST /api/ui. */
    render: (tree: unknown): Promise<unknown> => this.t.request("POST", "/api/ui", tree),
    /** Clear the overlay UI. DELETE /api/ui. */
    clear: (): Promise<unknown> => this.t.request("DELETE", "/api/ui"),
    /** Drain queued interaction events (clicks / close / minimize); each `{ type, id, x, y }`. GET /api/ui/events. */
    events: (): Promise<UIEvents> => this.t.request("GET", "/api/ui/events"),
    /** Configure auto display-scaling on hi-DPI / 4K. POST /api/ui/scaling. */
    scaling: (opts: UIScalingOptions): Promise<unknown> =>
      this.t.request("POST", "/api/ui/scaling", opts),
  };

  // ── Sound (play a developer-supplied sound, host-side) ──
  /**
   * Play a sound through the desktop app. Pass either a host-side `file` path
   * (or file:/data:/http(s): URL) OR inline base64 `bytes` (+ `mime`); `volume`
   * is 0..1. Thin wrapper over POST /api/sound. Requires the desktop audio host.
   */
  readonly sound = {
    play: (opts: SoundPlayOptions): Promise<unknown> => this.t.request("POST", "/api/sound", opts),
  };
}
