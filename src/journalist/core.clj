(ns journalist.core
  (:gen-class)
  (:use  ring.adapter.jetty)
  (:require [journalist.web.routes :as web]))

(defn -main [& args]
  (run-jetty #'web/app {:port 8080}))
