(ns HNRetweet.rest
  (:use [ring.util.codec :only [form-encode]]
        [clojure.tools.logging :only [warn]])
  (:require [cheshire.core :as json]))

(defn build-url [url-base query-map]
  (str url-base "?" (form-encode query-map)))

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
