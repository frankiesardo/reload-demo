# reload demo

Sample application showing the use of boot and boot-reload with browser, ios and android simultaneously.

## Setup

Prerequisite: you must have cordova installed and the ios and android toolchain. And boot, obviously.

### Bootstrap

Run the following in one terminal

```bash
cordova plugins add cordova-plugin-whitelist
cordova platforms add browser
cordova platforms add ios
cordova platforms add android
boot dev
```

Then, in another terminal:

```bash
cordova prepare
cordova run ios
cordova run android
cordova run browser
```

Cordova will by default open up a Chrome tab, an iOS simulator and an Android emulator.

Changes in `src/app/core.cljs` should be pushed to the three platforms simultaneously.


### Repl

You can only connect to one platform at a time to eval in the repl

- boot dev
- In Cursive: Run > Edit Configurations > Clojure Repl > Port is the one displayed after boot started
- At the repl prompt: (start-repl) (require 'app.core) (in-ns 'app.core) @app-state