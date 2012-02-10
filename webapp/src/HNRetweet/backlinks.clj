(ns HNRetweet.backlinks
  (:use [HNRetweet.rest])
  (:require [HNRetweet.creds :as creds]
            [oauth.client :as oauth]))

(defn hn-tweets-page [page since]
  (let [url "https://api.twitter.com/1/statuses/user_timeline.json"
        params {:screen_name "HNTweets", :trim_user 1,
                :include_entities 1, :exclude_replies 1,
                :count 200, :page page}
        params (if (nil? since) params (assoc params :since_id since))
        credentials (oauth/credentials creds/twitter-consumer
                                       creds/twitter-token
                                       creds/twitter-secret
                                       :GET
                                       url
                                       params)
        response (json-request url (merge credentials params))]
    (if-let [e (:error response)]
      (throw (new Error e))
      response)))

(defn hn-tweets [since]
  (doall
    (->>
      (map #(hn-tweets-page % since) (iterate inc 1))
      (take-while seq)
      (apply concat))))

(defn expand-ly-chunk [urls]
  (->>
    (json-request "https://api-ssl.bitly.com/v3/expand"
                  {"login" creds/dot-ly-login, "apiKey" creds/dot-ly-key,
                   "shortUrl" urls})
    (:data)
    (:expand)
    (map #(vector (:short_url %) (:long_url %)))
    (remove #(nil? (second %)))
    (into {})))

(defn expand-ly [urls]
  (->>
     (partition-all 15 urls)
     (map expand-ly-chunk)
     (apply merge)))

(defn expand-all-ly-urls-in-tweets [tweets]
  (let [urls (keep :expanded_url (mapcat #(-> % :entities :urls) tweets))
        ly-urls (filter #(re-matches #"http://\w+\.ly/\w+" %) urls)]
    (expand-ly ly-urls)))

(defn fetch-new-backlinks [since-tweet-id]
  (let [tweets (hn-tweets since-tweet-id)
        expanded-urls (expand-all-ly-urls-in-tweets tweets)
        hn-reg #"http://news\.ycombinator\.com/item\?id=(\d+)"]
    (for [t tweets
          url (keep :expanded_url (-> t :entities :urls))
          :let [expanded (get expanded-urls url url)
                tid (t :id)]
          :when expanded]
      (if-let [m (re-matches hn-reg expanded)]
        [(read-string (second m)) tid]
        [expanded tid]))))
