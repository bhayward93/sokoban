(ns sokoban.core
  (:gen-class)
  (require [cgsx.tools.matcher :refer :all :as matcher]
           [clojure.set :refer :all]))

(load-file "./src/sock2/socket.clj")
(load-file "./src/planner/planner(1a).clj")
(load-file "./src/Astar/Astar-search(2a).clj")
(load-file "./src/ops-search/ops-search(1b).clj")

(def s25 (startup-server 2222))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def facts
    '((non-manipulable wall) (non-manipulable large-box)
       (manipulable box) (light box)
       (heavy large-box) (heavy wall)
       )
    )

  (def rules
    '((rule 0 (is ?box manipulable)
        (adjacent ?x ?box)
        (is ?opposite-patch empty)
        (adjacent ?opposite-patch two-opposite-potential-patches) ;needs changing
        => (pushable ?box))
       (rule 1 (is empty ?patch)(adjacent ?x patch) => moveable)
       ))

;ADDED THE FACTS AND RULES IN SO BELOW NEEDS CHANGING
(def block-ops
  '{move
     {:name move
      :achieves (holds ?dest-patch ?agent)
      :when (
              (is    ?agent      worker)
              (is    ?dest-patch floor)
              (adj   ?src-patch  ?dest-patch)
              (holds ?src-patch  ?agent)
              (holds ?dest-patch nil)
              )
      :post ()
      :pre ()
      :del (
             (holds ?src-patch  ?agent)
             (holds ?dest-patch nil)
           )
      :add (
             (holds ?src-patch  nil)
             (holds ?dest-patch ?agent)
           )
      :cmd ((move ?agent ?dest-patch))
      :txt (?agent moves to ?dest-patch)
      :nl  (  )
     }

    push-box
     {:name push-box
      :achieves (on ?dest-patch ?agent)
      :when (
              (is    ?box             box)
              (is    ?agent           worker)
              (is    ?dest-patch      floor)
              (adj   ?box-src-patch   ?dest-patch)
              (adj   ?agent-src-patch ?box-src-patch)
              (holds ?dest-patch      nil)
              (holds ?agent-src-patch ?agent)
              (holds ?box-src-patch   ?box)
              )
      :post ((holds ?agent-src-patch ?agent))
      :pre ()
      :del (
             (holds ?dest-patch      nil)
             (holds ?agent-src-patch ?agent)
             (holds ?box-src-patch   ?box)
           )
      :add (
             (holds ?dest-patch      ?box)
             (holds ?agent-src-patch nil)
             (holds ?box-src-patch   ?agent)
           )
      :cmd ((push-box ?box))
      :txt (?agent pushes ?box to ?dest-patch)
      :nl  (  )
     }
   }
  )

(defn receive-state
  [state-list]
    (let [new-state (socket-read s25)]
      (println new-state)
      (if (= new-state -1)
        state-list
        (receive-state (conj state-list new-state))
        )
      )
  )

(defn apply-op
  [state {:keys [pre del add txt cmd nl]}]
  (mfind* [pre state]
          (union (mout add)
                 (difference state (mout del))
          cmd
                 )))

;Cannot get operators to work for setup-ops. This is the state to be used with setup-floor
;(apply-op state1 (setup-ops 'set-floor))
(def floor-state
  '#{
     (is ?patch empty)
     })
(def box-state
  '#{
     (has ?patch box)
     })
(def worker-state
  '#{
     (has ?patch worker)
     })
(def bay-state
  '#{
     (has ?patch bay)
     })

;The below command will show th result of moving, feeding in already predetermined to be matching pre-conds
;(apply-op state2 (block-ops 'move))

;This is the state for block-ops move to trigger
(def state2
'#{(is    ?agent      worker)
            (is    ?dest-patch floor)
            (adj   ?src-patch  ?dest-patch)
            (holds ?src-patch  ?agent)
            (holds ?dest-patch nil)})

(def goal-state
  '#{
    (is ?patch1 bay)(holds ?patch1 ?agent1)(is ?agent1 box)
    (is ?patch2 bay)(holds ?patch2 ?agent2)(is ?agent2 box)
    (is ?patch3 bay)(holds ?patch3 ?agent3)(is ?agent3 box)
  }
)

(defn a*lmg [state]
  (let [n (:state state)]
    (list
      
      )
    )
  )

;(eval (read-string (socket-read) ) )

(println "Awaiting new world state setup commands...")
(def state-list (receive-state #{}))
