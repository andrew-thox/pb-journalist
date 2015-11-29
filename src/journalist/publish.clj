(ns journalist.publish
  (:require [environ.core :refer [env]]
            [langohr.core :as rmq]
            [langohr.basic :as lb]
            [langohr.channel :as lch]
            [clj-json.core :as json]))


(defn publish-article [article]
  (let [conn  (rmq/connect {:uri (env :amqp-url)})
        ch    (lch/open conn)]
    (lb/publish ch "" (env :queue-name)
      (json/generate-string article)
      {:content-type "application/json"
       :type "article"
       :source "new-statesman"})
    (rmq/close ch)
    (rmq/close conn)))
