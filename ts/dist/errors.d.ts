/** Server returned a non-2xx HTTP status. */
export declare class RS3BuddyError extends Error {
    readonly status: number;
    readonly body?: unknown | undefined;
    constructor(status: number, message: string, body?: unknown | undefined);
}
/** Could not reach the server (down, wrong port, or game crashed). */
export declare class RS3BuddyConnectionError extends Error {
    readonly cause?: unknown | undefined;
    constructor(message: string, cause?: unknown | undefined);
}
//# sourceMappingURL=errors.d.ts.map