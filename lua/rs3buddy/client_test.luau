--!strict
local rs3buddy = require("./client")

local passed = 0
local function ok(cond: boolean, msg: string)
	assert(cond, "FAIL: " .. msg)
	passed += 1
end

-- Build a client whose transport uses a fake request fn we control.
local function client_with(fake): any
	return rs3buddy.new({ base_url = "http://x:1", request = fake })
end

-- 1. get_player returns the Position the server sends.
do
	local b = client_with(function(_m, url, _b, _h)
		ok(url == "http://x:1/api/player", "get_player hits /api/player")
		return 200, '{"tileX":3200,"tileZ":3200,"worldX":1638400,"worldZ":1638400}'
	end)
	local p = b:get_player()
	ok(p.tileX == 3200, "tileX parsed")
end

-- 2. update_player returns nil when server sends null.
do
	local b = client_with(function(_m, _u, _b, _h)
		return 200, "null"
	end)
	ok(b:update_player() == nil, "null -> nil")
end

-- 3. capture_frame encodes query options.
do
	local seen = { url = "" }
	local b = client_with(function(_m, url, _b, _h)
		seen.url = url
		return 200, '{"draws":[],"timestamp":1,"drawCount":0}'
	end)
	b:capture_frame({ include_mesh = true, target_fbo = 0 })
	ok(string.find(seen.url, "mesh=true", 1, true) ~= nil, "encodes mesh=true")
	ok(string.find(seen.url, "targetFbo=0", 1, true) ~= nil, "encodes targetFbo=0")
end

-- 4. get_entity_at encodes x,z,floor.
do
	local seen = { url = "" }
	local b = client_with(function(_m, url, _b, _h)
		seen.url = url
		return 200, "{}"
	end)
	b:get_entity_at(10, 20, 1)
	ok(string.find(seen.url, "x=10", 1, true) ~= nil, "x=10")
	ok(string.find(seen.url, "z=20", 1, true) ~= nil, "z=20")
	ok(string.find(seen.url, "floor=1", 1, true) ~= nil, "floor=1")
end

print(("client_test: %d assertions passed"):format(passed))
