import json
import threading
import unittest
from http.server import BaseHTTPRequestHandler, HTTPServer

from rs3buddy.transport import Transport
from rs3buddy.errors import RS3BuddyError, RS3BuddyConnectionError


def _serve(handler_cls):
    srv = HTTPServer(("127.0.0.1", 0), handler_cls)
    t = threading.Thread(target=srv.serve_forever, daemon=True)
    t.start()
    return srv


class TransportTests(unittest.TestCase):
    def test_get_parses_json_and_reuses_connection(self):
        seen_client_names = []
        local_ports = []

        class H(BaseHTTPRequestHandler):
            protocol_version = "HTTP/1.1"  # honor keep-alive (1.0 closes each response)

            def log_message(self, *a):  # silence
                pass

            def do_GET(self):
                seen_client_names.append(self.headers.get("X-Client-Name"))
                local_ports.append(self.client_address[1])
                payload = json.dumps({"ok": True}).encode()
                self.send_response(200)
                self.send_header("Content-Type", "application/json")
                self.send_header("Content-Length", str(len(payload)))
                self.end_headers()
                self.wfile.write(payload)

        srv = _serve(H)
        try:
            port = srv.server_address[1]
            t = Transport(base_url=f"http://127.0.0.1:{port}", client_name="tester")
            a = t.request("GET", "/api/x")
            b = t.request("GET", "/api/x")
            self.assertTrue(a["ok"])
            self.assertEqual(seen_client_names[0], "tester")   # header sent
            self.assertEqual(local_ports[0], local_ports[1])    # same client TCP port = keep-alive
            t.close()
        finally:
            srv.shutdown()

    def test_non_2xx_raises_rs3buddyerror(self):
        class H(BaseHTTPRequestHandler):
            def log_message(self, *a):
                pass

            def do_GET(self):
                payload = json.dumps({"error": "bad thing"}).encode()
                self.send_response(400)
                self.send_header("Content-Type", "application/json")
                self.send_header("Content-Length", str(len(payload)))
                self.end_headers()
                self.wfile.write(payload)

        srv = _serve(H)
        try:
            port = srv.server_address[1]
            t = Transport(base_url=f"http://127.0.0.1:{port}")
            with self.assertRaises(RS3BuddyError) as ctx:
                t.request("GET", "/api/x")
            self.assertEqual(ctx.exception.status, 400)
            self.assertEqual(str(ctx.exception), "bad thing")
            t.close()
        finally:
            srv.shutdown()

    def test_connection_refused_raises_connectionerror(self):
        t = Transport(base_url="http://127.0.0.1:1")
        with self.assertRaises(RS3BuddyConnectionError):
            t.request("GET", "/api/x")


if __name__ == "__main__":
    unittest.main()
