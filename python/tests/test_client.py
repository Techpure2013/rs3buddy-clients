import json
import threading
import unittest
from http.server import BaseHTTPRequestHandler, HTTPServer

from rs3buddy import RS3Buddy


def _serve(handler_cls):
    srv = HTTPServer(("127.0.0.1", 0), handler_cls)
    threading.Thread(target=srv.serve_forever, daemon=True).start()
    return srv


class ClientTests(unittest.TestCase):
    def test_get_player_returns_position(self):
        class H(BaseHTTPRequestHandler):
            def log_message(self, *a): pass
            def do_GET(self):
                assert self.path == "/api/player"
                payload = json.dumps({"tileX": 3200, "tileZ": 3200, "floor": 0,
                                      "worldX": 1638400, "worldZ": 1638400}).encode()
                self.send_response(200); self.send_header("Content-Length", str(len(payload))); self.end_headers()
                self.wfile.write(payload)
        srv = _serve(H)
        try:
            b = RS3Buddy(base_url=f"http://127.0.0.1:{srv.server_address[1]}")
            p = b.get_player()
            self.assertEqual(p["tileX"], 3200)
            self.assertEqual(p["floor"], 0)
            b.close()
        finally:
            srv.shutdown()

    def test_update_player_returns_none(self):
        class H(BaseHTTPRequestHandler):
            def log_message(self, *a): pass
            def do_GET(self):
                payload = b"null"
                self.send_response(200); self.send_header("Content-Length", str(len(payload))); self.end_headers()
                self.wfile.write(payload)
        srv = _serve(H)
        try:
            b = RS3Buddy(base_url=f"http://127.0.0.1:{srv.server_address[1]}")
            self.assertIsNone(b.update_player())
            b.close()
        finally:
            srv.shutdown()

    def test_capture_frame_encodes_query(self):
        seen = {}
        class H(BaseHTTPRequestHandler):
            def log_message(self, *a): pass
            def do_GET(self):
                seen["path"] = self.path
                payload = json.dumps({"draws": [], "timestamp": 1, "drawCount": 0}).encode()
                self.send_response(200); self.send_header("Content-Length", str(len(payload))); self.end_headers()
                self.wfile.write(payload)
        srv = _serve(H)
        try:
            b = RS3Buddy(base_url=f"http://127.0.0.1:{srv.server_address[1]}")
            b.capture_frame(include_mesh=True, target_fbo=0)
            self.assertIn("mesh=true", seen["path"])
            self.assertIn("targetFbo=0", seen["path"])
            b.close()
        finally:
            srv.shutdown()

    def test_get_entity_at_query(self):
        seen = {}
        class H(BaseHTTPRequestHandler):
            def log_message(self, *a): pass
            def do_GET(self):
                seen["path"] = self.path
                self.send_response(200); self.send_header("Content-Length", "2"); self.end_headers()
                self.wfile.write(b"{}")
        srv = _serve(H)
        try:
            b = RS3Buddy(base_url=f"http://127.0.0.1:{srv.server_address[1]}")
            b.get_entity_at(10, 20, 1)
            self.assertIn("x=10", seen["path"]); self.assertIn("z=20", seen["path"]); self.assertIn("floor=1", seen["path"])
            b.close()
        finally:
            srv.shutdown()


if __name__ == "__main__":
    unittest.main()
