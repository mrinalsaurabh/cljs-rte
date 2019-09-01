(ns cljs-rte.components.rte-box
  (:require [reagent.core :as r]
            [clojure.string :as string]
            [cljsjs.d3]
            [cljs-rte.events]
            [cljs-rte.subs]
            [utils.string :as util-string]
            [cljs-rte.components.events.rte]
            [cljs-rte.components.subs.rte]
            [re-frame.core :refer [dispatch subscribe]]))

(defn rte-component []
  (let [rand-id (util-string/rand-str 5)
        content @(subscribe [:rte-content])
        html-content ()]
    [:div {:class "editor"}
     [:div {:class "font-panel"}
      [:input {:type "button" :class "text-bold" :value "B"}]
      [:input {:type "button" :class "text-italics" :value "I"}]
      [:input {:type "button"
               :class "insert-image"
               :value "In"
               :on-click (fn [e]
                           (dispatch [:rte-insert-image "/images/beautiful-blur-bright-326055.jpg" "image--"]))}]]
     [:div {:id rand-id
            :class "display-panel"
            :contentEditable "true"
            :on-key-down (fn [e]
                           (.preventDefault e)
                           (let [key (.-key e)]
                             (dispatch [:rte-keydown key])))
            :on-key-up (fn [e]
                         (.preventDefault e)
                         (let [key (.-key e)]
                           (dispatch [:rte-keyup key])))
            :dangerouslySetInnerHTML {:__html content}}]]))
