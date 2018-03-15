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
POST /mine
```

**Get the full Blockchain.**

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

**Get all nodes.**

```sh
GET /nodes
```

**Resolve node conflicts.**

```sh
POST /nodes/resolve
```

## Contributing

Want to become a Blockchain [contributor](https://github.com/ThatGuyHughesy/blockchain/blob/master/CONTRIBUTORS.md)?  
Then checkout our [code of conduct](https://github.com/ThatGuyHughesy/blockchain/blob/master/CODE_OF_CONDUCT.md) and [contributing guidelines](https://github.com/ThatGuyHughesy/blockchain/blob/master/CONTRIBUTING.md).

## Copyright & License

Copyright (c) 2017 Conor Hughes - Released under the MIT license.