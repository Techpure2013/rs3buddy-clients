package com.rs3buddy;

import com.rs3buddy.models.Position;

/**
 * Manual live smoke test. Requires a running rs3buddy server.
 * Run with: gradle -q runSmoke   (or compile + java com.rs3buddy.SmokeLive)
 * Base URL from RS3BUDDY_BASEURL env var, else http://127.0.0.1:4400.
 */
public final class SmokeLive {
    public static void main(String[] args) {
        String base = System.getenv("RS3BUDDY_BASEURL");
        if (base == null || base.isEmpty()) {
            base = "http://127.0.0.1:4400";
        }
        RS3Buddy buddy = new RS3Buddy(base);
        System.out.println("baseUrl: " + buddy.baseUrl());
        System.out.println("status: " + buddy.getStatus());

        Position p = buddy.getPlayer();
        for (int i = 0; i < 8 && p == null; i++) {
            p = buddy.updatePlayer();
        }
        System.out.println("player: " + (p == null ? "null"
                : "tile(" + p.getTileX() + "," + p.getTileZ() + ")"));
    }
}
