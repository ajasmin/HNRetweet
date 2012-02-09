(defproject HNRetweet "1.0.0-SNAPSHOT"
  :description "Webapp portion of HNRetweet"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/tools.logging "0.2.3"]
                 [ring "1.0.2"]
                 [compojure "1.0.1"]
                 [cheshire "2.1.0"]
                 [com.google.appengine/appengine-api-1.0-sdk "1.6.2.1"]]
  :aot [HNRetweet.servlet])
