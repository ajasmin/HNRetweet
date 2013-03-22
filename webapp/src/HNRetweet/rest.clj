(ns HNRetweet.rest
  (:use [ring.util.codec :only [form-encode]]
        [clojure.tools.logging :only [warn]])
  (:require [cheshire.core :as json]))

(defn build-url [url-base query-map]
  (str url-base "?" (form-encode query-map)))

(defn json-request [url params]
  (let [url (build-url url params)
        contents (slurp url)]
                (json/parse-string contents true)))
