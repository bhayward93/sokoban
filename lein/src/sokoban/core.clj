(ns sokoban.core
  (:gen-class)
  (require [cgsx.tools.matcher :refer :all]
           [clojure.set :refer :all]))

(defn abs [n] (max n (- n)))

(defn ui-out [& input]
  (println input)
  )

;(load-file "./src/*") < Eliminates all of the below? 
; but for debugging its nice to not use socket.clj.

;NLP 
;(load-file "./src/nlp/lexicon.clj")
;(load-file "./src/nlp/morphologicalprocessor.clj")

;Core 
(load-file "./src/planner/planner(1a).clj")
(load-file "./src/Astar/Astar-search(2a).clj")
(load-file "./src/ops-search/ops-search(1b).clj")
(load-file "./src/sokoban/ops.clj")
(load-file "./src/sokoban/socket.clj")
(load-file "./src/sokoban/state-cleaning.clj")
;(load-file "./src/sokoban/route-building.clj")
(load-file "./src/astar-ops-search/astar-ops-search.clj")

(def initial-state
  (union world current-state)
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn exp [x n]
  (if (zero? n) 1
    (* x (exp x (dec n)))
    )
  )

(defn calc-dist [x1 y1 x2 y2]
  (Math/sqrt (+ (exp (- x2 x1) 2) (exp (- y2 y1) 2)))
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