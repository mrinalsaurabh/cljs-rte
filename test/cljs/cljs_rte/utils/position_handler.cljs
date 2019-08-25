(ns cljs-rte.test.utils.position-handler
  (:require [cljs.test :refer-macros [deftest is testing]]
            [cljs-rte.utils.position-handler :as pos]))

(deftest test-update-position-on-left-when-shift-pressed
  (testing "update position when inverted"
    (let [current-position {:start-line 0 :start 5 :end-line 0  :end 5  :inverted true}
          line-lengths [7]]
      (is (= {:start-line 0 :start 4 :end 5 :end-line 0 :inverted true}
             (pos/update-position-on-left current-position line-lengths true)))))

  (testing "update position when inverted not clear"
    (let [current-position {:start-line 0 :start 5 :end-line 0 :end 5}
          line-lengths [7]]
      (is (= {:start-line 0 :start 4 :end 5 :end-line 0 :inverted true}
             (pos/update-position-on-left current-position line-lengths true)))))

  (testing "update position when end just greater than start"
    (let [current-position {:start-line 0 :start 5 :end 6 :end-line 0}
          line-lengths [7]]
      (is (= {:start-line 0 :start 5 :end 5 :end-line 0 :inverted true}
             (pos/update-position-on-left current-position line-lengths true)))))

  (testing "update position when end is much greater than start"
    (let [current-position {:start-line 0 :start 5 :end 7 :end-line 0}
          line-lengths [7]]
      (is (= {:start 5 :start-line 0 :end 6 :end-line 0 :inverted false}
             (pos/update-position-on-left current-position line-lengths true)))))

  (testing "update position when inverted and start is 0 on zeroth line"
    (let [current-position {:start-line 0 :start 0 :end 7 :end-line 0 :inverted true}
          line-lengths [7]]
      (is (= {:start-line 0 :start 0 :end 7 :end-line 0 :inverted true}
             (pos/update-position-on-left current-position line-lengths true)))))

  (testing "update position when inverted and start is 0 on non zeroth line"
    (let [current-position {:start-line 1 :start 0 :end 5 :end-line 1 :inverted true}
          line-lengths [5 6]]
      (is (= {:start-line 0 :start 5 :end 5 :end-line 1 :inverted true}
             (pos/update-position-on-left current-position line-lengths true))))))

(deftest test-update-position-on-left-when-shift-not-pressed
  (testing "update position when inverted"
    (let [current-position {:start-line 0 :start 3 :end-line 0 :end 5 :inverted true}
          line-lengths [7]]
      (is (= {:start-line 0 :start 2 :end 2 :end-line 0 :inverted true}
             (pos/update-position-on-left current-position line-lengths false)))))

  (testing "update position when Arrow goes to above line"
    (let [current-position {:start-line 1 :start 0 :end-line 1 :end 0}
          line-lengths [7 7]]
      (is (= {:start-line 0 :start 7 :end 7 :end-line 0}
             (pos/update-position-on-left current-position line-lengths false))))))

(deftest test-update-position-on-right-when-shift-pressed
  (testing "update position when inverted"
    (let [current-position {:start-line 0 :start 5 :end-line 0 :end 5 :inverted true}
          line-lengths [7]]
      (is (= {:start-line 0 :start 5 :end-line 0 :end 6 :inverted false}
             (pos/update-position-on-right current-position line-lenghts true)))))

  (testing "update position when end just greater than start"
    (let [current-position {:start-line 0 :start 5 :end 6 :end-line 0}
          line-lengths [7]]
      (is (= {:start-line 0 :start 5 :end 7 :end-line 0}
             (pos/update-position-on-right current-position line-lenghts true)))))

  (testing "update position when inverted and start much less than end"
    (let [current-position {:start-line 0 :start 5 :end 7 :end-line 0 :inverted true}
          line-lengths [7]]
      (is (= {:start-line 0 :start 6 :end 7 :end-line 0 :inverted true}
             (pos/update-position-on-right current-position line-lenghts true)))))

  (testing "update position when end is end of a line"
    (let [current-position {:start-line 0 :start 5 :end 7 :end-line 0}
          line-lenghts [7 5]]
      (is (= {:start-line 0 :start 5 :end 0 :end-line 1}
             (pos/update-position-on-right current-position line-lenghts true))))))

(deftest test-update-position-on-right-when-shift-not-pressed
  (testing "update position when end of line for last line"
    (let [current-position {:start-line 0 :start 3 :end-line 0 :end 5}
          line-lengths [5]]
      (is (= {:start-line 0 :start 5 :end 5 :end-line 0}
             (pos/update-position-on-right current-position line-lengths false)))))

  (testing "update position when inverted"
    (let [current-position {:start-line 0 :start 3 :end-line 0 :end 5 :inverted true}
          line-lengths [6]]
      (is (= {:start-line 0 :start 4 :end 4 :end-line 0}
             (pos/update-position-on-right current-position line-lengths false)))))

  (testing "update position when end of line not for last line"
    (let [current-position {:start-line 0 :start 3 :end-line 0 :end 5}
          line-lengths [5 6]]
      (is (= {:start-line 1 :start 0 :end 0 :end-line 1}
             (pos/update-position-on-right current-position line-lengths false))))))
