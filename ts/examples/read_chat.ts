/**
 * Example: read the RS3 chatbox from TypeScript / JavaScript.
 *
 * Prerequisite: the rs3buddy launcher must be running — it injects the native
 * hook into the RS3 client, which starts the SDK server and writes the config
 * that connect() auto-discovers. With nothing running, chat.read() rejects with
 * RS3BuddyConnectionError.
 *
 * Run (after `npm install @rs3buddy/client`):
 *   npx ts-node examples/read_chat.ts      // or compile, then `node`
 */
import {
  RS3Buddy,
  RS3BuddyConnectionError,
  type ChatReadResult,
} from "@rs3buddy/client";

async function main(): Promise<void> {
  // No URL needed: connect() auto-discovers the server from the launcher's
  // config (%APPDATA%\rs3buddy\rs3buddy.json). A cold first read does a frame
  // capture, so allow a generous timeout.
  const buddy = RS3Buddy.connect({ timeoutMs: 15000 });

  const chat: ChatReadResult | null = await buddy.chat.read().catch((e: unknown) => {
    if (e instanceof RS3BuddyConnectionError) {
      console.error("Could not reach the SDK server — is the launcher running?");
      console.error(" ", e.message);
      return null;
    }
    throw e;
  });
  if (chat === null) return;

  console.log(`${chat.lineCount} chat lines:`);
  for (const line of chat.lines) {
    // line.color is the line's dominant colour.
    console.log(" ", line.color, JSON.stringify(line.text));
  }
}

void main();
