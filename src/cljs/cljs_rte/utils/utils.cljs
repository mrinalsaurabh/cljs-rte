(ns cljs-rte.utils.utils
  (:require [cljs-rte.utils.math :as math]
            [cemerick.url :as url]
            [cljs-time.format :as f]
            [cljs.pprint :as pprint]))

(defn format-balance [balance]
  (let [rounded-balance (math/round balance)]
    (pprint/cl-format nil "~:d" rounded-balance)))

(defn format-movement [movement]
  (let [formatted-movement (format-balance (math/abs movement))]
    (if (pos? movement)
      formatted-movement
      (str "(" formatted-movement ")"))))

(defn get-selected [selection]
  (let [choice (get (:query (url/url (.. js/window
                                          -location
                                          -href))) selection)]
    choice))

(defn selected-account []
  (get-selected "sfdc-account-id"))

(defn selected-account-name []
  (get-selected "account"))

(defn selected-country []
  (get-selected "country"))

(defn selected-currency []
  (let [currency (get-selected "currency")]
    (if (or (nil? currency)
            (= currency ""))
      "USD"
      currency)))
