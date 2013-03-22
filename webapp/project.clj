(defproject HNRetweet "1.0.0-SNAPSHOT"
  :description "Webapp portion of HNRetweet"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.logging "0.2.6"]
                 [ring "1.1.8"]
                 [compojure "1.1.5"]
                 [cheshire "5.0.2"]
                 [clj-oauth "1.3.1-SNAPSHOT"]
                 [com.google.appengine/appengine-api-1.0-sdk "1.7.6"]]
  :aot :all)
