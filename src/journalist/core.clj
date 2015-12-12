(ns journalist.core
  (:gen-class)
  (:use  ring.adapter.jetty)
  (:require [journalist.web.routes :as web]
            [journalist.logging.log :as log]
            [environ.core :refer [env]]))

(defn -main [& args]
  (log/info (env :amqp-url))
  (log/info (env :queue-name))
  (run-jetty #'web/app {:port 8080}))
