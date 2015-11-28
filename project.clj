(defproject journalist "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-json "0.3.2"]
                 [compojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [ring-json-params "0.1.0"]
                 ;logging
                 [ch.qos.logback/logback-classic "1.1.3"]]
  :main ^:skip-aot journalist.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
