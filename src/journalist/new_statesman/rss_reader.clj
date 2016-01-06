(ns journalist.new_statesman.rss_reader
  (:use clojure.string)
  (:require [clj-time.format :as f]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [net.cgrand.enlive-html :as html]
            [journalist.logging.log :as log]))

(def new_statesman_feed "http://www.newstatesman.com/feeds/site_feed.rss")
;E is day of the week, src: http://www.rabblemedia.net/java-simple-date-fomat-cheatsheet.html
(def date-formatter (f/formatter (t/default-time-zone) "YYYY-MM-dd HH:mm:ss" "E, dd MMM YYYY HH:mm:ss Z"))

(defn process-article [article]
  {:author (join (map html/text (html/select article [:dc:creator])))
   :title (join (map html/text (html/select article [:title])))
   :link (join (map html/text (html/select article [:link])))
   :publication "New Statesman"
   :publish_date (c/to-long (f/parse date-formatter (join (map html/text (html/select article [:pubDate])))))
   :acquistion_date (c/to-long (t/now))})

(defn process_rss_feed []
  (log/info "parsing New Statesman RSS feed")
  (map process-article (html/select (html/xml-resource (java.net.URL. new_statesman_feed)) [:item])))

