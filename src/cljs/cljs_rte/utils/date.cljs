(ns cljs-rte.utils.date
  (:require [cljs-time.core :as time]
            [cljs-time.format :as f]))

(defn parse [date-string]
  (f/parse (f/formatters :date) date-string))

(defn unparse-date [format date]
  (f/unparse (f/formatter format) date))

(defn format-date-with [string-date format]
  (let [formatted-date (parse string-date)]
    (unparse-date format formatted-date)))
