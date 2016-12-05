(ns sokoban.core
  (:gen-class))

(load-file "../sock2/socket.clj")
(def s25 (startup-server 2222))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;(def facts
;    '((non-manipulable wall) (non-manipulable large-box)
;       (manipulable box) (light box)
;       (heavy large-box) (heavy wall)
;       )
;    )
;
;  (def rules
;    '((rule 0 (is ?box manipulable)
;        (adjacent ?x ?box)
;        (is ?opposite-patch empty)
;        (adjacent ?opposite-patch two-opposite-potential-patches) ;needs changing
;        => (pushable ?box))
;       (rule 1 (is empty ?patch)(adjacent ?x patch) => moveable)
;       ))
;
;ADDED THE FACTS AND RULES IN SO BELOW NEEDS CHANGING
;(def block-ops
;  '{ push-box
;    { :pre ( (is ?box light)
;             (adjacent ?char ?box)
;             (is ?opposite-patch empty)
;             (adjacent ?opposite-patch two-opposite-potential-patches) ;needs changing
;             )
;     :del (
;            )
;     :add (
;            )
;     :txt (?char pushes ?box)                             ;needs changing
;     :cmd (move ?char ?box)
;     }
;    move-char
;    { :pre ( (adjacent ?patch empty))
;     :del (
;            )
;     :add (
;            )
;     :txt (move ?char to ?x ?y)                             ;needs changing
;     :cmd (drop-at ?s)
;     }
;    }
