--!strict
-- Minimal JSON encode/decode, vendored so the client is dependency-free in any
-- Lua host (Luau / standard Lua / LuaJIT). Adapted from rxi/json.lua (MIT) to
-- compile under Luau --!strict.
--
-- Copyright (c) 2020 rxi — MIT License. https://github.com/rxi/json.lua

local json = {}

-- ─── encode ──────────────────────────────────────────────────────────────────

local encode: (any) -> string

local escape_char_map: { [string]: string } = {
	["\\"] = "\\\\",
	['"'] = '\\"',
	["\b"] = "\\b",
	["\f"] = "\\f",
	["\n"] = "\\n",
	["\r"] = "\\r",
	["\t"] = "\\t",
}

local function escape_char(c: string): string
	return escape_char_map[c] or string.format("\\u%04x", string.byte(c))
end

local function encode_nil(_: any): string
	return "null"
end

local function encode_table(val: any, stack: { [any]: boolean }?): string
	local res = {}
	local s = stack or {}

	if s[val] then
		error("circular reference")
	end
	s[val] = true

	if rawget(val, 1) ~= nil or next(val) == nil then
		-- treat as array
		local n = 0
		for k in pairs(val) do
			if type(k) ~= "number" then
				error("invalid table: mixed or invalid key types")
			end
			n += 1
		end
		if n ~= #val then
			error("invalid table: sparse array")
		end
		for _, v in ipairs(val) do
			table.insert(res, encode(v))
		end
		s[val] = nil
		return "[" .. table.concat(res, ",") .. "]"
	else
		-- treat as object
		for k, v in pairs(val) do
			if type(k) ~= "string" then
				error("invalid table: mixed or invalid key types")
			end
			table.insert(res, encode(k) .. ":" .. encode(v))
		end
		s[val] = nil
		return "{" .. table.concat(res, ",") .. "}"
	end
end

local function encode_string(val: string): string
	return '"' .. (val:gsub('[%z\1-\31\\"]', escape_char)) .. '"'
end

local function encode_number(val: number): string
	if val ~= val or val <= -math.huge or val >= math.huge then
		error("unexpected number value '" .. tostring(val) .. "'")
	end
	return string.format("%.14g", val)
end

function encode(val: any): string
	local t = type(val)
	if t == "nil" then
		return encode_nil(val)
	elseif t == "table" then
		return encode_table(val, nil)
	elseif t == "string" then
		return encode_string(val)
	elseif t == "number" then
		return encode_number(val)
	elseif t == "boolean" then
		return tostring(val)
	end
	error("unexpected type '" .. t .. "'")
end

function json.encode(val: any): string
	return encode(val)
end

-- ─── decode ──────────────────────────────────────────────────────────────────

local parse: (str: string, idx: number) -> (any, number)

local function create_set(...: string): { [string]: boolean }
	local res: { [string]: boolean } = {}
	for _, v in ipairs({ ... }) do
		res[v] = true
	end
	return res
end

local space_chars = create_set(" ", "\t", "\r", "\n")
local delim_chars = create_set(" ", "\t", "\r", "\n", "]", "}", ",")
local escape_chars = create_set("\\", "/", '"', "b", "f", "n", "r", "t", "u")
local literals = create_set("true", "false", "null")

local literal_map: { [string]: any } = {
	["true"] = true,
	["false"] = false,
	["null"] = nil,
}

-- Find the first index >= idx whose char's membership in `set` differs from
-- `negate`. Missing keys are treated as `false` (NOT nil) so `nil ~= negate`
-- comparisons behave correctly: with negate=false this finds the first char
-- that IS in the set; with negate=true, the first char that is NOT in the set.
local function next_char(str: string, idx: number, set: { [string]: boolean }, negate: boolean): number
	for i = idx, #str do
		local inSet = set[str:sub(i, i)] == true
		if inSet ~= negate then
			return i
		end
	end
	return #str + 1
end

local function decode_error(str: string, idx: number, msg: string): never
	error(string.format("%s at position %d", msg, idx))
end

local function parse_string(str: string, i: number): (string, number)
	local res = ""
	local j = i + 1
	local k = j
	while j <= #str do
		local x = string.byte(str, j)
		if x < 32 then
			decode_error(str, j, "control character in string")
		elseif x == 92 then -- backslash
			res ..= str:sub(k, j - 1)
			j += 1
			local c = str:sub(j, j)
			if c == "u" then
				local hex = str:sub(j + 1, j + 4)
				local code = tonumber(hex, 16)
				if not code or not hex:match("^%x%x%x%x$") then
					decode_error(str, j - 1, "invalid unicode escape '\\u" .. hex .. "'")
				end
				res ..= utf8.char(code :: number)
				j += 4
			else
				if not escape_chars[c] then
					decode_error(str, j - 1, "invalid escape char '" .. c .. "'")
				end
				local map: { [string]: string } = {
					["\\"] = "\\", ["/"] = "/", ['"'] = '"',
					b = "\b", f = "\f", n = "\n", r = "\r", t = "\t",
				}
				res ..= map[c]
			end
			k = j + 1
		elseif x == 34 then -- double quote
			res ..= str:sub(k, j - 1)
			return res, j + 1
		end
		j += 1
	end
	decode_error(str, i, "expected closing quote for string")
	return res, i
end

local function parse_number(str: string, i: number): (number, number)
	local x = next_char(str, i, delim_chars, false)
	local s = str:sub(i, x - 1)
	local n = tonumber(s)
	if not n then
		decode_error(str, i, "invalid number '" .. s .. "'")
	end
	return n :: number, x
end

local function parse_literal(str: string, i: number): (any, number)
	local x = next_char(str, i, delim_chars, false)
	local word = str:sub(i, x - 1)
	if not literals[word] then
		decode_error(str, i, "invalid literal '" .. word .. "'")
	end
	return literal_map[word], x
end

local function parse_array(str: string, i: number): ({ any }, number)
	local res = {}
	local n = 1
	i += 1
	while true do
		local x
		i = next_char(str, i, space_chars, true)
		if str:sub(i, i) == "]" then
			i += 1
			break
		end
		x, i = parse(str, i)
		res[n] = x
		n += 1
		i = next_char(str, i, space_chars, true)
		local chr = str:sub(i, i)
		i += 1
		if chr == "]" then
			break
		end
		if chr ~= "," then
			decode_error(str, i, "expected ']' or ','")
		end
	end
	return res, i
end

local function parse_object(str: string, i: number): ({ [string]: any }, number)
	local res: { [string]: any } = {}
	i += 1
	while true do
		local key: any
		local val: any
		i = next_char(str, i, space_chars, true)
		if str:sub(i, i) == "}" then
			i += 1
			break
		end
		if str:sub(i, i) ~= '"' then
			decode_error(str, i, "expected string for key")
		end
		key, i = parse(str, i)
		i = next_char(str, i, space_chars, true)
		if str:sub(i, i) ~= ":" then
			decode_error(str, i, "expected ':' after key")
		end
		i = next_char(str, i + 1, space_chars, true)
		val, i = parse(str, i)
		res[key] = val
		i = next_char(str, i, space_chars, true)
		local chr = str:sub(i, i)
		i += 1
		if chr == "}" then
			break
		end
		if chr ~= "," then
			decode_error(str, i, "expected '}' or ','")
		end
	end
	return res, i
end

local char_func_map: { [string]: (string, number) -> (any, number) } = {
	['"'] = parse_string,
	["0"] = parse_number, ["1"] = parse_number, ["2"] = parse_number,
	["3"] = parse_number, ["4"] = parse_number, ["5"] = parse_number,
	["6"] = parse_number, ["7"] = parse_number, ["8"] = parse_number,
	["9"] = parse_number, ["-"] = parse_number,
	["t"] = parse_literal, ["f"] = parse_literal, ["n"] = parse_literal,
	["["] = parse_array, ["{"] = parse_object,
}

function parse(str: string, idx: number): (any, number)
	local chr = str:sub(idx, idx)
	local f = char_func_map[chr]
	if f then
		return f(str, idx)
	end
	decode_error(str, idx, "unexpected character '" .. chr .. "'")
	return nil, idx
end

function json.decode(str: string): any
	if type(str) ~= "string" then
		error("expected argument of type string")
	end
	local res, idx = parse(str, next_char(str, 1, space_chars, true))
	idx = next_char(str, idx, space_chars, true)
	if idx <= #str then
		decode_error(str, idx, "trailing garbage")
	end
	return res
end

return json
