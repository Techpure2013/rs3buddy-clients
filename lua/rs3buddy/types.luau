--!strict
-- AUTO-GENERATED from schema/rs3buddy.schema.json — do not edit by hand.
-- Regenerate: npm run gen:models:lua

export type Position = {
	tileX: number,
	tileZ: number,
	worldX: number,
	worldZ: number,
}

export type DrawInfo = {
	index: number,
	shaderId: number,
	vertexCount: number,
	meshId: number,
	targetFbo: number,
	viewport: Rect,
	textures: {{
	slot: number,
	id: number,
	width: number,
	height: number,
}},
	uniforms: UniformValueMap,
	sampleRect: SampleRect?,
	spriteRect: SpriteRect?,
	screenBbox: ScreenBbox?,
	rotationM12: number?,
	rotationM21: number?,
	composedQuads: {ComposedQuad}?,
}

export type Rect = {
	x: number,
	y: number,
	width: number,
	height: number,
}

export type UniformValueMap = { [string]: any }

export type SampleRect = {
	x: number,
	y: number,
	w: number,
	h: number,
}

export type SpriteRect = {
	x: number,
	y: number,
	w: number,
	h: number,
}

export type ScreenBbox = {
	x: number,
	y: number,
	w: number,
	h: number,
}

export type ComposedQuad = {
	sampleRect: SampleRect,
	spriteRect: SampleRect,
	screen: {
	x: number,
	y: number,
	w: number,
	h: number,
},
	m12: number,
	m21: number,
	whitesprite: boolean?,
	color: {number}?,
	_colourOff: number?,
	_topleft: number?,
}

export type CaptureOptions = {
	includeMesh: boolean?,
	includeTexturePixels: boolean?,
	filter: {
	shaderId: number?,
	meshId: number?,
	targetFbo: number?,
	drawType: string?,
}?,
	detectPlayer: boolean?,
}

export type ShaderInfo = {
	id: number,
	vertexSource: string,
	fragmentSource: string,
	uniforms: {{
	name: string,
	type: string,
	location: number,
}},
	attributes: {{
	name: string,
	type: string,
	location: number,
}},
	matchType: string,
	kind: string,
	fragmentHash: string,
	vertexHash: string,
}

export type TextureInfo = {
	id: number,
	width: number,
	height: number,
	format: string,
	source: "snapshot" | "frame-harvest"?,
}

export type SceneSnapshot = {
	timestamp: number,
	player: any,
	npcs: {Entity},
	scenery: {Entity},
	floor: {Entity},
	water: {Entity},
	particles: {Entity},
	other: {Entity},
	viewProj: any,
}

export type PlayerInfo = {
	world: WorldPos,
	tile: TileCoord,
	chunk: ChunkCoord,
	floor: number,
}

export type WorldPos = {
	x: number,
	y: number,
	z: number,
}

export type TileCoord = {
	x: number,
	z: number,
}

export type ChunkCoord = {
	x: number,
	z: number,
}

export type Entity = {
	id: string,
	drawIndex: number,
	kind: EntityKind,
	world: WorldPos,
	tile: TileCoord,
	chunk: ChunkCoord,
	floor: number,
	meshId: number,
	shaderId: number,
	vertexCount: number,
	tag: string,
	screen: any,
}

export type EntityKind = "player" | "npc" | "scenery" | "floor" | "water" | "particles" | "ui" | "unknown"

export type ChatReadResult = {
	ok: boolean,
	font: any,
	lineCount: number,
	stale: boolean,
	ageMs: number,
	lines: {ChatLine},
	locate: ChatLocate,
}

export type ChatLine = {
	y: number,
	text: string,
	color: ChatColor,
	runs: {ChatRun},
	glyphs: {ChatGlyph},
}

export type ChatColor = {number}

export type ChatRun = {
	text: string,
	color: ChatColor,
}

export type ChatGlyph = {
	char: string,
	x: number,
	color: ChatColor,
}

export type ChatLocate = {
	allChat: ChatRect,
	cog: ChatRect,
	quickChat: ChatRect,
	box: ChatBox,
}

export type ChatRect = {
	x: number,
	y: number,
	w: number,
	h: number,
}

export type ChatBox = {
	x0: number,
	y0: number,
	x1: number,
	y1: number,
}

export type DrawItem = any

export type Shape = {
	id: string?,
	anchor: Anchor,
	primitive: Primitive,
	vertices: {number},
	indices: {number}?,
	color: any,
	thickness: number?,
	pointSize: number?,
	occludedByWorld: boolean?,
	blend: BlendMode?,
	animation: AnimationSpec?,
	group: string?,
	shader: {
	fragmentSource: string,
	vertexSource: string?,
}?,
	uniforms: UniformValueMap?,
	conformToGround: boolean?,
	groundOffset: number?,
}

export type Anchor = any

export type TilePos = {
	x: number,
	z: number,
	floor: number,
	liftY: number?,
}

export type ScreenPos = {
	x: number,
	y: number,
	normalized: boolean?,
}

export type Primitive = "triangles" | "triangle_strip" | "triangle_fan" | "lines" | "line_strip" | "line_loop" | "points"

export type Color = any

export type BlendMode = "normal" | "additive" | "multiply"

export type AnimationSpec = {
	kind: AnimationKind,
	period: number?,
	amplitude: number?,
}

export type AnimationKind = "pulse" | "flow" | "fade" | "rotate"

export type Billboard = {
	kind: string,
	id: string?,
	anchor: Anchor,
	text: string,
	font: string?,
	fontSize: number?,
	color: Color?,
	background: Color?,
	halign: HAlign?,
	valign: VAlign?,
	worldScale: number?,
	padding: number?,
	occludedByWorld: boolean?,
	group: string?,
}

export type HAlign = "left" | "center" | "right"

export type VAlign = "top" | "middle" | "bottom"

export type ImageItem = {
	kind: string,
	id: string?,
	anchor: Anchor,
	width: number,
	height: number,
	color: Color?,
	occludedByWorld: boolean?,
	group: string?,
}

export type PostFxPassInput = {
	id: string,
	fragmentSource: string,
	uniforms: UniformValueMap?,
	order: number?,
	enabled: boolean?,
}

export type ShaderFxInput = {
	id: string,
	matchType: string?,
	matchHash: string?,
	fragmentSource: string?,
	vertexSource: string?,
	enabled: boolean?,
}

export type PlayerNameResult = {
	ok: boolean,
	found: boolean,
	displayName: string,
	name: string,
	prefix: string?,
	suffix: string?,
	title: string?,
}

export type FrameCaptureResult = {
	draws: {DrawInfo},
	drawCount: number,
}

return {}
