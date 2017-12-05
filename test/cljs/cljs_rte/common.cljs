(ns cljs-rte.test.common
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.test :refer-macros [async deftest is testing]]
            [reagent.core :as reagent :refer [atom]]
            [cljs.core.async :as async :refer [chan put! <!]]))

(def isClient (not (nil? (try (.-document js/window)
                              (catch js/Object e nil)))))

(defn add-test-div []
  (let [doc     js/document
        body    (.-body js/document)
        div     (.createElement doc "div")]
    (.appendChild body div)
    div))

(defn with-mounted-component [component function]
  (when isClient
    (let [div (add-test-div)]
      (let [component (reagent/render-component component div #(function component div))]
        (reagent/unmount-component-at-node div)
        (reagent/flush)
        (.removeChild (.-body js/document) div)))))

(defn found-in
  ([content element print?]
   (let [result (.-innerHTML element)]
     (if (re-find content result)
       true
       (do (when print?
             (println "Not found: " result))
           false))))
  ([content element]
   (found-in content element false)))

(defn set-url-params [params]
  (aset js/location "location" params))
