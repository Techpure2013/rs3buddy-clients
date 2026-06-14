--!strict
local transport = require("./transport")

local passed = 0
local function ok(cond: boolean, msg: string)
	assert(cond, "FAIL: " .. msg)
	passed += 1
end

-- 1. GET parses JSON, sends headers, hits base_url+route.
do
	local seen = { method = "", url = "", client = nil :: string? }
	local fake = function(method: string, url: string, _body: string?, headers: { [string]: string }): (number, string)
		seen.method = method
		seen.url = url
		seen.client = headers["X-Client-Name"]
		return 200, '{"ok":true}'
	end
	local t = transport.new({ base_url = "http://x:1", request = fake, client_name = "tester" })
	local r = t:request("GET", "/api/player")
	ok(r.ok == true, "parses json body")
	ok(seen.url == "http://x:1/api/player", "builds url")
	ok(seen.client == "tester", "sends X-Client-Name")
end

-- 2. non-2xx raises RS3BuddyError with server message.
do
	local fake = function(_m: string, _u: string, _b: string?, _h: { [string]: string }): (number, string)
		return 400, '{"error":"bad thing"}'
	end
	local t = transport.new({ base_url = "http://x:1", request = fake })
	local okp, err = pcall(function()
		t:request("GET", "/api/x")
	end)
	ok(not okp, "raises on 400")
	ok(type(err) == "table" and err.kind == "RS3BuddyError", "tagged RS3BuddyError")
	ok(err.status == 400 and err.message == "bad thing", "carries status+message")
end

-- 3. transport-layer failure raises RS3BuddyConnectionError.
do
	local fake = function(_m: string, _u: string, _b: string?, _h: { [string]: string }): (number, string)
		error("socket boom")
	end
	local t = transport.new({ base_url = "http://x:1", request = fake })
	local okp, err = pcall(function()
		t:request("GET", "/api/x")
	end)
	ok(not okp, "raises on transport error")
	ok(type(err) == "table" and err.kind == "RS3BuddyConnectionError", "tagged ConnectionError")
end

print(("transport_test: %d assertions passed"):format(passed))
