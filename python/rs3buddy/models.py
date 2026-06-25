# AUTO-GENERATED from schema/rs3buddy.schema.json — do not edit by hand.
# Regenerate: npm run gen:models:py

from __future__ import annotations

from typing import Literal, NotRequired, TypedDict


class Position(TypedDict):
    tileX: float
    tileZ: float
    worldX: float
    worldZ: float


class Texture(TypedDict):
    slot: float
    id: float
    width: float
    height: float


class Rect(TypedDict):
    x: float
    y: float
    width: float
    height: float


type UniformValueMap = dict[str, float | list[float]]


class SampleRect(TypedDict):
    x: float
    y: float
    w: float
    h: float


class SpriteRect(TypedDict):
    x: float
    y: float
    w: float
    h: float


class ScreenBbox(TypedDict):
    x: float
    y: float
    w: float
    h: float


class Screen(TypedDict):
    x: float
    y: float
    w: float
    h: float


class ComposedQuad(TypedDict):
    sampleRect: SampleRect
    spriteRect: SampleRect
    screen: Screen
    m12: float
    m21: float
    whitesprite: NotRequired[bool]
    color: NotRequired[list[float]]
    field_colourOff: NotRequired[float]
    field_topleft: NotRequired[float]


class Filter(TypedDict):
    shaderId: NotRequired[float]
    meshId: NotRequired[float]
    targetFbo: NotRequired[float]
    drawType: NotRequired[str]


class CaptureOptions(TypedDict):
    includeMesh: NotRequired[bool]
    includeTexturePixels: NotRequired[bool]
    filter: NotRequired[Filter]
    detectPlayer: NotRequired[bool]


class Uniform(TypedDict):
    name: str
    type: str
    location: float


class Attribute(TypedDict):
    name: str
    type: str
    location: float


class ShaderInfo(TypedDict):
    id: float
    vertexSource: str
    fragmentSource: str
    uniforms: list[Uniform]
    attributes: list[Attribute]
    matchType: str
    kind: str
    fragmentHash: str
    vertexHash: str


class TextureInfo(TypedDict):
    id: float
    width: float
    height: float
    format: str
    source: NotRequired[Literal["snapshot", "frame-harvest"]]


class WorldPos(TypedDict):
    x: float
    y: float
    z: float


class TileCoord(TypedDict):
    x: float
    z: float


class ChunkCoord(TypedDict):
    x: float
    z: float


class Screen1(TypedDict):
    x: float
    y: float
    depth: float


type EntityKind = Literal[
    "player", "npc", "scenery", "floor", "water", "particles", "ui", "unknown"
]


type ChatColor = list[float]


class ChatRun(TypedDict):
    text: str
    color: ChatColor


class ChatGlyph(TypedDict):
    char: str
    x: float
    color: ChatColor


class ChatRect(TypedDict):
    x: float
    y: float
    w: float
    h: float


class ChatBox(TypedDict):
    x0: float
    y0: float
    x1: float
    y1: float


class BarRect(TypedDict):
    x: float
    y: float
    w: float
    h: float


class BuffRect(TypedDict):
    x: float
    y: float
    w: float
    h: float


class SkillRect(TypedDict):
    x: float
    y: float
    w: float
    h: float


type SkillName = Literal[
    "attack",
    "defence",
    "strength",
    "constitution",
    "ranged",
    "prayer",
    "magic",
    "cooking",
    "woodcutting",
    "fletching",
    "fishing",
    "firemaking",
    "crafting",
    "smithing",
    "mining",
    "herblore",
    "agility",
    "thieving",
    "slayer",
    "farming",
    "runecrafting",
    "hunter",
    "construction",
    "summoning",
    "dungeoneering",
    "divination",
    "invention",
    "archaeology",
    "necromancy",
]


class AbilityRect(TypedDict):
    x: float
    y: float
    w: float
    h: float


class ProgressBar(TypedDict):
    combo: str
    name: str | None
    x: float
    y: float
    w: float
    h: float
    percent: float
    confident: bool


class ProgressGroup(TypedDict):
    combo: str
    name: str | None
    count: float
    stableCount: float
    percents: list[float]
    minPercent: float
    maxPercent: float
    confident: bool


class ProgressBegan(TypedDict):
    combo: str
    name: str | None


class ProgressEnded(TypedDict):
    combo: str
    name: str | None
    maxPercent: float


class Shader(TypedDict):
    fragmentSource: str
    vertexSource: NotRequired[str]


class Anchor2(TypedDict):
    mode: Literal["world"]
    world: WorldPos


class Anchor4(TypedDict):
    mode: Literal["entity"]
    entityId: str
    offset: NotRequired[WorldPos]


class TilePos(TypedDict):
    x: float
    z: float
    floor: float
    liftY: NotRequired[float]


class ScreenPos(TypedDict):
    x: float
    y: float
    normalized: NotRequired[bool]


type Primitive = Literal[
    "triangles",
    "triangle_strip",
    "triangle_fan",
    "lines",
    "line_strip",
    "line_loop",
    "points",
]


type Color = str | list[float]


type BlendMode = Literal["normal", "additive", "multiply"]


type AnimationKind = Literal["pulse", "flow", "fade", "rotate"]


type HAlign = Literal["left", "center", "right"]


type VAlign = Literal["top", "middle", "bottom"]


class PostFxPassInput(TypedDict):
    id: str
    fragmentSource: str
    uniforms: NotRequired[UniformValueMap]
    order: NotRequired[float]
    enabled: NotRequired[bool]


class ShaderFxInput(TypedDict):
    id: str
    matchType: NotRequired[str]
    matchHash: NotRequired[str]
    fragmentSource: NotRequired[str]
    vertexSource: NotRequired[str]
    enabled: NotRequired[bool]


class PlayerNameResult(TypedDict):
    ok: bool
    found: bool
    displayName: str
    name: str
    prefix: NotRequired[str]
    suffix: NotRequired[str]
    title: NotRequired[str]


type WidgetType = Literal[
    "panel",
    "row",
    "column",
    "stack",
    "grid",
    "spacer",
    "label",
    "gauge",
    "image",
    "divider",
    "badge",
    "button",
    "accordion",
    "worldLabel",
    "worldMarker",
    "tile",
]


type Spacing = float | list[float]


class ShadowSpec(TypedDict):
    dx: NotRequired[float]
    dy: NotRequired[float]
    blur: NotRequired[float]
    spread: NotRequired[float]
    color: NotRequired[Color]


type Align = Literal["start", "center", "end", "stretch"]


type Justify = Literal["start", "center", "end", "between", "around", "evenly"]


type ScreenAnchor = Literal[
    "top-left",
    "top",
    "top-right",
    "left",
    "center",
    "right",
    "bottom-left",
    "bottom",
    "bottom-right",
]


class UIPoint(TypedDict):
    x: float
    y: float


class UITile(TypedDict):
    x: float
    z: float
    floor: NotRequired[float]


class UIWorld(TypedDict):
    x: float
    y: float
    z: float


class DrawInfo(TypedDict):
    index: float
    shaderId: float
    vertexCount: float
    meshId: float
    targetFbo: float
    viewport: Rect
    textures: list[Texture]
    uniforms: UniformValueMap
    sampleRect: NotRequired[SampleRect]
    spriteRect: NotRequired[SpriteRect]
    screenBbox: NotRequired[ScreenBbox]
    rotationM12: NotRequired[float]
    rotationM21: NotRequired[float]
    composedQuads: NotRequired[list[ComposedQuad]]


class PlayerInfo(TypedDict):
    world: WorldPos
    tile: TileCoord
    chunk: ChunkCoord
    floor: float


class Entity(TypedDict):
    id: str
    drawIndex: float
    kind: EntityKind
    world: WorldPos
    tile: TileCoord
    chunk: ChunkCoord
    floor: float
    meshId: float
    shaderId: float
    vertexCount: float
    tag: str
    screen: Screen1 | None


class ChatLine(TypedDict):
    y: float
    text: str
    color: ChatColor
    runs: list[ChatRun]
    glyphs: list[ChatGlyph]


class ChatLocate(TypedDict):
    allChat: ChatRect
    cog: ChatRect
    quickChat: ChatRect
    box: ChatBox


class Bar(TypedDict):
    name: str
    combo: str | None
    found: bool
    fillPct: float | None
    value: float | None
    max: float | None
    text: str | None
    rect: BarRect | None


class Buff(TypedDict):
    kind: Literal["buff", "debuff"]
    name: str | None
    iconColorHash: float | None
    value: float | None
    text: str | None
    rect: BuffRect


class Skill(TypedDict):
    name: str
    level: float | None
    base: float | None
    rect: SkillRect


class AbilitySlot(TypedDict):
    name: str
    rect: AbilityRect
    atlas: AbilityRect
    activating: bool
    onCooldown: bool
    cooldownText: str
    cooldownSeconds: float | None
    usable: bool
    color: list[float]


class ProgressReadResult(TypedDict):
    ok: bool
    ageMs: float
    count: float
    bars: list[ProgressBar]
    groups: list[ProgressGroup]
    began: list[ProgressBegan]
    ended: list[ProgressEnded]


class Anchor1(TypedDict):
    mode: Literal["tile"]
    tile: TilePos


class Anchor3(TypedDict):
    mode: Literal["screen"]
    screen: ScreenPos


type Anchor = Anchor1 | Anchor2 | Anchor3 | Anchor4


class AnimationSpec(TypedDict):
    kind: AnimationKind
    period: NotRequired[float]
    amplitude: NotRequired[float]


class Billboard(TypedDict):
    kind: Literal["billboard"]
    id: NotRequired[str]
    anchor: Anchor
    text: str
    font: NotRequired[str]
    fontSize: NotRequired[float]
    color: NotRequired[Color]
    background: NotRequired[Color]
    halign: NotRequired[HAlign]
    valign: NotRequired[VAlign]
    worldScale: NotRequired[float]
    padding: NotRequired[float]
    occludedByWorld: NotRequired[bool]
    group: NotRequired[str]


class ImageItem(TypedDict):
    kind: Literal["image"]
    id: NotRequired[str]
    anchor: Anchor
    width: float
    height: float
    color: NotRequired[Color]
    occludedByWorld: NotRequired[bool]
    group: NotRequired[str]


class FrameCaptureResult(TypedDict):
    draws: list[DrawInfo]
    drawCount: float


class UIProps(TypedDict):
    width: NotRequired[float | Literal["auto"] | Literal["flex"]]
    height: NotRequired[float | Literal["auto"] | Literal["flex"]]
    pad: NotRequired[Spacing]
    margin: NotRequired[Spacing]
    gap: NotRequired[float]
    bg: NotRequired[Color]
    color: NotRequired[Color]
    fill: NotRequired[Color]
    track: NotRequired[Color]
    outline: NotRequired[Color]
    outlineWidth: NotRequired[float]
    radius: NotRequired[float]
    opacity: NotRequired[float]
    shadow: NotRequired[bool | ShadowSpec]
    blend: NotRequired[BlendMode]
    animation: NotRequired[AnimationSpec]
    align: NotRequired[Align]
    justify: NotRequired[Justify]
    font: NotRequired[str]
    fontSize: NotRequired[float]
    text: NotRequired[str | float]
    value: NotRequired[float]
    max: NotRequired[float]
    min: NotRequired[float]
    vertical: NotRequired[bool]
    src: NotRequired[str]
    tint: NotRequired[Color]
    anchor: NotRequired[ScreenAnchor | UIPoint]
    tile: NotRequired[UITile]
    world: NotRequired[UIWorld]
    id: NotRequired[str]
    group: NotRequired[str]


class SceneSnapshot(TypedDict):
    timestamp: float
    player: PlayerInfo | None
    npcs: list[Entity]
    scenery: list[Entity]
    floor: list[Entity]
    water: list[Entity]
    particles: list[Entity]
    other: list[Entity]
    viewProj: list[float] | None


class ChatReadResult(TypedDict):
    ok: bool
    font: str | None
    lineCount: float
    stale: bool
    ageMs: float
    lines: list[ChatLine]
    locate: ChatLocate


class BarsReadResult(TypedDict):
    ok: bool
    stale: bool
    ageMs: float
    bars: list[Bar]
    began: list[str]
    ended: list[str]


class BuffsReadResult(TypedDict):
    ok: bool
    stale: bool
    ageMs: float
    buffs: list[Buff]
    debuffs: list[Buff]


class SkillsReadResult(TypedDict):
    ok: bool
    stale: bool
    ageMs: float
    skills: list[Skill]


class AbilitiesReadResult(TypedDict):
    ok: bool
    stale: bool
    ageMs: float
    abilities: list[AbilitySlot]


class Shape(TypedDict):
    id: NotRequired[str]
    anchor: Anchor
    primitive: Primitive
    vertices: list[float]
    indices: NotRequired[list[float]]
    color: Color | list[Color]
    thickness: NotRequired[float]
    pointSize: NotRequired[float]
    occludedByWorld: NotRequired[bool]
    blend: NotRequired[BlendMode]
    animation: NotRequired[AnimationSpec]
    group: NotRequired[str]
    shader: NotRequired[Shader]
    uniforms: NotRequired[UniformValueMap]
    conformToGround: NotRequired[bool]
    groundOffset: NotRequired[float]


class UIWidget(TypedDict):
    type: WidgetType
    props: NotRequired[UIProps]
    children: NotRequired[list[UIWidget]]


type DrawItem = Shape | Billboard | ImageItem


class RS3buddyApi(TypedDict):
    position: Position
    drawInfo: DrawInfo
    captureOptions: CaptureOptions
    shaderInfo: ShaderInfo
    textureInfo: TextureInfo
    sceneSnapshot: SceneSnapshot
    entity: Entity
    playerInfo: PlayerInfo
    tileCoord: TileCoord
    worldPos: WorldPos
    chunkCoord: ChunkCoord
    entityKind: EntityKind
    chatReadResult: ChatReadResult
    barsReadResult: BarsReadResult
    buffsReadResult: BuffsReadResult
    skillsReadResult: SkillsReadResult
    skillName: SkillName
    abilitiesReadResult: AbilitiesReadResult
    progressReadResult: ProgressReadResult
    drawItem: DrawItem
    postFxPassInput: PostFxPassInput
    shaderFxInput: ShaderFxInput
    playerNameResult: PlayerNameResult
    frameCaptureResult: FrameCaptureResult
    uiWidget: UIWidget


type Model = RS3buddyApi
