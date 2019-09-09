(ns cljs-rte.utils.image-video-handler
  (:require [cljs-rte.utils.text-handler :as thd]
            [cljs-rte.utils.position-handler :as phd]))

(defn insert-image [text position image-path caption]
  (let [new-reality (thd/insert-new-text-character position text "Enter")
        image-element {:image image-path :caption caption :text "" :type :image}
        new-text (:text new-reality)
        new-position (:position new-reality)
        current-line (:start-line new-position)
        line-lengths (:line-lengths new-reality)
        current-line-length (nth line-lengths current-line)
        text-with-image (if (= 0 current-line-length)
                          (assoc-in new-text [current-line] image-element)
                          (into [] (concat (take current-line new-text) [image-element] (nthrest new-text current-line))))]
    {:text text-with-image
     :position (phd/update-position-on-write-conditionally new-position [] "Enter")
     :line-lengths (map #(count (:text %)) text-with-image)}))