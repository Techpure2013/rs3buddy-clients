package com.rs3buddy;

import com.fasterxml.jackson.databind.JsonNode;
import com.rs3buddy.models.Position;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TransportTest {

    private static HttpServer serve(String body, int status, AtomicReference<String> seenClient) throws Exception {
        HttpServer srv = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        srv.createContext("/", exchange -> {
            if (seenClient != null) {
                seenClient.set(exchange.getRequestHeaders().getFirst("X-Client-Name"));
            }
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });
        srv.start();
        return srv;
    }

    @Test
    void getDeserializesAndSendsClientName() throws Exception {
        AtomicReference<String> seen = new AtomicReference<>();
        HttpServer srv = serve("{\"tileX\":3200,\"tileZ\":3200,\"worldX\":1638400,\"worldZ\":1638400}", 200, seen);
        try {
            int port = srv.getAddress().getPort();
            Transport t = new Transport("http://127.0.0.1:" + port, "tester");
            Position p = t.request("GET", "/api/player", null, Position.class);
            assertNotNull(p);
            assertEquals(3200.0, p.getTileX());
            assertEquals("tester", seen.get());
        } finally {
            srv.stop(0);
        }
    }

    @Test
    void non2xxThrowsRS3BuddyException() throws Exception {
        HttpServer srv = serve("{\"error\":\"bad thing\"}", 400, null);
        try {
            int port = srv.getAddress().getPort();
            Transport t = new Transport("http://127.0.0.1:" + port);
            RS3BuddyException ex = assertThrows(RS3BuddyException.class,
                    () -> t.requestJson("GET", "/api/x", null));
            assertEquals(400, ex.status());
            assertEquals("bad thing", ex.getMessage());
        } finally {
            srv.stop(0);
        }
    }

    @Test
    void nullBodyReturnsNull() throws Exception {
        HttpServer srv = serve("null", 200, null);
        try {
            int port = srv.getAddress().getPort();
            Transport t = new Transport("http://127.0.0.1:" + port);
            JsonNode n = t.requestJson("GET", "/api/player", null);
            assertTrue(n == null || n.isNull());
        } finally {
            srv.stop(0);
        }
    }

    @Test
    void connectionRefusedThrowsConnectionException() {
        Transport t = new Transport("http://127.0.0.1:1");
        assertThrows(RS3BuddyConnectionException.class,
                () -> t.requestJson("GET", "/api/x", null));
    }
}
