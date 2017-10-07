(ns blockchain.handler
  (:use compojure.core)
  (:require [clojure.walk :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [cheshire.core :as cheshire]
            [blockchain.blockchain :as blockchain]))

(defroutes app-routes
  (GET "/mine" []
    (as-> (blockchain/mine) block
          (cheshire/generate-string {:message "New Block forged"
                                     :index (:index block)
                                     :transactions (:transactions block)
                                     :proof (:proof block)
                                     :previous-hash (:previous-hash block)})))
  (POST "/transactions/new" {request :body}
    (cheshire/generate-string {:message (str "Transaction will be added to Block "
                                             (blockchain/new-transaction! (keywordize-keys request)))}))
  (GET "/chain" []
    (cheshire/generate-string {:chain @blockchain/chain
                               :length (count @blockchain/chain)}))
  (POST "/nodes/new" {request :body}
    (cheshire/generate-string {:message "New node has been addded"
                               :nodes (blockchain/new-node! (keywordize-keys request))}))
  (GET "/nodes/resolve" []
    (if (blockchain/resolve-conflicts @blockchain/nodes 0)
      (cheshire/generate-string {:message "Chain was replace" 
                                 :new-chain @blockchain/chain})
      (cheshire/generate-string {:message "Chain is authoritative"
                                 :chain @blockchain/chain})))
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
