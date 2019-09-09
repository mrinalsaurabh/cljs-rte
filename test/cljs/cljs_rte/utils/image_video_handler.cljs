(ns cljs-rte.test.utils.image-video-handler
  (:require [cljs.test :refer-macros [deftest is testing]]
            [cljs-rte.utils.image-video-handler :as ivhd]))

(deftest test-insert-image
  (testing "should insert new line on a pressing an enter when selection within single line"
    (let [text [{:text "abcdef"}]
          position {:start-line 0 :start 3 :end-line 0 :end 3}
          image-path "some/src"
          caption "some-caption"]
      (is (= {:text [{:text "abc"}
                     {:image image-path :caption caption :text "" :type :image}
                     {:text "def"}]
              :position {:start-line 2 :start 0 :end-line 2 :end 0}
              :line-lengths [3 0 3]}
             (ivhd/insert-image text position image-path caption)))))

  (testing "should insert new line character on a pressing an enter when selection in multiple line"
    (let [text [{:text "abcdef"} {:text "ghijkl"} {:text "mnoprq"}]
          position {:start-line 0 :start 3 :end-line 2 :end 3}
          image-path "some/src"
          caption "some-caption"]
      (is (= {:text [{:text "abc"} {:image image-path :caption caption :text ""  :type :image} {:text "prq"}]
              :position {:start-line 2 :start 0 :end-line 2 :end 0}
              :line-lengths [3 0 3]}
             (ivhd/insert-image text position image-path caption)))))

  (testing "should insert new line when nil and enter is pressed"
    (let [text nil
          position nil
          image-path "some/src"
          caption "some-caption"]
      (is (= {:text [{:image image-path :caption caption :text ""  :type :image}]
              :position {:start-line 1 :start 0 :end-line 1 :end 0}
              :line-lengths [0]}
             (ivhd/insert-image text position image-path caption))))))