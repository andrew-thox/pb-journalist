(ns journalist.core
  (:gen-class)
  (:require [journalist.consumers :as consumers]
            [environ.core :refer [env]]
            [langohr.channel :as lch]
            [langohr.core :as rmq]
            [langohr.queue :as lq]
            [langohr.consumers :as lc]))

(defn -main [& args]
  (let [conn  (rmq/connect {:uri (env :amqp-url)})
        ch    (lch/open conn)]
    (println (format "[main] Connected. Channel id: %d" (.getChannelNumber ch)))
    (lq/declare ch (env :task-queue-name) {:exclusive false :auto-delete false})
    (lc/subscribe ch (env :task-queue-name) consumers/task-consumer {:auto-ack false})
    (.addShutdownHook (Runtime/getRuntime) (Thread. #(do (rmq/close ch) (rmq/close conn))))))
