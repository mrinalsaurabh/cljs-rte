(ns cljs-rte.test.acceptance.rte
  (:require [cljs.test :refer-macros [deftest is testing run-tests use-fixtures]]
            [re-frame.core :refer [dispatch-sync dispatch subscribe]]
            [cljs-rte.components.rte-box :as rte]
            [cljs-rte.test.common :as common]))

