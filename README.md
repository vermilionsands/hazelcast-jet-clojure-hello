# hazelcast-jet-hello

Hazelcast Jet getting started example in Clojure

## Usage

In repl REPL:
```clj
(require '[hazelcast-jet-hello.core :as hz])

(hz/liftoff!)
```

You should see Hazelcast Jet starting and printing in the REPL:
```clj
Count of hello: 4
Count of world: 6
```

## License

Copyright © 2018 vermilionsands

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
