package com.rs3buddy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * HTTP transport: one shared {@link HttpClient} (keep-alive connection reuse) +
 * JSON (de)serialization via a shared Jackson {@link ObjectMapper}.
 */
public final class Transport {
    private final String baseUrl;
    private final String clientName;
    private final HttpClient http;
    private final ObjectMapper mapper = new ObjectMapper();

    public Transport(String baseUrl) {
        this(baseUrl, null);
    }

    public Transport(String baseUrl, String clientName) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new RS3BuddyConnectionException("Transport: baseUrl required", null);
        }
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.clientName = clientName;
        // One client, reused across calls -> HTTP/2 keep-alive, no socket per call.
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String baseUrl() {
        return baseUrl;
    }

    /** Send a request and deserialize the JSON response into {@code type}. */
    public <T> T request(String method, String route, Object body, Class<T> type) {
        JsonNode node = requestJson(method, route, body);
        if (node == null || node.isNull()) {
            return null;
        }
        try {
            return mapper.treeToValue(node, type);
        } catch (IOException e) {
            throw new RS3BuddyConnectionException("failed to parse response for " + route, e);
        }
    }

    /** Send a request and return the raw parsed JSON tree (null when body is empty/null). */
    public JsonNode requestJson(String method, String route, Object body) {
        String payload = null;
        if (body != null) {
            try {
                payload = mapper.writeValueAsString(body);
            } catch (IOException e) {
                throw new RS3BuddyConnectionException("failed to encode request body for " + route, e);
            }
        }

        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + route))
                .timeout(Duration.ofSeconds(5))
                .header("Accept", "application/json");
        if (payload != null) {
            b.header("Content-Type", "application/json");
        }
        if (clientName != null) {
            b.header("X-Client-Name", clientName);
        }
        b.method(method, payload != null
                ? HttpRequest.BodyPublishers.ofString(payload)
                : HttpRequest.BodyPublishers.noBody());

        HttpResponse<String> resp;
        try {
            resp = http.send(b.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RS3BuddyConnectionException("request to " + route + " failed: " + e.getMessage(), e);
        }

        String text = resp.body();
        JsonNode parsed = null;
        if (text != null && !text.isEmpty()) {
            try {
                parsed = mapper.readTree(text);
            } catch (IOException e) {
                parsed = null; // non-JSON body; leave null
            }
        }

        int status = resp.statusCode();
        if (status < 200 || status >= 300) {
            String msg = "HTTP " + status;
            if (parsed != null && parsed.has("error") && parsed.get("error").isTextual()) {
                msg = parsed.get("error").asText();
            }
            throw new RS3BuddyException(status, msg, text);
        }
        return parsed;
    }
}
