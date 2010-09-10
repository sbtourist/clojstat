(ns com.blogspot.sbtourist.clojure.jstat.reader
 (:use
   [clojure.java.io]
   [clojure.string :only (blank? split)]
   )
 )

(defn- prepare-data [stats-line-seq]
  (let [header-line (first stats-line-seq)]
    (for [stats-line (rest stats-line-seq)] (hash-map :header-line header-line :stats-line stats-line))
    )
  )

(defn- merge-single-stats [counter single-stats-map]
  (let [not-blank? (fn [s] (not (blank? s)))
        headers (filter not-blank? (split (single-stats-map :header-line) #"\s+"))
        stats (filter not-blank? (split (single-stats-map :stats-line) #"\s+"))
        ]
    (hash-map 
      counter 
      (reduce merge (map #(hash-map %1 %2) headers stats))
      )
    )
  )

(defn jstat-reader [file]
  (with-open [stats (reader file)]
    (reduce #(into %1 %2)
      (map merge-single-stats
        (iterate inc 0) (prepare-data (line-seq stats))
        )
      )
    )
  )