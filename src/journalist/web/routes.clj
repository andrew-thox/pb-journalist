(ns journalist.web.routes
     (:gen-class)
     (:refer-clojure :exclude [resolve])
     (:use ring.middleware.json-params)
     (:require [journalist.new_statesman.rss_reader]
               [journalist.new_statesman.archive]
               [clj-json.core :as json]
               [journalist.logging.log :as log]
               [journalist.utils]
               [compojure.core :refer [defroutes GET]])
  )

(defn json-response [data & [status]]
      {:status (or status 200)
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string data)})

(defroutes handler
  (GET "/" []
    (let [author "author4" title "title4"]
      (json-response {"hello" author "test" title})))

  (GET "/acquisitions/:publication/rss-feed" [publication]
    (let [process-rss-feed (:rss-feed (journalist.utils/resolve_publication publication))
          articles ((ns-resolve *ns* (symbol process-rss-feed)))]
      (json-response {"publication" publication "source" "rss-feed" "articles" articles})))

  (GET "/acquisitions/:publication/archive" [publication]
    (log/info publication)
    (log/info (:archive (journalist.utils/resolve_publication publication)))
    ;TODO: Sym should move out of let?
    (let [process-archives (:archive (journalist.utils/resolve_publication publication))
          sym ((ns-resolve *ns* (symbol process-archives)))]
      (json-response {"publication" publication "source" "web-archive"}))))

(def app
  (-> handler
      wrap-json-params))