package com.rs3buddy;

import com.rs3buddy.models.ChatLine;
import com.rs3buddy.models.ChatReadResult;

import java.util.List;

/**
 * Read the RS3 chatbox and print every line with its colour.
 *
 * The launcher must be running (native injected into RS3 -> SDK server up);
 * {@link RS3Buddy#connect()} auto-discovers the port from the launcher's
 * %APPDATA%\rs3buddy\rs3buddy.json — no URL needed.
 *
 * NOTE: examples/ is NOT in the Gradle source set, so it isn't built by
 * `gradle build`. To run it, build the client jar and compile this file against
 * it plus the Jackson jar (both on the classpath):
 *
 *   gradle jar
 *   # then (paths shown for Windows; use your real Jackson jar locations):
 *   javac -cp "build/libs/rs3buddy-client-0.1.0.jar" -d build/examples examples/ReadChat.java
 *   java  -cp "build/examples;build/libs/rs3buddy-client-0.1.0.jar;<jackson-databind.jar>;<jackson-core.jar>;<jackson-annotations.jar>" com.rs3buddy.ReadChat
 *
 * The Jackson jars are pulled in by Gradle; find them under your Gradle cache
 * (e.g. ~/.gradle/caches/modules-2/.../jackson-*-2.17.1.jar). On Linux/macOS use
 * ':' instead of ';' as the classpath separator.
 */
public final class ReadChat {
    public static void main(String[] args) {
        // connect() with no URL -> discover the running server via the launcher.
        RS3Buddy buddy = RS3Buddy.connect("read-chat-example");
        System.out.println("connected: " + buddy.baseUrl());

        ChatReadResult chat = buddy.chat.read();
        Double lineCount = chat.getLineCount();
        System.out.println("lineCount: " + (lineCount == null ? 0 : lineCount.intValue()));

        List<ChatLine> lines = chat.getLines();
        if (lines == null) {
            return;
        }
        for (ChatLine line : lines) {
            List<Double> color = line.getColor(); // [r, g, b]
            String rgb = (color != null && color.size() == 3)
                    ? "(" + color.get(0).intValue() + "," + color.get(1).intValue()
                            + "," + color.get(2).intValue() + ")"
                    : "(?)";
            String text = line.getText();
            System.out.println(rgb + " " + (text == null ? "" : text));
        }
    }
}
