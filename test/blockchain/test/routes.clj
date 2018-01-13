(ns blockchain.test.routes
  (:require [clojure.test :refer :all]
            [clojure.walk :refer [keywordize-keys]]
            [blockchain.routes :refer :all]
            [ring.mock.request :as mock]))

(deftest test-new-transaction-endpoint
  (testing "New transaction endpoint"
    (let [response (app (-> (mock/request :post "/transactions/new")
                            (mock/content-type "application/json")
                            (mock/json-body {:sender "Test Sender"
                                             :recipient "Test Recipient"
                                             :amount 10})))
          body (-> response :body read-string)]
      (is (= (:status response) 200))
      (is (= (:message body) "Transaction will be added to Block 2")))))

(deftest test-mine-endpoint
  (testing "Mine new block endpoint"
    (let [response (app (-> (mock/request :post "/mine")))
          body (-> response :body read-string)]
      (is (= (:status response) 200))
      (is (= (:proof body) 35089)))))

(deftest test-chain-endpoint
  (testing "Get chain endpoint"
    (let [response (app (-> (mock/request :get "/chain")))
          body (-> response :body read-string)]
      (is (= (:status response) 200))
      (is (= (:length body) 3)))))

(deftest test-new-node-endpoint
  (testing "New node endpoint"
    (let [response (app (-> (mock/request :post "/nodes/new")
                            (mock/content-type "application/json")
                            (mock/json-body {:address "http://127.0.0.1"})))
          body (-> response :body read-string)]
      (is (= (:status response) 200))
      (is (= (:nodes body) ["http://127.0.0.1"])))))

(deftest test-nodes-endpoint
  (testing "Get nodes endpoint"
    (let [response (app (-> (mock/request :get "/nodes")))
          body (-> response :body read-string)]
      (is (= (:status response) 200))
      (is (= (:nodes body) ["http://127.0.0.1"])))))