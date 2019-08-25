(ns cljs-rte.components.events.rte
  (:require [re-frame.core :refer [reg-event-db reg-event-fx dispatch subscribe]]
            [day8.re-frame.http-fx]
            [clojure.string :as str]
            [cljs-rte.utils.text-handler :as text-handler]
            [cljs-rte.utils.position-handler :as position-handler]
            [ajax.core :as ajax]))

(reg-event-db
 :rte-keypress
 (fn [db [_ content]]
   (assoc-in db [:rte :rte-content] "content")))


(reg-event-db
 :rte-keydown
 (fn [db [_ content]]
   (let [shift-key-pressed (get-in db [:rte :shift-pressed])
         current-position (if (nil? (get-in db [:rte :cursor-position]))
                            {:start-line 0 :start 0 :end 0 :end-line 0}
                            (get-in db [:rte :cursor-position]))
         line-lengths (get-in db [:rte :line-lengths])]
     (case content
       "Shift" (assoc-in db [:rte :shift-pressed] true)
       "ArrowRight" (assoc-in db [:rte :cursor-position]
                              (position-handler/update-position-on-right current-position line-lengths shift-key-pressed))
       "ArrowLeft" (assoc-in db [:rte :cursor-position]
                             (position-handler/update-position-on-left current-position line-lengths shift-key-pressed))
       "Backspace" db
       db))))

(reg-event-db
 :rte-keyup
 (fn [db [_ content]]
   (cond
     (= content "Shift") (assoc-in db [:rte :shift-pressed] false)
     (or (= (count content) 1) (some #{content} ["Enter" "Backspace"]))
     (let [position (get-in db [:rte :cursor-position])
           text (get-in db [:rte :rte-content])
           new-text-positions (text-handler/insert-new-text-character position text content)]
       (-> db
           (assoc-in [:rte :rte-content] (:text new-text-positions))
           (assoc-in [:rte :cursor-position] (:position new-text-positions))
           (assoc-in [:rte :line-lengths] (:line-lengths new-text-positions))))
     :else db)))