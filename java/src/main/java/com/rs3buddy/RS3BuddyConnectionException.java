package com.rs3buddy;

/** Thrown when the server cannot be reached (down, wrong port, or game crashed). */
public class RS3BuddyConnectionException extends RuntimeException {
    public RS3BuddyConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
