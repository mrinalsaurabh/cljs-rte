(ns cljs-rte.test.runner
  (:require [cljs.test :as test]
            [doo.runner :refer-macros [doo-all-tests doo-tests]]
            [cljs-rte.test.acceptance.rte]
            [cljs-rte.test.utils.date]
            [cljs-rte.test.unit.rte-box.events]
            [cljs-rte.test.utils.html-creator]
            [cljs-rte.test.utils.position-handler]
            [cljs-rte.test.utils.text-handler]))

(doo-tests 'cljs-rte.test.acceptance.rte)

(test/run-tests 'cljs-rte.test.unit.rte-box.events
                'cljs-rte.test.utils.date
                'cljs-rte.test.utils.html-creator
                'cljs-rte.test.utils.position-handler
                'cljs-rte.test.utils.text-handler)
