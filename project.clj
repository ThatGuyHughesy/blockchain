(defproject blockchain "0.1.0-SNAPSHOT"
  :description "Clojure implementation of blockchain"
  :url "https://github.com/ThatGuyHughesy/blockchain"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :author "Conor Hughes <hello@conorhughes.me>"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.0"]
                 [ring "1.6.3"]
                 [ring/ring-json "0.4.0"]
                 [clj-http "3.7.0"]
                 [clj-time "0.14.0"]
                 [environ "1.1.0"]
                 [slingshot "0.12.2"]
                 [pandect "0.6.1"]]
  :dev-dependencies [[lein-clojars "0.9.1"]]
  :profiles {:test
             {:dependencies [[ring/ring-mock "0.3.2"]]}}
  :ring {:handler blockchain.routes/app}
  :plugins [[lein-environ "1.0.0"]
            [lein-ring "0.12.1"]])
