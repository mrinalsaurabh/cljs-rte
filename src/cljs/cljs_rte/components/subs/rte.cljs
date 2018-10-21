(ns cljs-rte.components.subs.rte
  (:require [re-frame.core :refer [reg-sub]]
            [cljs-rte.utils.html-creator :as html-creator]))

(reg-sub
 :rte-content
 (fn [db _]
   (let [text (get-in db [:rte :rte-content])
         position (get-in db [:rte :cursor-position])]
     (html-creator/text-to-html text position))))
