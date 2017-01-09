(declare find-goals clear-goals)
  
(defn find-goals [state]
  (mfor ['(goal ?g) state] (mout '(goal ?g)))
  )

(defn clear-goals [state goals]
  (union (difference state goals) '((goal none)))
  )