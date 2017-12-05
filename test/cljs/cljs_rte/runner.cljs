(ns cljs-rte.test.runner
  (:require [cljs.test :as test]
            [doo.runner :refer-macros [doo-all-tests doo-tests]]
            [cljs-rte.test.acceptance.rte]
            [cljs-rte.test.utils.date]
            [cljs-rte.test.unit.rte-box.events]))

(doo-tests 'cljs-rte.test.acceptance.rte)

(test/run-tests 'cljs-rte.test.unit.rte-box.events
                'cljs-rte.test.utils.date)
