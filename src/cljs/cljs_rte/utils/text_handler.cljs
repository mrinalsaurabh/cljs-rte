(ns cljs-rte.utils.text-handler
  (:require [clojure.string :as str]
            [cljs-rte.utils.position-handler :as ph]))

(defmulti update-line-text-conditionally (fn [text position current character condition]
                                           [condition
                                            (or (some #{character} ["Backspace" "Enter"]) :default)]))

(defmethod update-line-text-conditionally :default [text position current character]
  [{:text character}])

(defmethod update-line-text-conditionally [:new-box "Enter"] [text position current character]
  [{:text ""}])

(defmethod update-line-text-conditionally [:equal-start-end-current :default] [text position current character]
  (update-in text [current :text]
             (fn [line-text]
               (let [start-position (or (:start position) 0)
                     end-position (or (:end position) 0)
                     line-text (or line-text "")
                     new-text-value (str (subs line-text 0 start-position)
                                         character
                                         (subs line-text end-position))]
                 (when-not (empty? new-text-value) new-text-value)))))

(defmethod update-line-text-conditionally [:equal-start-end-current "Enter"] [text position current character]
  (let [start-position (or (:start position) 0)
        end-position (or (:end position) 0)
        line-text (or (get-in text [current :text]) "")
        this-line-text (subs line-text 0 start-position)
        next-line-text (subs line-text end-position)
        full-text (update-in text [current :text]
                             (constantly this-line-text))]
    (assoc full-text (inc current) {:text next-line-text})))

(defmethod update-line-text-conditionally [:equal-start-current :default] [text position current character]
  (update-in text [current :text]
             (fn [line-text]
               (let [start-position (or (:start position) 0)
                     new-text-value (str (subs line-text 0 start-position)
                                         character)]
                 (when-not (empty? new-text-value) new-text-value)))))

(defmethod update-line-text-conditionally [:equal-start-current "Enter"] [text position current character]
  (update-in text [current :text]
             (fn [line-text]
               (let [start-position (or (:start position) 0)
                     new-text-value (subs line-text 0 start-position)]
                 (when-not (empty? new-text-value) new-text-value)))))

(defmethod update-line-text-conditionally [:end-gt-current :default] [text position current character]
  (update-in text [current :text] (constantly nil)))

(defmethod update-line-text-conditionally [:equal-end-current :default] [text position current character]
  (let [start-line (or (:start-line position) 0)
        end-position (or (:end position) 0)
        current-line-value (:text (nth text current))
        first-line-value (:text (nth text start-line))
        current-line-new-value (str first-line-value (subs current-line-value end-position))
        temp-texts (update-in text [current] (constantly nil))]
    (update-in temp-texts [start-line :text] (constantly current-line-new-value))))

(defmethod update-line-text-conditionally [:equal-end-current "Enter"] [text position current character]
  (update-in text [current :text]
             (fn [line-text]
               (let [end-position (or (:end position) 0)
                     current-line-new-value (subs line-text end-position)]
                 current-line-new-value))))

(defn insert-new-text-character [position text character]
  (let [start-line (or (:start-line position) 0)
        end-line (or (:end-line position) 0)
        affected-lines (range start-line (inc end-line))
        new-text (reduce
                  (fn [final current]
                    (cond
                      (= nil text)
                      (update-line-text-conditionally final position current character :new-box)

                      (= start-line end-line current)
                      (update-line-text-conditionally final position current character :equal-start-end-current)

                      (= start-line current)
                      (update-line-text-conditionally final position current character :equal-start-current)

                      (> end-line current)
                      (update-line-text-conditionally final position current character :end-gt-current)

                      :else
                      (update-line-text-conditionally final position current character :equal-end-current)))
                  text affected-lines)
        filtered-new-text (remove #(nil? (:text %)) new-text)]
    {:text (vec filtered-new-text)
     :position (ph/update-position-on-write-conditionally position character)
     :line-lengths (map #(count (:text %)) filtered-new-text)}))