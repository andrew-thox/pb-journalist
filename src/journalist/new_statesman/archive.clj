(ns journalist.new_statesman.archive
  (:use clojure.string)
  (:use compojure.core)
  (:use clojurewerkz.urly.core)
  (:require [journalist.logging.log :as log]
            [journalist.publish :as queue]
            [clj-http.client :as client]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [cheshire.core :refer :all]
            [net.cgrand.enlive-html :as html]))

(def base_url "http://www.newstatesman.com")
(def contributors_url "http://www.newstatesman.com/our-writers")

;Will allow inputs from either format, but will output in the first
(def date-formatter (f/formatter (t/default-time-zone) "YYYY-MM-dd HH:mm:ss" "dd MMM yyyy"))


(defn get-article [uri]
  (html/html-resource (java.net.URL. (str base_url uri))))


(defn get-contributors [contributors_url]
  (let [contributors (html/select (html/html-resource (java.net.URL. contributors_url))[:ul.contributors :> :li :> :div :> :span :> :a])]
    (for [contributor contributors]
      {:name (join (:content contributor))
       :url (:href (:attrs contributor))})))

(defn get-publish-date [article-link]
  (let [article (html/html-resource (java.net.URL. article-link))
        date-published (f/parse date-formatter (trim (first (map html/text (html/select article [:div.article-date])))))]
  (f/unparse date-formatter date-published)))



(defn parse_article [article]
  ;If the article has an image asscoiated with it, then it has 4 <a> tags.
  ;Get rid of the first a tag so they can all be the same
  ;TODO: I don't think you are supposed to define a var in a function. Fold into let!
  (def a-tags (if (= (count (html/select article [:a])) 4) (drop 1 (html/select article [:a])) (html/select article [:a])))
  (let [article-map {:author (trim (first (map html/text (html/html-snippet (first (:content (nth a-tags 2)))))))
                     :title (trim (first (map html/text (html/html-snippet (first (:content (nth a-tags 1)))))))
                     :link (str base_url (join (:href (:attrs (nth a-tags 1)))))
                     :publication "New Statesman"
                     :publish_date (get-publish-date (str base_url (join (:href (:attrs (nth a-tags 1))))))
                     :acquistion_date (f/unparse date-formatter (t/now))}]
    (do (log/info (:title article-map)) (queue/publish-article article-map) article-map)))



(defn create_infinite_scroll_url [page_no short_link]
      (clojure.string/replace "http://www.newstatesman.com/views/ajax?view_name=writers_articles_list_page&view_display_id=page_1&view_path=writers%2Fshort_link&page=page_number"
                              #"page_number|short_link"
                              {"page_number" page_no "short_link" short_link})
      )

(defn parse_infinite_scroll_page [page short_link]
  (log/info "----------PAGE--------------" page short_link)
      (html/html-snippet (:data (last (parse-string (:body (client/get (create_infinite_scroll_url page short_link) {:accept :json})) true))))
)

(defn get_articles [html-resource]
      (html/select html-resource [:ul.article-list :> :li]))

(defn parse_articles [articles]
  (for [article articles]
         (parse_article article)))

(defn do_infinite_scroll [short_link]
      (loop [page 1 articles_on_this_page 10 li ()]
            (if (< articles_on_this_page 10)
              li
              (recur (inc page) (count (parse_articles (get_articles (parse_infinite_scroll_page (str page) short_link)))) (concat li (parse_articles (get_articles (parse_infinite_scroll_page (str page) short_link)))
                                                              ))
              )
            )
      )

(defn get_articles_by_contributor [contributor_url]
  (let [resource (html/html-resource (java.net.URL. contributor_url))
        short_link (.replaceAll
                     (path-of
                      (:href
                       (:attrs
                        (first
                         (html/select resource [[:link (html/attr= :rel "shortlink")]])))))
                   "/writers/" "")
        base_articles (html/select resource [:ul.article-list :> :li])]
   (concat (do_infinite_scroll short_link) (parse_articles base_articles)))
)

(defn process-archive []
      (let [contributors '({:name "Bryan Appleyard", :url "/writers/313246"})]
           (for [contributor  contributors]
                (let [articles (get_articles_by_contributor (str base_url (:url contributor)))]
                     articles))))

