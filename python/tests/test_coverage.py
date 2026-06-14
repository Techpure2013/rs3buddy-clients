import unittest
from rs3buddy import RS3Buddy

EXPECTED = [
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
]


class CoverageTest(unittest.TestCase):
    def test_every_route_method_exists(self):
        b = RS3Buddy(base_url="http://127.0.0.1:1")
        for name in EXPECTED:
            self.assertTrue(callable(getattr(b, name, None)), f"missing method: {name}")


if __name__ == "__main__":
    unittest.main()
