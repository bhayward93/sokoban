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

(def initial-state
  (union world-state target-state)
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn get-routes [goals route-list]
  (cond
    (empty? goals) route-list
    :else (recur (rest goals) (cons (get (planner initial-state (first goals) box-ops) :cmds) route-list))
    )
  )

(defn complete-puzzle []
  (let [goals (get (planner initial-state '(complete) goal-ops) :cmds)]
    (get-routes goals '())
    )
  )
