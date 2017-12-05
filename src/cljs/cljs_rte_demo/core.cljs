(ns cljs-rte-demo.core
  (:require [cljs-rte-demo.routes :as routes]
            [cljs-rte-demo.config :as config]))

(defn ^:export init []
  (routes/init!))
