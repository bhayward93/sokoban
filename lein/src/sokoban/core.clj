(ns sokoban.core
  (:gen-class)
  (require [cgsx.tools.matcher :refer :all]
           [clojure.set :refer :all]))

(defn ui-out [& input]
  (println input)
  )

(load-file "./src/planner/planner(1a).clj")
(load-file "./src/Astar/Astar-search(2a).clj")
(load-file "./src/ops-search/ops-search(1b).clj")
(load-file "./src/sokoban/ops.clj")
(load-file "./src/sokoban/socket.clj")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def goal-state
  '#{
    (is patch-5-8 loaded)
    (is patch-2-5 loaded)
    (is patch-6-2 loaded)
  }
)
