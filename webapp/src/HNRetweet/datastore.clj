(ns HNRetweet.datastore
  (:import (com.google.appengine.api.datastore
             DatastoreService DatastoreServiceFactory Query Entity KeyFactory
             EntityNotFoundException)))

(def datastore-tl
  (proxy [ThreadLocal] []
    (initialValue [] (DatastoreServiceFactory/getDatastoreService))))

(defn datastore []
  (.get datastore-tl))

(defn put-entity [kind name propmap]
  (let [e (new Entity kind name)]
    (doseq [[k v] propmap]
      (.setProperty e k v))
    (.put (datastore) e)))

(defn put-entities [coll]
  (.put (datastore)
        (for [[kind name propmap] coll]
          (let [e (new Entity kind name)]
            (doseq [[k v] propmap] (.setProperty e k v))
            e))))

(defn get-entity [kind name]
  (try
    (->>
      (KeyFactory/createKey kind name)
      (.get (datastore))
      (.getProperties)
      (into {}))
    (catch EntityNotFoundException e nil)))

(defn set-prop [name val]
  (put-entity "prop" name {"value" val}))

(defn get-prop [name]
  (get (get-entity "prop" name) "value"))

