(ns blockchain.blockchain
  (require [clj-time.core :as time]
           [clj-time.coerce :as time-coerce]
           [cheshire.core :refer :all]
           [pandect.algo.sha256 :refer :all])
  (:import (java.util UUID)))

(def node-identifier (str (UUID/randomUUID)))

(def genesis-block {:index 0
                    :timestamp 123456789
                    :transactions []
                    :hash "9cb345d0ec9bfc248497286166505ed9f643aca499d86b74088000909226c91f"
                    :previous-hash 0
                    :proof 100})

(def chain (atom [genesis-block]))

(def current_transactions (atom []))

(defn- last-block []
  (last @chain))

(defn- calculate-hash [index hash timestamp transactions]
  (sha256 (str index hash timestamp transactions)))

(defn- new-block [proof]
  (let [transactions @current_transactions
        previous-block (last-block)
        next-index (inc (:index previous-block))
        next-timestamp (time-coerce/to-long (time/now))
        next-block {:index next-index
                    :timestamp next-timestamp
                    :transactions transactions
                    :hash (calculate-hash next-index (:hash previous-block) next-timestamp transactions)
                    :previous-hash (:hash previous-block)
                    :proof proof}]
    (reset! current_transactions [])
    (swap! chain conj next-block)
    next-block))

(defn new-transaction [{:keys [sender recipient amount] :as transaction}]
  (swap! current_transactions conj {:sender sender
                                    :recipient recipient
                                    :amount amount})
  (-> (last-block) :index))

(defn- valid-proof [previous-block proof]
  (let [guess-hash (sha256 (str (:proof previous-block) proof))]
    (= (subs guess-hash 0 4) "0000")))

(defn- proof-of-work [previous-block]
  (loop [proof 0]
    (if (not (valid-proof previous-block proof))
      (recur (inc proof)) proof)))

(defn mine []
  (let [previous-block (last-block)
        proof (proof-of-work previous-block)]
    (new-transaction {:sender "0"
                      :recipient node-identifier
                      :amount 1})
    (new-block proof)))