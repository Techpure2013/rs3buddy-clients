-- AUTO-GENERATED from schema/rs3buddy.schema.json — do not edit by hand.
-- Regenerate: npm run gen:models:lua
-- LuaCATS annotations for lua-language-server (standard Lua / LuaJIT).

---@class Position
---@field tileX number
---@field tileZ number
---@field worldX number
---@field worldZ number

---@class DrawInfo
---@field index number
---@field shaderId number
---@field vertexCount number
---@field meshId number
---@field targetFbo number
---@field viewport Rect
---@field textures table[]
---@field uniforms UniformValueMap
---@field sampleRect? SampleRect
---@field spriteRect? SpriteRect
---@field screenBbox? ScreenBbox
---@field rotationM12? number
---@field rotationM21? number
---@field composedQuads? ComposedQuad[]

---@class Rect
---@field x number
---@field y number
---@field width number
---@field height number

---@alias UniformValueMap table<string, any>

---@class SampleRect
---@field x number
---@field y number
---@field w number
---@field h number

---@class SpriteRect
---@field x number
---@field y number
---@field w number
---@field h number

---@class ScreenBbox
---@field x number
---@field y number
---@field w number
---@field h number

---@class ComposedQuad
---@field sampleRect SampleRect
---@field spriteRect SampleRect
---@field screen table
---@field m12 number
---@field m21 number
---@field whitesprite? boolean
---@field color? number[]
---@field _colourOff? number
---@field _topleft? number

---@class CaptureOptions
---@field includeMesh? boolean
---@field includeTexturePixels? boolean
---@field filter? table
---@field detectPlayer? boolean

---@class ShaderInfo
---@field id number
---@field vertexSource string
---@field fragmentSource string
---@field uniforms table[]
---@field attributes table[]
---@field matchType string
---@field kind string
---@field fragmentHash string
---@field vertexHash string

---@class TextureInfo
---@field id number
---@field width number
---@field height number
---@field format string
---@field source? "snapshot"|"frame-harvest"

---@class SceneSnapshot
---@field timestamp number
---@field player any
---@field npcs Entity[]
---@field scenery Entity[]
---@field floor Entity[]
---@field water Entity[]
---@field particles Entity[]
---@field other Entity[]
---@field viewProj any

---@class PlayerInfo
---@field world WorldPos
---@field tile TileCoord
---@field chunk ChunkCoord
---@field floor number

---@class WorldPos
---@field x number
---@field y number
---@field z number

---@class TileCoord
---@field x number
---@field z number

---@class ChunkCoord
---@field x number
---@field z number

---@class Entity
---@field id string
---@field drawIndex number
---@field kind EntityKind
---@field world WorldPos
---@field tile TileCoord
---@field chunk ChunkCoord
---@field floor number
---@field meshId number
---@field shaderId number
---@field vertexCount number
---@field tag string
---@field screen any

---@alias EntityKind "player"|"npc"|"scenery"|"floor"|"water"|"particles"|"ui"|"unknown"

---@class ChatReadResult
---@field ok boolean
---@field font any
---@field lineCount number
---@field stale boolean
---@field ageMs number
---@field lines ChatLine[]
---@field locate ChatLocate

---@class ChatLine
---@field y number
---@field text string
---@field color ChatColor
---@field runs ChatRun[]
---@field glyphs ChatGlyph[]

---@alias ChatColor number[]

---@class ChatRun
---@field text string
---@field color ChatColor

---@class ChatGlyph
---@field char string
---@field x number
---@field color ChatColor

---@class ChatLocate
---@field allChat ChatRect
---@field cog ChatRect
---@field quickChat ChatRect
---@field box ChatBox

---@class ChatRect
---@field x number
---@field y number
---@field w number
---@field h number

---@class ChatBox
---@field x0 number
---@field y0 number
---@field x1 number
---@field y1 number

---@class BarsReadResult
---@field ok boolean
---@field stale boolean
---@field ageMs number
---@field bars BarValue[]

---@class BarValue
---@field name BarName
---@field found boolean
---@field value any
---@field max any
---@field text string
---@field anchor any
---@field region any

---@alias BarName "hitpoints"|"adrenaline"|"prayer"|"summoning"

---@class BarRect
---@field x number
---@field y number
---@field w number
---@field h number

---@class BarBox
---@field x0 number
---@field y0 number
---@field x1 number
---@field y1 number

---@class AbilitiesReadResult
---@field ok boolean
---@field stale boolean
---@field ageMs number
---@field abilities AbilitySlot[]

---@class AbilitySlot
---@field name string
---@field rect AbilityRect
---@field atlas AbilityRect
---@field activating boolean
---@field onCooldown boolean
---@field cooldownText string
---@field cooldownSeconds any
---@field usable boolean
---@field color number[]

---@class AbilityRect
---@field x number
---@field y number
---@field w number
---@field h number

---@class ProgressReadResult
---@field ok boolean
---@field ageMs number
---@field count number
---@field bars ProgressBar[]
---@field groups ProgressGroup[]
---@field began ProgressBegan[]
---@field ended ProgressEnded[]

---@class ProgressBar
---@field combo string
---@field name any
---@field x number
---@field y number
---@field w number
---@field percent number
---@field confident boolean

---@class ProgressGroup
---@field combo string
---@field name any
---@field count number
---@field stableCount number
---@field percents number[]
---@field minPercent number
---@field maxPercent number
---@field confident boolean

---@class ProgressBegan
---@field combo string
---@field name any

---@class ProgressEnded
---@field combo string
---@field name any
---@field maxPercent number

---@alias DrawItem any

---@class Shape
---@field id? string
---@field anchor Anchor
---@field primitive Primitive
---@field vertices number[]
---@field indices? number[]
---@field color any
---@field thickness? number
---@field pointSize? number
---@field occludedByWorld? boolean
---@field blend? BlendMode
---@field animation? AnimationSpec
---@field group? string
---@field shader? table
---@field uniforms? UniformValueMap
---@field conformToGround? boolean
---@field groundOffset? number

---@alias Anchor any

---@class TilePos
---@field x number
---@field z number
---@field floor number
---@field liftY? number

---@class ScreenPos
---@field x number
---@field y number
---@field normalized? boolean

---@alias Primitive "triangles"|"triangle_strip"|"triangle_fan"|"lines"|"line_strip"|"line_loop"|"points"

---@alias Color any

---@alias BlendMode "normal"|"additive"|"multiply"

---@class AnimationSpec
---@field kind AnimationKind
---@field period? number
---@field amplitude? number

---@alias AnimationKind "pulse"|"flow"|"fade"|"rotate"

---@class Billboard
---@field kind string
---@field id? string
---@field anchor Anchor
---@field text string
---@field font? string
---@field fontSize? number
---@field color? Color
---@field background? Color
---@field halign? HAlign
---@field valign? VAlign
---@field worldScale? number
---@field padding? number
---@field occludedByWorld? boolean
---@field group? string

---@alias HAlign "left"|"center"|"right"

---@alias VAlign "top"|"middle"|"bottom"

---@class ImageItem
---@field kind string
---@field id? string
---@field anchor Anchor
---@field width number
---@field height number
---@field color? Color
---@field occludedByWorld? boolean
---@field group? string

---@class PostFxPassInput
---@field id string
---@field fragmentSource string
---@field uniforms? UniformValueMap
---@field order? number
---@field enabled? boolean

---@class ShaderFxInput
---@field id string
---@field matchType? string
---@field matchHash? string
---@field fragmentSource? string
---@field vertexSource? string
---@field enabled? boolean

---@class PlayerNameResult
---@field ok boolean
---@field found boolean
---@field displayName string
---@field name string
---@field prefix? string
---@field suffix? string
---@field title? string

---@class FrameCaptureResult
---@field draws DrawInfo[]
---@field drawCount number

---@class UIWidget
---@field type WidgetType
---@field props? UIProps
---@field children? UIWidget[]

---@alias WidgetType "panel"|"row"|"column"|"stack"|"grid"|"spacer"|"label"|"gauge"|"image"|"divider"|"badge"|"button"|"accordion"|"worldLabel"|"worldMarker"|"tile"

---@class UIProps
---@field width? any
---@field height? any
---@field pad? Spacing
---@field margin? Spacing
---@field gap? number
---@field bg? Color
---@field color? Color
---@field fill? Color
---@field track? Color
---@field outline? Color
---@field outlineWidth? number
---@field radius? number
---@field opacity? number
---@field shadow? any
---@field blend? BlendMode
---@field animation? AnimationSpec
---@field align? Align
---@field justify? Justify
---@field font? string
---@field fontSize? number
---@field text? any
---@field value? number
---@field max? number
---@field min? number
---@field vertical? boolean
---@field src? string
---@field tint? Color
---@field anchor? any
---@field tile? UITile
---@field world? UIWorld
---@field id? string
---@field group? string

---@alias Spacing any

---@class ShadowSpec
---@field dx? number
---@field dy? number
---@field blur? number
---@field spread? number
---@field color? Color

---@alias Align "start"|"center"|"end"|"stretch"

---@alias Justify "start"|"center"|"end"|"between"|"around"|"evenly"

---@alias ScreenAnchor "top-left"|"top"|"top-right"|"left"|"center"|"right"|"bottom-left"|"bottom"|"bottom-right"

---@class UIPoint
---@field x number
---@field y number

---@class UITile
---@field x number
---@field z number
---@field floor? number

---@class UIWorld
---@field x number
---@field y number
---@field z number

return {}
