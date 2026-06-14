# rs3buddy clients

Official client libraries for **rs3buddy** in four languages. Each client is a
thin, typed wrapper over the local rs3buddy HTTP server: you `connect()` to the
running server and call simple methods like `chat.read()` to read live game data.

These packages are generated from a private SDK and published here as a clean,
ready-to-use distribution. There is no build step for consumers — install the
package for your language and go.

## Languages

| Language       | Package                          | Install |
|----------------|----------------------------------|---------|
| TypeScript     | `@rs3buddy/client`               | `npm install @rs3buddy/client` |
| Python         | `rs3buddy`                       | `pip install rs3buddy` |
| Lua / Luau     | `rs3buddy`                       | `luarocks install luasocket` (then vendor the `rs3buddy/` package) |
| Java           | `com.rs3buddy:rs3buddy-client`   | Gradle: `implementation "com.rs3buddy:rs3buddy-client:0.1.0"` |

## How it works

Start the rs3buddy launcher (it brings up the local server). Then `connect()` —
with no URL the client auto-discovers the running server — and read the chatbox:

```ts
// TypeScript
import { RS3Buddy } from "@rs3buddy/client";
const buddy = RS3Buddy.connect();
const chat = await buddy.chat.read();
console.log(`${chat.lineCount} chat lines`);
```

```python
# Python
from rs3buddy import RS3Buddy
buddy = RS3Buddy.connect()
chat = buddy.chat.read()
print(chat["lineCount"], "chat lines")
```

```lua
-- Lua / Luau
local rs3buddy = require("rs3buddy")
local buddy = rs3buddy.connect()
local chat = buddy.chat:read()
print(chat.lineCount .. " chat lines")
```

```java
// Java
RS3Buddy buddy = RS3Buddy.connect();
ChatReadResult chat = buddy.chat.read();
System.out.println(chat.getLineCount() + " chat lines");
```

The shared behavioral contract every client implements lives in
[`CONTRACT.md`](./CONTRACT.md). Per-language usage and setup live in each
package's own `README.md`.

---

_Generated distribution — version 0.1.0._
