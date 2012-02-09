(ns HNRetweet.rest
  (:use [ring.util.codec :only [url-encode]]
        [clojure.tools.logging :only [warn]])
  (:require [cheshire.core :as json]))

; --- http://stackoverflow.com/q/3644219
(defn make-query-string [m & [encoding]]
  (let [s #(if (instance? clojure.lang.Named %) (name %) %)
        enc (or encoding "UTF-8")]
    (->> (for [[k vs] m
               v (if (sequential? vs) vs [vs])]
           (str (url-encode (s k) enc)
                "="
                (url-encode (str v) enc)))
         (interpose "&")
         (apply str))))

(defn build-url [url-base query-map & [encoding]]
  (str url-base "?" (make-query-string query-map encoding)))

; ---

(defn json-request [url params]
  (let [url (build-url url params)
        f (fn f [try-count]
            (try
              (let [contents (slurp url)]
                (json/parse-string contents true))
              (catch java.io.IOException e
                (warn (str "Error fetching " url " Retry " try-count))
                (Thread/sleep 10000)
                (f (inc try-count)))))]
    (f 1)))
