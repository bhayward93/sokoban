(ns sokoban.astar-ops-search.astar-ops-search
  (:require [cgsx.tools.matcher :refer :all]
            [clojure.set :refer :all]))

(declare astar-apply-op get-locality astar-apply-all)

(def test-ops
  '{:move
    {:range ('((:eval (- (? x) 2)) ?y)
             '((:eval (- (? x) 1)) ?y)
             '((:eval (+ (? x) 1)) ?y)
             '((:eval (+ (? x) 2)) ?y)
             '(?x (:eval (- (? y) 2)))
             '(?x (:eval (- (? y) 1)))
             '(?x (:eval (+ (? y) 1)))
             '(?x (:eval (+ (? y) 2)))
             )
     }
    }
  )

(defn astar-apply-op [op state world cost]
  (mfor* [(:when op) (seq state)]
         (mfor* [(:if op) (get-locality (mout (:at op)) (mout (:range op)) world)]
                (mlet ['?cost cost]
                      {:state (union (set (mout (:state (:add op))))
	                               (difference state (set (mout (:state (:del op))))))
                       :world (union (set (mout (:world (:add op))))
	                              (difference world (set (mout (:world (:del op))))))
	                     :cmd   (mout (:cmd op))
	                     :txt   (mout (:txt op))
	                     :cost  (mout (:cost op))
                      }
                      )
           )
   )
  )

(defn get-locality [co-ordinates range world]
                    (mlet ['(?wx ?wy) co-ordinates]
	                    (intersection world
	                                  (set (mout range)))
                     )
                    )

(defn astar-apply-all [ops state world cost]
  (reduce concat
    (map (fn [x] (astar-apply-op (x ops) state world cost))
      (keys ops)
      )))
