# rs3buddy — UI overlay: HTML tags & CSS

Version 0.1.0 · last updated 2026-06-21

Author your in-game HUD as HTML + CSS and pass it to `ui.html(html, css)`. The
server compiles it into the widget engine, draws it in-game, and auto-scales it for
the display. **Each `ui.html(...)` call replaces the whole overlay** — poll
`ui.events()` for clicks, update your own state, and call `ui.html(...)` again.

For the `ui.*` method signatures in your language, see its API reference
([TypeScript](../ts/README.md) · [Python](../python/README.md) ·
[Lua](../lua/README.md) · [Java](../java/README.md)). This page is the tag + CSS
support list. A full working overlay is in [examples.md](examples.md).

## Supported HTML tags

Unknown tags become a column container, so a page never hard-fails.

| tag(s) | becomes |
|---|---|
| `panel` | panel (rounded container / HUD root) |
| `div`, `section`, `article`, `main`, `column`, `col`, `vstack` | column (vertical flex) |
| `row`, `header`, `footer`, `nav`, `hstack` | row (horizontal flex) |
| `span`, `p`, `label`, `text`, `h1`–`h4`, `small`, `b`, `strong` | text label (`h1`–`h4` = larger font) |
| `button`, `a`, `btn` | button |
| `progress`, `gauge`, `meter`, `bar` | gauge / stat bar (`value`, `max`, `min`, `vertical` attrs) |
| `img`, `image` | image (`src` attr) |
| `hr`, `divider` | divider |
| `badge` | badge |

**Special attributes**

- `id` — so a widget's clicks surface in `ui.events()`.
- `draggable` (on a panel) — lets the user move the panel; the game camera stays put.
- `consume` — the widget blocks clicks from reaching the game behind it.
- `anchor` — pins a root, e.g. `top-right`, `bottom-left`, `center`.
- `variant` (on a button) — `primary` / `secondary` / `good` / `danger` / `ghost` / `plain`.
- `icon` — `close` / `minimize` / `maximize` (drawn, font-free).
- `action="minimize"` / `action="close"` — built-in button behaviors (an icon button
  defaults its icon to its action).

## Supported CSS

Selectors: **tag**, **`.class`**, **`#id`**, **descendant** (`.row button`), and
inline `style="..."` (inline wins). Unknown CSS is ignored.

| CSS | effect |
|---|---|
| `background` (incl. `linear-gradient(...)`), `background-color`, `background-image: url(...)` | element background (gradient → two-stop) |
| `color` | text colour |
| `fill` | gauge fill colour |
| `border` (`1px solid #rrggbb`), `border-color`, `border-width` | outline |
| `border-radius` | corner radius |
| `box-shadow` | drop shadow |
| `padding`, `margin`, `gap` | spacing |
| `display: flex` (+ `flex-direction`) | flex row / column |
| `justify-content` | main-axis distribution (`flex-start` / `center` / `flex-end` / `space-between` / …) |
| `align-items` | cross-axis alignment (`flex-start` / `center` / `flex-end` / `stretch`) |
| `width`, `height` | size (px number, `auto`, or `%` → flex) |
| `flex` | `flex: 1` grows the element |
| `font-size`, `font-family` | text font |
| `opacity` | element opacity |

## Minimal example

```html
<panel anchor='top-right' draggable consume>
  <header><span class='title'>Tracker</span>
    <button id='close' icon='close' variant='plain'></button></header>
  <hr/>
  <div class='row'><span class='k'>Prayer</span><span class='v'>612</span></div>
</panel>
```
```css
panel { background:#241d12; border:1px solid #b0904a; border-radius:10px; padding:12px; gap:8px; width:220px }
.title { color:#f5c54a; font-size:16px }
.row { display:flex; justify-content:space-between }
.k { color:#c9bfa6 } .v { color:#fff }
```

This renders a draggable brown-stone panel in the top-right with a title, a close
button, and one `Prayer` row.
