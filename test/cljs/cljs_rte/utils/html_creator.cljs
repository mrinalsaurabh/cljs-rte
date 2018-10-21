(ns cljs-rte.test.utils.html-creator
  (:require [cljs.test :refer-macros [deftest is testing]]
            [cljs-rte.utils.html-creator :as html-creator]))


(deftest test-text-to-html
  (testing "should create paragraph if nothing is there"
    (let [text [{:text "abc"} {:text "def"}]
          position {:start-line 0 :start 1 :end-line 0 :end 1}]
      (is (= "<p>a<span class='selected'></span>bc</p><p>def</p>" (html-creator/text-to-html text position)))))

  (testing "should cover text within single line selection"
    (let [text [{:text "abcdefghi"}]
          position {:start-line 0 :start 1 :end-line 0 :end 4}]
      (is (= "<p>a<span class='selected'>bcd</span>efghi</p>" (html-creator/text-to-html text position)))))

  (testing "should cover text within two line selection"
    (let [text [{:text "abcdefghi"} {:text "jklmno"}]
          position {:start-line 0 :start 1 :end-line 1 :end 4}]
      (is (= "<p>a<span class='selected'>bcdefghi</span></p><p><span class='selected'>jklm</span>no</p>"
             (html-creator/text-to-html text position)))))

  (testing "should cover text within multiple line selection"
    (let [text [{:text "abc"} {:text "def"} {:text "ghi"}]
          position {:start-line 0 :start 1 :end-line 2 :end 2}]
      (is (= (str "<p>a<span class='selected'>bc</span></p>"
                  "<p><span class='selected'>def</span></p>"
                  "<p><span class='selected'>gh</span>i</p>")
             (html-creator/text-to-html text position))))))

