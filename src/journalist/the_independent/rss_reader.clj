(ns journalist.the_independent.rss_reader
  (:use clojure.string)
  (:require [net.cgrand.enlive-html :as html]
            [clj-time.format :as f]
            [clj-time.core :as t]
            [journalist.logging.log :as log]))

(def rss-feeds {:default "http://www.independent.co.uk/news/rss"
                :news "http://www.independent.co.uk/news/rss"})

(def ISO8601-X-formatter (f/formatter (t/default-time-zone) "YYYY-MM-dd HH:mm:ss" "YYYY-MM-dd'T'HH:mm:ssZ"))

(defn process-article [article]
  (let [article-map {:author (join (map html/text (html/select article [:author])))
                     :title (join (map html/text (html/select article [:title])))
                     :link (join (map html/text (html/select article [:link])))
                     :publication "The Independent"
                     :publish_date (f/unparse ISO8601-X-formatter
                      (f/parse ISO8601-X-formatter (join (map html/text (html/select article [:dc:date])))))
                     :acquistion_date (f/unparse ISO8601-X-formatter (t/now))}]
    article-map))

;TODO: This function can be shared across all outlets
(defn process-rss-feed [ & [feed-key]]
  (log/info "parsing The Independent RSS feed")
  (let [feed-key (or feed-key :default)
        feed ((keyword feed-key) rss-feeds)
        articles (html/select (html/xml-resource (java.net.URL. feed)) [:item])]
    (process-article (first articles))))
