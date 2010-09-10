(ns com.blogspot.sbtourist.clojure.jstat.reader_test (:use clojure.test clojure.java.io com.blogspot.sbtourist.clojure.jstat.reader))

(deftest test-reader-output
  (let [stats (jstat-reader (resource "jstat.txt"))]
    (is (= 0 (first (keys stats))))
    (is (= "1" ((stats 0) "Timestamp")))
    (is (= "2" ((stats 0) "PC")))
    (is (= "3" ((stats 0) "PU")))
    (is (= "4" ((stats 0) "OC")))
    (is (= "5" ((stats 0) "OU")))
    (is (= "6" ((stats 0) "YGC")))
    (is (= "7" ((stats 0) "FGC")))
    (is (= "8" ((stats 0) "FGCT")))
    (is (= "9" ((stats 0) "GCT")))
    )
  )


