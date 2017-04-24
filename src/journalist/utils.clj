(ns journalist.utils
	(:require [clojure.string :as str]
			  [journalist.publish :as queue]
			  [journalist.new_statesman.rss_reader]
              [journalist.evening_standard.rss_reader])
    (:use [journalist.publications]))

(defn resolve_publication [publication-slug]
  (first (filter
    #(let [x (% :slug)] (= (compare x publication-slug) 0))
  outlets))
)

;http://stackoverflow.com/questions/29033853/dynamically-loading-an-aliased-namespace-to-another-clojure-namespace

(defn process_rss_feed
  "Provides a way to run a task without using the consumer"
   [outlet]
   (let [task {:outlet outlet, :acquisition-method "rss-feed", :acquisition-source "default"}
   	  acq-fn-spec ((keyword (:acquisition-method task)) (journalist.utils/resolve_publication (:outlet task)))
      acq-fn-ns (first (str/split acq-fn-spec #"/"))
      acq-fn-name (last (str/split acq-fn-spec #"/"))
      acq-fn (ns-resolve (symbol acq-fn-ns) (symbol (name acq-fn-name)))]
   (doall (map queue/publish-article (acq-fn)))))

