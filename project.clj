(defproject journalist "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/urly "1.0.0"]
                 [enlive "1.1.6"]
                 ;web
                 ;[clj-json "0.3.2"]
                 [compojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 ;[ring-json-params "0.1.0"]
                 ;queue
                 [com.novemberain/langohr "3.0.0-rc2"]
                 ;logging
                 [ch.qos.logback/logback-classic "1.1.3"]
                 ;environment
                 [environ "0.5.0"]
                 ;avro
                 [com.damballa/abracad "0.4.13"]
                 ;utils
                 [clj-time "0.11.0"]]
  :plugins [[lein-environ "1.0.1"]
            [lein-gorilla "0.4.0"]]
  :main ^:skip-aot journalist.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
