(def +version+ "0.1.0")

(set-env!
 :source-paths    #{"src"}
 :resource-paths  #{"resources"}
 :target-path     "www"
 :dependencies
 '[[org.clojure/clojurescript "1.7.170"]
   [reagent "0.6.0-alpha"]
   [cljsjs/material "1.1.3-1"]

   [org.clojure/test.check "0.8.2" :scope "test"]
   [crisptrutski/boot-cljs-test "0.2.0-SNAPSHOT" :scope "test"]

   [adzerk/boot-cljs          "1.7.48-6"   :scope "test"]
   [adzerk/boot-cljs-repl     "0.2.0"      :scope "test"]
   [adzerk/boot-reload        "0.4.3-SNAPSHOT"      :scope "test"]
   [pandeiro/boot-http        "0.7.0"      :scope "test"]
   [crisptrutski/boot-cljs-test "0.2.0-SNAPSHOT" :scope "test"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]])

(def host (.getHostAddress (java.net.Inet4Address/getLocalHost)))

(task-options!
 serve  {:dir "/"
         :port 3000}
 cljs-repl {:ws-host host
            :ip host}
 reload {:ws-host host
         :asset-host (str "http://" host ":3000")})

(deftask build []
  (comp (cljs)))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (speak)
        (build)))

(deftask production []
  (task-options! cljs   {:optimizations :advanced})
  identity)

(deftask development []
  #_(set-env! :source-paths #(into % #{"test"})
              :resource-paths #(into % #{"devcards"}))
  (task-options! cljs   {:optimizations :none :source-map true}
                 reload {:on-jsload 'app.core/reload})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)
        #_(test-cljs)))

;; Tests

(ns-unmap 'boot.user 'test)

(deftask testing []
  (set-env! :source-paths #(into % #{"test"}))
  identity)

(task-options!
 test-cljs {:js-env :phantom})

(deftask test []
  (comp (testing)
        (test-cljs :exit?  true)))

(deftask autotest []
  (comp (testing)
        (watch)
        (test-cljs)))

(deftask deploy []
  (println "I'm not implemented yet")
  identity)
