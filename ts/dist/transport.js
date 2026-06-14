"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.Transport = void 0;
exports.resolveBaseUrl = resolveBaseUrl;
const http = __importStar(require("node:http"));
const fs = __importStar(require("node:fs"));
const path = __importStar(require("node:path"));
const errors_1 = require("./errors");
/** True when `v` is a JSON object carrying a string `error` field. */
function hasErrorString(v) {
    return (typeof v === "object" &&
        v !== null &&
        "error" in v &&
        typeof v.error === "string");
}
/** Best human-readable message for a non-2xx response body. */
function errorMessage(body, status) {
    return hasErrorString(body) ? body.error : `HTTP ${status}`;
}
/** Resolve the server port: explicit baseUrl wins, else rs3buddy.json. */
function resolveBaseUrl(opts = {}) {
    if (opts.baseUrl)
        return opts.baseUrl.replace(/\/+$/, "");
    const cfg = readConfig();
    if (cfg && typeof cfg.port === "number") {
        return `http://127.0.0.1:${cfg.port}`;
    }
    throw new errors_1.RS3BuddyConnectionError("No baseUrl given and rs3buddy.json not found (set RS3BUDDY_CONFIG, run from the launcher dir, or pass { baseUrl }).");
}
function readConfig() {
    const candidates = [];
    if (process.env.RS3BUDDY_CONFIG)
        candidates.push(process.env.RS3BUDDY_CONFIG);
    // Well-known per-user path the launcher writes on serve() (USERPROFILE fallback).
    const appData = process.env.APPDATA || process.env.USERPROFILE;
    if (appData)
        candidates.push(path.join(appData, "rs3buddy", "rs3buddy.json"));
    candidates.push(path.join(process.cwd(), "rs3buddy.json"));
    for (const p of candidates) {
        try {
            const parsed = JSON.parse(fs.readFileSync(p, "utf8"));
            if (parsed && typeof parsed === "object" && "port" in parsed) {
                const port = parsed.port;
                if (typeof port === "number")
                    return { port };
            }
            return {};
        }
        catch {
            /* try next */
        }
    }
    return null;
}
class Transport {
    base;
    agent;
    clientName;
    timeoutMs;
    constructor(opts = {}) {
        this.base = resolveBaseUrl(opts);
        this.agent = new http.Agent({ keepAlive: true, maxSockets: 8 });
        this.clientName = opts.clientName;
        this.timeoutMs = opts.timeoutMs ?? 5000;
    }
    get baseUrl() {
        return this.base;
    }
    /** Send a request; parse JSON; map failures to typed errors. */
    request(method, route, body) {
        const url = new URL(this.base + route);
        const payload = body === undefined ? undefined : JSON.stringify(body);
        const headers = { Accept: "application/json" };
        if (payload !== undefined) {
            headers["Content-Type"] = "application/json";
            headers["Content-Length"] = String(Buffer.byteLength(payload));
        }
        if (this.clientName)
            headers["X-Client-Name"] = this.clientName;
        return new Promise((resolve, reject) => {
            const req = http.request({
                method,
                hostname: url.hostname,
                port: url.port,
                path: url.pathname + url.search,
                headers,
                agent: this.agent,
                timeout: this.timeoutMs,
            }, (res) => {
                const chunks = [];
                res.on("data", (c) => chunks.push(c));
                res.on("end", () => {
                    const text = Buffer.concat(chunks).toString("utf8");
                    const status = res.statusCode ?? 0;
                    let parsed = undefined;
                    if (text.length > 0) {
                        try {
                            parsed = JSON.parse(text);
                        }
                        catch {
                            parsed = text;
                        }
                    }
                    if (status < 200 || status >= 300) {
                        reject(new errors_1.RS3BuddyError(status, errorMessage(parsed, status), parsed));
                    }
                    else {
                        resolve(parsed);
                    }
                });
            });
            req.on("timeout", () => req.destroy(new Error("request timeout")));
            req.on("error", (e) => reject(new errors_1.RS3BuddyConnectionError(`request to ${route} failed: ${e.message}`, e)));
            if (payload !== undefined)
                req.write(payload);
            req.end();
        });
    }
}
exports.Transport = Transport;
