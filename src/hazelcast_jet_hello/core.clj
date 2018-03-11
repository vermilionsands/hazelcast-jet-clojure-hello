(ns hazelcast-jet-hello.core
  (:import [com.hazelcast.jet Jet Pipeline Source Sources Traversers Sinks]
           [com.hazelcast.jet.function DistributedFunction DistributedPredicate DistributedFunctions]
           [com.hazelcast.jet.aggregate AggregateOperations])
  (:gen-class))

(def pipeline
  (let [p (Pipeline/create)]
    (.drainTo
      (.groupBy
        (.filter
          (.flatMap
            (.drawFrom p (Sources/list "text"))
            (reify DistributedFunction
              (apply [_ word]
                (Traversers/traverseArray (.split (.toLowerCase ^String word) "\\W+")))))
          (reify DistributedPredicate
            (test [_ x]
              (not (.isEmpty x)))))
        (DistributedFunctions/wholeItem) (AggregateOperations/counting))
      (Sinks/map "counts"))
    p))

(defn liftoff! []
  (try
    (let [jet  (Jet/newJetInstance)
          text (doto (.getList jet "text")
                 (.add "hello world hello hello world")
                 (.add "world world world hello world"))
          _ (.join (.newJob jet ^Pipeline pipeline))
          result (into {} (.getMap jet "counts"))]
      (println "Count of hello:" (result "hello"))
      (println "Count of world:" (result "world")))
    (finally
        (Jet/shutdownAll))))