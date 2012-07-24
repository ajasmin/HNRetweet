(ns HNRetweet.handler
  (:use [compojure.core]
        [ring.util.codec :only [url-encode]]
        [ring.util.response :only [redirect]]
        [clojure.tools.logging :only [info]]
        [HNRetweet.datastore]
        [HNRetweet.backlinks :only [fetch-new-backlinks]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defroutes main-routes
  ; Retweet based on hacker news post id
  (GET ["/retweet/:hnid" :hnid #"[0-9]+"] [url title hnid]
       (let [hnid (read-string hnid)]
         (if-let [twid (get (get-entity "backlink" hnid) "tweet-id")]
           (redirect (str "http://twitter.com/intent/retweet?tweet_id=" twid))
           ; If we can't find this tweet post a new one
           (redirect (str "http://twitter.com/intent/tweet?text=" (url-encode
             (str
              (if title (str title ": "))
              (if url (str url " "))
              "Comments: http://news.ycombinator.com/item?id=" hnid)))))))

  ; Retweet based on post url
  (GET "/retweet" [url title]
       (when url
         (if-let [twid (get (get-entity "backlink" url) "tweet-id")]
           (redirect; Retweet
             (str "http://twitter.com/intent/retweet?tweet_id=" twid))
           (redirect ; If we can't find this tweet post a new one
             (str "http://twitter.com/intent/tweet?text="
               (url-encode (str (if title (str title ": ")) url)))))))

  (GET "/cron/fetch-new-backlinks" []
       (let [last-tweet-id (get-prop "last-tweet-id")
             backlinks (fetch-new-backlinks last-tweet-id)]
         (when (seq backlinks)
           (put-entities
             (map (fn [[hnid twid]] ["backlink" hnid {"tweet-id" twid}])
                  (filter (fn [[hnid twid]] (or (not (string? hnid)) (< (count hnid) 500))) backlinks))) ; datastore length restriction
           (set-prop "last-tweet-id" (second (first backlinks))))
         (info (str "Fetched " (count (into #{} (map second backlinks))) " tweets")))
       "OK")
  (route/not-found "Not Found"))

(def handler
    (handler/api main-routes))
