import * as http from "node:http";
import * as fs from "node:fs";
import * as path from "node:path";
import { RS3BuddyError, RS3BuddyConnectionError } from "./errors";

export interface TransportOptions {
  /** Explicit base URL. Overrides rs3buddy.json. */
  baseUrl?: string;
  /** Sent as X-Client-Name on every request when provided. */
  clientName?: string;
  /** Per-request timeout (ms). Default 5000. */
  timeoutMs?: number;
}

/** True when `v` is a JSON object carrying a string `error` field. */
function hasErrorString(v: unknown): v is { error: string } {
  return (
    typeof v === "object" &&
    v !== null &&
    "error" in v &&
    typeof (v as Record<string, unknown>).error === "string"
  );
}

/** Best human-readable message for a non-2xx response body. */
function errorMessage(body: unknown, status: number): string {
  return hasErrorString(body) ? body.error : `HTTP ${status}`;
}

/** Resolve the server port: explicit baseUrl wins, else rs3buddy.json. */
export function resolveBaseUrl(opts: TransportOptions = {}): string {
  if (opts.baseUrl) return opts.baseUrl.replace(/\/+$/, "");
  const cfg = readConfig();
  if (cfg && typeof cfg.port === "number") {
    return `http://127.0.0.1:${cfg.port}`;
  }
  throw new RS3BuddyConnectionError(
    "No baseUrl given and rs3buddy.json not found (set RS3BUDDY_CONFIG, run from the launcher dir, or pass { baseUrl }).",
  );
}

function readConfig(): { port?: number } | null {
  const candidates: string[] = [];
  if (process.env.RS3BUDDY_CONFIG) candidates.push(process.env.RS3BUDDY_CONFIG);
  // Well-known per-user path the launcher writes on serve() (USERPROFILE fallback).
  const appData = process.env.APPDATA || process.env.USERPROFILE;
  if (appData) candidates.push(path.join(appData, "rs3buddy", "rs3buddy.json"));
  candidates.push(path.join(process.cwd(), "rs3buddy.json"));

  for (const p of candidates) {
    try {
      const parsed: unknown = JSON.parse(fs.readFileSync(p, "utf8"));
      if (parsed && typeof parsed === "object" && "port" in parsed) {
        const port = (parsed as Record<string, unknown>).port;
        if (typeof port === "number") return { port };
      }
      return {};
    } catch {
      /* try next */
    }
  }
  return null;
}

export class Transport {
  private readonly base: string;
  private readonly agent: http.Agent;
  private readonly clientName: string | undefined;
  private readonly timeoutMs: number;

  constructor(opts: TransportOptions = {}) {
    this.base = resolveBaseUrl(opts);
    this.agent = new http.Agent({ keepAlive: true, maxSockets: 8 });
    this.clientName = opts.clientName;
    this.timeoutMs = opts.timeoutMs ?? 5000;
  }

  get baseUrl(): string {
    return this.base;
  }

  /** Send a request; parse JSON; map failures to typed errors. */
  request<T>(method: string, route: string, body?: unknown): Promise<T> {
    const url = new URL(this.base + route);
    const payload = body === undefined ? undefined : JSON.stringify(body);
    const headers: Record<string, string> = { Accept: "application/json" };
    if (payload !== undefined) {
      headers["Content-Type"] = "application/json";
      headers["Content-Length"] = String(Buffer.byteLength(payload));
    }
    if (this.clientName) headers["X-Client-Name"] = this.clientName;

    return new Promise<T>((resolve, reject) => {
      const req = http.request(
        {
          method,
          hostname: url.hostname,
          port: url.port,
          path: url.pathname + url.search,
          headers,
          agent: this.agent,
          timeout: this.timeoutMs,
        },
        (res) => {
          const chunks: Buffer[] = [];
          res.on("data", (c) => chunks.push(c));
          res.on("end", () => {
            const text = Buffer.concat(chunks).toString("utf8");
            const status = res.statusCode ?? 0;
            let parsed: unknown = undefined;
            if (text.length > 0) {
              try {
                parsed = JSON.parse(text);
              } catch {
                parsed = text;
              }
            }
            if (status < 200 || status >= 300) {
              reject(new RS3BuddyError(status, errorMessage(parsed, status), parsed));
            } else {
              resolve(parsed as T);
            }
          });
        },
      );
      req.on("timeout", () => req.destroy(new Error("request timeout")));
      req.on("error", (e) =>
        reject(new RS3BuddyConnectionError(`request to ${route} failed: ${e.message}`, e)),
      );
      if (payload !== undefined) req.write(payload);
      req.end();
    });
  }
}
