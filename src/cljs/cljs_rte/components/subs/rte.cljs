(ns cljs-rte.components.subs.rte
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :rte-content
  (fn [db _]
    (get-in db [:rte :rte-content])))
