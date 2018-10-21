(ns cljs-rte.utils.html-creator
  (:require [clojure.string :as str]))

(defn text-to-html [text position]
  (letfn [(span-position [line text position]
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
                :else  text)))]
    (apply str (map-indexed (fn [idx curr]
                              (str "<p>" (span-position idx (:text curr) position) "</p>")) text))))