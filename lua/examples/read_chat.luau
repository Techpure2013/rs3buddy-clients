--!strict
-- Example: read the RS3 chatbox from Luau.
--
-- Prerequisite: the rs3buddy launcher must be running — it injects the native
-- hook into the RS3 client, which starts the SDK server and writes the config
-- that connect() auto-discovers (%APPDATA%\rs3buddy\rs3buddy.json). With nothing
-- running, the read below raises an RS3BuddyConnectionError.
--
-- connect() with no base_url only works on hosts that expose io/os (standard
-- Luau CLI, lune, etc.). On SANDBOXED hosts (Roblox) there is no io/os, so
-- auto-discovery returns nil — pass an explicit base_url AND an HTTP request fn:
--   rs3buddy.connect({ base_url = "http://127.0.0.1:1234", request = my_http_fn })
--
-- Run (from clients/lua, with a Luau runtime that resolves dir modules):
--   lune run examples/read_chat.luau      -- or your host's equivalent

local rs3buddy = require("../rs3buddy")

-- No URL needed: connect() auto-discovers the server from the launcher's config.
local buddy = rs3buddy.connect()

-- A cold first read does a frame capture server-side, so it can take a moment.
local ok, chat = pcall(function()
	return buddy.chat:read()
end)

if not ok then
	-- `chat` holds the raised error: a tagged table (see transport.luau) or string.
	local err = chat :: any
	print("Could not read the chatbox.")
	print("Is the launcher running (native injected into RS3)?")
	if type(err) == "table" and err.message ~= nil then
		print("  " .. tostring(err.message))
	else
		print("  " .. tostring(err))
	end
	return
end

local result = chat :: any
print(tostring(result.lineCount) .. " chat lines:")
print("")
for _, line in ipairs(result.lines or {}) do
	-- line.color is the line's dominant colour {r, g, b}: clan green, public
	-- white, ... line.text is the recognised message text.
	local c = line.color or { 0, 0, 0 }
	local colour = string.format("(%s, %s, %s)", tostring(c[1]), tostring(c[2]), tostring(c[3]))
	print("  " .. colour .. "  " .. tostring(line.text))
end
