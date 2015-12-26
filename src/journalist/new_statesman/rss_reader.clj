(ns journalist.new_statesman.rss_reader
  (:use clojure.string)
  (:require [journalist.publish :as queue]
            [clj-time.format :as f]
            [clj-time.core :as t]
            [net.cgrand.enlive-html :as html]
            [journalist.logging.log :as log]))


(def new_statesman_feed (html/xml-resource (java.net.URL. "http://www.newstatesman.com/feeds/site_feed.rss")))
;E is day of the week, src: http://www.rabblemedia.net/java-simple-date-fomat-cheatsheet.html
(def date-formatter (f/formatter (t/default-time-zone) "YYYY-MM-dd HH:mm:ss" "E, dd MMM YYYY HH:mm:ss Z"))

;TODO: Actually adding this to the queue should be optional
(defn process-article [article]
  (let [article-map {:author (join (map html/text (html/select article [:dc:creator])))
                     :title (join (map html/text (html/select article [:title])))
                     :link (join (map html/text (html/select article [:link])))
                     :publication "New Statesman"
                     :publish_date (f/unparse date-formatter (f/parse date-formatter (join (map html/text (html/select article [:pubDate])))))
                     :acquistion_date (f/unparse date-formatter (t/now))}]
    (do (queue/publish-article article-map) article-map)))


(defn process_rss_feed []
  (log/info "processing new-statesmen rss feed")
  (let [articles (html/select new_statesman_feed [:item])]
    (for [article articles]
      (process-article article))))

