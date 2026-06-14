package com.rs3buddy;

import com.rs3buddy.models.Position;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class RS3BuddyTest {

    /** Mock server capturing the request path; replies with the given body. */
    private static HttpServer serve(String body, AtomicReference<String> seenPath) throws Exception {
        HttpServer srv = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        srv.createContext("/", exchange -> {
            if (seenPath != null) {
                String pathPlusQuery = exchange.getRequestURI().toString();
                seenPath.set(pathPlusQuery);
            }
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });
        srv.start();
        return srv;
    }

    private static String base(HttpServer srv) {
        return "http://127.0.0.1:" + srv.getAddress().getPort();
    }

    @Test
    void getPlayerReturnsPosition() throws Exception {
        AtomicReference<String> path = new AtomicReference<>();
        HttpServer srv = serve("{\"tileX\":3200,\"tileZ\":3200,\"worldX\":1638400,\"worldZ\":1638400}", path);
        try {
            RS3Buddy b = new RS3Buddy(base(srv));
            Position p = b.getPlayer();
            assertNotNull(p);
            assertEquals(3200.0, p.getTileX());
            assertEquals("/api/player", path.get());
        } finally {
            srv.stop(0);
        }
    }

    @Test
    void updatePlayerReturnsNullOnNull() throws Exception {
        HttpServer srv = serve("null", null);
        try {
            RS3Buddy b = new RS3Buddy(base(srv));
            assertNull(b.updatePlayer());
        } finally {
            srv.stop(0);
        }
    }

    @Test
    void captureFrameEncodesQuery() throws Exception {
        AtomicReference<String> path = new AtomicReference<>();
        HttpServer srv = serve("{\"draws\":[],\"timestamp\":1,\"drawCount\":0}", path);
        try {
            RS3Buddy b = new RS3Buddy(base(srv));
            b.captureFrame(true, false, null, null, 0);
            assertTrue(path.get().contains("mesh=true"), path.get());
            assertTrue(path.get().contains("targetFbo=0"), path.get());
        } finally {
            srv.stop(0);
        }
    }

    @Test
    void getEntityAtEncodesQuery() throws Exception {
        AtomicReference<String> path = new AtomicReference<>();
        HttpServer srv = serve("{}", path);
        try {
            RS3Buddy b = new RS3Buddy(base(srv));
            b.getEntityAt(10, 20, 1);
            assertTrue(path.get().contains("x=10"), path.get());
            assertTrue(path.get().contains("z=20"), path.get());
            assertTrue(path.get().contains("floor=1"), path.get());
        } finally {
            srv.stop(0);
        }
    }
}
