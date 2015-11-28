(ns journalist.web.routes
     (:gen-class)
     (:refer-clojure :exclude [resolve])
     (:use ring.middleware.json-params)
     (:require [clj-json.core :as json])
     (:require [compojure.core :refer [defroutes GET]])
  )

(defn json-response [data & [status]]
      {:status (or status 200)
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string data)})

(defroutes handler
  (GET "/" []
    (let [author "author4" title "title4"]
      (json-response {"hello" author "test" title}))))

(def app
  (-> handler
      wrap-json-params))