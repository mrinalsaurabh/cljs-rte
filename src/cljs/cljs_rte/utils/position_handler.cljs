(ns cljs-rte.utils.position-handler)

(defmulti update-position-on-write-conditionally (fn [position line-lengths character]
                                                   (some #{character} ["Backspace" "Enter"])))

(defmethod update-position-on-write-conditionally :default [position]
  (let [start-line (or (:start-line position) 0)
        start-position (or (:start position) 0)
        new-start-position (inc start-position)]
    {:start new-start-position
     :start-line start-line
     :end new-start-position
     :end-line start-line}))

(defmethod update-position-on-write-conditionally "Enter" [position]
  (let [start-line (if (:start-line position) (inc (:start-line position)) 0)
        cursor-position 0]
    {:start cursor-position :start-line start-line :end cursor-position :end-line start-line}))

(defmethod update-position-on-write-conditionally "Backspace" [position line-lengths]
  (let [start-line (or (:start-line position) 0)
        start (or (:start position) 0)
        end-line (or (:end-line position) 0)
        end (or (:end position) 0)]
        (cond
          (and (= 0 start end start-line end-line))
          position
          (and (= start end 0) (= start-line end-line))
          (let [new-line (max (dec start-line) 0)
                new-start (nth line-lengths new-line)]
            {:start new-start :start-line new-line :end new-start :end-line new-line})
          (and (= start end) (= start-line end-line))
          (let [new-start (dec start)]
            {:start new-start :start-line start-line :end new-start :end-line start-line})
          :else (merge position {:end start :end-line start-line}))))

(defmulti update-position-on-right (fn [current-position line-lenghts shift-key-pressed]
                                     [shift-key-pressed (= true (:inverted current-position))]))

(defmethod update-position-on-right [true false] [current-position line-lenghts]
  (let [end (:end current-position)
        end-line (:end-line current-position)
        to-next-line? (and (> (inc end) (nth line-lenghts end-line)) (< end-line (dec (count line-lenghts))))
        new-end (if to-next-line? 0 (min (nth line-lenghts end-line) (inc end)))
        new-end-line (if to-next-line? (inc end-line) end-line)]
    (assoc current-position :end new-end :end-line new-end-line)))

(defmethod update-position-on-right [true true] [current-position line-lenghts]
  (let [start (:start current-position)
        end (:end current-position)]
    (if (= start end)
      (merge current-position {:start start :end (inc start) :inverted false})
      (merge current-position {:start (inc start) :end end :inverted true}))))

(defmethod update-position-on-right [false true] [current-position line-lenghts]
  (let [end (:start current-position)
        end-line (:start-line current-position)
        to-next-line? (and (> (inc end) (nth line-lenghts end-line)) (< end-line (dec (count line-lenghts))))
        new-end (if to-next-line? 0 (min (nth line-lenghts end-line) (inc end)))
        new-end-line (if to-next-line? (inc end-line) end-line)]
    {:start new-end :start-line new-end-line :end new-end :end-line new-end-line}))

(defmethod update-position-on-right :default [current-position line-lenghts]
  (let [end (:end current-position)
        end-line (:end-line current-position)
        to-next-line? (and (> (inc end) (nth line-lenghts end-line)) (< end-line (dec (count line-lenghts))))
        new-end (if to-next-line? 0 (min (nth line-lenghts end-line) (inc end)))
        new-end-line (if to-next-line? (inc end-line) end-line)]
    {:start new-end :start-line new-end-line :end new-end :end-line new-end-line}))


(defmulti update-position-on-left (fn [current-position line-lenghts shift-key-pressed]
                                    [shift-key-pressed (or (:inverted current-position)
                                                           (= (:start current-position)
                                                              (:end current-position)))]))

(defmethod update-position-on-left [true false] [current-position line-lenghts]
  (let [start (:start current-position)
        end (:end current-position)
        new-end (dec end)]
    (if (= start new-end)
      (merge current-position {:start start :end new-end :inverted true})
      (merge current-position {:start start :end new-end :inverted false}))))


(defmethod update-position-on-left [true true] [current-position line-lenghts]
  (let [start (:start current-position)
        start-line (:start-line current-position)
        to-prev-line? (and (< (dec start) 0) (> start-line 0))
        new-start (if to-prev-line? (nth line-lenghts (dec start-line)) (max 0 (dec start)))
        new-start-line (if to-prev-line? (dec start-line) start-line)]
    (assoc current-position :start new-start :start-line new-start-line :inverted true)))

(defmethod update-position-on-left [false true] [current-position line-lenghts]
  (let [start (:start current-position)
        start-line (:start-line current-position)
        to-prev-line? (and (< (dec start) 0) (> start-line 0))
        new-start (if to-prev-line? (nth line-lenghts (dec start-line)) (max 0 (dec start)))
        new-start-line (if to-prev-line? (dec start-line) start-line)]
    (merge current-position
           {:end-line new-start-line :start new-start :end new-start :start-line new-start-line})))

(defmethod update-position-on-left :default [current-position line-lenghts]
  (let [end (:end current-position)
        new-end (max (dec end) 0)]
    (assoc current-position :start new-end :end new-end)))
