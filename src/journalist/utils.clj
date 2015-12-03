(ns journalist.utils
    (:use [journalist.publications]))

(defn resolve_publication [publication-slug]
  (first (filter
    #(let [x (% :slug)] (= (compare x publication-slug) 0))
  outlets))
)
