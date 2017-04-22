(ns journalist.consumers
  (:require [clojure.string :as str]
            [abracad.avro :as avro]
            [langohr.basic :as lb]
            [journalist.utils]
            [journalist.schemas :as schemas]
            [journalist.publish :as queue]
            [journalist.logging.log :as log]
            ;It would be cool if we could have a better way to do this
            [journalist.new_statesman.rss_reader]))

(defn task-consumer
  [ch {:keys [content-type delivery-tag] :as meta} ^bytes payload]
  (let [task (->> payload (avro/decode schemas/task-schema))
        acq-fn-spec ((keyword (:acquisition-method task)) (journalist.utils/resolve_publication (:outlet task)))
        acq-fn-ns (first (str/split acq-fn-spec #"/"))
        acq-fn-name (last (str/split acq-fn-spec #"/"))
        acq-fn (ns-resolve (symbol acq-fn-ns) (symbol (name acq-fn-name)))]
    ;TODO: This log line could be formatted a bit better
    ;(log/info "processing task - " (:outlet task) ":" (:acquisition-method task))
    (println "Processing task")
    (doall (map queue/publish-article (acq-fn)))
    (lb/ack ch delivery-tag)))