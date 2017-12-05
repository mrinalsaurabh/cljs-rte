(ns ^:figwheel-no-load cljs-rte.dev
  (:require [cljs-rte-demo.core :as core]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(figwheel/watch-and-reload
 :websocket-url "ws://localhost:3449/figwheel-ws"
 :hawk-options {:watcher :polling}
 :jsload-callback core/init)
