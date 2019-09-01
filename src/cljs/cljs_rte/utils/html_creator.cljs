(ns cljs-rte.utils.html-creator
  (:require [clojure.string :as str]))

(defn span-position [line text position]
            (let [start-line (:start-line position)
                  start-position (:start position)
                  end-line (:end-line position)
                  end-position (:end position)]
              (cond
                (= start-line end-line line)
                (str (subs text 0 start-position)
                     "<span class='selected'>"
                     (subs text start-position end-position)
                     "</span>"
                     (subs text end-position))
                (= start-line line)
                (str (subs text 0 start-position)
                     "<span class='selected'>"
                     (subs text start-position)
                     "</span>")
                (> end-line line) (str "<span class='selected'>" text "</span>")
                (= end-line line)
                (str "<span class='selected'>"
                     (subs text 0 end-position)
                     "</span>"
                     (subs text end-position))
                :else  text)))

(defmulti current-tag-renderer (fn [index position selected? current-content]
                                 (cond
                                   (not (nil? (:image current-content))) :image
                                   :else :text)))

(defmethod current-tag-renderer :image [index position selected? current-content]
  (if selected?
    (str "<p>"
         "<span class='selected'>"
         "<img src='" (:image current-content) "' alt='" (:caption current-content) "'>"
         "</span>"
         "</p>")
    (str "<p>"
         "<img src='" (:image current-content) "' alt='" (:caption current-content) "'>"
         "</p>")))

(defmethod current-tag-renderer :text [index position selected? current-content]
  (if selected?
    (str "<p>" (span-position index (:text current-content) position) "</p>")
    (str "<p>" (:text current-content) "</p>")))

(defn text-to-html [text position]
  (let [selected-lines (range (or (:start-line position) 0)
                              (inc (or (:end-line position) 0)))]
    (apply str (map-indexed (fn [idx curr]
                              (if (some #{idx} selected-lines)
                                (current-tag-renderer idx position true curr)
                                (current-tag-renderer idx position false curr))) text))))