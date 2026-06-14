// Manual: node clients/ts/test/smoke.live.mjs  (requires a running server + rs3buddy.json or RS3BUDDY_BASEURL)
import { RS3Buddy } from "../dist/index.js";
const base = process.env.RS3BUDDY_BASEURL;
const buddy = new RS3Buddy(base ? { baseUrl: base } : {});
console.log("baseUrl:", buddy.baseUrl);
const status = await buddy.getStatus();
console.log("status.connected:", status?.connected);
let p = await buddy.getPlayer();
for (let i = 0; i < 8 && !p; i++) p = await buddy.updatePlayer();
console.log("player:", p);
