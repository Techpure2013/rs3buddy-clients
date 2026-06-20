# rs3buddy (Java)

Typed HTTP client for the rs3buddy server. Behavior contract: `../CONTRACT.md`.
Targets Java 17+ (built-in `java.net.http.HttpClient`; Jackson for JSON binding).

## Use in your Gradle project (from git / local path)

    // settings.gradle
    includeBuild("path/to/clients/java")
    // build.gradle
    dependencies { implementation "com.rs3buddy:rs3buddy-client:0.1.0" }

## Use

    import com.rs3buddy.RS3Buddy;
    import com.rs3buddy.models.Position;

    var buddy = new RS3Buddy("http://127.0.0.1:4400");
    Position p = buddy.getPlayer();           // null if not tracked
    while (running) {
        Position cur = buddy.updatePlayer();
        if (cur == null) cur = buddy.getPlayer();
    }

## UI overlay

Author the HUD as HTML + CSS and POST it; the server renders it with the same
widget engine the SDK uses (clicks / drag / scaling all work). Your app owns the
state — poll `buddy.ui.events()` for clicks and re-render by calling `html` again.

    buddy.ui.html("<panel><button id='go'>Go</button></panel>", "panel{padding:12px}");
    buddy.ui.scaling(1.5, null, null);   // exponent only; null args are omitted
    JsonNode evs = buddy.ui.events();    // drain queued clicks (each {type, id, x, y})
    buddy.ui.clear();

A complete, runnable HUD (state + re-render loop) is in
[`examples/Hud.java`](examples/Hud.java).

## Sound

Play a developer-supplied sound through the desktop app (requires the audio host).
Pass a host-side `file` path (or `file:`/`data:`/`http(s):` URL), or inline base64
`bytes` (+ `mime`); `volume` is 0..1 and is omitted when null.

    buddy.sound.play("C:\\sfx\\ping.wav", 0.8);
    buddy.sound.playBytes(base64Wav, "audio/wav", null);

Generated model classes live in `src/main/java/com/rs3buddy/models/` (committed —
no codegen step for consumers). Regenerate from the schema with
`npm run gen:models:java` from the repo root.
