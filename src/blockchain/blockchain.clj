(ns blockchain.blockchain
  (:require [clojure.walk :refer :all]
            [clj-time.core :as time]
            [clj-time.coerce :as time-coerce]
            [clj-http.client :as client]
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

(def nodes (atom []))

(defn- last-block []
  "Return the previous Block in the Blockchain."
  (last @chain))

(defn- calculate-hash [index hash timestamp transactions]
  "Create a SHA-256 hash of a Block."
  (sha256 (str index hash timestamp transactions)))

(defn- new-block! [proof]
  "Create a new Block in the Blockchain."
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

(defn new-transaction! [sender recipient amount]
  "Create a new transaction to go into the next mined Block."
  (swap! current_transactions conj {:sender sender
                                    :recipient recipient
                                    :amount amount})
  (:index (last-block)))

(defn- valid-proof? [previous-block proof]
  "Determine if a given proof is valid."
  (let [guess-hash (sha256 (str (:proof previous-block) proof))]
    (= (subs guess-hash 0 4) "0000")))

(defn- proof-of-work [previous-block]
  "Simple Proof of Work Algorithm:
   Find a number p' such that hash(pp') contains leading 4 zeroes, where p is the previous p'."
  (loop [proof 0]
    (if-not (valid-proof? previous-block proof)
      (recur (inc proof)) proof)))

(defn mine! []
  "Run Proof of Work Algorithm.
   Receive reward for finding the proof.
   Forge the new Block by adding it to the Blockchain."
  (let [previous-block (last-block)
        proof (proof-of-work previous-block)]
    (new-transaction! "0" node-identifier 1)
    (new-block! proof)))

(defn new-node! [address]
  "Add a new node to the list of nodes."
  (swap! nodes conj address))

(defn valid-chain? [chain last-block current-index]
  "Determine if a given Blockchain is valid."
  (let [block (get chain current-index)
        valid? (valid-proof? last-block (:proof block))]
    (if (= current-index (-> chain count dec))
      valid?
      (if valid?
        (recur chain block (inc current-index))
        false))))

(defn resolve-conflicts! [nodes index]
  "Consensus algorithm that resolves conflicts by replacing the Blockhain with the longest one in the network."
  (let [request (-> (client/get (str "http://" (get nodes index) "/chain")
                                {:socket-timeout 10000
                                 :conn-timeout 10000
                                 :as :json})
                    :body
                    keywordize-keys)
        node-chain (:chain request)
        node-chain-length (:length request)
        conflict? (and (< (count @chain) node-chain-length)
                       (valid-chain? node-chain (get node-chain 0) 1))]
    (if (= index (-> nodes count dec))
      conflict?
      (if conflict?
        (reset! chain node-chain)
        (recur nodes (inc index))))))