--!strict
-- rs3buddy — typed Lua/Luau client for the rs3buddy HTTP server.
-- Behavior contract: ../CONTRACT.md
-- Pluggable transport: runs in any Lua host (luasocket, Roblox HttpService, a custom bridge).

local transport = require("./transport")
local types = require("./types")

export type Position = types.Position
export type DrawInfo = types.DrawInfo
export type ShaderInfo = types.ShaderInfo
export type TextureInfo = types.TextureInfo
export type SceneSnapshot = types.SceneSnapshot
export type ChatReadResult = types.ChatReadResult
export type DrawItem = types.DrawItem
export type PostFxPassInput = types.PostFxPassInput
export type ShaderFxInput = types.ShaderFxInput
export type PlayerNameResult = types.PlayerNameResult
export type FrameCaptureResult = types.FrameCaptureResult
export type TransportOptions = transport.TransportOptions

-- ── Chat reader namespace (reached via client.chat) ──
-- A tiny table bound over the transport so callers write `client.chat:read()`,
-- mirroring the TS SDK's `chat.read()`. Kept separate from the flat RS3Buddy
-- methods because it's the one route with its own sub-namespace. The `read`
-- method is defined below `q` (the query-string helper) so it can use it.
local ChatAPI = {}
ChatAPI.__index = ChatAPI

export type ChatAPI = typeof(setmetatable(
	{} :: { _t: transport.Transport },
	ChatAPI
))
export type ChatReadOptions = { x0: number?, y0: number?, x1: number?, y1: number? }

local RS3Buddy = {}
RS3Buddy.__index = RS3Buddy

export type RS3Buddy = typeof(setmetatable(
	{} :: { _t: transport.Transport, chat: ChatAPI },
	RS3Buddy
))

-- Build a "?key=val&..." query string, skipping nil values.
local function q(params: { [string]: any }): string
	local parts: { string } = {}
	for k, v in pairs(params) do
		if v ~= nil then
			table.insert(parts, tostring(k) .. "=" .. tostring(v))
		end
	end
	if #parts == 0 then
		return ""
	end
	return "?" .. table.concat(parts, "&")
end

-- Read the chatbox as structured lines + per-glyph colour. Thin wrapper over
-- GET /api/chat; the heavy work (frame + screen capture, glyph recognition,
-- line grouping, colour sampling) runs server-side. Pass x0/y0/x1/y1 (window
-- px) to override the default bottom-left chatbox region. Returns:
--   { ok = true, lineCount = n, lines = { { y, text, color = {r,g,b},
--       glyphs = { { char, x, color = {r,g,b} } } } } }  (lines top-to-bottom)
function ChatAPI.read(self: ChatAPI, opts: ChatReadOptions?): ChatReadResult
	local o: ChatReadOptions = opts or {}
	return self._t:request("GET", "/api/chat" .. q({ x0 = o.x0, y0 = o.y0, x1 = o.x1, y1 = o.y1 }))
end

local function new(opts: TransportOptions): RS3Buddy
	local t = transport.new(opts)
	return setmetatable({
		_t = t,
		chat = setmetatable({ _t = t }, ChatAPI),
	}, RS3Buddy)
end

-- Connect to the running SDK server. With no base_url in opts, the transport
-- auto-discovers the server from the launcher's config (rs3buddy.json under
-- %APPDATA%\rs3buddy). The launcher must be running (native injected into RS3 ->
-- SDK server up). Sandboxed hosts with no io/os (Roblox) must pass a
-- base_url (and a request fn). Mirrors the Python client's RS3Buddy.connect().
local function connect(opts: TransportOptions?): RS3Buddy
	return new(opts or {})
end

function RS3Buddy.base_url(self: RS3Buddy): string
	return self._t.base_url
end

-- ── Player (both hit GET /api/player; ride the native passive tap) ──
function RS3Buddy.get_player(self: RS3Buddy): Position?
	return self._t:request("GET", "/api/player")
end
function RS3Buddy.update_player(self: RS3Buddy): Position?
	return self._t:request("GET", "/api/player")
end
-- The local player's name + title(s), recovered from the chat input prompt
-- (opt-in; the name never appears in the chat feed itself). `found` is false
-- until the prompt has been seen at least once.
function RS3Buddy.get_player_name(self: RS3Buddy): PlayerNameResult
	return self._t:request("GET", "/api/player/name")
end

-- ── Status / window / heap ──
function RS3Buddy.get_status(self: RS3Buddy): any return self._t:request("GET", "/api/status") end
function RS3Buddy.get_window(self: RS3Buddy): any return self._t:request("GET", "/api/window") end
function RS3Buddy.get_heap(self: RS3Buddy): any return self._t:request("GET", "/api/heap") end

-- ── Capture / shaders / textures ──
export type CaptureFrameOptions = {
	include_mesh: boolean?,
	include_texture_pixels: boolean?,
	shader_id: number?,
	mesh_id: number?,
	target_fbo: number?,
}
function RS3Buddy.capture_frame(self: RS3Buddy, opts: CaptureFrameOptions?): FrameCaptureResult
	local o: CaptureFrameOptions = opts or {}
	return self._t:request("GET", "/api/frame" .. q({
		mesh = if o.include_mesh then "true" else nil,
		pixels = if o.include_texture_pixels then "true" else nil,
		shaderId = o.shader_id,
		meshId = o.mesh_id,
		targetFbo = o.target_fbo,
	}))
end
function RS3Buddy.get_shaders(self: RS3Buddy): { ShaderInfo } return self._t:request("GET", "/api/shaders") end
function RS3Buddy.get_textures(self: RS3Buddy): { TextureInfo } return self._t:request("GET", "/api/textures") end
function RS3Buddy.get_captured_textures(self: RS3Buddy): any return self._t:request("GET", "/api/captured-textures") end
function RS3Buddy.refresh_captured_textures(self: RS3Buddy): any return self._t:request("POST", "/api/captured-textures/refresh") end
function RS3Buddy.captured_textures_stats(self: RS3Buddy): any return self._t:request("GET", "/api/captured-textures/stats") end
function RS3Buddy.clear_captured_textures(self: RS3Buddy): any return self._t:request("DELETE", "/api/captured-textures") end
function RS3Buddy.get_captured_texture(self: RS3Buddy, id: number): any return self._t:request("GET", "/api/captured-texture/" .. tostring(id)) end
function RS3Buddy.read_texture(self: RS3Buddy, id: number, x: number?, y: number?, w: number?, h: number?): any
	return self._t:request("GET", "/api/texture/" .. tostring(id) .. q({ x = x or 0, y = y or 0, w = w or 0, h = h or 0 }))
end
function RS3Buddy.screen_capture(self: RS3Buddy): any return self._t:request("GET", "/api/screen-capture") end
function RS3Buddy.get_glyphs_on_screen(self: RS3Buddy, opts: { [string]: number }?): any return self._t:request("GET", "/api/glyphs-on-screen" .. q(opts or {})) end
function RS3Buddy.glyph_at_point(self: RS3Buddy, x: number, y: number, opts: { [string]: number }?): any
	local body: { [string]: number } = { x = x, y = y }
	if opts then for k, v in pairs(opts) do body[k] = v end end
	return self._t:request("POST", "/api/glyph-at-point", body)
end

-- ── Scene ──
function RS3Buddy.get_scene(self: RS3Buddy): SceneSnapshot return self._t:request("GET", "/api/scene") end
function RS3Buddy.get_scene_player(self: RS3Buddy): any return self._t:request("GET", "/api/scene/player") end
function RS3Buddy.get_npcs(self: RS3Buddy, radius: number?, floor: number?): any return self._t:request("GET", "/api/scene/npcs" .. q({ radius = radius, floor = floor })) end
function RS3Buddy.get_scenery(self: RS3Buddy, radius: number?, floor: number?): any return self._t:request("GET", "/api/scene/scenery" .. q({ radius = radius, floor = floor })) end
function RS3Buddy.get_floor_tiles(self: RS3Buddy, radius: number?, floor: number?): any return self._t:request("GET", "/api/scene/floor_tiles" .. q({ radius = radius, floor = floor })) end
function RS3Buddy.get_water(self: RS3Buddy, radius: number?, floor: number?): any return self._t:request("GET", "/api/scene/water" .. q({ radius = radius, floor = floor })) end
function RS3Buddy.get_entity_at(self: RS3Buddy, x: number, z: number, floor: number?): any return self._t:request("GET", "/api/scene/at" .. q({ x = x, z = z, floor = floor })) end

-- ── Draw / overlay ──
function RS3Buddy.draw_shape(self: RS3Buddy, shape: DrawItem): any return self._t:request("POST", "/api/draw/shape", shape) end
function RS3Buddy.draw_scene(self: RS3Buddy, shapes: { DrawItem }): any return self._t:request("POST", "/api/draw/scene", shapes) end
function RS3Buddy.list_shapes(self: RS3Buddy): any return self._t:request("GET", "/api/draw") end
function RS3Buddy.remove_shape(self: RS3Buddy, id: string): any return self._t:request("DELETE", "/api/draw/" .. id) end
function RS3Buddy.clear_shapes(self: RS3Buddy, group: string?): any return self._t:request("DELETE", "/api/draw" .. q({ group = group })) end

-- ── Fonts ──
-- List the registered render/DRAW fonts (alias -> .ttf), NOT the recognition registry.
function RS3Buddy.list_fonts(self: RS3Buddy): any return self._t:request("GET", "/api/fonts/registered") end
function RS3Buddy.register_font(self: RS3Buddy, name: string, path: string): any return self._t:request("POST", "/api/fonts/register", { name = name, path = path }) end
function RS3Buddy.unregister_font(self: RS3Buddy, name: string): any return self._t:request("DELETE", "/api/fonts/" .. name) end

-- ── Sprites ──
function RS3Buddy.list_trained_sprites(self: RS3Buddy): any return self._t:request("GET", "/api/sprites/trained") end
function RS3Buddy.save_trained_sprite(self: RS3Buddy, req: any): any return self._t:request("POST", "/api/sprites/trained", req) end
function RS3Buddy.delete_trained_sprite(self: RS3Buddy, name: string): any return self._t:request("DELETE", "/api/sprites/trained/" .. name) end

-- ── Atlas ──
function RS3Buddy.import_sprite_hash(self: RS3Buddy, req: any): any return self._t:request("POST", "/api/atlas/import-spritehash", req) end
function RS3Buddy.train_quad(self: RS3Buddy, req: any): any return self._t:request("POST", "/api/atlas/train-quad", req) end
function RS3Buddy.atlas_sync(self: RS3Buddy): any return self._t:request("POST", "/api/atlas/sync") end
function RS3Buddy.atlas_sprites(self: RS3Buddy): any return self._t:request("GET", "/api/atlas/sprites") end
function RS3Buddy.atlas_lookup(self: RS3Buddy, hash: number): any return self._t:request("GET", "/api/atlas/lookup/" .. tostring(hash)) end
function RS3Buddy.recognize_quads(self: RS3Buddy, quads: { any }): any return self._t:request("POST", "/api/atlas/recognize-quads", { quads = quads }) end

-- ── OCR ──
function RS3Buddy.match_text(self: RS3Buddy, req: any): any return self._t:request("POST", "/api/match/text", req) end
function RS3Buddy.match_region(self: RS3Buddy, req: any): any return self._t:request("POST", "/api/match/region", req) end

-- ── Post-FX (fullscreen post-processing passes) ──
-- Register or replace a fullscreen post-FX pass; returns { id = ... }.
function RS3Buddy.add_post_fx(self: RS3Buddy, pass: PostFxPassInput): any return self._t:request("POST", "/api/shaders/postfx", pass) end
-- All post-FX passes, in render order (ascending `order`).
function RS3Buddy.list_post_fx(self: RS3Buddy): { PostFxPassInput } return self._t:request("GET", "/api/shaders/postfx") end
-- Remove a post-FX pass; returns { removed = bool }.
function RS3Buddy.remove_post_fx(self: RS3Buddy, id: string): any return self._t:request("DELETE", "/api/shaders/postfx/" .. id) end
-- Enable/disable a post-FX pass without removing it; returns { ok = bool }.
function RS3Buddy.set_post_fx_enabled(self: RS3Buddy, id: string, enabled: boolean): any return self._t:request("POST", "/api/shaders/postfx/" .. id .. "/enabled", { enabled = enabled }) end

-- ── Custom game-shader FX (replace RS3's own shaders, by type or hash) ──
-- Register or replace a custom game-shader FX; returns { id = ... }.
function RS3Buddy.add_shader_fx(self: RS3Buddy, o: ShaderFxInput): any return self._t:request("POST", "/api/shaders/fx", o) end
-- All active custom game-shader FX.
function RS3Buddy.list_shader_fx(self: RS3Buddy): { ShaderFxInput } return self._t:request("GET", "/api/shaders/fx") end
-- Remove a shader FX (that shader reverts to stock); returns { removed = bool }.
function RS3Buddy.remove_shader_fx(self: RS3Buddy, id: string): any return self._t:request("DELETE", "/api/shaders/fx/" .. id) end
-- Enable/disable a shader FX without removing it; returns { ok = bool }.
function RS3Buddy.set_shader_fx_enabled(self: RS3Buddy, id: string, enabled: boolean): any return self._t:request("POST", "/api/shaders/fx/" .. id .. "/enabled", { enabled = enabled }) end

return {
	new = new,
	connect = connect,
	RS3Buddy = RS3Buddy,
}
