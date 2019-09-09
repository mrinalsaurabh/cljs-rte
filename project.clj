(def version (if-let [version (System/getenv "GO_PIPELINE_COUNTER")]
               version
               "0.1.0-SNAPSHOT"))

(defproject cljs-rte version
  :dependencies [[cljs-ajax "0.5.8"]
                 [com.cemerick/url "0.1.1"]
                 [day8.re-frame/http-fx "0.1.3"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async "0.2.391"]
                 [cljsjs/d3 "4.3.0-5"]
                 [reagent "0.6.0"]
                 [reagent-utils "0.2.0"]
                 [re-frame "0.9.4"]
                 [re-com "2.0.0"]
                 [secretary "1.2.3"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-sassy "1.0.8"]
            [cljs-simple-cache-buster "0.2.1"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/cljs" "src/cljc"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :sass {:src "sass"
         :dst "resources/public/css/"}

  ;; Exclude the demo and compiled files from the output of either 'lein jar' or 'lein install'
  :jar-exclusions   [#"(?:^|\/)cljs_rte_demo\/" #"(?:^|\/)demo\/" #"(?:^|\/)compiled.*\/" #"html$"]

  :cljs-simple-cache-buster {:cljsbuild-id ["min" "dev" "test"]
                             :fingerprint ~version
                             :template-file ["resources/public/index.html"
                                             "sass/_general.scss"
                                             "sass/_rte.scss"
                                             "sass/_variables.scss"]
                             :output-to ["resources/public/index.html"
                                         "sass/_general.scss"
                                         "sass/_rte.scss"
                                         "sass/_variables.scss"]}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.8.2"]
                   [figwheel-sidecar "0.5.9"]
                   [com.cemerick/piggieback "0.2.1"]]

    :plugins      [[lein-figwheel "0.5.9"]
                   [lein-doo "0.1.7"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs" "src/cljc" "env"]
     :figwheel     {:on-jsload "cljs-rte-demo.core/mount-root"}
     :compiler     {:main                 cljs-rte.dev
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}}}

    {:id           "min"
     :source-paths ["src/cljs" "src/cljc"]
     :compiler     {:output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "src/cljc" "test/cljs"]
     :compiler     {:main          cljs-rte.test.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :foreign-libs [{:file "d3/d3.js"
                                    :provides ["d3"]}]
                    :output-dir   "resources/public/js/compiled/test/out"
                    :optimizations :none}}]}

  :doo {:build "test"
        :alias {:default [:phantom]
                :browsers [:phantom]
                :all [:browsers :headless]}})