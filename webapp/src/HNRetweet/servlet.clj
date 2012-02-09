(ns HNRetweet.servlet
  (:require ring.util.servlet HNRetweet.handler)
  (:gen-class :extends javax.servlet.http.HttpServlet))

(ring.util.servlet/defservice
  (fn [request]
    (HNRetweet.handler/handler
      (assoc request
             :path-info (.getPathInfo (:servlet-request request))
             :context (.getContextPath (:servlet-request request))))))
