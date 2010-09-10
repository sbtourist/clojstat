(ns com.blogspot.sbtourist.clojure.jstat.clj-runner
 (:use
   [com.blogspot.sbtourist.clojure.jstat.viewer])
 )

(defonce server (agent nil))

(defn run-server [port]
  (send-off server #(if (nil? %1) (jstat-viewer-run port) %1))
  )

(defn stop-server []
  (send-off server #(when (not (nil? %1)) (do (.stop %1) nil)))
  )