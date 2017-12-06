(ns utils.string)

(defn rand-str [len]
    (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))
