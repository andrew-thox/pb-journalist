(ns journalist.publish
  (:require [environ.core :refer [env]]
            [langohr.core :as rmq]
            [langohr.basic :as lb]
            [langohr.channel :as lch]
            [abracad.avro :as avro]
            [journalist.schemas :as schemas]
            [journalist.logging.log :as log]))


(defn publish-article [article]
  (let [conn  (rmq/connect {:uri (env :amqp-url)})
        ch    (lch/open conn)]
    (lb/publish ch "" (env :article-queue-name)
      (->> article (avro/binary-encoded schemas/auto-schema))
      {:mandatory true
       :content-type "avro/binary"
       :type "article"
       :source "pb.journalist"})
    (println "publishing article")
    (rmq/close ch)
    (rmq/close conn))0)
