"""RS3Buddy — typed Python client. One method per HTTP route."""
from __future__ import annotations

from typing import Any, Optional
from urllib.parse import urlencode

from .transport import Transport
from .models import (
    Position, ShaderInfo, TextureInfo, SceneSnapshot, ChatReadResult,
    BarsReadResult, AbilitiesReadResult, ProgressReadResult,
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


class _BarsAPI:
    """Status-bar reader namespace, reached via RS3Buddy.bars."""

    def __init__(self, t: Transport) -> None:
        self._t = t

    def read(self) -> BarsReadResult:
        """Read the four status bars (HP / adrenaline / prayer / summoning).

        Each entry has ``value``, ``max`` (when current/max is shown),
        ``found``, ``text`` and the located ``anchor`` + ``region``. Thin
        wrapper over GET /api/bars; recognition runs server-side.
        """
        return self._t.request("GET", "/api/bars")


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
    """Progress-bar reader namespace, reached via RS3Buddy.progress."""

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
