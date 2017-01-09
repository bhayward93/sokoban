(ns sokoban.core
  (:gen-class)
  (require [cgsx.tools.matcher :refer :all]
           [clojure.set :refer :all]))

(defn abs [n] (max n (- n)))

(defn ui-out [& input]
  (println input)
  )

(load-file "./src/planner/planner(1a).clj")
;(load-file "./src/Astar/Astar-search(2a).clj")
;(load-file "./src/ops-search/ops-search(1b).clj")
(load-file "./src/sokoban/ops.clj")
(load-file "./src/sokoban/socket.clj")
(load-file "./src/sokoban/state-cleaning.clj")
(load-file "./src/sokoban/route-building.clj")

(def initial-state
  (union world-state target-state)
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn complete-puzzle []
  (let [goals (get-goals initial-state)]
;    (get-routes goals initial-state '())
    )
  )