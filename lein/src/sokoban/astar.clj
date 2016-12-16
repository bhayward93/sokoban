(defn a*lmg [state]
  (let [n (:state state)
        c (:cost cost)
       ]
    (list
      {:state () :cost ()}
      )
    )
  )

(defn move [world player x y]
  (mfind* ['([(+ (first player) x) (+ (second player) y) ?z] [?x ?y 1]) world] (? x, ?y, ?z))
  )
