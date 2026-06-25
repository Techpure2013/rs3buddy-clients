"""RS3Buddy — typed Python client. One method per HTTP route."""
from __future__ import annotations

from typing import Any, Optional, overload
from urllib.parse import urlencode

from .transport import Transport
from .models import (
    Position, ShaderInfo, TextureInfo, SceneSnapshot, ChatReadResult,
    BarsReadResult, Bar, BuffsReadResult, Buff, SkillsReadResult, Skill, SkillName,
    AbilitiesReadResult, ProgressReadResult,
    DrawItem, PostFxPassInput, ShaderFxInput, PlayerNameResult, FrameCaptureResult,
)


def _q(**params: Any) -> str:
    pairs = {k: v for k, v in params.items() if v is not None}
    return ("?" + urlencode(pairs)) if pairs else ""


class _ChatAPI:
    """Chatbox reader namespace, reached via RS3Buddy.chat."""

    def __init__(self, t: Transport) -> None:
        self._t = t

    def read(
        self,
        x0: Optional[int] = None,
        y0: Optional[int] = None,
        x1: Optional[int] = None,
        y1: Optional[int] = None,
    ) -> ChatReadResult:
        """Read the chatbox as structured lines + per-glyph colour.

        Thin wrapper over GET /api/chat; the heavy work (frame + screen
        capture, glyph recognition, line grouping, colour sampling) runs
        server-side. Pass x0/y0/x1/y1 (window px) to override the default
        bottom-left chatbox region. Returns:
            { "ok": True, "lineCount": int,
              "lines": [ { "y": int, "text": str, "color": [r, g, b],
                           "glyphs": [ { "char": str, "x": int,
                                         "color": [r, g, b] } ] } ] }
        Lines are ordered top-to-bottom by y.
        """
        return self._t.request("GET", "/api/chat" + _q(x0=x0, y0=y0, x1=x1, y1=y1))


# Map a friendly stat alias to its canonical bar name.
_BAR_ALIAS = {
    "hp": "hitpoints", "hitpoints": "hitpoints", "pray": "prayer", "prayer": "prayer",
    "adren": "adrenaline", "adrenaline": "adrenaline", "summ": "summoning", "summoning": "summoning",
}


class _BarsAPI:
    """Bar reader namespace, reached via RS3Buddy.bars."""

    def __init__(self, t: Transport) -> None:
        self._t = t

    @overload
    def read(self) -> BarsReadResult: ...
    @overload
    def read(self, name: str) -> Optional[Bar]: ...
    def read(self, name: Optional[str] = None) -> Any:
        """Read the bars (GET /api/bars).

        With no argument, returns the full result: ``bars`` is the four stat
        bars (hitpoints, adrenaline, prayer, summoning) followed by any dynamic
        bars on screen (skilling, conjure timers, ...), each the SAME shape --
        ``fillPct`` (exact, from the bar's GPU geometry) plus ``value`` /
        ``max`` / ``text`` (from the digits the game draws at the bar, when
        any). All from one capture, so they're in sync.

        With a ``name`` (a stat alias like ``"hp"``, or any bar's name), returns
        just that one bar dict, or ``None`` if it is not on screen.
        """
        result = self._t.request("GET", "/api/bars")
        if not name:
            return result
        want = _BAR_ALIAS.get(str(name).lower(), str(name))
        bars = result.get("bars", []) if isinstance(result, dict) else []
        for bar in bars:
            if isinstance(bar, dict) and bar.get("name") == want:
                return bar
        return None

    def names(self) -> Any:
        """The dynamic-bar friendly-name registry (combo -> name). GET /api/bars/names."""
        return self._t.request("GET", "/api/bars/names")

    def name(self, combo: str, name: str) -> Any:
        """Name (or, with an empty name, un-name) a dynamic bar combo. POST /api/bars/name."""
        return self._t.request("POST", "/api/bars/name", {"combo": combo, "name": name})


class _BuffsAPI:
    """Buff/debuff reader namespace, reached via RS3Buddy.buffs."""

    def __init__(self, t: Transport) -> None:
        self._t = t

    @overload
    def read(self) -> BuffsReadResult: ...
    @overload
    def read(self, name: str) -> Optional[Buff]: ...
    def read(self, name: Optional[str] = None) -> Any:
        """Read the buff bar (GET /api/buffs).

        With no argument, returns the full result: ``buffs`` and ``debuffs`` come
        back as separate lists of the SAME shape (``kind`` tells them apart), each
        cell carrying ``name`` (from its icon's colour signature), ``value`` (the
        timer/stack), ``text`` and ``rect``.

        With a ``name`` (e.g. ``"buff:necrosis"`` or just ``"necrosis"`` — the
        sprite prefix is optional), returns just that one buff/debuff dict, or
        ``None`` if it is not active.
        """
        result = self._t.request("GET", "/api/buffs")
        if not name:
            return result

        def bare(s: str) -> str:
            return (s.split(":")[-1] if ":" in s else s).lower()

        want = bare(str(name))
        if isinstance(result, dict):
            for entry in list(result.get("buffs", [])) + list(result.get("debuffs", [])):
                nm = entry.get("name") if isinstance(entry, dict) else None
                if nm is not None and bare(str(nm)) == want:
                    return entry
        return None

    def names(self) -> Any:
        """The buff/debuff icon-name registry (iconColorHash -> name). GET /api/buffs/names."""
        return self._t.request("GET", "/api/buffs/names")

    def name(self, icon_color_hash: Any, name: str) -> Any:
        """Name (or, empty name to un-name) an icon by its iconColorHash. POST /api/buffs/name."""
        return self._t.request("POST", "/api/buffs/name", {"colorHash": icon_color_hash, "name": name})


class _SkillsAPI:
    """Skills-interface reader namespace, reached via RS3Buddy.skills."""

    def __init__(self, t: Transport) -> None:
        self._t = t

    @overload
    def read(self) -> SkillsReadResult: ...
    @overload
    def read(self, name: SkillName) -> Optional[Skill]: ...
    def read(self, name: Optional[str] = None) -> Any:
        """Read the skills interface (GET /api/skills).

        With no argument, returns every skill cell on screen, each with its
        current ``level`` and ``base`` (trained) level. With a skill ``name`` (a
        ``SkillName`` like ``"attack"``), returns just that one skill dict, or
        ``None`` if it is not on screen.
        """
        result = self._t.request("GET", "/api/skills")
        if not name:
            return result
        want = str(name).lower()
        skills = result.get("skills", []) if isinstance(result, dict) else []
        for skill in skills:
            if isinstance(skill, dict) and str(skill.get("name", "")).lower() == want:
                return skill
        return None


class _AbilitiesAPI:
    """Action-bar reader namespace, reached via RS3Buddy.abilities."""

    def __init__(self, t: Transport) -> None:
        self._t = t

    def read(self) -> AbilitiesReadResult:
        """Read the action bar(s): each slot's ability + cooldown + usable state.

        Each entry has ``name``, ``rect``, ``onCooldown`` / ``cooldownText`` /
        ``cooldownSeconds`` and ``usable``. Thin wrapper over GET
        /api/abilities; recognition runs server-side.
        """
        return self._t.request("GET", "/api/abilities")


class _ProgressAPI:
    """Progress-bar reader namespace, reached via RS3Buddy.progress.

    .. deprecated::
        Dynamic bars are now part of the unified ``bars`` namespace -- they
        appear in ``RS3Buddy.bars.read()`` alongside the stat bars, same shape.
        Kept for back-compat; prefer ``RS3Buddy.bars``.
    """

    def __init__(self, t: Transport) -> None:
        self._t = t

    def read(self, name: Optional[str] = None, combo: Optional[str] = None) -> ProgressReadResult:
        """Detect on-screen progress bars (conjure timers, skilling, adrenaline, ...).

        Each bar TYPE is identified by its colour signature (``combo``, or a
        friendly ``name`` you registered) -- no training. Returns the raw
        ``bars``, a per-type ``groups`` aggregate (flicker-proof ``stableCount``
        plus each fill %), and per-type ``began`` / ``ended`` events. Pass
        ``name`` or ``combo`` to read just one bar type. GET /api/progress.
        """
        return self._t.request("GET", "/api/progress" + _q(name=name, combo=combo))

    def names(self) -> Any:
        """The combo -> friendly-name registry (GET /api/progress/names)."""
        return self._t.request("GET", "/api/progress/names")

    def set_name(self, combo: str, name: str) -> Any:
        """Name a bar combo so you can read it by name (empty name removes it).

        POST /api/progress/name.
        """
        return self._t.request("POST", "/api/progress/name", {"combo": combo, "name": name})


class _UIAPI:
    """Overlay-UI namespace, reached via RS3Buddy.ui.

    Author the HUD as HTML + CSS and POST it; the server compiles it to the
    same widget engine the SDK renders (clicks / drag / scaling all work).
    Your app owns the state: poll ``events()`` for clicks (each event carries
    the clicked widget's ``id``) and re-render by calling ``html()`` again.
    """

    def __init__(self, t: Transport) -> None:
        self._t = t

    def html(self, html: str, css: str = "") -> Any:
        """Render an HTML + CSS "page" to the overlay (replaces the current UI).

        Thin wrapper over POST /api/ui/html with body
        ``{"html": ..., "css": ...}``. Compiles to the SAME engine as
        ``render()``, so drag / clicks / scaling / events all work. This is
        the primary authoring path; returns ``{"ok": True, "size": int}``.
        """
        return self._t.request("POST", "/api/ui/html", {"html": html, "css": css})

    def render(self, tree: Any) -> Any:
        """Render a raw widget tree (``{type, props, children}``) to the overlay.

        Thin wrapper over POST /api/ui; ``tree`` is sent as the request body.
        """
        return self._t.request("POST", "/api/ui", tree)

    def clear(self) -> Any:
        """Clear the overlay UI (DELETE /api/ui)."""
        return self._t.request("DELETE", "/api/ui")

    def events(self) -> Any:
        """Drain queued interaction events (clicks / close / minimize).

        Thin wrapper over GET /api/ui/events; returns ``{"events": [ ... ]}``
        where each event is ``{"type": str, "id": str, "x": int, "y": int}``.
        Poll this in your loop and react (re-render via ``html()``).
        """
        return self._t.request("GET", "/api/ui/events")

    def scaling(
        self,
        exponent: Optional[float] = None,
        scale: Optional[float] = None,
        base_height: Optional[int] = None,
    ) -> Any:
        """Configure auto display-scaling (how big the UI is on hi-DPI / 4K).

        Thin wrapper over POST /api/ui/scaling; only the given keys are sent.
        ``exponent`` 1 = proportional (≈constant physical size); >1 = bigger
        on 4K (default 1.5). ``base_height`` maps to the ``baseHeight`` key.
        """
        body = {k: v for k, v in (
            ("exponent", exponent),
            ("scale", scale),
            ("baseHeight", base_height),
        ) if v is not None}
        return self._t.request("POST", "/api/ui/scaling", body)


class _SoundAPI:
    """Sound playback namespace, reached via RS3Buddy.sound."""

    def __init__(self, t: Transport) -> None:
        self._t = t

    def play(
        self,
        file: Optional[str] = None,
        bytes: Optional[str] = None,
        mime: Optional[str] = None,
        volume: Optional[float] = None,
    ) -> Any:
        """Play a sound through the desktop app's audio host.

        Thin wrapper over POST /api/sound. Provide EITHER ``file`` (a path or
        ``file:``/``data:``/``http(s):`` URL) OR base64 ``bytes`` with an
        optional ``mime`` (defaults server-side to ``audio/wav``). ``volume``
        is an optional 0..1 gain. Only the given keys are sent. Returns
        ``{"ok": True}``; ``{"ok": False, "error": ...}`` when no audio host
        is available (sound requires the desktop app).
        """
        body = {k: v for k, v in (
            ("file", file),
            ("bytes", bytes),
            ("mime", mime),
            ("volume", volume),
        ) if v is not None}
        return self._t.request("POST", "/api/sound", body)


class RS3Buddy:
    def __init__(
        self,
        base_url: Optional[str] = None,
        client_name: Optional[str] = None,
        timeout: float = 5.0,
    ) -> None:
        self._t = Transport(base_url=base_url, client_name=client_name, timeout=timeout)
        self.chat = _ChatAPI(self._t)
        self.bars = _BarsAPI(self._t)
        self.buffs = _BuffsAPI(self._t)
        self.skills = _SkillsAPI(self._t)
        self.abilities = _AbilitiesAPI(self._t)
        self.progress = _ProgressAPI(self._t)
        self.ui = _UIAPI(self._t)
        self.sound = _SoundAPI(self._t)

    @classmethod
    def connect(
        cls,
        base_url: Optional[str] = None,
        client_name: Optional[str] = None,
        timeout: float = 5.0,
    ) -> "RS3Buddy":
        """Connect to the running SDK server.

        With no base_url, auto-discovers the server from the launcher's config
        (rs3buddy.json under %APPDATA%/rs3buddy, then $RS3BUDDY_CONFIG / the cwd).
        The launcher must be running (native injected into RS3 -> SDK server up).
        Pass base_url only to target a specific server.
        """
        return cls(base_url=base_url, client_name=client_name, timeout=timeout)

    @property
    def base_url(self) -> str:
        return self._t.base_url

    def close(self) -> None:
        self._t.close()

    # ── Player (both hit GET /api/player; ride the native passive tap) ──
    def get_player(self) -> Optional[Position]:
        """Acquire the player position (cold call may take a frame or two)."""
        return self._t.request("GET", "/api/player")

    def update_player(self) -> Optional[Position]:
        """Cheap refresh; None if not currently tracked -> call get_player() again."""
        return self._t.request("GET", "/api/player")

    def get_player_name(self) -> PlayerNameResult:
        """The local player's name + title(s), recovered from the chat input prompt.

        Opt-in; the name never appears in the chat feed itself. ``found`` is False
        until the prompt has been seen at least once.
        """
        return self._t.request("GET", "/api/player/name")

    # ── Status / window / heap ──
    def get_status(self) -> Any: return self._t.request("GET", "/api/status")
    def get_window(self) -> Any: return self._t.request("GET", "/api/window")
    def get_heap(self) -> Any: return self._t.request("GET", "/api/heap")

    # ── Capture / shaders / textures ──
    def capture_frame(self, include_mesh: bool = False, include_texture_pixels: bool = False,
                      shader_id: Optional[int] = None, mesh_id: Optional[int] = None,
                      target_fbo: Optional[int] = None) -> FrameCaptureResult:
        return self._t.request("GET", "/api/frame" + _q(
            mesh="true" if include_mesh else None,
            pixels="true" if include_texture_pixels else None,
            shaderId=shader_id, meshId=mesh_id, targetFbo=target_fbo))
    def get_shaders(self) -> list[ShaderInfo]: return self._t.request("GET", "/api/shaders")
    def get_textures(self) -> list[TextureInfo]: return self._t.request("GET", "/api/textures")
    def get_captured_textures(self) -> Any: return self._t.request("GET", "/api/captured-textures")
    def refresh_captured_textures(self) -> Any: return self._t.request("POST", "/api/captured-textures/refresh")
    def captured_textures_stats(self) -> Any: return self._t.request("GET", "/api/captured-textures/stats")
    def clear_captured_textures(self) -> Any: return self._t.request("DELETE", "/api/captured-textures")
    def get_captured_texture(self, id: int) -> Any: return self._t.request("GET", f"/api/captured-texture/{id}")
    def read_texture(self, id: int, x: int = 0, y: int = 0, w: int = 0, h: int = 0) -> Any:
        return self._t.request("GET", f"/api/texture/{id}" + _q(x=x, y=y, w=w, h=h))
    def screen_capture(self) -> Any: return self._t.request("GET", "/api/screen-capture")
    def get_glyphs_on_screen(self, **opts: int) -> Any: return self._t.request("GET", "/api/glyphs-on-screen" + _q(**opts))
    def glyph_at_point(self, x: int, y: int, **opts: int) -> Any: return self._t.request("POST", "/api/glyph-at-point", {"x": x, "y": y, **opts})

    # ── Scene ──
    def get_scene(self) -> SceneSnapshot: return self._t.request("GET", "/api/scene")
    def get_scene_player(self) -> Any: return self._t.request("GET", "/api/scene/player")
    def get_npcs(self, radius: Optional[int] = None, floor: Optional[int] = None) -> Any: return self._t.request("GET", "/api/scene/npcs" + _q(radius=radius, floor=floor))
    def get_scenery(self, radius: Optional[int] = None, floor: Optional[int] = None) -> Any: return self._t.request("GET", "/api/scene/scenery" + _q(radius=radius, floor=floor))
    def get_floor_tiles(self, radius: Optional[int] = None, floor: Optional[int] = None) -> Any: return self._t.request("GET", "/api/scene/floor_tiles" + _q(radius=radius, floor=floor))
    def get_water(self, radius: Optional[int] = None, floor: Optional[int] = None) -> Any: return self._t.request("GET", "/api/scene/water" + _q(radius=radius, floor=floor))
    def get_entity_at(self, x: int, z: int, floor: Optional[int] = None) -> Any: return self._t.request("GET", "/api/scene/at" + _q(x=x, z=z, floor=floor))

    # ── Draw / overlay ──
    def draw_shape(self, shape: DrawItem) -> Any: return self._t.request("POST", "/api/draw/shape", shape)
    def draw_scene(self, shapes: list[DrawItem]) -> Any: return self._t.request("POST", "/api/draw/scene", shapes)
    def list_shapes(self) -> Any: return self._t.request("GET", "/api/draw")
    def remove_shape(self, id: str) -> Any: return self._t.request("DELETE", f"/api/draw/{id}")
    def clear_shapes(self, group: Optional[str] = None) -> Any: return self._t.request("DELETE", "/api/draw" + _q(group=group))

    # ── Fonts ──
    def list_fonts(self) -> Any:
        """List the registered render/DRAW fonts (alias -> .ttf), NOT the recognition registry."""
        return self._t.request("GET", "/api/fonts/registered")
    def register_font(self, name: str, path: str) -> Any: return self._t.request("POST", "/api/fonts/register", {"name": name, "path": path})
    def unregister_font(self, name: str) -> Any: return self._t.request("DELETE", f"/api/fonts/{name}")

    # ── Sprites ──
    def list_trained_sprites(self) -> Any: return self._t.request("GET", "/api/sprites/trained")
    def save_trained_sprite(self, req: Any) -> Any: return self._t.request("POST", "/api/sprites/trained", req)
    def delete_trained_sprite(self, name: str) -> Any: return self._t.request("DELETE", f"/api/sprites/trained/{name}")

    # ── Atlas ──
    def import_sprite_hash(self, req: Any) -> Any: return self._t.request("POST", "/api/atlas/import-spritehash", req)
    def train_quad(self, req: Any) -> Any: return self._t.request("POST", "/api/atlas/train-quad", req)
    def atlas_sync(self) -> Any: return self._t.request("POST", "/api/atlas/sync")
    def atlas_sprites(self) -> Any: return self._t.request("GET", "/api/atlas/sprites")
    def atlas_lookup(self, hash: int) -> Any: return self._t.request("GET", f"/api/atlas/lookup/{hash}")
    def recognize_quads(self, quads: list[Any]) -> Any: return self._t.request("POST", "/api/atlas/recognize-quads", {"quads": quads})

    # ── OCR ──
    def match_text(self, req: Any) -> Any: return self._t.request("POST", "/api/match/text", req)
    def match_region(self, req: Any) -> Any: return self._t.request("POST", "/api/match/region", req)

    # ── Post-FX (fullscreen post-processing passes) ──
    def add_post_fx(self, pass_: PostFxPassInput) -> Any:
        """Register or replace a fullscreen post-FX pass; returns {"id": ...}."""
        return self._t.request("POST", "/api/shaders/postfx", pass_)
    def list_post_fx(self) -> list[PostFxPassInput]:
        """All post-FX passes, in render order (ascending ``order``)."""
        return self._t.request("GET", "/api/shaders/postfx")
    def remove_post_fx(self, id: str) -> Any:
        """Remove a post-FX pass; returns {"removed": bool}."""
        return self._t.request("DELETE", f"/api/shaders/postfx/{id}")
    def set_post_fx_enabled(self, id: str, enabled: bool) -> Any:
        """Enable/disable a post-FX pass without removing it; returns {"ok": bool}."""
        return self._t.request("POST", f"/api/shaders/postfx/{id}/enabled", {"enabled": enabled})

    # ── Custom game-shader FX (replace RS3's own shaders, by type or hash) ──
    def add_shader_fx(self, o: ShaderFxInput) -> Any:
        """Register or replace a custom game-shader FX; returns {"id": ...}."""
        return self._t.request("POST", "/api/shaders/fx", o)
    def list_shader_fx(self) -> list[ShaderFxInput]:
        """All active custom game-shader FX."""
        return self._t.request("GET", "/api/shaders/fx")
    def remove_shader_fx(self, id: str) -> Any:
        """Remove a shader FX (that shader reverts to stock); returns {"removed": bool}."""
        return self._t.request("DELETE", f"/api/shaders/fx/{id}")
    def set_shader_fx_enabled(self, id: str, enabled: bool) -> Any:
        """Enable/disable a shader FX without removing it; returns {"ok": bool}."""
        return self._t.request("POST", f"/api/shaders/fx/{id}/enabled", {"enabled": enabled})
