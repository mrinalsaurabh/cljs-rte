(ns cljs-rte.test.utils.date
  (:require [cljs.test :refer-macros [deftest is testing]]
            [cljs-rte.utils.date :as date-utils]))

(deftest test-date-utils
  (testing "should format date into day, Month format"
    (is (= (date-utils/format-date-with "2017-10-11" "dd MMM") "11 Oct"))
    (is (= (date-utils/format-date-with "2017-10-11" "dd MMMM") "11 October"))
    (is (= (date-utils/format-date-with "2017-10-11" "yyyy/MMM/dd") "2017/Oct/11"))))
