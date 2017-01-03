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

(defn get-goals [world-state]
 (let [state (planner world-state '(complete) goal-ops)]
  
	 (cond
	   (not= world-state state) (get-goals (union state target-state))
	   :else
	   state
	   )
	 )
 )

(defn complete-puzzle []
  (planner initial-state '(complete) goal-ops)
  )