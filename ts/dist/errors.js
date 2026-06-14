"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.RS3BuddyConnectionError = exports.RS3BuddyError = void 0;
/** Server returned a non-2xx HTTP status. */
class RS3BuddyError extends Error {
    status;
    body;
    constructor(status, message, body) {
        super(message);
        this.status = status;
        this.body = body;
        this.name = "RS3BuddyError";
    }
}
exports.RS3BuddyError = RS3BuddyError;
/** Could not reach the server (down, wrong port, or game crashed). */
class RS3BuddyConnectionError extends Error {
    cause;
    constructor(message, cause) {
        super(message);
        this.cause = cause;
        this.name = "RS3BuddyConnectionError";
    }
}
exports.RS3BuddyConnectionError = RS3BuddyConnectionError;
