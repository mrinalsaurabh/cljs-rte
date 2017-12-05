(ns cljs-rte.utils.math)

(defn round [number]
  (let [number-as-integer (long number)
        remainder (- number number-as-integer)]
    (if (>= remainder 0.5)
      (inc number-as-integer)
      number-as-integer)))

(defn abs [number]
  (if (pos? number)
    number
    (* number -1)))
