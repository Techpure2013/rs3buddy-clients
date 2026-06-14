package com.rs3buddy;

/** Thrown when the server returns a non-2xx HTTP status. */
public class RS3BuddyException extends RuntimeException {
    private final int status;
    private final String body;

    public RS3BuddyException(int status, String message, String body) {
        super(message);
        this.status = status;
        this.body = body;
    }

    /** The HTTP status code returned by the server. */
    public int status() {
        return status;
    }

    /** The raw response body (may be JSON), or null. */
    public String body() {
        return body;
    }
}
