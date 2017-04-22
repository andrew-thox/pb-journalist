(ns journalist.evening_standard.rss_reader
  (:use clojure.string)
  (:require [clj-time.format :as f]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [net.cgrand.enlive-html :as html]))

(def evening_standard_feed "http://www.standard.co.uk/news/rss")
;E is day of the week, src: http://www.rabblemedia.net/java-simple-date-fomat-cheatsheet.html
(def date-formatter (f/formatter (t/default-time-zone) "YYYY-MM-dd HH:mm:ss" "E, dd MMM YYYY HH:mm:ss Z"))

(defn process-article [article]
  {:author (join (map html/text (html/select article [:dc:creator])))
   :title (join (map html/text (html/select article [:title])))
   :link (join (map html/text (html/select article [:link])))
   :publication "Evening Standard"
   :publish_date (c/to-long (f/parse date-formatter (join (map html/text (html/select article [:pubDate])))))
   :acquistion_date (c/to-long (t/now))})

(defn process_rss_feed []
  (println "Processing feed")
  (map process-article (html/select (html/xml-resource (java.net.URL. evening_standard_feed)) [:item])))

