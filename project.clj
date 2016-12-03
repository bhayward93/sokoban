(defproject sokoban

(def block-ops
  '{ push-box
    { :pre ( (is ?box light)
             (adjacent ?char ?box)
             (is ?opposite-patch empty)
             (adjacent ?opposite-patch two-opposite-potential-patches) ;needs changing
             )
     :del (
            )
     :add (
            )
     :txt (?char pushes ?box)                             ;needs changing
     :cmd (move ?char ?box)
     }
    move-char
    { :pre ( (adjacent ?patch empty))
     :del (
            )
     :add (
            )
     :txt (move ?char to ?x ?y)                             ;needs changing
     :cmd (drop-at ?s)
     }
    })

