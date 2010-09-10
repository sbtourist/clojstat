(ns com.blogspot.sbtourist.clojure.jstat.viewer 
 (:use
   [clojure.string :only (blank? split)]
   [clojure.contrib.json]
   [compojure.core]
   [compojure.route :as routes]
   [hiccup.core]
   [hiccup.form-helpers]
   [hiccup.page-helpers]
   [ring.adapter.jetty]
   [ring.middleware.file]
   [com.blogspot.sbtourist.clojure.jstat.reader]))

(defn- make-header [timestamps stats]
  [:tr [:th] (for [ts timestamps] [:th {:scope "col"} ts])]
  )

(defn- make-row [timestamps stats datum]
  [:tr [:th {:scope "row"} datum] (for [ts timestamps] [:td ((stats ts) datum)])]
  )

(defn- make-stats-table [file timestamps stats data]
  [:table {:id "table-data"}
   [:caption file]
   [:thead (make-header timestamps stats)]
   [:tbody (for [datum data] (make-row timestamps stats datum))]
   ]
  )

(defn make-stats-page [file stats data interval]
  (let [timestamps (take-nth interval (sort (keys stats)))]
    (html
      [:html
       [:head
        [:title "ClojStat Viewer"]
        (include-css "/static/css/basic.css" "/static/css/visualize.css" "/static/css/visualize-light.css") (include-js "/static/js/jquery.js" "/static/js/visualize.js" "/static/js/viewer.js")
        ]
       [:body
        [:div {:id "table-container" :align "center"}
         (make-stats-table file timestamps stats (split data #","))
         ]
        [:div {:id "chart-container" :align "center"}]
        ]
       ]
      )
    )
  )

(defn make-post-page []
  (html
    [:html
     [:head
      [:title "ClojStat Viewer"]
      ]
     [:body
      [:h1 {:align "center"} "ClojStat Viewer"]
      [:div {:id "form-container" :align "center"}
       (form-to [:POST "/view"]
         (label :file "JStat File")
         [:br]
         (text-field :file)
         [:br]
         (label :stats "JStat Stats")
         [:br]
         (text-field :stats)
         [:br]
         (label :interval "Samples interval")
         [:br]
         (text-field :interval)
         [:br]
         (submit-button "View")
         )
       ]
      ]
     ]
    )
  )

(defroutes jstat-viewer-routes
  (GET "/post" [] (make-post-page))
  (POST "/view" {params :params} (make-stats-page (params "file") (jstat-reader (params "file")) (params "stats") (Integer/parseInt (get params "interval" "1"))))
  (routes/files "/static" {:root "static"})
  )

(defn jstat-viewer-run [port]
  (run-jetty jstat-viewer-routes {:port port :join? false})
  )