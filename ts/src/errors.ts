/** Server returned a non-2xx HTTP status. */
export class RS3BuddyError extends Error {
  constructor(
    readonly status: number,
    message: string,
    readonly body?: unknown,
  ) {
    super(message);
    this.name = "RS3BuddyError";
  }
}

/** Could not reach the server (down, wrong port, or game crashed). */
export class RS3BuddyConnectionError extends Error {
  constructor(message: string, readonly cause?: unknown) {
    super(message);
    this.name = "RS3BuddyConnectionError";
  }
}
