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

Generated model classes live in `src/main/java/com/rs3buddy/models/` (committed —
no codegen step for consumers). Regenerate from the schema with
`npm run gen:models:java` from the repo root.
