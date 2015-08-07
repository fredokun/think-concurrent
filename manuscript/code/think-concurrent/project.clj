(defproject think-concurrent "0.1.0-SNAPSHOT"
  :description "The code support for the book 'Think concurrent with clojure/core.async'."
  :url "https://github.com/fredokun/think-concurrent"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]]
  :plugins [[cider/cider-nrepl "0.10.0-SNAPSHOT"]]
  :main ^:skip-aot think-concurrent.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
