(ns sokoban.core
  (:gen-class)
  (require [cgsx.tools.matcher :refer :all :as matcher]
           [clojure.set :refer :all]))

(load-file "./src/sock2/socket.clj")
;(def s25 (startup-server 2222))

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
(def setup-ops
  '{set-floor
    {:pre (is ?patch empty)
     :del (is ?patch empty)          
     :add (is ?patch floor)
     :txt (?patch is a floor)
     :cmd (set-floor ?patch)
     :nl  ()
     }

    set-box
    {:pre (has ?patch box)
     :del (has ?patch box)
     :add (
            (is ?patch floor)
            (holds ?patch box)
          )
     :txt (?box is on floor ?patch)
     :cmd (set-box ?patch)
     :nl  ()
     }

    set-worker
    {:pre (has ?patch worker)
     :del (has ?patch worker)
     :add (
            (is ?patch floor)
            (holds ?patch worker)
          )
     :txt (?worker starts at floor ?patch)
     :cmd (set-worker ?patch)
     :nl  ()
     }

    set-bay
    {:pre (has ?patch bay)
     :del (has ?patch bay)
     :add (
            (is ?patch floor)
            (is ?patch bay)
          )
     :txt (?patch is a bay)
     :cmd (set-bay ?patch)
     :nl  ()
     }

     set-adj
    {:pre ((is ?patch floor)
           (is ?adj-patch floor)
            )
     :del ()
     :add ((adj ?patch ?adj-patch)
            )
     :txt (?adj-patch is adjacent to ?patch)
     :cmd (set-adj ?patch ?adj-patch)
     :nl  ()
     }
    }
  )

(def block-ops
  '{move
     {:pre (
             (is    ?agent      worker)
             (is    ?dest-patch floor)
             (adj   ?src-patch  ?dest-patch)
             (holds ?src-patch  ?agent)
             (holds ?dest-patch nil)
           )
      :del (
             (holds ?src-patch  ?agent)
             (holds ?dest-patch nil)
           )
      :add (
             (holds ?src-patch  nil)
             (holds ?dest-patch ?agent)
           )
      :txt (?agent moves to ?dest-patch)
      :cmd (move ?agent ?src-patch ?dest-patch)
      :nl  (  )
     }

    push-box
     {:pre (
             (is    ?box             box)
             (is    ?agent           worker)
             (is    ?dest-patch      floor)
             (adj   ?box-src-patch   ?dest-patch)
             (adj   ?agent-src-patch ?box-src-patch)
             (holds ?dest-patch      nil)
             (holds ?agent-src-patch ?agent)
             (holds ?box-src-patch   ?box)
           )
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
      :txt (?agent pushes ?box to ?dest-patch)
      :cmd (push ?box ?agent ?box-src-patch ?agent-src-patch ?dest-patch)
      :nl  (  )
     }
   }
  )


(defn apply-op
  [state {:keys [pre del add txt cmd nl]}]
  (mfind* [pre state]
          (union (mout add)
                 (difference state (mout del))
                 )))

;Cannot get operators to work for setup-ops. This is the state to be used with setup-floor
;(apply-op state1 (setup-ops 'set-floor))
(def state1
  '#{
     (is patch empty)
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