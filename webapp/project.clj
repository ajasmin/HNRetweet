(defproject HNRetweet "1.0.0-SNAPSHOT"
  :description "Webapp portion of HNRetweet"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/tools.logging "0.2.3"]
                 [ring "1.1.0"]
                 [compojure "1.1.0"]
                 [cheshire "4.0.0"]
                 [clj-oauth-nohttp "1.3.1-SNAPSHOT"]
                 [com.google.appengine/appengine-api-1.0-sdk "1.6.6"]]
  :aot [HNRetweet.servlet])
