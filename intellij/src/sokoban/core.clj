(ns
  ^{:author p4048801}
  sokoban.core)

(load-file "../sock2/socket.clj")
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
