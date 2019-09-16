(ns cljs-rte.test.utils.html-creator
  (:require-macros [hiccups.core :as hiccups :refer [html]])
  (:require [cljs.test :refer-macros [deftest is testing]]
            [cljs-rte.utils.html-creator :as html-creator]))


(deftest test-text-to-html
  (testing "should create paragraph if nothing is there"
    (let [text [{:text "abc"} {:text "def"}]
          position {:start-line 0 :start 1 :end-line 0 :end 1}]
      (is (= (html [:p [:span "a"] [:span {:class "selected"}] [:span "bc"]]
                   [:p "def"])
             (html-creator/text-to-html text position)))))

  (testing "should cover text within single line selection"
    (let [text [{:text "abcdefghi"}]
          position {:start-line 0 :start 1 :end-line 0 :end 4}]
      (is (= (html [:p [:span "a"] [:span {:class "selected"} "bcd"] [:span "efghi"]])
             (html-creator/text-to-html text position)))))

  (testing "should cover text within two line selection"
    (let [text [{:text "abcdefghi"} {:text "jklmno"}]
          position {:start-line 0 :start 1 :end-line 1 :end 4}]
      (is (= (html [:p [:span "a"] [:span {:class "selected"} "bcdefghi"]]
                   [:p [:span {:class "selected"} "jklm"] [:span "no"]])
             (html-creator/text-to-html text position)))))

  (testing "should cover text within multiple line selection"
    (let [text [{:text "abc"} {:text "def"} {:text "ghi"}]
          position {:start-line 0 :start 1 :end-line 2 :end 2}]
      (is (= (html [:p [:span "a"] [:span {:class "selected"} "bc"]]
                   [:p [:span {:class "selected"} "def"]]
                   [:p [:span {:class "selected"} "gh"] [:span "i"]])
             (html-creator/text-to-html text position)))))

  (testing "should not select multiple lines for new line selection"
    (let [text [{:text "abc"} {:text "def"}]
          position {:start-line 1 :start 0 :end-line 1 :end 0}]
      (is (= (html [:p "abc"]
                   [:p [:span] [:span {:class "selected"}] [:span "def"]])
             (html-creator/text-to-html text position))))))

(deftest test-image-to-html
  (testing "should render image with img tag when selected"
    (let [content [{:image "images/image-1.jpg" :caption "test-image"  :type :image}]
          position {:start-line 0 :start 0 :end-line 0 :end 0}]
      (is (= (html [:figure {:class "selected"}
                    [:img {:src "images/image-1.jpg" :alt "test-image" :width ""}]
                    [:figcaption "test-image"]])
             (html-creator/text-to-html content position)))))

  (testing "should render image with img tag"
    (let [content [{:image "images/image-1.jpg" :caption "test-image"  :type :image} {:text "Next line"}]
          position {:start-line 1 :start 0 :end-line 1 :end 0}]
      (is (= (html [:figure
                    [:img {:src "images/image-1.jpg" :alt "test-image" :width ""}]
                    [:figcaption "test-image"]]
                   [:p [:span] [:span {:class "selected"}] [:span "Next line"]])
             (html-creator/text-to-html content position))))))

