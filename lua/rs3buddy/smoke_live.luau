--!nocheck
-- Manual: luau clients/lua/rs3buddy/smoke_live.luau   (or run under standalone Lua
-- with luasocket installed). Requires a running rs3buddy server.
-- Set the base URL via the RS3BUDDY_BASEURL env var, else defaults to :4400.
--
-- Demonstrates the two ways to give the client a transport:
--   1. auto-detected luasocket (standalone Lua) — pass only base_url
--   2. inject your own request fn (Roblox HttpService, a custom bridge, ...)
local rs3buddy = require("./client")

local base = os.getenv("RS3BUDDY_BASEURL") or "http://127.0.0.1:4400"

-- (1) standalone Lua: luasocket auto-detected, no request fn needed.
local buddy = rs3buddy.new({ base_url = base })

print("base_url:", buddy:base_url())
local status = buddy:get_status()
print("status.connected:", status and status.connected)

local p = buddy:get_player()
for _ = 1, 8 do
	if p then
		break
	end
	p = buddy:update_player()
end
print("player:", p and ("tile(" .. tostring(p.tileX) .. "," .. tostring(p.tileZ) .. ")") or "nil")
