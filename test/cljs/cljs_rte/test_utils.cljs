(ns cljs-rte.test.test-utils)

(defn equals-json [expected-data actual-data]
  (= (pr-str expected-data)
     (pr-str actual-data)))
