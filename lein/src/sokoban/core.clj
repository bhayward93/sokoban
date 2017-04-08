(ns sokoban.core
  (:gen-class)
  (:require [cgsx.tools.matcher :refer :all]
            [clojure.set :refer :all]
            [sokoban.helpers.helpers :refer :all]
            [astar.astar-search :refer :all]
            [ops-search.ops-search :refer :all]
            [planner.planner :refer :all]
            [sokoban.socket.socket :refer :all]
            [sokoban.ops.ops :refer :all]
            [sokoban.route-building.route-building :refer :all]
            [sokoban.state-cleaning.state-cleaning :refer :all]
            [sokoban.astar-ops-search.astar-ops-search :refer :all]
            [sokoban.bsp.bsp :refer :all]
            [sokoban.bsp.helpers :refer :all]))

;(load-file "./src/*") < Eliminates all of the below? 
; but for debugging its nice to not use socket.clj.

;NLP 
;(load-file "./src/nlp/lexicon.clj")
;(load-file "./src/nlp/morphologicalprocessor.clj")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def s25 (open-socket 2222))
  (println "Awaiting new world setup commands...")
  (def world (receive-state s25 '#{}))
  (println "Awaiting new state setup commands...")
  (def current-state (receive-state s25 '#{}))
  (def initial-state (union world current-state)))


(defn collect-result [key result collected-results]
  (into '{} (concat collected-results (key result)))
  )

(defn extract-result
  "Extracts a list of values from the A* result matching a key."
  [cmd result-state]
  (remove nil? (map cmd result-state))
)

(defn complete-puzzle []
  (let [goals (get-goals initial-state)]
;    (get-routes goals initial-state '())
    )
  )

;(format-state object-state)
(defn format-state [state]
  (let [s (:state state)
        c (:cost state)]
    (apply-all lmg-ops s #{})
    )
 )

(defn alt-lmg [state]
  (let [s (:state state)
        w (:world state)
        c (:cost state)]
    (astar-apply-all lmg-ops-test s w c)
    )
  )
