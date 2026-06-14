--!nocheck
-- Package entry point: `require("rs3buddy")` resolves this file (Luau dir-module
-- convention). It re-exports the client module so the rest of the package can be
-- plain requirable siblings (require("./client"), require("./transport")).
-- --!nocheck: this is a trivial runtime re-export; luau-analyze's static resolver
-- doesn't follow the relative require here (the runtime does — see client_test).
return require("./client")
