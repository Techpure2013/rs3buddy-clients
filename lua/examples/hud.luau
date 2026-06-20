--!strict
-- Example: a "Tracker" HUD authored with HTML + CSS over the Luau client.
--
-- Luau owns the state and re-renders; buttons report clicks via buddy.ui:events().
-- Mirrors the Java Hud example (../../java/examples/Hud.java).
--
-- STALL-FREE: get_player() hits GET /api/player, which does the CHEAP passive read
-- (no GPU, no frame capture) — safe to poll every loop. (Only ?detect=1 forces the
-- heavy capture-based detection; this example never asks for that.)
--
-- Prerequisite: the rs3buddy launcher must be running — it injects the native hook
-- into the RS3 client, which starts the SDK server and writes the config that
-- connect() auto-discovers (%APPDATA%\rs3buddy\rs3buddy.json). With nothing running,
-- connect()/the reads below raise an RS3BuddyConnectionError.
--
-- connect() with no base_url only works on hosts that expose io/os (standard Luau
-- CLI, lune, etc.). On SANDBOXED hosts (Roblox) there is no io/os, so pass an
-- explicit base_url AND an HTTP request fn (see read_chat.luau).
--
-- Run (from clients/lua, with a Luau runtime that resolves dir modules):
--   lune run examples/hud.luau            -- or your host's equivalent

local rs3buddy = require("../rs3buddy")

-- No URL needed: connect() auto-discovers the server from the launcher's config.
local buddy = rs3buddy.connect({ client_name = "hud-example" })
print("connected: " .. buddy:base_url())

-- The CSS is a plain string — author the HUD like a webpage. Selectors map to the
-- same widget engine the SDK renders (panel/header/button/div/span/hr).
local CSS = table.concat({
	"panel{background:#241d12;border:1px solid #b0904a;border-radius:10px;padding:12px;gap:8px;width:220px;align-items:stretch}",
	"header{display:flex;justify-content:space-between;align-items:center}",
	".title{color:#f5c54a;font-size:16px}",
	".ctrl{display:flex;gap:6px}",
	".row{display:flex;justify-content:space-between;gap:12px}",
	".k{color:#c9bfa6}.v{color:#ffffff}.accent{color:#8fd1ff}",
	".btns{display:flex;gap:8px}.btns button{flex:1}",
	".good{background:#3f7a3f}.danger{background:#b03b30}.info{background:#2f5a7a}",
}, "\n")

-- HUD state (Luau owns it; the server is stateless between renders).
local running = false
local minimized = false
local located = false
local ticks = 0
local tileX, tileZ = 0, 0
local startX: number?, startZ: number? = nil, nil
local lastHtml = ""

-- Sleep helper. Prefer the host's real sleep (lune `task.wait`, LuaSocket
-- `socket.sleep`); fall back to an os.clock busy-wait so the example still runs on
-- a bare Luau CLI. Keep the loop period ~0.3 s, matching the Java example's
-- Thread.sleep(300).
local function sleep(seconds: number)
	local taskLib = (_G :: any).task
	if taskLib and type(taskLib.wait) == "function" then
		taskLib.wait(seconds)
		return
	end
	local ok, socket = pcall(require, "socket" :: any)
	if ok and type((socket :: any).sleep) == "function" then
		(socket :: any).sleep(seconds)
		return
	end
	-- Last resort: busy-wait on os.clock (CPU-spins; fine for a demo).
	local deadline = os.clock() + seconds
	while os.clock() < deadline do end
end

-- Build the HUD markup for the current state. Buttons carry an `id` so clicks come
-- back through ui:events() tagged with it.
local function build_html(): string
	if minimized then
		return "<panel anchor='top-right' draggable consume>"
			.. "<header><span class='title'>Tracker</span><span class='ctrl'>"
			.. "<button id='min' icon='maximize' variant='plain'></button>"
			.. "<button id='close' icon='close' variant='plain'></button></span></header>"
			.. "</panel>"
	end

	local dist = if startX ~= nil and startZ ~= nil
		then math.abs(tileX - startX) + math.abs(tileZ - startZ)
		else 0
	local totalSecs = math.floor(ticks * 0.6)
	local secs = totalSecs % 60
	local mins = math.floor(totalSecs / 60)
	local tile = if located then (tostring(tileX) .. ", " .. tostring(tileZ)) else "—"
	local secsStr = if secs < 10 then ("0" .. tostring(secs)) else tostring(secs)
	local startClass = if running then "" else "good"
	local startLabel = if running then "Pause" else "Start"

	return "<panel anchor='top-right' draggable consume>"
		.. "<header><span class='title'>Tracker</span><span class='ctrl'>"
		.. "<button id='min' icon='minimize' variant='plain'></button>"
		.. "<button id='close' icon='close' variant='plain'></button></span></header>"
		.. "<hr/>"
		.. "<div class='row'><span class='k'>Tile</span><span class='accent'>" .. tile .. "</span></div>"
		.. "<div class='row'><span class='k'>Distance</span><span class='v'>" .. tostring(dist) .. " tiles</span></div>"
		.. "<div class='row'><span class='k'>Time</span><span class='v'>" .. tostring(mins) .. ":" .. secsStr .. "</span></div>"
		.. "<div class='row'><span class='k'>Ticks</span><span class='v'>" .. tostring(ticks) .. "</span></div>"
		.. "<hr/>"
		.. "<div class='btns'>"
		.. "<button id='start' class='" .. startClass .. "'>" .. startLabel .. "</button>"
		.. "<button id='reset' class='danger'>Reset</button>"
		.. "</div>"
		.. "<div class='btns'>"
		.. "<button id='min' class='info'>Minimize</button>"
		.. "<button id='close' class='danger'>Exit</button>"
		.. "</div>"
		.. "</panel>"
end

while true do
	-- cheap passive read (GET /api/player) — safe to poll. Not-tracked-yet raises,
	-- so swallow it and keep the last known tile.
	local ok, pos = pcall(function()
		return buddy:get_player()
	end)
	if ok and type(pos) == "table" and pos.tileX ~= nil and pos.tileZ ~= nil then
		tileX = pos.tileX
		tileZ = pos.tileZ
		located = true
	end

	-- Drain queued click events; each carries the clicked widget's `id`.
	local resp = buddy.ui:events()
	local events = if type(resp) == "table" then resp.events else nil
	if type(events) == "table" then
		for _, e in ipairs(events) do
			local id = if type(e) == "table" then e.id else nil
			if id == "close" then
				buddy.ui:clear()
				return
			elseif id == "min" then
				minimized = not minimized
			elseif id == "start" then
				running = not running
				if running then
					startX, startZ = tileX, tileZ
				end
			elseif id == "reset" then
				ticks = 0
				startX, startZ = tileX, tileZ
			end
		end
	end

	if running and not minimized then
		ticks += 1
	end

	-- re-POST only when the HUD actually changed (avoids needless work).
	local html = build_html()
	if html ~= lastHtml then
		buddy.ui:html(html, CSS)
		lastHtml = html
	end

	sleep(0.3)
end
