--!strict
local rs3buddy = require("./client")

local EXPECTED = {
	"get_player", "update_player", "get_status", "get_window", "get_heap",
	"capture_frame", "get_shaders", "get_textures", "get_captured_textures",
	"refresh_captured_textures", "captured_textures_stats", "clear_captured_textures",
	"get_captured_texture", "read_texture", "screen_capture", "get_glyphs_on_screen", "glyph_at_point",
	"get_scene", "get_scene_player", "get_npcs", "get_scenery", "get_floor_tiles", "get_water", "get_entity_at",
	"draw_shape", "draw_scene", "list_shapes", "remove_shape", "clear_shapes",
	"list_fonts", "register_font", "unregister_font",
	"list_trained_sprites", "save_trained_sprite", "delete_trained_sprite",
	"import_sprite_hash", "train_quad", "atlas_sync", "atlas_sprites", "atlas_lookup", "recognize_quads",
	"match_text", "match_region",
}

local b = rs3buddy.new({ base_url = "http://127.0.0.1:1", request = function(): (number, string)
	return 200, "{}"
end })

local passed = 0
for _, name in ipairs(EXPECTED) do
	assert(type((b :: any)[name]) == "function", "missing method: " .. name)
	passed += 1
end

print(("coverage_test: %d methods present"):format(passed))
