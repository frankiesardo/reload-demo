(set-env!
 :source-paths    #{"src"}
 :resource-paths  #{"resources"}
 :dependencies '[[org.clojure/clojurescript "1.7.170"]
                 [reagent "0.6.0-alpha"]
                 [cljsjs/material "1.1.3-1"]

                 [org.clojure/test.check "0.8.2" :scope "test"]
                 [crisptrutski/boot-cljs-test "0.2.0-SNAPSHOT" :scope "test"]

                 [cljsjs/boot-cljsjs        "0.5.1"      :scope "test"]
                 [adzerk/boot-cljs          "1.7.48-6"   :scope "test"]
                 [adzerk/boot-cljs-repl     "0.2.0"      :scope "test"]
                 [adzerk/boot-reload        "0.4.1"      :scope "test"]
                 [pandeiro/boot-http        "0.6.3"      :scope "test"]
                 ])

(require
 '[cljsjs.boot-cljsjs    :refer [from-cljsjs]]
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]])

(deftask build []
  (comp (cljs)
        (sift :add-jar {'cljsjs/material #"^cljsjs/material/production/material.min.inc.css$"})))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (speak)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced}
                 from-cljsjs {:profile :production})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none}
                 reload {:on-jsload 'app.core/reload}
                 from-cljsjs {:profile :development})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))

;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(ns-unmap 'boot.user 'test)

(deftask testing []
  (set-env! :source-paths #(conj % "test"))
  (task-options! test-cljs {:js-env :phantom})
  identity)

(deftask test []
  (comp (testing)
        (test-cljs :exit?  true)))

(deftask auto-test []
  (comp (testing)
        (watch)
        (test-cljs)))
