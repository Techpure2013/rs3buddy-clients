export interface TransportOptions {
    /** Explicit base URL. Overrides rs3buddy.json. */
    baseUrl?: string;
    /** Sent as X-Client-Name on every request when provided. */
    clientName?: string;
    /** Per-request timeout (ms). Default 5000. */
    timeoutMs?: number;
}
/** Resolve the server port: explicit baseUrl wins, else rs3buddy.json. */
export declare function resolveBaseUrl(opts?: TransportOptions): string;
export declare class Transport {
    private readonly base;
    private readonly agent;
    private readonly clientName;
    private readonly timeoutMs;
    constructor(opts?: TransportOptions);
    get baseUrl(): string;
    /** Send a request; parse JSON; map failures to typed errors. */
    request<T>(method: string, route: string, body?: unknown): Promise<T>;
}
//# sourceMappingURL=transport.d.ts.map