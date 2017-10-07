# blockchain
Clojure implementation of blockchain inspired by this [article](https://hackernoon.com/learn-blockchains-by-building-one-117428612f46)

[![Build Status](https://travis-ci.org/ThatGuyHughesy/blockchain.svg?branch=master)](https://travis-ci.org/ThatGuyHughesy/blockchain)

## Development

Run locally
```sh
$ lein ring server-headless
```

Testing
```sh
$ lein test
```

## Endpoints

**Add a new transaction to a block.**

```sh
POST /transactions/new
```

Parameters

| Name          | Type          | Description
| ------------- | ------------- |------------------------- |
| sender        | string        | Who is sending the coins |
| recipient     | string        | Who to send the coins to |
| amount        | int           | Number of coins          |

**Mine a new block.**

```sh
GET /mine
```

**Return the full Blockchain.**

```sh
GET /chain
```

**Add a new node.**

```sh
POST /nodes/new
```

Parameters

| Name          | Type          | Description
| ------------- | ------------- |------------------------- |
| address       | url           | URL of node              |

**Resolve node conflicts.**

```sh
GET /nodes/resolve
```

## Copyright & License

Copyright (c) 2017 Conor Hughes - Released under the MIT license.