(ns cljs-rte-demo.routes
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [re-frame.core :refer [dispatch]]
            [goog.history.EventType :as EventType]
            [cemerick.url :as url]
            [cljs-rte-demo.demo :as demo])

  (:import goog.history.Html5History
           goog.Uri))


;; -------------------------
;; Initialize view
(defn init! []
  (try
    (reagent/render-component [demo/page] (.getElementById js/document "app"))
    (catch js/Error e)))

;; -------------------------
;; History
(defn hook-browser-navigation! []
  (let [history (doto (Html5History.)
                  (events/listen
                   EventType/NAVIGATE
                   (fn [event]
                     (secretary/dispatch! (.-token event))))
                  (.setUseFragment false)
                  (.setPathPrefix "")
                  (.setEnabled true))]
    (events/listen js/document "click"
                   (fn [e]
                     (let [path (.getPath (.parse Uri (.-href (.-target e))))
                           title (.-title (.-target e))]
                       (when (secretary/locate-route path)
                         (. e preventDefault)
                         (. history (setToken path title))))))))

;; need to run this after routes have been defined
(hook-browser-navigation!)
