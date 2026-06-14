--!strict
-- HTTP transport with a PLUGGABLE request function so the client runs in any Lua
-- host (Luau/Roblox HttpService, standalone luasocket, LuaJIT, a custom bridge, ...).

local json = require("./json")

-- The injectable HTTP primitive: (method, url, body?, headers) -> (status, body_text)
export type RequestFn = (method: string, url: string, body: string?, headers: { [string]: string }) -> (number, string)

export type TransportOptions = {
	base_url: string?,
	request: RequestFn?,
	client_name: string?,
}

-- Lua has no typed exceptions; we raise tagged tables via error().
export type RS3BuddyError = { kind: "RS3BuddyError", status: number, message: string, body: any }
export type RS3BuddyConnectionError = { kind: "RS3BuddyConnectionError", message: string }

local Transport = {}
Transport.__index = Transport

export type Transport = typeof(setmetatable(
	{} :: {
		base_url: string,
		_request: RequestFn,
		_client_name: string?,
	},
	Transport
))

local function rs3_error(status: number, message: string, body: any): never
	error({ kind = "RS3BuddyError", status = status, message = message, body = body } :: RS3BuddyError)
end
local function conn_error(message: string): never
	error({ kind = "RS3BuddyConnectionError", message = message } :: RS3BuddyConnectionError)
end

-- Default transport: try luasocket if present. NEVER a hard dependency — sandboxed
-- hosts (Roblox, etc.) inject their own request fn instead.
local function default_request(): RequestFn?
	local ok, http = pcall(require, "socket.http" :: any)
	local ok2, ltn12 = pcall(require, "ltn12" :: any)
	if not ok or not ok2 then
		return nil
	end
	return function(method, url, body, headers)
		local chunks = {}
		local h: { [string]: string } = {}
		for key, value in pairs(headers) do
			h[key] = value
		end
		if body then
			h["Content-Length"] = tostring(#body)
		end
		local _, status = (http :: any).request({
			method = method,
			url = url,
			source = if body then (ltn12 :: any).source.string(body) else nil,
			headers = h,
			sink = (ltn12 :: any).sink.table(chunks),
		})
		return tonumber(status) or 0, table.concat(chunks)
	end
end

-- Best-effort server auto-discovery: read the port the launcher wrote to
-- %APPDATA%\rs3buddy\rs3buddy.json (USERPROFILE fallback) and turn it into a
-- base URL. Mirrors the Python client's _read_config + resolve_base_url.
-- Returns nil on ANY failure — the whole body is wrapped in pcall and os/io are
-- cast to `any` so sandboxed hosts (Roblox) that lack the os/io stdlibs
-- just get nil here instead of erroring (they inject base_url/request instead).
local function resolve_config_url(): string?
	local ok, url = pcall(function(): string?
		local osLib = os :: any
		local ioLib = io :: any
		if not osLib or not ioLib then
			return nil
		end
		local getenv = osLib.getenv
		local open = ioLib.open
		if type(getenv) ~= "function" or type(open) ~= "function" then
			return nil
		end
		local root = getenv("APPDATA") or getenv("USERPROFILE")
		if not root or root == "" then
			return nil
		end
		local path = root .. "\\rs3buddy\\rs3buddy.json"
		local fh = open(path, "r")
		if not fh then
			return nil
		end
		local text = fh:read("*a")
		fh:close()
		if not text or text == "" then
			return nil
		end
		local cfg = json.decode(text)
		if type(cfg) ~= "table" then
			return nil
		end
		local port = cfg.port
		if type(port) ~= "number" then
			return nil
		end
		return "http://127.0.0.1:" .. tostring(math.floor(port))
	end)
	if ok then
		return url
	end
	return nil
end

local function new(opts: TransportOptions): Transport
	local base = opts.base_url
	if not base or base == "" then
		-- No explicit base_url: try to auto-discover the launcher's server before
		-- giving up. Host-specific sandboxes get nil and fall through to the error.
		base = resolve_config_url()
	end
	if not base or base == "" then
		conn_error("Transport.new: base_url required and rs3buddy.json not found "
			.. "(pass base_url, or run the launcher so it writes %APPDATA%\\rs3buddy\\rs3buddy.json).")
	end
	local req = opts.request or default_request()
	if not req then
		conn_error("Transport.new: no request function provided and luasocket not available. Pass opts.request.")
	end
	return setmetatable({
		base_url = (base :: string):gsub("/+$", ""),
		_request = req :: RequestFn,
		_client_name = opts.client_name,
	}, Transport)
end

function Transport.request(self: Transport, method: string, route: string, body: any?): any
	local payload: string? = if body ~= nil then json.encode(body) else nil
	local headers: { [string]: string } = { Accept = "application/json" }
	if payload ~= nil then
		headers["Content-Type"] = "application/json"
	end
	if self._client_name then
		headers["X-Client-Name"] = self._client_name :: string
	end

	local ok, status, raw = pcall(self._request, method, self.base_url .. route, payload, headers)
	if not ok then
		conn_error("request to " .. route .. " failed: " .. tostring(status))
	end

	local parsed: any = nil
	if raw ~= nil and #(raw :: string) > 0 then
		local pok, value = pcall(json.decode, raw :: string)
		parsed = if pok then value else raw
	end

	local st = status :: number
	if st < 200 or st >= 300 then
		local msg: string
		if type(parsed) == "table" and type(parsed.error) == "string" then
			msg = parsed.error
		else
			msg = "HTTP " .. tostring(st)
		end
		rs3_error(st, msg, parsed)
	end
	return parsed
end

return {
	new = new,
	Transport = Transport,
	resolve_config_url = resolve_config_url,
}
