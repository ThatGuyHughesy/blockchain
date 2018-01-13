(ns blockchain.routes
  (:use compojure.core)
  (:require [clojure.walk :refer [keywordize-keys]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [blockchain.endpoints :as endpoints]))

(defn generate-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (pr-str data)})

(defroutes app-routes
  (POST "/mine" []
    (-> (endpoints/mine-block)
        (generate-response)))
  (GET "/chain" []
    (-> (endpoints/get-chain)
        (generate-response)))
  (POST "/transactions/new" {request :body}
    (-> (keywordize-keys request)
        (endpoints/add-transaction)
        (generate-response)))
  (GET "/nodes" []
    (-> (endpoints/get-nodes)
        (generate-response)))
  (POST "/nodes/new" {request :body}
    (-> (keywordize-keys request)
        (endpoints/add-node)
        (generate-response)))
  (POST "/nodes/resolve" []
    (-> (endpoints/resolve-node)
        (generate-response)))
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))