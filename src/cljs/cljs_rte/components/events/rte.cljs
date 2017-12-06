(ns cljs-rte.components.events.rte
  (:require [re-frame.core :refer [reg-event-db reg-event-fx dispatch subscribe]]
            [day8.re-frame.http-fx]
            [clojure.string :as str]
            [ajax.core :as ajax]))

(reg-event-db
  :rte-keypress 
  (fn [db [_ key]]
    (let [current-content (get-in db [:rte :rte-content])]
    (assoc-in db [:rte :rte-content] (str current-content "<p>" key "</p>")))))