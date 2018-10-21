(ns cljs-rte.test.utils.text-handler
  (:require [cljs.test :refer-macros [deftest is testing]]
            [cljs-rte.utils.text-handler :as thd]))

(deftest test-insert-new-text-character
  (testing "should insert one character on a single select"
    (let [text [{:text "abcdef"} {:text "gh jkl"}]
          position {:start-line 1 :start 2 :end-line 1 :end 2}
          character "i"]
      (is (= {:text [{:text "abcdef"} {:text "ghi jkl"}]
              :position {:start-line 1 :start 3 :end-line 1 :end 3}
              :line-lengths [6 7]}
             (thd/insert-new-text-character position text character)))))

  (testing "should insert one character on a multi select in single line"
    (let [text [{:text "abcdef"} {:text "gh jkl"}]
          position {:start-line 1 :start 2 :end-line 1 :end 7}
          character "i"]
      (is (= {:text [{:text "abcdef"} {:text "ghi"}]
              :position {:start-line 1 :start 3 :end-line 1 :end 3}
              :line-lengths [6 3]}
             (thd/insert-new-text-character position text character)))))

  (testing "should insert one character on a multi select in multi line"
    (let [text [{:text "abcdef"} {:text "gh jkl"}]
          position {:start-line 0 :start 2 :end-line 1 :end 6}
          character "i"]
      (is (= {:text [{:text "abi"}]
              :position {:start-line 0 :start 3 :end-line 0 :end 3}
              :line-lengths [3]}
             (thd/insert-new-text-character position text character)))))

  (testing "should insert one character on a multi select in multi line and append lines"
    (let [text [{:text "abcdef"} {:text "gh jkl"}]
          position {:start-line 0 :start 2 :end-line 1 :end 3}
          character "i"]
      (is (= {:text [{:text "abijkl"}]
              :position {:start-line 0 :start 3 :end-line 0 :end 3}
              :line-lengths [6]}
             (thd/insert-new-text-character position text character)))))

  (testing "should devour lines in between multi line select"
    (let [text [{:text "abcdef"} {:text "ghi"} {:text "jklmno"}]
          position {:start-line 0 :start 2 :end-line 2 :end 3}
          character "i"]
      (is (= {:text [{:text "abimno"}]
              :position {:start-line 0 :start 3 :end-line 0 :end 3}
              :line-lengths [6]}
             (thd/insert-new-text-character position text character)))))

  (testing "should maintain minimum 1 empty line"
    (let [text [{:text ""} {:text "def"}]
          position {:start-line 0 :start 0 :end-line 1 :end 3}
          character "i"]
      (is (= {:text [{:text "i"}]
              :position {:start-line 0 :start 1 :end-line 0 :end 1}
              :line-lengths [1]}
             (thd/insert-new-text-character position text character)))))

  (testing "should insert one character on first line when all values are nil"
    (let [text nil
          position nil
          character "i"]
      (is (= {:text [{:text "i"}]
              :position {:start-line 0 :start 1 :end-line 0 :end 1}
              :line-lengths [1]}
             (thd/insert-new-text-character position text character))))))