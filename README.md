# reload demo

Sample application showing the use of boot and boot-reload with browser, ios and android simultaneously.

## Setup

Prerequisite: you must have cordova installed and the ios and android toolchain. And boot, obviously.

```bash
brew install boot-clj
brew install npm
npm install -g cordova
```


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

As you save changes in `src/app/core.cljs` should be pushed to the three platforms simultaneously.


### Repl

You can only connect to one platform at a time to eval in the repl.
So you need to only have e.g. iOS simulator running and no Android or browser app running.

- boot dev
- In Cursive: Run > Edit Configurations > Clojure Repl > Remote Repl > Host (localhost) Port (is the one displayed after boot started e.g 6544)
- At the repl prompt: `(start-repl) (require 'app.core) (in-ns 'app.core) @app-state`

### Example: take picture using camera

You need this additional cordova plugin:

```bash
cordova plugins add cordova-plugin-camera
```
Then in your repl you can execute:

```cljs
(defn take-picture! []
  (.getPicture js/navigator.camera
               (fn success [uri] (println "Image uri:" uri))
               (fn fail [msg] (println "Failure:" msg))
               #js {:quality 50}))
```

### Work with React Native

Check out this boot task: [boot-react-native](https://github.com/mjmeintjes/boot-react-native)