/**
 * A world position in tile coordinates.
 */
export interface Position {
    tileX: number;
    tileZ: number;
    /**
     * World X in RS3 engine units (tileX * 512).
     */
    worldX: number;
    /**
     * World Z in RS3 engine units (tileZ * 512).
     */
    worldZ: number;
}
/**
 * Simplified draw call info — one thing the GPU drew this frame.
 */
export interface DrawInfo {
    /**
     * Draw call index within the frame (0-based).
     */
    index: number;
    /**
     * GL program / shader ID.
     */
    shaderId: number;
    /**
     * Total vertices drawn.
     */
    vertexCount: number;
    /**
     * Internal mesh ID (same mesh = same geometry).
     */
    meshId: number;
    /**
     * Which framebuffer this was drawn to (0 = screen).
     */
    targetFbo: number;
    viewport: Rect;
    /**
     * Textures bound during this draw.
     */
    textures: {
        slot: number;
        id: number;
        width: number;
        height: number;
    }[];
    uniforms: UniformValueMap;
    sampleRect?: SampleRect;
    spriteRect?: SpriteRect;
    screenBbox?: ScreenBbox;
    /**
     * Off-diagonal model-matrix entries surfaced for the Composed UI renderer. `m12` / `m21` map to `modelMatrix[8]` / `modelMatrix[1]` in column-major. Non-zero values mean the draw rotates / shears its source rect.
     */
    rotationM12?: number;
    rotationM21?: number;
    /**
     * Per-quad UI decode — one entry per triangle pair). Present only for UI draws (`drawType === "ui"`) that carry indices + uvs + atlasMin/Extents AND have a primary bound texture with valid pixel dimensions. When present, consumers should iterate `composedQuads` for hit-testing and sprite identification — each quad has its own sample/sprite/ screen rect, so a single draw with 50 sprite quads exposes 50 clickable elements instead of one giant collapsed rect.
     *
     * When absent (no indices, no atlas attributes, etc.), consumers fall back to the per-draw `sampleRect` / `spriteRect` / `screenBbox` fields.
     */
    composedQuads?: ComposedQuad[];
}
/**
 * A rectangle on screen.
 */
export interface Rect {
    x: number;
    y: number;
    width: number;
    height: number;
}
export interface UniformValueMap {
    [k: string]: number | number[];
}
/**
 * Atlas sub-rectangle (in atlas pixels) sampled by a draw. Computed by walking the draw's UV range in the shared `_uvs` array and multiplying the min/max UVs by the primary bound texture's pixel dimensions. Empty / out-of-range draws receive `undefined`.
 */
export interface SampleRect {
    x: number;
    y: number;
    w: number;
    h: number;
}
/**
 * EXACT sprite sub-rectangle in atlas pixels — the whole sprite the draw rendered, NOT just the sampled sub-region. Computed from the per-vertex `aTextureUVAtlasMin` + `aTextureUVAtlasExtents` vertex attributes when the RS3 UI shader carries them. Multiplying the UV values by the bound atlas's pixel dimensions reproduces the sprite origin and extent exactly.
 *
 * When the bound program lacks these attributes (most non-UI shaders), `spriteRect` is `undefined` and consumers fall back to  {@link  SampleRect } . The Live-UI "View in Atlas" cross-link prefers `spriteRect` because it boxes the WHOLE sprite (which is what the user wants to see), not the sub-sample the draw happened to read.
 */
export interface SpriteRect {
    x: number;
    y: number;
    w: number;
    h: number;
}
/**
 * Screen-space bounding box (in screen pixels) where a draw landed. Computed from the draw's vertex-position range in the shared `_vertexPositions` array. UI draws (`drawType === "ui" && targetFbo === 0`) are the only draws that get a usable bbox here today; world draws use model+projection matrices the server doesn't apply, so their raw positions are 3D world coords with no direct screen mapping.
 */
export interface ScreenBbox {
    x: number;
    y: number;
    w: number;
    h: number;
}
/**
 * One UI quad decoded from a draw's index buffer. RS3 UI draws batch many sprite quads into a single GPU draw (each quad is two triangles = 6 indices). The legacy per-draw `sampleRect` / `spriteRect` / `screenBbox` collapse all quads into one bounding rect, which loses per-sprite resolution. `composedQuads` walks the index buffer in groups of 6 and emits one entry per quad with its own atlas + screen rects.
 *
 * Coordinate spaces:   - `sampleRect`  — atlas pixels (sub-region the quad sampled)   - `spriteRect`  — atlas pixels (WHOLE sprite the quad belongs to)   - `screen.x/y/w/h` — screen pixels (top-left + extents of the quad)   - `m12` / `m21` — shear/rotation off-diagonals
 *
 * Each quad's `topleft` vertex carries the atlas-min/extents/UV-origin (those attributes are constant across all 4 vertices of one quad in RS3's UI shader). Degenerate quads (zero extent or NaN-poisoned) are skipped at decode time so consumers never see a w==0 entry.
 */
export interface ComposedQuad {
    sampleRect: SampleRect;
    spriteRect: SampleRect;
    screen: {
        x: number;
        y: number;
        w: number;
        h: number;
    };
    m12: number;
    m21: number;
    /**
     * True when the shader hardcodes a solid white fill (reference whitesprite sentinel: sample origin < -60000). Consumers render a plain rect instead of sampling the atlas.
     */
    whitesprite?: boolean;
    /**
     * Per-vertex aVertexColour RGBA in [0,1] (the glyph/sprite tint). RS3 multiplies white UI/text pixels by this colour. Undefined when the program lacks aVertexColour or it wasn't captured.
     *
     * @minItems 4
     * @maxItems 4
     */
    color?: [number, number, number, number];
    /**
     * DEBUG (chat-colour trace): the draw's colour-pool base offset (`server.colourDataOffset`, vec4 units) and this quad's top-left vertex index. The tint is read at `pool[(_colourOff + _topleft)]`. Temporary — remove once the colour read is verified.
     */
    _colourOff?: number;
    _topleft?: number;
}
/**
 * Options for captureFrame().
 */
export interface CaptureOptions {
    /**
     * Include full mesh vertex data (slower). Default: false.
     */
    includeMesh?: boolean;
    /**
     * Include texture pixel snapshots (slower). Default: false.
     */
    includeTexturePixels?: boolean;
    /**
     * Only capture draws matching these filters.
     */
    filter?: {
        shaderId?: number;
        meshId?: number;
        targetFbo?: number;
        /**
         * Restrict capture to a single draw class ("ui" | "floor" | "animated" | "water" | "particles"). Non-matching draws are skipped at record time, so their vertex buffers and textures are never read back (no glGet* GPU sync). The overlay passes "ui".
         */
        drawType?: string;
    };
    /**
     * Run player (occlusion-mesh) detection during this capture. Off by default — the server does NO player introspection on a normal frame grab. Only trackPlayer() (→ /api/player) sets this, so the per-frame path stays stall-free. When set, the captured frame carries the player's world tile (frame.playerPosition).
     */
    detectPlayer?: boolean;
}
/**
 * Info about a loaded shader program.
 */
export interface ShaderInfo {
    id: number;
    vertexSource: string;
    fragmentSource: string;
    uniforms: {
        name: string;
        type: string;
        location: number;
    }[];
    attributes: {
        name: string;
        type: string;
        location: number;
    }[];
    /**
     * Classified shader class for `addShaderFx({ matchType })` — "animated"/"floor"/"water"/…
     */
    matchType: string;
    /**
     * Friendly kind (npc/scenery/floor/water/particles/ui/unknown).
     */
    kind: string;
    /**
     * FNV-1a hash of fragmentSource → pass as `addShaderFx({ matchHash })` to target THIS fragment.
     */
    fragmentHash: string;
    /**
     * FNV-1a hash of vertexSource (to target the vertex stage).
     */
    vertexHash: string;
}
/**
 * Info about a loaded texture.
 */
export interface TextureInfo {
    id: number;
    width: number;
    height: number;
    format: string;
    /**
     * Where this entry was sourced from. Useful for debugging UIs.
     */
    source?: "snapshot" | "frame-harvest";
}
/**
 * A full scene snapshot for one frame.
 */
export interface SceneSnapshot {
    /**
     * Frame timestamp (ms).
     */
    timestamp: number;
    /**
     * The local player (from C++ isTinted detection), or null.
     */
    player: PlayerInfo | null;
    /**
     * Living animated characters (excluding the player).
     */
    npcs: Entity[];
    /**
     * Static world objects (trees, doors, buildings).
     */
    scenery: Entity[];
    /**
     * Floor / terrain tile draws.
     */
    floor: Entity[];
    /**
     * Water surface draws.
     */
    water: Entity[];
    /**
     * Particle / effect draws.
     */
    particles: Entity[];
    /**
     * Anything that didn't fit a clean kind.
     */
    other: Entity[];
    /**
     * The frame's combined view-projection (camera) matrix, column-major, or null if no 3D draw exposed one. Project any world point yourself: clip = viewProj · [x,y,z,1]; screenXY = clip.xy / clip.w · 0.5 + 0.5.
     */
    viewProj: number[] | null;
}
export interface PlayerInfo {
    world: WorldPos;
    tile: TileCoord;
    chunk: ChunkCoord;
    floor: number;
}
export interface WorldPos {
    /**
     * Absolute world X (RS3 game units, ~100k–4M typical range).
     */
    x: number;
    /**
     * Absolute world Y (height; floor 0 ≈ 1152, floor 3 ≈ 5040).
     */
    y: number;
    /**
     * Absolute world Z.
     */
    z: number;
}
export interface TileCoord {
    /**
     * Absolute tile X (= floor(world.x / 512)).
     */
    x: number;
    /**
     * Absolute tile Z (= floor(world.z / 512)).
     */
    z: number;
}
export interface ChunkCoord {
    x: number;
    z: number;
}
/**
 * One thing in the scene this frame.
 */
export interface Entity {
    /**
     * Stable per-entity identity within a frame. Same physical entity tends to get the same id across frames because it's hashed from (meshId, shaderId). For unique-instance tracking use  {@link  drawIndex } .
     */
    id: string;
    /**
     * Draw call index — uniquely identifies this draw within the frame.
     */
    drawIndex: number;
    /**
     * What this is. See  {@link  EntityKind } .
     */
    kind: "player" | "npc" | "scenery" | "floor" | "water" | "particles" | "ui" | "unknown";
    world: WorldPos;
    tile: TileCoord;
    chunk: ChunkCoord;
    /**
     * Floor level (0–3 typical; see FLOOR_Y_BASE / FLOOR_Y_STEP).
     */
    floor: number;
    /**
     * RS3 mesh ID — same value means same geometry across frames.
     */
    meshId: number;
    /**
     * GL program / shader ID.
     */
    shaderId: number;
    /**
     * Vertex count of this draw (rough size proxy).
     */
    vertexCount: number;
    /**
     * Short, stable per-model fingerprint — a 5-char base36 hash of the mesh identity. Match a specific model with this (e.g. `o.tag === "k3p9z"`) instead of a raw vertexCount, which can run into the millions.
     */
    tag: string;
    /**
     * Screen position of this entity's world origin. x/y are normalized [0,1] (0,0 = bottom-left, matching gl_FragCoord); depth is window depth [0,1]. Null when behind the camera or no view-projection was found this frame. Multiply x/y by your viewport size to get pixels.
     */
    screen: {
        x: number;
        y: number;
        depth: number;
    } | null;
}
/**
 * What kind of thing an Entity represents.
 */
export type EntityKind = "player" | "npc" | "scenery" | "floor" | "water" | "particles" | "ui" | "unknown";
/**
 * Result of reading the chatbox (GET /api/chat).
 */
export interface ChatReadResult {
    ok: boolean;
    /**
     * Dominant font this read, or null if nothing was recognised.
     */
    font: string | null;
    lineCount: number;
    /**
     * False whenever a fresh capture happened on this call.
     */
    stale: boolean;
    /**
     * Age of the cached glyph set this read used, in ms.
     */
    ageMs: number;
    /**
     * Lines ordered top-to-bottom (oldest first).
     */
    lines: ChatLine[];
    locate: ChatLocate;
}
/**
 * One chat line. Lines are ordered top-to-bottom (oldest first).
 */
export interface ChatLine {
    /**
     * Baseline y (window px).
     */
    y: number;
    /**
     * The line's text, with spaces inserted on x-gaps.
     */
    text: string;
    color: ChatColor;
    /**
     * Contiguous same-colour segments (tag / name / message).
     */
    runs: ChatRun[];
    /**
     * Per-glyph detail.
     */
    glyphs: ChatGlyph[];
}
/**
 * RGB triple, 0-255 per channel: [r, g, b].
 */
export type ChatColor = number[];
/**
 * A contiguous same-colour segment of a line (e.g. clan tag, sender name, the message body). Render each run in its own colour to reproduce a multi-colour line.
 */
export interface ChatRun {
    text: string;
    color: ChatColor;
}
/**
 * One recognised glyph on a chat line.
 */
export interface ChatGlyph {
    /**
     * The recognised character.
     */
    char: string;
    /**
     * Screen x (window px) of the glyph's left edge.
     */
    x: number;
    color: ChatColor;
}
/**
 * Located chatbox anchors + the text region the reader scanned.
 */
export interface ChatLocate {
    allChat: ChatRect;
    cog: ChatRect;
    quickChat: ChatRect;
    box: ChatBox;
}
/**
 * A located UI rect in window px (top-left x/y + size).
 */
export interface ChatRect {
    x: number;
    y: number;
    w: number;
    h: number;
}
/**
 * The recognised chat text region in window px.
 */
export interface ChatBox {
    x0: number;
    y0: number;
    x1: number;
    y1: number;
}
/**
 * Result of reading the bars (GET /api/bars). `bars` always starts with the four stat bars in fixed order (hitpoints, adrenaline, prayer, summoning), followed by every dynamic bar currently on screen — ALL from a single capture, so every reading is from the same frame (no two-endpoint drift). Check each bar's `found` / `fillPct` / `value`.
 */
export interface BarsReadResult {
    ok: boolean;
    /**
     * False whenever a fresh capture happened on this call.
     */
    stale: boolean;
    /**
     * Age of the cached glyph set this read used, in ms.
     */
    ageMs: number;
    /**
     * The bars: the four stat bars (always present) followed by any dynamic bars on screen. Every entry has the same `Bar` shape.
     */
    bars: Bar[];
    /**
     * Names (or combos, when unnamed) of dynamic bar types that appeared on this poll.
     */
    began: string[];
    /**
     * Names (or combos, when unnamed) of dynamic bar types that vanished on this poll.
     */
    ended: string[];
}
/**
 * One bar's reading — the SAME shape for EVERY bar, whether it's a persistent stat orb (hitpoints / adrenaline / prayer / summoning) or a transient bar detected on screen (a skilling action, a conjure timer, an activity bar, …). Fields that don't apply to a given bar are `null`; the shape never changes, so one code path reads them all.
 */
export interface Bar {
    /**
     * Identifier. For the four stats it is the stat name. For a detected bar it is the registered friendly name (set via POST /api/bars/name) if one exists, else its colour-signature `combo`. Pass it to `bars.read(name)` to read just this bar.
     */
    name: string;
    /**
     * Colour-signature type id for a detected bar (stable across frames as the bar fills or moves); `null` for the four stat bars.
     */
    combo: string | null;
    /**
     * True when the bar is currently drawn on screen.
     */
    found: boolean;
    /**
     * Exact fill 0–100 read from the bar's GPU geometry (the drawn fill width). Present whenever the bar is on screen — even when no number is drawn — so this is the value to use for %-based logic such as alert thresholds.
     */
    fillPct: number | null;
    /**
     * Current value, recovered from the digit glyphs the game itself draws at the bar (e.g. an orb's "10200"). Exact — matched against the font glyphs, not OCR — it only requires the digits to be on screen at capture. `null` when the bar draws no number.
     */
    value: number | null;
    /**
     * Max value when the bar shows "current / max" (e.g. hitpoints 10200/10200 → 10200); `null` when only a single number (or none) is drawn.
     */
    max: number | null;
    /**
     * Raw recognised text incl. separators, e.g. "10200/10200" or "100%"; `null` when none.
     */
    text: string | null;
    /**
     * The bar's screen rect in window px, or `null` when it is not on screen.
     */
    rect: BarRect | null;
}
/**
 * A located UI rect in window px (top-left x/y + size).
 */
export interface BarRect {
    x: number;
    y: number;
    w: number;
    h: number;
}
/**
 * Result of reading the buff bar (GET /api/buffs). Buffs and debuffs are returned in separate arrays — both made of the same `Buff` shape — ALL from a single capture, so every reading is from the same frame.
 */
export interface BuffsReadResult {
    ok: boolean;
    /**
     * True when no fresh capture was available yet and the cache was empty.
     */
    stale: boolean;
    /**
     * Age of the cached glyph set this read used, in ms.
     */
    ageMs: number;
    /**
     * Active buffs (green-bordered cells).
     */
    buffs: Buff[];
    /**
     * Active debuffs (red-bordered cells).
     */
    debuffs: Buff[];
}
/**
 * One buff or debuff cell on the buff bar. Buffs and debuffs share the SAME shape — they're told apart by `kind` (the cell's border tint: green = buff, red = debuff) and returned in separate `buffs` / `debuffs` arrays by the read.
 */
export interface Buff {
    /**
     * "buff" (green-bordered cell) or "debuff" (red-bordered cell).
     */
    kind: "buff" | "debuff";
    /**
     * Friendly effect name (e.g. "bone_shield", "prayer_renewal"), resolved from the icon's COLOUR signature; `null` when the icon isn't named yet. Train an unknown icon with `buffs.name(iconColorHash, "…")` (POST /api/buffs/name).
     */
    name: string | null;
    /**
     * Colour-hash identity of the cell's icon (stable across game sessions). This is the key you pass to `buffs.name()` to give the effect a `name`; `null` when no icon was found in the cell.
     */
    iconColorHash: number | null;
    /**
     * The numeric timer/stack drawn in the cell (e.g. a 50-second shield, 5 stacks), recovered from the digit glyphs the game draws — exact, not OCR. `null` when no number is drawn.
     */
    value: number | null;
    /**
     * Raw recognised text in the cell incl. non-digit chars (e.g. "5m"); `null` when none.
     */
    text: string | null;
    rect: BuffRect;
}
/**
 * A located UI rect in window px (top-left x/y + size).
 */
export interface BuffRect {
    x: number;
    y: number;
    w: number;
    h: number;
}
/**
 * Result of reading the skills interface (GET /api/skills) — one `Skill` per skill cell currently on screen, all from a single capture.
 */
export interface SkillsReadResult {
    ok: boolean;
    /**
     * True when no fresh capture was available yet and the cache was empty.
     */
    stale: boolean;
    /**
     * Age of the cached glyph set this read used, in ms.
     */
    ageMs: number;
    /**
     * The skills currently on screen (each with current `level` + `base`).
     */
    skills: Skill[];
}
/**
 * One skill cell on the skills interface — anchored on the skill's icon, with the two level numbers drawn beside it read out exactly (glyph-matched, not OCR).
 */
export interface Skill {
    /**
     * Skill name — one of  {@link  SkillName }  (e.g. "attack", "herblore", "necromancy"). Typed as a plain string on the result so an unexpected trained name never breaks a read; pass a `SkillName` to `skills.read(name)`.
     */
    name: string;
    /**
     * Current (live) level — equals `base` normally, LOWER when a debuff drains the skill, HIGHER when a potion boosts it. Use this for live logic. `null` when no level number is drawn.
     */
    level: number | null;
    /**
     * Base (trained) level — what the skill sits at unbuffed. `null` when none is drawn.
     */
    base: number | null;
    rect: SkillRect;
}
/**
 * A located UI rect in window px (top-left x/y + size).
 */
export interface SkillRect {
    x: number;
    y: number;
    w: number;
    h: number;
}
/**
 * The RS3 skills, by name — the value of `Skill.name` and the argument to `skills.read(name)`. (These are the icon names trained in sprites.json as `skill:<name>`, with the `skill:` prefix stripped.)
 */
export type SkillName = "attack" | "defence" | "strength" | "constitution" | "ranged" | "prayer" | "magic" | "cooking" | "woodcutting" | "fletching" | "fishing" | "firemaking" | "crafting" | "smithing" | "mining" | "herblore" | "agility" | "thieving" | "slayer" | "farming" | "runecrafting" | "hunter" | "construction" | "summoning" | "dungeoneering" | "divination" | "invention" | "archaeology" | "necromancy";
/**
 * Result of reading the action bar(s) (GET /api/abilities). `abilities` is in reading order — rows top-to-bottom, then left-to-right.
 */
export interface AbilitiesReadResult {
    ok: boolean;
    /**
     * False whenever a fresh capture happened on this call.
     */
    stale: boolean;
    /**
     * Age of the cached glyph set this read used, in ms.
     */
    ageMs: number;
    /**
     * Recognised ability slots.
     */
    abilities: AbilitySlot[];
}
/**
 * One recognised action-bar slot.
 */
export interface AbilitySlot {
    /**
     * Ability id WITHOUT the "ability:" prefix, e.g. "anticipation".
     */
    name: string;
    rect: AbilityRect;
    atlas: AbilityRect;
    /**
     * True on the frame(s) RS3 paints the activation-flash sweep over this slot. NOTE: the sweep is the GLOBAL-cooldown animation — it flashes across the WHOLE bar on any cast, so it is NOT per-ability. To tell which slot actually fired, use the cooldown timer (`onCooldown`), not this.
     */
    activating: boolean;
    /**
     * True when a cooldown number overlaps the slot.
     */
    onCooldown: boolean;
    /**
     * Recognised cooldown text, e.g. "5" or "1:23"; "" when none.
     */
    cooldownText: string;
    /**
     * Parsed cooldown in whole seconds, or null when unreadable / not on CD.
     */
    cooldownSeconds: number | null;
    /**
     * False when the icon is greyed out (not enough resource / unavailable). Derived from the per-vertex tint, so it depends on correct tint capture.
     */
    usable: boolean;
    /**
     * The slot's per-vertex tint [r, g, b], 0-255 (a grey triple ⇒ unusable).
     */
    color: number[];
}
/**
 * A located UI rect in window px (top-left x/y + size).
 */
export interface AbilityRect {
    x: number;
    y: number;
    w: number;
    h: number;
}
/**
 * Result of GET /api/progress. Detects every progress bar by shape, identifies the TYPE by colour signature (`combo`, or a friendly `name` if registered), measures fill %, and tracks begin/end per type. Pass `?name=` / `?combo=` to read just one bar type.
 */
export interface ProgressReadResult {
    ok: boolean;
    /**
     * Age of the cached glyph set this read used, in ms.
     */
    ageMs: number;
    /**
     * Number of bar TYPES on screen (== groups.length).
     */
    count: number;
    /**
     * Raw per-bar snapshot this frame.
     */
    bars: ProgressBar[];
    /**
     * Per-type aggregate (count + each fill %).
     */
    groups: ProgressGroup[];
    /**
     * Bar types that appeared this poll.
     */
    began: ProgressBegan[];
    /**
     * Bar types that went fully gone this poll.
     */
    ended: ProgressEnded[];
}
/**
 * One progress bar detected this frame (raw snapshot; no stable id — bars may move).
 */
export interface ProgressBar {
    /**
     * Colour-signature key identifying the bar TYPE (sorted colorHashes).
     */
    combo: string;
    /**
     * Friendly name for this combo if one is registered, else null.
     */
    name: string | null;
    /**
     * Screen x of the bar's left edge (window px).
     */
    x: number;
    /**
     * Screen y (window px).
     */
    y: number;
    /**
     * Drawn width (window px).
     */
    w: number;
    /**
     * Drawn height (window px).
     */
    h: number;
    /**
     * Fill percent, 0-100.
     */
    percent: number;
    /**
     * True once both a track + fill colour have been seen (a real bar, not a stray piece).
     */
    confident: boolean;
}
/**
 * Per bar-TYPE aggregate: how many of this type are on screen + each one's fill.
 */
export interface ProgressGroup {
    combo: string;
    name: string | null;
    /**
     * Bars of this type detected THIS frame (can dip on a flickered frame).
     */
    count: number;
    /**
     * Flicker-proof count: the max seen across the grace window.
     */
    stableCount: number;
    /**
     * Each bar's fill percent, highest first.
     */
    percents: number[];
    minPercent: number;
    maxPercent: number;
    confident: boolean;
}
/**
 * A bar TYPE that just appeared this poll (count went 0 -> >0).
 */
export interface ProgressBegan {
    combo: string;
    name: string | null;
}
/**
 * A bar TYPE that just went fully gone this poll (count -> 0 past the grace window).
 */
export interface ProgressEnded {
    combo: string;
    name: string | null;
    /**
     * The highest fill % this type reached before it vanished.
     */
    maxPercent: number;
}
/**
 * A single submittable overlay item — geometry Shape, text Billboard, or image sprite. Clean wire form of the SDK's `DrawItem` (uses the wire  {@link  ImageItem }  above). This is what `drawShape` accepts and `drawScene` accepts an array of.
 */
export type DrawItem = Shape | Billboard | ImageItem;
/**
 * A single drawable. Most fields have sensible defaults — at minimum you need `anchor`, `primitive`, `vertices`, and `color`.
 */
export interface Shape {
    /**
     * Optional id. If omitted, one is generated when this shape is submitted.
     */
    id?: string;
    /**
     * Where the shape lives. See  {@link  Anchor } .
     */
    anchor: {
        mode: "tile";
        tile: TilePos;
    } | {
        mode: "world";
        world: WorldPos;
    } | {
        mode: "screen";
        screen: ScreenPos;
    } | {
        mode: "entity";
        entityId: string;
        offset?: WorldPos;
    };
    /**
     * Vertex primitive interpretation.
     */
    primitive: "triangles" | "triangle_strip" | "triangle_fan" | "lines" | "line_strip" | "line_loop" | "points";
    /**
     * Flat array of x,y,z triplets (length must be multiple of 3) in the anchor's local coord space. For 2D anchors (tile/screen), Y is treated as a relative offset above the anchor plane (use 0 for flat).
     */
    vertices: number[];
    /**
     * Optional index buffer. If omitted, vertices are used in array order.
     */
    indices?: number[];
    /**
     * Fill color. Either a single color for the whole shape, or one color per vertex (length must match vertex count) for gradients.
     */
    color: Color | Color[];
    /**
     * Line width in PIXELS (resolution-independent). Default 1.
     */
    thickness?: number;
    /**
     * Point size in PIXELS. Default 4.
     */
    pointSize?: number;
    /**
     * If true, the shape can be obscured by world geometry (uses sampled depth + polygon offset internally to avoid z-fighting). If false (default), always renders on top.
     */
    occludedByWorld?: boolean;
    /**
     * Blend mode. Default "normal".
     */
    blend?: "normal" | "additive" | "multiply";
    animation?: AnimationSpec;
    /**
     * Optional grouping tag — `draw.clear(group)` removes all shapes in a group.
     */
    group?: string;
    /**
     * Advanced — the "raw shader" layer: your own GLSL for THIS shape. Compiled once and cached by source hash; on a compile error the shape falls back to the built-in shader and the error is recorded (`shaders.getCompileStatus`), so a typo never breaks your overlay. Builtin uniforms are always in scope: `uMouse` vec2 · `uTime` float seconds · `uFrame` int · `uResolution` vec2.
     */
    shader?: {
        fragmentSource: string;
        vertexSource?: string;
    };
    uniforms?: UniformValueMap;
    /**
     * If true, this shape's world-anchor Y is replaced with the terrain ground height at its (x,z) before submit (resolved via buddy.terrain). Lets you place an object by x/z alone and have it sit on the floor.
     */
    conformToGround?: boolean;
    /**
     * World-Y lift added on top of the conformed ground height (default 0) — raise/lower the grounded shape. Only used when conformToGround is true.
     */
    groundOffset?: number;
}
/**
 * Anchor — where a Shape's local vertex (0,0,0) ends up in the world. The library applies the anchor transform; the user only specifies local coords, never matrices.
 */
export type Anchor = {
    mode: "tile";
    tile: TilePos;
} | {
    mode: "world";
    world: WorldPos;
} | {
    mode: "screen";
    screen: ScreenPos;
} | {
    mode: "entity";
    entityId: string;
    offset?: WorldPos;
};
/**
 * Tile coordinates: integer tile X/Z + floor 0–3 + optional Y lift in world units.
 */
export interface TilePos {
    /**
     * Absolute tile X.
     */
    x: number;
    /**
     * Absolute tile Z.
     */
    z: number;
    /**
     * Floor index (0 = ground floor).
     */
    floor: number;
    /**
     * Extra Y offset in world units above the floor plane. Default 5 to avoid z-fighting with terrain. Set higher to lift the shape further; set 0 to sit exactly on the floor plane.
     */
    liftY?: number;
}
/**
 * Screen pixel coordinates for HUD/UI overlays.
 */
export interface ScreenPos {
    /**
     * X — pixels by default, or 0–1 if `normalized` is true.
     */
    x: number;
    /**
     * Y — pixels by default, or 0–1 if `normalized` is true.
     */
    y: number;
    /**
     * When true, x/y are 0–1 viewport-relative instead of absolute pixels.
     */
    normalized?: boolean;
}
/**
 * OpenGL primitive type.
 */
export type Primitive = "triangles" | "triangle_strip" | "triangle_fan" | "lines" | "line_strip" | "line_loop" | "points";
/**
 * Color — accepts:   • `"#rrggbb"` or `"#rrggbbaa"` hex   • `"rgb(r,g,b)"` or `"rgba(r,g,b,a)"` (a in 0–1)   • `[r, g, b]` or `[r, g, b, a]` tuples (0–255)   • Named colors: red, green, blue, yellow, cyan, magenta, white, black,     gray, orange, purple, gold, silver, pink, brown, transparent.
 */
export type Color = string | [number, number, number] | [number, number, number, number];
/**
 * Blending mode for compositing the shape against the screen.
 */
export type BlendMode = "normal" | "additive" | "multiply";
export interface AnimationSpec {
    kind: AnimationKind;
    /**
     * Cycle duration in seconds. Default 1.5s.
     */
    period?: number;
    /**
     * Animation amplitude (kind-dependent). Default 1.0.
     */
    amplitude?: number;
}
/**
 * Optional animation effect driven shader-side.
 */
export type AnimationKind = "pulse" | "flow" | "fade" | "rotate";
/**
 * Text billboard — a string of characters that always faces the camera, rendered through FreeType on the C++ side. Anchored in world / tile / screen / entity space like any other Shape.
 *
 * Size semantics:   • `fontSize` is in pixels at 1080p — the renderer auto-scales for 4K/2K     so 16px stays visually consistent across resolutions.   • By default the billboard is pixel-stable (same on-screen size at any     distance). Set `worldScale > 0` to make it shrink with distance like     in-world signage. The number is "world units per pixel of glyph at     unit distance" — `worldScale=1` ≈ same physical height as one tile.
 *
 * The text renderer keeps a per-font glyph atlas; first use of a new font triggers a one-shot rasterization on the GL thread, after which billboards are cheap (a single textured quad per glyph).
 */
export interface Billboard {
    /**
     * Sentinel — distinguishes Billboard from geometry Shape in unions.
     */
    kind: "billboard";
    /**
     * Optional id; auto-minted on submit if absent.
     */
    id?: string;
    /**
     * Where the billboard center sits.
     */
    anchor: {
        mode: "tile";
        tile: TilePos;
    } | {
        mode: "world";
        world: WorldPos;
    } | {
        mode: "screen";
        screen: ScreenPos;
    } | {
        mode: "entity";
        entityId: string;
        offset?: WorldPos;
    };
    /**
     * The text to render. Unicode codepoints up to U+FFFF supported.
     */
    text: string;
    /**
     * Font name (e.g. "arial", "consolas", "segoeui"). Default: "arial".
     */
    font?: string;
    /**
     * Font size in pixels at 1080p. Default 16.
     */
    fontSize?: number;
    /**
     * Text color. Default white.
     */
    color?: string | [number, number, number] | [number, number, number, number];
    /**
     * Optional background panel color. Set alpha to 0 to disable.
     */
    background?: string | [number, number, number] | [number, number, number, number];
    /**
     * Horizontal alignment relative to anchor. Default "center".
     */
    halign?: "left" | "center" | "right";
    /**
     * Vertical alignment relative to anchor. Default "middle".
     */
    valign?: "top" | "middle" | "bottom";
    /**
     * Distance-scaling factor. `0` (default) = pixel-stable: same on-screen size at any distance. `>0` = scales with distance; the value is the approximate world-unit height of one pixel of glyph.
     */
    worldScale?: number;
    /**
     * Pixel padding around the text when a background is drawn. Default 4.
     */
    padding?: number;
    /**
     * If true, the billboard can be obscured by world geometry. Default false (always-on-top, like all overlays).
     */
    occludedByWorld?: boolean;
    /**
     * Optional grouping tag for batch removal.
     */
    group?: string;
}
/**
 * Horizontal alignment of a text billboard relative to its anchor.
 */
export type HAlign = "left" | "center" | "right";
/**
 * Vertical alignment of a text billboard relative to its anchor.
 */
export type VAlign = "top" | "middle" | "bottom";
/**
 * Image / sprite draw - WIRE variant. Same as the SDK's image draw item except the encoded-image byte field is omitted: raw byte arrays are not JSON-serializable. Over HTTP a sprite's pixels travel as base64 in the request body, not as a typed field on the draw item.
 */
export interface ImageItem {
    /**
     * Sentinel - distinguishes an image from Shape / Billboard in the union.
     */
    kind: "image";
    /**
     * Optional id; auto-minted on submit if absent.
     */
    id?: string;
    /**
     * Top-left anchor (screen pixels) or a world anchor.
     */
    anchor: {
        mode: "tile";
        tile: TilePos;
    } | {
        mode: "world";
        world: WorldPos;
    } | {
        mode: "screen";
        screen: ScreenPos;
    } | {
        mode: "entity";
        entityId: string;
        offset?: WorldPos;
    };
    /**
     * Destination width in pixels.
     */
    width: number;
    /**
     * Destination height in pixels.
     */
    height: number;
    /**
     * Optional tint multiplied into the sprite. Default white (untinted).
     */
    color?: string | [number, number, number] | [number, number, number, number];
    /**
     * If true, world geometry can occlude it. Default false (always on top).
     */
    occludedByWorld?: boolean;
    /**
     * Optional grouping tag for batch removal.
     */
    group?: string;
}
/**
 * One post-processing pass: a GLSL fragment shader applied fullscreen. Samples the previous result via `uScene` (sampler2D) at `gl_FragCoord.xy / uResolution`. Builtins available: `uScene`, `uResolution`, `uTime`, `uFrame`. `order` sets its slot in the chain (ascending).
 */
export interface PostFxPassInput {
    id: string;
    fragmentSource: string;
    uniforms?: UniformValueMap;
    order?: number;
    enabled?: boolean;
}
/**
 * One custom game-shader FX: replace/patch RS3's own shader, matched by classified type ("water"/"floor"/"foliage"/"animated"/"sky"/"particles"/"main"/"ui"/ "shadow"/"tinted"/"postprocess") or by exact source hash. fragmentSource and/or vertexSource replace that stage; an empty replacement leaves the stock shader.
 */
export interface ShaderFxInput {
    id: string;
    matchType?: string;
    matchHash?: string;
    fragmentSource?: string;
    vertexSource?: string;
    enabled?: boolean;
}
/**
 * Result of GET /api/player/name — the local player's name + title(s), recovered from the chat input prompt (opt-in; the name never appears in the chat feed). When `found` is false the name fields are empty strings. `prefix`/`suffix` are the title text before/after the name; `title` is whichever is present.
 */
export interface PlayerNameResult {
    ok: boolean;
    found: boolean;
    displayName: string;
    name: string;
    prefix?: string;
    suffix?: string;
    title?: string;
}
/**
 * Result of GET /api/frame — one captured frame's draw calls. The SDK's internal `Frame` (which carries a `dispose()` function) is deliberately not exposed; the wire form is just the serializable draw list + metadata.
 */
export interface FrameCaptureResult {
    draws: DrawInfo[];
    drawCount: number;
}
/**
 * A widget node on the wire: a built-in `type`, optional `props`, and child widgets. (Bare-text children are a TS authoring nicety; the wire model lists widget children only — use a `label` for text.)
 */
export interface UIWidget {
    type: WidgetType;
    props?: UIProps;
    children?: UIWidget[];
}
/**
 * Built-in widget kinds the engine lays out + flattens directly.
 */
export type WidgetType = "panel" | "row" | "column" | "stack" | "grid" | "spacer" | "label" | "gauge" | "image" | "divider" | "badge" | "button" | "accordion" | "worldLabel" | "worldMarker" | "tile";
/**
 * Style + content props for a widget (JSON-serializable subset of `Props`).
 */
export interface UIProps {
    width?: number | "auto" | "flex";
    height?: number | "auto" | "flex";
    pad?: Spacing;
    margin?: Spacing;
    gap?: number;
    bg?: Color;
    color?: Color;
    fill?: Color;
    track?: Color;
    outline?: Color;
    outlineWidth?: number;
    radius?: number;
    opacity?: number;
    shadow?: boolean | ShadowSpec;
    blend?: BlendMode;
    animation?: AnimationSpec;
    align?: Align;
    justify?: Justify;
    font?: string;
    fontSize?: number;
    text?: string | number;
    value?: number;
    max?: number;
    min?: number;
    vertical?: boolean;
    /**
     * Image source (a path or URL on the wire; bytes are TS-only).
     */
    src?: string;
    tint?: Color;
    anchor?: ScreenAnchor | UIPoint;
    tile?: UITile;
    world?: UIWorld;
    id?: string;
    group?: string;
}
/**
 * Padding/margin: one value (all sides), [vertical, horizontal], or [top,right,bottom,left].
 */
export type Spacing = number | [number, number] | [number, number, number, number];
/**
 * Drop-shadow spec for a widget. `shadow: true` uses the soft default; an object tunes it. The engine renders a single shadow from the widget's outer silhouette (box → 9-sliced gaussian sprite; text/image → alpha-shaped native shadow pass).
 */
export interface ShadowSpec {
    /**
     * Horizontal offset in px. Default 0.
     */
    dx?: number;
    /**
     * Vertical offset in px. Default 3.
     */
    dy?: number;
    /**
     * Softness (gaussian blur radius) in px. Default 8.
     */
    blur?: number;
    /**
     * Grow the silhouette by this many px before blurring. Default 0.
     */
    spread?: number;
    /**
     * Shadow color. Default rgba(0,0,0,0.5).
     */
    color?: string | [number, number, number] | [number, number, number, number];
}
/**
 * Cross-axis alignment of a container's children.
 */
export type Align = "start" | "center" | "end" | "stretch";
/**
 * Main-axis distribution of a container's children.
 */
export type Justify = "start" | "center" | "end" | "between" | "around" | "evenly";
/**
 * Screen corner/edge a root is pinned to (auto-follows window resize).
 */
export type ScreenAnchor = "top-left" | "top" | "top-right" | "left" | "center" | "right" | "bottom-left" | "bottom" | "bottom-right";
/**
 * Screen-pixel point (for an explicit `anchor`).
 */
export interface UIPoint {
    x: number;
    y: number;
}
/**
 * Tile anchor for world-space widgets.
 */
export interface UITile {
    x: number;
    z: number;
    floor?: number;
}
/**
 * World-space (engine units) anchor.
 */
export interface UIWorld {
    x: number;
    y: number;
    z: number;
}
//# sourceMappingURL=models.d.ts.map