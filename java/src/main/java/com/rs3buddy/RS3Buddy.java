package com.rs3buddy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rs3buddy.models.AbilitiesReadResult;
import com.rs3buddy.models.Bar;
import com.rs3buddy.models.BarsReadResult;
import com.rs3buddy.models.Buff;
import com.rs3buddy.models.BuffsReadResult;
import com.rs3buddy.models.Skill;
import com.rs3buddy.models.SkillsReadResult;
import com.rs3buddy.models.ProgressReadResult;
import com.rs3buddy.models.ChatReadResult;
import com.rs3buddy.models.FrameCaptureResult;
import com.rs3buddy.models.PlayerNameResult;
import com.rs3buddy.models.PostFxPassInput;
import com.rs3buddy.models.Position;
import com.rs3buddy.models.SceneSnapshot;
import com.rs3buddy.models.ShaderFxInput;
import com.rs3buddy.models.ShaderInfo;
import com.rs3buddy.models.TextureInfo;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Typed Java client for the rs3buddy HTTP server. One method per route.
 * Behavior contract: ../CONTRACT.md
 */
public final class RS3Buddy {
    private final Transport t;

    /** Chatbox reader namespace; reached via {@code buddy.chat.read()}. */
    public final Chat chat;

    /** Status-bar reader namespace; reached via {@code buddy.bars.read()}. */
    public final Bars bars;

    /** Buff/debuff reader namespace; reached via {@code buddy.buffs.read()}. */
    public final Buffs buffs;

    /** Skills-interface reader namespace; reached via {@code buddy.skills.read()}. */
    public final Skills skills;

    /** Action-bar reader namespace; reached via {@code buddy.abilities.read()}. */
    public final Abilities abilities;

    /** Progress-bar reader namespace; reached via {@code buddy.progress.read()}. */
    public final Progress progress;

    /** Overlay-UI namespace; reached via {@code buddy.ui.html(...)}. */
    public final UI ui;

    /** Sound-playback namespace; reached via {@code buddy.sound.play(...)}. */
    public final Sound sound;

    public RS3Buddy(String baseUrl) {
        this.t = new Transport(baseUrl);
        this.chat = new Chat();
        this.bars = new Bars();
        this.buffs = new Buffs();
        this.skills = new Skills();
        this.abilities = new Abilities();
        this.progress = new Progress();
        this.ui = new UI();
        this.sound = new Sound();
    }

    public RS3Buddy(String baseUrl, String clientName) {
        this.t = new Transport(baseUrl, clientName);
        this.chat = new Chat();
        this.bars = new Bars();
        this.buffs = new Buffs();
        this.skills = new Skills();
        this.abilities = new Abilities();
        this.progress = new Progress();
        this.ui = new UI();
        this.sound = new Sound();
    }

    /**
     * Connect to the running SDK server, auto-discovering its port from the
     * launcher's config. Reads {@code %APPDATA%\rs3buddy\rs3buddy.json} (falling
     * back to {@code %USERPROFILE%}) — the well-known file the launcher writes
     * when it injects into RS3 and brings the server up. The launcher must be
     * running. To target a specific server instead, use {@code new RS3Buddy(url)}.
     *
     * @throws RS3BuddyConnectionException if the config can't be found or read.
     */
    public static RS3Buddy connect() {
        return connect(null);
    }

    /**
     * Connect to the running SDK server, auto-discovering its port (see
     * {@link #connect()}), tagging requests with {@code clientName} via the
     * {@code X-Client-Name} header.
     *
     * @throws RS3BuddyConnectionException if the config can't be found or read.
     */
    public static RS3Buddy connect(String clientName) {
        return new RS3Buddy("http://127.0.0.1:" + resolvePort(), clientName);
    }

    /**
     * Read the launcher's {@code rs3buddy.json} ({@code %APPDATA%\rs3buddy\}, or
     * {@code %USERPROFILE%} when APPDATA is unset) and return its {@code "port"}.
     */
    private static int resolvePort() {
        String base = System.getenv("APPDATA");
        if (base == null || base.isEmpty()) {
            base = System.getenv("USERPROFILE");
        }
        if (base == null || base.isEmpty()) {
            throw new RS3BuddyConnectionException(
                    "Cannot locate rs3buddy.json: neither APPDATA nor USERPROFILE is set. "
                            + "Is the launcher running?", null);
        }
        Path config = Path.of(base, "rs3buddy", "rs3buddy.json");
        String text;
        try {
            text = Files.readString(config);
        } catch (IOException e) {
            throw new RS3BuddyConnectionException(
                    "Could not read " + config + " — is the launcher running?", e);
        }
        JsonNode node;
        try {
            node = new ObjectMapper().readTree(text);
        } catch (IOException e) {
            throw new RS3BuddyConnectionException(
                    "Could not parse " + config + " — is the launcher running?", e);
        }
        JsonNode port = node == null ? null : node.get("port");
        if (port == null || !port.isInt()) {
            throw new RS3BuddyConnectionException(
                    "No int \"port\" in " + config + " — is the launcher running?", null);
        }
        return port.intValue();
    }

    public String baseUrl() {
        return t.baseUrl();
    }

    /** Build a "?k=v&..." query string, skipping null values. */
    private static String q(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> e : params.entrySet()) {
            if (e.getValue() == null) continue;
            sb.append(sb.length() == 0 ? "?" : "&");
            sb.append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8));
            sb.append("=");
            sb.append(URLEncoder.encode(String.valueOf(e.getValue()), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

    private static Map<String, Object> map(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(String.valueOf(kv[i]), kv[i + 1]);
        }
        return m;
    }

    // ── Player (both hit GET /api/player; ride the native passive tap) ──
    /** Acquire the player position (cold call may take a frame or two). null if not tracked. */
    public Position getPlayer() {
        return t.request("GET", "/api/player", null, Position.class);
    }

    /** Cheap refresh; null if not currently tracked -> call getPlayer() again. */
    public Position updatePlayer() {
        return t.request("GET", "/api/player", null, Position.class);
    }

    /**
     * The local player's name + title(s), recovered from the chat input prompt
     * (opt-in; the name never appears in the chat feed itself). {@code isFound()}
     * is false until the prompt has been seen at least once.
     */
    public PlayerNameResult getPlayerName() {
        return t.request("GET", "/api/player/name", null, PlayerNameResult.class);
    }

    // ── Status / window / heap ──
    public JsonNode getStatus() { return t.requestJson("GET", "/api/status", null); }
    public JsonNode getWindow() { return t.requestJson("GET", "/api/window", null); }
    public JsonNode getHeap() { return t.requestJson("GET", "/api/heap", null); }

    // ── Capture / shaders / textures ──
    public FrameCaptureResult captureFrame(boolean includeMesh, boolean includeTexturePixels,
                                 Integer shaderId, Integer meshId, Integer targetFbo) {
        return t.request("GET", "/api/frame" + q(map(
                "mesh", includeMesh ? "true" : null,
                "pixels", includeTexturePixels ? "true" : null,
                "shaderId", shaderId, "meshId", meshId, "targetFbo", targetFbo)), null,
                FrameCaptureResult.class);
    }
    public FrameCaptureResult captureFrame() { return captureFrame(false, false, null, null, null); }
    public ShaderInfo[] getShaders() { return t.request("GET", "/api/shaders", null, ShaderInfo[].class); }
    public TextureInfo[] getTextures() { return t.request("GET", "/api/textures", null, TextureInfo[].class); }
    public JsonNode getCapturedTextures() { return t.requestJson("GET", "/api/captured-textures", null); }
    public JsonNode refreshCapturedTextures() { return t.requestJson("POST", "/api/captured-textures/refresh", null); }
    public JsonNode capturedTexturesStats() { return t.requestJson("GET", "/api/captured-textures/stats", null); }
    public JsonNode clearCapturedTextures() { return t.requestJson("DELETE", "/api/captured-textures", null); }
    public JsonNode getCapturedTexture(int id) { return t.requestJson("GET", "/api/captured-texture/" + id, null); }
    public JsonNode readTexture(int id, int x, int y, int w, int h) {
        return t.requestJson("GET", "/api/texture/" + id + q(map("x", x, "y", y, "w", w, "h", h)), null);
    }
    public JsonNode readTexture(int id) { return readTexture(id, 0, 0, 0, 0); }
    public JsonNode screenCapture() { return t.requestJson("GET", "/api/screen-capture", null); }
    public JsonNode getGlyphsOnScreen(Map<String, Object> opts) { return t.requestJson("GET", "/api/glyphs-on-screen" + q(opts == null ? map() : opts), null); }
    public JsonNode glyphAtPoint(int x, int y, Map<String, Object> opts) {
        Map<String, Object> body = map("x", x, "y", y);
        if (opts != null) body.putAll(opts);
        return t.requestJson("POST", "/api/glyph-at-point", body);
    }

    // ── Scene ──
    public SceneSnapshot getScene() { return t.request("GET", "/api/scene", null, SceneSnapshot.class); }
    public JsonNode getScenePlayer() { return t.requestJson("GET", "/api/scene/player", null); }
    public JsonNode getNpcs(Integer radius, Integer floor) { return t.requestJson("GET", "/api/scene/npcs" + q(map("radius", radius, "floor", floor)), null); }
    public JsonNode getScenery(Integer radius, Integer floor) { return t.requestJson("GET", "/api/scene/scenery" + q(map("radius", radius, "floor", floor)), null); }
    public JsonNode getFloorTiles(Integer radius, Integer floor) { return t.requestJson("GET", "/api/scene/floor_tiles" + q(map("radius", radius, "floor", floor)), null); }
    public JsonNode getWater(Integer radius, Integer floor) { return t.requestJson("GET", "/api/scene/water" + q(map("radius", radius, "floor", floor)), null); }
    public JsonNode getEntityAt(int x, int z, Integer floor) { return t.requestJson("GET", "/api/scene/at" + q(map("x", x, "z", z, "floor", floor)), null); }

    // ── Draw / overlay ──
    public JsonNode drawShape(Object shape) { return t.requestJson("POST", "/api/draw/shape", shape); }
    public JsonNode drawScene(List<Object> shapes) { return t.requestJson("POST", "/api/draw/scene", shapes); }
    public JsonNode listShapes() { return t.requestJson("GET", "/api/draw", null); }
    public JsonNode removeShape(String id) { return t.requestJson("DELETE", "/api/draw/" + id, null); }
    public JsonNode clearShapes(String group) { return t.requestJson("DELETE", "/api/draw" + q(map("group", group)), null); }

    // ── Fonts ──
    /** List the registered render/DRAW fonts (alias -&gt; .ttf), NOT the recognition registry. */
    public JsonNode listFonts() { return t.requestJson("GET", "/api/fonts/registered", null); }
    public JsonNode registerFont(String name, String path) { return t.requestJson("POST", "/api/fonts/register", map("name", name, "path", path)); }
    public JsonNode unregisterFont(String name) { return t.requestJson("DELETE", "/api/fonts/" + name, null); }

    // ── Sprites ──
    public JsonNode listTrainedSprites() { return t.requestJson("GET", "/api/sprites/trained", null); }
    public JsonNode saveTrainedSprite(Object req) { return t.requestJson("POST", "/api/sprites/trained", req); }
    public JsonNode deleteTrainedSprite(String name) { return t.requestJson("DELETE", "/api/sprites/trained/" + name, null); }

    // ── Atlas ──
    public JsonNode importSpriteHash(Object req) { return t.requestJson("POST", "/api/atlas/import-spritehash", req); }
    public JsonNode trainQuad(Object req) { return t.requestJson("POST", "/api/atlas/train-quad", req); }
    public JsonNode atlasSync() { return t.requestJson("POST", "/api/atlas/sync", null); }
    public JsonNode atlasSprites() { return t.requestJson("GET", "/api/atlas/sprites", null); }
    public JsonNode atlasLookup(long hash) { return t.requestJson("GET", "/api/atlas/lookup/" + hash, null); }
    public JsonNode recognizeQuads(List<Object> quads) { return t.requestJson("POST", "/api/atlas/recognize-quads", map("quads", quads)); }

    // ── OCR ──
    public JsonNode matchText(Object req) { return t.requestJson("POST", "/api/match/text", req); }
    public JsonNode matchRegion(Object req) { return t.requestJson("POST", "/api/match/region", req); }

    // ── Post-FX (fullscreen post-processing passes) ──
    /** Register or replace a fullscreen post-FX pass; the response carries its {@code id}. */
    public JsonNode addPostFx(PostFxPassInput pass) { return t.requestJson("POST", "/api/shaders/postfx", pass); }
    /** All post-FX passes, in render order (ascending {@code order}). */
    public PostFxPassInput[] listPostFx() { return t.request("GET", "/api/shaders/postfx", null, PostFxPassInput[].class); }
    /** Remove a post-FX pass; response carries {@code removed}. */
    public JsonNode removePostFx(String id) { return t.requestJson("DELETE", "/api/shaders/postfx/" + id, null); }
    /** Enable/disable a post-FX pass without removing it; response carries {@code ok}. */
    public JsonNode setPostFxEnabled(String id, boolean enabled) { return t.requestJson("POST", "/api/shaders/postfx/" + id + "/enabled", map("enabled", enabled)); }

    // ── Custom game-shader FX (replace RS3's own shaders, by type or hash) ──
    /** Register or replace a custom game-shader FX; the response carries its {@code id}. */
    public JsonNode addShaderFx(ShaderFxInput o) { return t.requestJson("POST", "/api/shaders/fx", o); }
    /** All active custom game-shader FX. */
    public ShaderFxInput[] listShaderFx() { return t.request("GET", "/api/shaders/fx", null, ShaderFxInput[].class); }
    /** Remove a shader FX (that shader reverts to stock); response carries {@code removed}. */
    public JsonNode removeShaderFx(String id) { return t.requestJson("DELETE", "/api/shaders/fx/" + id, null); }
    /** Enable/disable a shader FX without removing it; response carries {@code ok}. */
    public JsonNode setShaderFxEnabled(String id, boolean enabled) { return t.requestJson("POST", "/api/shaders/fx/" + id + "/enabled", map("enabled", enabled)); }

    // ── Chat ──
    /**
     * Chatbox reader. Thin wrapper over GET /api/chat; the heavy work (frame +
     * screen capture, glyph recognition, line grouping, colour sampling) runs
     * server-side. Returns a typed {@link ChatReadResult} whose {@code getLines()}
     * each expose {@code getText()}, {@code getColor()} ([r, g, b]) and per-glyph
     * detail. Lines are ordered top-to-bottom by y.
     */
    public final class Chat {
        /** Read the chatbox using the default bottom-left region. */
        public ChatReadResult read() {
            return t.request("GET", "/api/chat", null, ChatReadResult.class);
        }

        /** Read the chatbox, overriding the region (window px). */
        public ChatReadResult read(Integer x0, Integer y0, Integer x1, Integer y1) {
            return t.request("GET", "/api/chat"
                    + q(map("x0", x0, "y0", y0, "x1", x1, "y1", y1)), null, ChatReadResult.class);
        }
    }

    // ── Bars ──
    /**
     * Bar reader. One {@code Bar} shape for every bar: the four stat bars
     * (hitpoints, adrenaline, prayer, summoning — always present) plus any
     * dynamic bars on screen (skilling, conjure timers, ...). Each carries
     * {@code fillPct} (exact, from the bar's GPU geometry) plus {@code value} /
     * {@code max} / {@code text} (from the digits the game draws at the bar, when
     * any). All from one capture, so the readings are in sync.
     */
    public final class Bars {
        /** Read every bar (4 stats + any dynamic bars), each with its exact {@code fillPct}. */
        public BarsReadResult read() {
            return t.request("GET", "/api/bars", null, BarsReadResult.class);
        }

        /** Read one bar by stat alias ({@code "hp"}) or name; {@code null} if it is not on screen. */
        public Bar read(String name) {
            BarsReadResult all = read();
            String want = aliasBar(name);
            if (all != null && all.getBars() != null) {
                for (Bar b : all.getBars()) {
                    if (want != null && want.equals(b.getName())) return b;
                }
            }
            return null;
        }

        /** The dynamic-bar friendly-name registry (combo -&gt; name). GET /api/bars/names. */
        public JsonNode names() {
            return t.requestJson("GET", "/api/bars/names", null);
        }

        /** Name (or, with an empty name, un-name) a dynamic bar combo. POST /api/bars/name. */
        public JsonNode name(String combo, String name) {
            return t.requestJson("POST", "/api/bars/name", map("combo", combo, "name", name));
        }
    }

    /** Map a friendly stat alias to its canonical bar name (else the name as-is). */
    private static String aliasBar(String n) {
        if (n == null) return null;
        switch (n.toLowerCase()) {
            case "hp": case "hitpoints": return "hitpoints";
            case "pray": case "prayer": return "prayer";
            case "adren": case "adrenaline": return "adrenaline";
            case "summ": case "summoning": return "summoning";
            default: return n;
        }
    }

    // ── Buffs / debuffs ──
    /**
     * Buff/debuff reader. {@code buffs} and {@code debuffs} come back as separate
     * lists of the same {@code Buff} shape (the {@code kind} field tells them
     * apart). Each cell's {@code name} is resolved from its icon's colour
     * signature; train an unnamed icon via {@link Buffs#name(Object, String)}.
     */
    public final class Buffs {
        /** Read all active buffs + debuffs (separate lists, same {@code Buff} shape). */
        public BuffsReadResult read() {
            return t.request("GET", "/api/buffs", null, BuffsReadResult.class);
        }

        /** Read one buff/debuff by name ({@code "buff:necrosis"} or just {@code "necrosis"}); {@code null} if not active. */
        public Buff read(String name) {
            BuffsReadResult all = read();
            if (all == null || name == null) return null;
            String want = bareName(name);
            for (List<Buff> list : java.util.Arrays.asList(all.getBuffs(), all.getDebuffs())) {
                if (list == null) continue;
                for (Buff b : list) {
                    if (b.getName() != null && bareName(b.getName()).equals(want)) return b;
                }
            }
            return null;
        }

        /** The buff/debuff icon-name registry (iconColorHash -&gt; name). GET /api/buffs/names. */
        public JsonNode names() {
            return t.requestJson("GET", "/api/buffs/names", null);
        }

        /** Name (or, with an empty name, un-name) an icon by its {@code iconColorHash}. POST /api/buffs/name. */
        public JsonNode name(Object iconColorHash, String name) {
            return t.requestJson("POST", "/api/buffs/name", map("colorHash", iconColorHash, "name", name));
        }
    }

    /** Strip an optional sprite prefix and lower-case: "buff:necrosis" -&gt; "necrosis". */
    private static String bareName(String n) {
        if (n == null) return null;
        int i = n.indexOf(':');
        return (i >= 0 ? n.substring(i + 1) : n).toLowerCase();
    }

    // ── Skills ──
    /**
     * Skills-interface reader. Each {@code Skill} carries its current
     * {@code level} and {@code base} (trained) level; {@code Skill.getName()} is
     * one of the RS3 skill names.
     */
    public final class Skills {
        /** Read every skill currently on screen (each with current {@code level} + {@code base}). */
        public SkillsReadResult read() {
            return t.request("GET", "/api/skills", null, SkillsReadResult.class);
        }

        /** Read one skill by name ({@code "attack"}, {@code "herblore"}, …); {@code null} if not on screen. */
        public Skill read(String name) {
            SkillsReadResult all = read();
            if (all == null || name == null || all.getSkills() == null) return null;
            String want = name.toLowerCase();
            for (Skill s : all.getSkills()) {
                if (s.getName() != null && s.getName().toLowerCase().equals(want)) return s;
            }
            return null;
        }
    }

    // ── Ability bars ──
    /**
     * Action-bar reader. Thin wrapper over GET /api/abilities; each slot exposes
     * its ability {@code name}, {@code rect}, cooldown and {@code usable} state.
     */
    public final class Abilities {
        /** Read the action bar(s). */
        public AbilitiesReadResult read() {
            return t.request("GET", "/api/abilities", null, AbilitiesReadResult.class);
        }
    }

    // ── Progress bars ──
    /**
     * Progress-bar reader (action progress, conjure timers, skilling,
     * adrenaline, ...). Thin wrapper over GET /api/progress; each bar TYPE is
     * identified by its colour signature ({@code combo}, or a friendly
     * {@code name} you registered). The {@link ProgressReadResult} carries the
     * raw {@code bars}, a per-type {@code groups} aggregate (flicker-proof
     * {@code stableCount} + each fill %), and per-type {@code began}/{@code ended}.
     *
     * @deprecated Dynamic bars are now part of the unified {@link Bars} namespace —
     *     they appear in {@code bars.read()} alongside the stat bars (same
     *     {@code Bar} shape). Kept for back-compat; prefer {@code buddy.bars}.
     */
    @Deprecated
    public final class Progress {
        /** Detect every progress bar on screen. */
        public ProgressReadResult read() {
            return t.request("GET", "/api/progress", null, ProgressReadResult.class);
        }

        /** Read just one bar type, by friendly {@code name} or colour-signature {@code combo}. */
        public ProgressReadResult read(String name, String combo) {
            return t.request("GET", "/api/progress"
                    + q(map("name", name, "combo", combo)), null, ProgressReadResult.class);
        }

        /** The combo -&gt; friendly-name registry. */
        public JsonNode names() {
            return t.requestJson("GET", "/api/progress/names", null);
        }

        /** Name a bar combo so you can read it by name (empty name removes it). */
        public JsonNode setName(String combo, String name) {
            return t.requestJson("POST", "/api/progress/name", map("combo", combo, "name", name));
        }
    }

    // ── Overlay UI ──
    /**
     * Overlay UI. Author the HUD as HTML + CSS and POST it; the server compiles it
     * to the same widget engine the SDK renders (clicks / drag / scaling all work).
     * Your app owns the state: poll {@link #events()} for clicks (each event carries
     * the clicked widget's {@code id}) and re-render by calling {@link #html} again.
     */
    public final class UI {
        /** Render an HTML + CSS "page" to the overlay (replaces the current UI). */
        public JsonNode html(String html, String css) {
            return t.requestJson("POST", "/api/ui/html", map("html", html, "css", css == null ? "" : css));
        }

        /** Render a raw widget tree ({@code {type, props, children}}) to the overlay. */
        public JsonNode render(Object tree) {
            return t.requestJson("POST", "/api/ui", tree);
        }

        /** Clear the overlay UI. */
        public JsonNode clear() {
            return t.requestJson("DELETE", "/api/ui", null);
        }

        /** Drain queued interaction events (clicks / close / minimize); each {@code {type, id, x, y}}. */
        public JsonNode events() {
            return t.requestJson("GET", "/api/ui/events", null);
        }

        /**
         * Configure auto display-scaling (how big the UI is on hi-DPI / 4K).
         * Body: {@code { exponent?, scale?, baseHeight? }} — null args are omitted.
         * {@code exponent} 1 = proportional (≈constant physical size); &gt;1 = bigger
         * on 4K (default 1.5). {@code scale} overrides the exponent curve;
         * {@code baseHeight} is the reference window height it normalises against.
         */
        public JsonNode scaling(Double exponent, Double scale, Double baseHeight) {
            Map<String, Object> body = new LinkedHashMap<>();
            if (exponent != null) body.put("exponent", exponent);
            if (scale != null) body.put("scale", scale);
            if (baseHeight != null) body.put("baseHeight", baseHeight);
            return t.requestJson("POST", "/api/ui/scaling", body);
        }
    }

    // ── Sound ──
    /**
     * Sound playback (host-side). Thin wrapper over POST /api/sound; play either a
     * host-side {@code file} path (or {@code file:}/{@code data:}/{@code http(s):}
     * URL) via {@link #play}, or inline base64 audio via {@link #playBytes}.
     * {@code volume} is 0..1. Requires the desktop audio host (no-op headless).
     */
    public final class Sound {
        /**
         * Play an audio file on the host. {@code file} is a path or a
         * {@code file:}/{@code data:}/{@code http(s):} URL; {@code volume} (0..1)
         * is omitted when null (host default).
         */
        public JsonNode play(String file, Double volume) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("file", file);
            if (volume != null) body.put("volume", volume);
            return t.requestJson("POST", "/api/sound", body);
        }

        /**
         * Play inline base64-encoded audio. {@code base64} is the audio payload,
         * {@code mime} its MIME type (e.g. {@code "audio/wav"}); {@code volume}
         * (0..1) is omitted when null (host default).
         */
        public JsonNode playBytes(String base64, String mime, Double volume) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("bytes", base64);
            body.put("mime", mime);
            if (volume != null) body.put("volume", volume);
            return t.requestJson("POST", "/api/sound", body);
        }
    }
}
