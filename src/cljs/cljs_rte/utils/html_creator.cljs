(ns cljs-rte.utils.html-creator
  (:require-macros [hiccups.core :as hiccups :refer [html]])
  (:require [hiccups.runtime :as hiccupsrt]
            [clojure.string :as str]))

(defn span-position [line text position]
  (let [start-line (:start-line position)
        start-position (:start position)
        end-line (:end-line position)
        end-position (:end position)]
    (cond
      (= start-line end-line line)
      (list [:span (subs text 0 start-position)]
            [:span {:class "selected"} (subs text start-position end-position)]
            [:span (subs text end-position)])
      (= start-line line)
      (list [:span (subs text 0 start-position)]
            [:span {:class "selected"} (subs text start-position)])
      (> end-line line) [:span {:class "selected"} text]
      (= end-line line)
      (list [:span {:class "selected"} (subs text 0 end-position)]
            [:span (subs text end-position)])
      :else text)))

(defmulti current-tag-renderer (fn [index position selected? current-content]
                                   (or (:type current-content) :text)))

(defmethod current-tag-renderer :image [index position selected? current-content]
  (if selected?
    [:figure {:class "selected"}
     [:img {:src (:image current-content) :alt (:caption current-content) :width ""}]
     [:figcaption (:caption current-content)]]
    [:figure
     [:img {:src (:image current-content) :alt (:caption current-content) :width ""}]
     [:figcaption (:caption current-content)]]))

(defmethod current-tag-renderer :text [index position selected? current-content]
  (if selected?
    [:p (span-position index (:text current-content) position)]
    [:p (:text current-content)]))

(defn text-to-html [text position]
  (let [selected-lines (range (or (:start-line position) 0)
                              (inc (or (:end-line position) 0)))]
    (apply str (map-indexed (fn [idx curr]
                              (if (some #{idx} selected-lines)
                                (html (current-tag-renderer idx position true curr))
                                (html (current-tag-renderer idx position false curr)))) text))))