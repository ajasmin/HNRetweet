(ns HNRetweet.handler
  (:use [compojure.core]
        [ring.util.codec :only [url-encode]]
        [clojure.tools.logging :only [info]]
        [HNRetweet.datastore]
        [HNRetweet.backlinks :only [fetch-new-backlinks]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defroutes main-routes
  (GET ["/retweet/:hnid" :hnid #"[0-9]+"] [hnid]
       (let [hnid (read-string hnid)]
         (if-let [twid (get (get-entity "backlink" hnid) "tweet-id")]
           {:status 302 ; Retweet
            :headers {"Location" (str "http://twitter.com/intent/retweet?tweet_id=" twid)}}
           {:status 302 ; If we can't find this tweet post a new one
            :headers {"Location" (str "http://twitter.com/intent/tweet?text="
                                      (url-encode (str "http://news.ycombinator.com/item?id=" hnid)))}})))
  (GET "/cron/fetch-new-backlinks" []
       (let [last-tweet-id (get-prop "last-tweet-id")
             backlinks (fetch-new-backlinks last-tweet-id)]
         (when (seq backlinks)
           (put-entities
             (map (fn [[hnid twid]] ["backlink" hnid {"tweet-id" twid}]) backlinks))
           (set-prop "last-tweet-id" (second (first backlinks))))
         (info (str "Fetched " (count backlinks) " tweets")))
       "OK")
  (route/not-found "Page not found"))

(def handler
    (handler/site main-routes))
