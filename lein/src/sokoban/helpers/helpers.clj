(ns sokoban.helpers.helpers)

(defn abs
  "Returns the absolute value of a provided number."
  [n]
  (max n (- n)))

(defn exp
  "Returns... SOMETHING. I can't remember what."
  [x n]
  (if (zero? n) 1
    (* x (exp x (dec n)))
    )
  )

(defn calc-dist
  "Calculates the distance between two sets of co-ordinates."
  [x1 y1 x2 y2]
  (Math/sqrt (+ (exp (- x2 x1) 2) (exp (- y2 y1) 2)))
  )

(defn ui-out
  "Prints output to standard output."
  [& input]
  (println input)
  )
