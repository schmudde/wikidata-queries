;; Do not edit.
;; This file is generated from wikidata_queries.org.
;; Edit wikidata_queries.org and and tangle the file.

(ns wikidata-queries
  (:require [mundaneum.query :refer [query entity describe]]
            [mundaneum.properties :refer [wdt]]))

(defn ^:private query-class-and-rank
  " In: valid wikidata ID (Q####...)
   Out: collection of entities which are instance-of of x or any subclass of x
        They are ranked by the total number of predicates associated with the item.
        Includes logos where available."
  [x]
  (query
   `{:select [?wikidataId ?wikidataIdLabel ?urlLabel ?logoLabel [(count ?predicate :distinct? true) ?count]]
     :where [[?wikidataId ?predicate ?object]
             [?wikidataId (cat ~(wdt :instance-of) (* ~(wdt :subclass-of))) ~x]
             [?wikidataId ~(wdt :official-website) ?url]
             [:optional [[?wikidataId ~(wdt :logo-image) ?logo]]]]
     :group-by [?wikidataId ?wikidataIdLabel ?urlLabel ?logoLabel]
     :order-by [(desc ?count)]}))

(defn search-coll
  "In: search-string: the string to search for
       search-keyword: the keyword attribute to search - is it a :wikidataIdLabel, a :urlLabel, etc...?
       search-domain-coll: the search space
  Out: nil or a collection of matches"
  [search-string search-keyword search-domain-coll]
  (when search-string
    (let [conform #(when % (clojure.string/lower-case %))
          matches (keep #(when (= (conform (search-keyword %))
                                  (conform search-string))
                           %)
                        search-domain-coll)]
      (when (seq matches) matches))))
