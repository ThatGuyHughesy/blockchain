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
          (cheshire/generate-string {:message "New Block Forged"
                                     :index (:index block)
                                     :transactions (:transactions block)
                                     :proof (:proof block)
                                     :previous-hash (:previous-hash block)})))
  (POST "/transactions/new" {request :body}
    (as-> (blockchain/new-transaction (keywordize-keys request)) block-index
          (cheshire/generate-string {:message (str "Transaction will be added to Block " block-index)})))
  (GET "/chain" []
    (cheshire/generate-string {:chain @blockchain/chain :length (count @blockchain/chain)}))
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
