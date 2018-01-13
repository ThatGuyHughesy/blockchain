(ns blockchain.endpoints
  (:require [blockchain.blockchain :as blockchain]))

(defn get-chain []
  {:chain @blockchain/chain
   :length (count @blockchain/chain)})

(defn add-transaction [{:keys [sender recipient amount]}]
  {:message (str "Transaction will be added to Block "
                 (blockchain/new-transaction! sender recipient amount))})

(defn mine-block []
  (let [block (blockchain/mine!)]
    {:message "New Block forged"
     :index (:index block)
     :transactions (:transactions block)
     :proof (:proof block)
     :previous-hash (:previous-hash block)}))

(defn get-nodes []
  {:nodes @blockchain/nodes
   :length (count @blockchain/nodes)})

(defn add-node [request]
  {:message "New node has been added"
   :nodes (blockchain/new-node! (:address request))})

(defn resolve-node []
  (if (blockchain/resolve-conflicts! @blockchain/nodes 0)
    {:message "Chain was replaced"
     :chain @blockchain/chain}
    {:message "Chain is authoritative"
     :chain @blockchain/chain}))