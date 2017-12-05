(ns cljs-rte-demo.demo
  (:require [reagent.core :as reagent]
            [cljs-rte.components.rte-box :as rte]
            [re-frame.core :refer [dispatch subscribe]]))


(defn page []
  [rte/rte-component])
