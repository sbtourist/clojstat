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

(defn- merge-headers-with-stats [counter header-to-stats-map]
  (let [not-blank? #(not (blank? %1))
        headers (filter not-blank? (split (header-to-stats-map :header-line) #"\s+"))
        stats (filter not-blank? (split (header-to-stats-map :stats-line) #"\s+"))
        ]
    (hash-map 
      counter 
      (reduce merge (map #(hash-map %1 (Double/parseDouble %2)) headers stats))
      )
    )
  )

(defn- merge-with-mean [clocks all-stats]
  (let [keys-to-vals #(for [k %1] (%2 k)) merged (apply merge-with + (keys-to-vals clocks all-stats)) divisor (.size clocks)]
    (apply merge (for [entry merged] (hash-map (key entry) (long (/ (val entry) divisor)))))
    )
  )

(defn sample [all-stats interval]
  (let [clocks (take-nth interval (sort (keys all-stats)))]
    (reduce #(into %1 %2) (map #(hash-map %1 (all-stats %1)) clocks))
    )
  )

(defn mean [all-stats interval]
  (loop [clocks (sort (keys all-stats)) cursor 0 index 1 result {}]
    (if (empty? clocks)
      result
      (recur (drop interval clocks) (+ cursor interval) (+ index 1) (assoc result index (merge-with-mean (take interval clocks) all-stats)))
      )
    )
  )

(defn jstat-reader [stats-fn stats-interval stats-file]
  (with-open [stats (reader stats-file)]
    (stats-fn
      (reduce #(into %1 %2)
        (map merge-headers-with-stats
          (iterate inc 1) (prepare-data (line-seq stats))
          )
        )
      stats-interval)
    )
  )