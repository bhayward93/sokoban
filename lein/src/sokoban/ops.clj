;    (planner (conj world-state object-state) '(at patch-4-8 worker-3) goal-ops)

(def step-ops
  '{:move
     {:name move
     :achieves (at '(?dx ?dy) ?w)
     :when (
             (at '(?wx ?wy) ?w)
             (on '(?sx ?sy) none)
             (on '(?dx ?dy) none)
             (connects '(?sx ?sy) '(?dx ?dy))
             (goal ?g)
             (:guard (and (< (calc-dist (? sx) (? sy) (? wx) (? wy))
                             (calc-dist (? dx) (? dy) (? wx) (? wy))
                             )
                          (not= 'none (? g))
                          )
                    )
             (available '(?sx ?sy))
             )
     :post ((unavailable '(?sx ?sy)) (destination '(?dx ?dy) ?g) (at '(?sx ?sy) ?w))
     :pre ((destination '(?dx ?dy) ?g))
     :del ((at '(?sx ?sy) ?w) (destination '(?sx ?sy) ?g))
     :add ((at '(?dx ?dy) ?w))
     :cmd (move ?w '(?dx ?dy))
     :txt (?w moves to '(?dx ?dy))
     }
   
    :move-back
    {:name move-back
     :achieves (at '(?dx ?dy) ?w)
     :when (
             (isa ?w worker)
             (at '(?wx ?wy) ?w)
             (on '(?sx ?sy) none)
             (connects '(?sx ?sy) '(?dx ?dy))
             (goal ?g)
             (available '(?sx ?sy))
             (:guard (not= 'none (? g)))
             )
     :post ((unavailable '(?sx ?sy)) (destination '(?dx ?dy) ?g) (at '(?sx ?sy) ?w))
     :pre ((destination '(?dx ?dy) ?g))
     :del ((at '(?sx ?sy) ?w) (destination '(?sx ?sy) ?g))
     :add ((at '(?dx ?dy) ?w))
     :cmd (move ?w '(?dx ?dy))
     :txt (?w moves to '(?dx ?dy))
     }
    
    :push-box
    {:name push-box
     :achieves (on '(?dx ?dy) ?b)
     :when (
             (isa ?w worker)
             (at (?swx ?swy) ?w)
             (on '(?bx ?by) ?b)
             (on '(?wx ?wy) ?o)
             (connects '(?wx ?wy) '(?sx ?sy))
             (connects '(?sx ?sy) '(?dx ?dy))
             (:guard (and (or (and (>= (abs (- (? dx) (? bx)))
                                       (abs (- (? dy) (? by)))
                                       )
                                   (< (abs (- (? sx) (? bx)))
                                      (abs (- (? dx) (? bx)))
                                      )
                                   (> (abs (- (? wx) (? dx)))
                                      (abs (- (? sx) (? dx)))
                                      )
                                   )
                              (and (>= (abs (- (? dy) (? by)))
                                       (abs (- (? dx) (? bx)))
                                       )
                                   (< (abs (- (? sy) (? by)))
                                      (abs (- (? dy) (? by)))
                                      )
                                   (> (abs (- (? wy) (? dy)))
                                      (abs (- (? sy) (? dy)))
                                      )
                               )
                              )
                          (not= 'none (? g))
                          (or (= 'none (? o))
                              (= (? b) (? o))
                              )
                          )
                     )
             (available '(?sx ?sy))
             )
     :post ((unavailable '(?sx ?sy)) (destination '(?dx ?dy) ?g) (on '(?sx ?sy) ?b))
     :pre ((destination '(?dx ?dy) ?g))
     :del ((on '(?sx ?sy) ?b) (on '(?dx ?dy) none) (at '(?wx ?wy) ?w) (destination '(?sx ?sy) ?g))
     :add ((on '(?sx ?sy) none) (on '(?dx ?dy) ?b) (at '(?sx ?sy) ?w))
     :cmd (push-box ?w '(?dx ?dy))
     :txt (?w pushes ?b to '(?dx ?dy))
     }
    
    :remove-goal
    {:name remove-goal
     :achieves (remove-goal ?g)
     :del ((goal ?g))
     :add ((goal none))
     }
     
    :finish
    {:name finish
     :achieves (?loc '(?dx ?dy) ?i)
     :when (
             (goal none)
             (:guard (or (= 'on (? loc))
                         (= 'at (? loc))
                         )
                     )
             )
     :post ((unavailable '(?dx ?dy)) (goal '(?loc '(?dx ?dy) ?i)) (?loc '(?dx ?dy) ?i) (remove-goal '(?loc '(?dx ?dy) ?i)))
     }
     
		:set-goal
		{:name set-goal
		 :achieves (goal ?g)
     :del ((goal none))
		 :add ((goal ?g))
		 }
    
    :mark-tile
    {:name mark-tile
     :achieves (unavailable ?d)
     :del ((available ?d))
     }
  
		:protect-dest
		{:name protect-dest
		 :achieves (destination ?d ?g)
		 :add ((destination ?d ?g))
		 }
    }
  )

(def worker-ops
  '{:move-junctions
    {:name move-junctions
	    :achieves (at '(?dx ?dy) ?w)
	    :when (
              (isa ?w worker)
              (at '(?wx ?wy) ?w)
              (on '(?sx ?sy) none)
	            (links '(?sx ?sy) '(?dx ?dy))
              (available '(?sx ?sy))
              (goal ?g)
              (:guard (and (or (and (>= (abs (- (? dx) (? wx)))
                                        (abs (- (? dy) (? wy)))
                                        )
                                    (< (abs (- (? sx) (? wx)))
                                       (abs (- (? dx) (? wx)))
                                       )
                                    )
                                (and (>= (abs (- (? dy) (? wy)))
                                         (abs (- (? dx) (? wx)))
                                     )
                                     (< (abs (- (? sy) (? wy)))
                                        (abs (- (? dy) (? wy)))
                                        )
                                 )
                                )
                           (not= 'none (? g))
                           )
                      )
              )
     :post ((unavailable '(?sx ?sy)) (at '(?sx ?sy) ?w))
     :del ((at '(?sx ?sy) ?w))
     :add ((at '(?dx ?dy) ?w))
     :cmd ((at '(?dx ?dy) ?w))
     }
    
    :move
    {:name move
     :achieves (at '(?dx ?dy) ?w)
     :when (
             (isa ?w worker)
             (at '(?wx ?wy) ?w)
             (on '(?sx ?sy) none)
             (connects '(?sx ?sy) '(?dx ?dy))
             (available '(?sx ?sy))
             (goal ?g)
	           (:guard (and (or (and (>= (abs (- (? dx) (? wx)))
	                                     (abs (- (? dy) (? wy)))
	                                     )
	                                 (< (abs (- (? sx) (? wx)))
	                                    (abs (- (? dx) (? wx)))
	                                    )
	                                 )
                              (and (>= (abs (- (? dy) (? wy)))
                                       (abs (- (? dx) (? wx)))
                                       )
                                   (< (abs (- (? sy) (? wy)))
                                      (abs (- (? dy) (? wy)))
                                      )
                                )
                             )
                         (not= 'none (? g))
                         )
	                   )
             )
     :post ((unavailable '(?sx ?sy)) (at '(?sx ?sy) ?w))
     :del ((at '(?sx ?sy) ?w))
     :add ((at '(?dx ?dy) ?w))
     :cmd ((at '(?dx ?dy) ?w))
     }
    
    :move-junctions-alt
    {:name move-junctions-alt
	    :achieves (at '(?dx ?dy) ?w)
	    :when (
              (isa ?w worker)
              (at '(?wx ?wy) ?w)
              (on '(?sx ?sy) none)
	            (links '(?sx ?sy) '(?dx ?dy))
              (available '(?sx ?sy))
              (goal ?g)
              (:guard (not= 'none (? g)))
              )
     :post ((unavailable '(?sx ?sy)) (block '(?sx ?sy) '(?dx ?dy)) (at '(?sx ?sy) ?w))
     :del ((at '(?sx ?sy) ?w))
     :add ((at '(?dx ?dy) ?w))
     :cmd ((at '(?dx ?dy) ?w))
     }
    
    :move-alt
    {:name move-alt
     :achieves (at '(?dx ?dy) ?w)
     :when (
             (isa ?w worker)
             (at '(?wx ?wy) ?w)
             (on '(?sx ?sy) none)
             (connects '(?sx ?sy) '(?dx ?dy))
             (available '(?sx ?sy))
             (goal ?g)
             (:guard (not= 'none (? g)))
             )
     :post ((unavailable '(?sx ?sy)) (at '(?sx ?sy) ?w))
     :del ((at '(?sx ?sy) ?w))
     :add ((at '(?dx ?dy) ?w))
     :cmd ((at '(?dx ?dy) ?w))
     }
    
    :block-link
    {:name block-link
     :achieves (block '(?sx ?sy) '(?dx ?dy))
     :when (
             (links '(?sx ?sy) '(?dx ?dy))
             (connects '(?mx ?my) '(?sx ?sy))
             (:guard (or (< (abs (- (? mx) (? dx)))
                            (abs (- (? sx) (? dx)))
                            )
                         (< (abs (- (? my) (? dy)))
                            (abs (- (? sy) (? dy)))
                            )
                         )
                     )
             )
     :del ((available '(?mx ?my)))
     }
    
    :finish
    {:name finish
     :achieves (at '(?dx ?dy) ?w)
     :when ((goal none))
     :post ((unavailable '(?dx ?dy)) (goal '(at '(?dx ?dy) ?w)) (at '(?dx ?dy) ?w))
     }
     
		:set-goal
		{:name set-goal
		 :achieves (goal ?g)
     :del ((goal none))
		 :add ((goal ?g))
		 }
  
    :mark-junction
    {:name mark-junction
     :achieves (unavailable ?j)
     :del ((available ?j))
     }

    }
  )

(def box-ops
  '{:push-junctions
    {:name push-junctions
	    :achieves (on '(?dx ?dy) ?b)
	    :when (
              (isa ?w worker)
              (at (?swx ?swy) ?w)
              (on '(?bx ?by) ?b)
              (on '(?wx ?wy) none)
              (connects '(?wx ?wy) '(?sx ?sy))
	            (links '(?sx ?sy) '(?dx ?dy))
              (available '(?sx ?sy))
              (goal ?g)
              (:guard (and (or (and (>= (abs (- (? dx) (? bx)))
                                        (abs (- (? dy) (? by)))
                                        )
                                    (< (abs (- (? sx) (? bx)))
                                       (abs (- (? dx) (? bx)))
                                       )
                                    (> (abs (- (? wx) (? dx)))
                                       (abs (- (? sx) (? dx)))
                                       )
                                    )
                                (and (>= (abs (- (? dy) (? by)))
                                         (abs (- (? dx) (? bx)))
                                     )
                                     (< (abs (- (? sy) (? by)))
                                        (abs (- (? dy) (? by)))
                                        )
                                     (> (abs (- (? wy) (? dy)))
                                        (abs (- (? sy) (? dy)))
                                        )
                                 )
                                )
                           (not= 'none (? g))
                           )
                      )
              )
     :post ((unavailable '(?sx ?sy)) (on '(?sx ?sy) ?b) (at '(?wx ?wy) ?w))
     :del ((on '(?sx ?sy) ?b) (on '(?dx ?dy) none) (at '(?swx ?swy) ?w))
     :add ((on '(?dx ?dy) ?b) (on '(?sx ?sy) none) (at '(?wx ?wy) ?w))
     :cmd ({
            :del ((on '(?sx ?sy) ?b) (on '(?dx ?dy) none) (at '(?swx ?swy) ?w))
            :add ((on '(?dx ?dy) ?b) (on '(?sx ?sy) none) (at '(?wx ?wy) ?w))
            :cmd ((on '(?dx ?dy) ?b))
            })
     }
    
    :push-box
    {:name push-box
     :achieves (on '(?dx ?dy) ?b)
     :when (
             (isa ?w worker)
             (at (?swx ?swy) ?w)
             (on '(?bx ?by) ?b)
             (connects '(?wx ?wy) '(?sx ?sy))
             (connects '(?sx ?sy) '(?dx ?dy))
             (available '(?sx ?sy))
             (goal ?g)
	           (:guard (and (or (and (>= (abs (- (? dx) (? bx)))
	                                     (abs (- (? dy) (? by)))
	                                     )
	                                 (< (abs (- (? sx) (? bx)))
	                                    (abs (- (? dx) (? bx)))
	                                    )
	                                 (> (abs (- (? wx) (? dx)))
	                                    (abs (- (? sx) (? dx)))
	                                    )
	                                 )
                              (and (>= (abs (- (? dy) (? by)))
                                    (abs (- (? dx) (? bx)))
                                    )
                                (< (abs (- (? sy) (? by)))
                                   (abs (- (? dy) (? by)))
                                   )
                                (> (abs (- (? wy) (? dy)))
                                   (abs (- (? sy) (? dy)))
                                   )
                                )
                             )
                         (not= 'none (? g))
                         )
	                   )
             )
     :post ((unavailable '(?sx ?sy)) (on '(?sx ?sy) ?b) (at '(?wx ?wy) ?w))
     :del ((on '(?sx ?sy) ?b) (on '(?dx ?dy) none) (at '(?swx ?swy) ?w))
     :add ((on '(?dx ?dy) ?b) (on '(?sx ?sy) none) (at '(?wx ?wy) ?w))
     :cmd ({
            :del ((on '(?sx ?sy) ?b) (on '(?dx ?dy) none) (at '(?swx ?swy) ?w))
            :add ((on '(?dx ?dy) ?b) (on '(?sx ?sy) none) (at '(?wx ?wy) ?w))
            :cmd ((on '(?dx ?dy) ?b))
            })
     }
    
    :push-junctions-alt
    {:name push-junctions-alt
	    :achieves (on '(?dx ?dy) ?b)
	    :when (
              (isa ?w worker)
              (at (?swx ?swy) ?w)
              (on '(?bx ?by) ?b)
              (on '(?wx ?wy) none)
              (connects '(?wx ?wy) '(?sx ?sy))
	            (links '(?sx ?sy) '(?dx ?dy))
              (available '(?sx ?sy))
              (goal ?g)
              (:guard (and (or (and (> (abs (- (? wx) (? dx)))
                                       (abs (- (? sx) (? dx)))
                                       )
                                    (= (? wy) (? dy))
                                    )
                               (and (> (abs (- (? wy) (? dy)))
                                       (abs (- (? sy) (? dy)))
                                       )
                                    (= (? wx) (? dx))
                                    )
                               )
                           (not= 'none (? g))
                           )
                      )
              )
     :post ((unavailable '(?sx ?sy)) (block '(?sx ?sy) '(?dx ?dy)) (on '(?sx ?sy) ?b) (at '(?wx ?wy) ?w))
     :del ((on '(?sx ?sy) ?b) (on '(?dx ?dy) none) (at '(?swx ?swy) ?w))
     :add ((on '(?dx ?dy) ?b) (on '(?sx ?sy) none) (at '(?wx ?wy) ?w))
     :cmd ({
            :del ((on '(?sx ?sy) ?b) (on '(?dx ?dy) none) (at '(?swx ?swy) ?w))
            :add ((on '(?dx ?dy) ?b) (on '(?sx ?sy) none) (at '(?wx ?wy) ?w))
            :cmd ((on '(?dx ?dy) ?b))
            })
     }
    
    :block-link
    {:name block-link
     :achieves (block '(?sx ?sy) '(?dx ?dy))
     :when (
             (links '(?sx ?sy) '(?dx ?dy))
             (connects '(?mx ?my) '(?sx ?sy))
             (:guard (or (< (abs (- (? mx) (? dx)))
                            (abs (- (? sx) (? dx)))
                            )
                         (< (abs (- (? my) (? dy)))
                            (abs (- (? sy) (? dy)))
                            )
                         )
                     )
             )
     :del ((available '(?mx ?my)))
     }
    
    :move
    {:name move
     :achieves (at '(?dx ?dy) ?w)
     :when ((at '(?sx ?sy) ?w))
     :cmd ((at '(?dx ?dy) ?w))
     :del ((at '(?sx ?sy) ?w))
     :add ((at '(?dx ?dy) ?w))
     }
    
    :finish
    {:name finish
     :achieves (on '(?dx ?dy) ?b)
     :when ((goal none))
     :post ((unavailable '(?dx ?dy)) (goal '(on '(?dx ?dy) ?b)) (on '(?dx ?dy) ?b))
     }
     
		:set-goal
		{:name set-goal
		 :achieves (goal ?g)
     :del ((goal none))
		 :add ((goal ?g))
		 }
  
    :mark-junction
    {:name mark-junction
     :achieves (unavailable ?j)
     :del ((available ?j))
     }
    }
  )

(def goal-ops
  '{:load-bay
    {:name load-bay
     :achieves (loaded '(?dx ?dy) ?b)
     :when (
             (unloaded '(?dx ?dy))
             (on '(?sx ?sy) ?b)
             (isa ?b box)
             )
     :post ((mark '(?dx ?dy) ?b) (find-bay ?b))
     :cmd ((on '(?dx ?dy) ?b))
     }
    
    :find-bay
    {:name find-bay
     :achieves (find-bay ?b)
     :when (
             (unloaded '(?dx ?dy))
             (isa ?b box)
             )
     :post ((loaded '(?dx ?dy) ?b))
     }
      
    :mark
    {:name mark
     :achieves (mark '(?dx ?dy) ?b)
     :del ((unloaded '(?dx ?dy)))
     :add ((loaded '(?dx ?dy) ?b))
     }
    
    :unload-bay
    {:name unload-bay
     :achieves (unloaded '(?dx ?dy) ?b)
     :when (
             (loaded '(?dx ?dy) ?b)
             )
     :post ((unmark '(?dx ?dy) ?b) (find-marked-bay ?b))
     }
    
    :unmark
    {:name unmark
     :achieves (unmark '(?dx ?dy) ?b)
     :del ((loaded '(?dx ?dy) ?b))
     :add ((unloaded '(?dx ?dy)))
     }
    
    
    :find-marked-bay
    {:name find-marked-bay
     :achieves (find-marked-bay ?b)
     :when (
              (loaded '(?dx ?dy) ?b)
              )
     :post ((unloaded '(?dx ?dy) ?b))
     }
      
    :mark-box
    {:name mark-box
     :achieves (mark-box ?b)
     :when ((isa ?b box))
     :post ((loaded '(?dx ?dy) ?b) (unloaded '(?dx ?dy) ?b))
     :del ((isa ?b box))
     }
    
    :complete
    {:name complete
     :achieves (complete)
     :when ((isa ?b box))
     :post ((mark-box ?b) (complete))
     :del ((isa ?b box))
     }
    }
 )


;    :nudge
;    {:name nudge
;     :achieves (at '(?dx ?dy) ?w)
;     :when (
;             (isa ?w worker)
;             (on '(?sx ?sy) ?b)
;             (on '(?dx ?dy) none)
;             (on '(?xx ?xy) none)
;             (at '(?swx ?swy) none) 
;             (connects '(?sx ?sy) '(?dx ?dy))
;             (connects '(?swx ?swy) '(?sx ?sy))
;             (connects '(?dx ?dy) '(?xx ?xy))
;             (available '(?dx ?dy))
;             (goal ?g)
;             (guard (or (= (? swx) (? sx) (? dx) (? xx))
;                        (= (? swy) (? sy) (? dy) (? xy))
;                        )
;                    )
;             )
;     :post ((at '(?dx ?dy) ?w) (on '(?dx ?dy) ?b) (at '(?sx ?sy) ?w) (on '(?sx ?sy) ?b))
;     }


(def lmg-ops-test
  '{:move
    {:name move
     :when ((at '(?wx ?wy) ?w))
     :if ((2 '(?dx ?dy)))
     :at '(?wx ?wy)
     :range ((2 '((:eval (- (? wx) 1)) ?wy))
             (2 '((:eval (+ (? wx) 1)) ?wy))
             (2 '(?wx (:eval (- (? wy) 1))))
             (2 '(?wx (:eval (+ (? wy) 1))))
             )
     :add {:world ()
           :state ((at '(?dx ?dy) w0))}
     :del {:world ()
           :state ((at '(?wx ?wy) w0))}
     :cmd ()
     :txt (w0 moves to '(?dx ?dy))
     :cost (:eval (+ (? cost) 2))
     }
    
    :push-box
    {:name push-box
     :when ((at '(?wx ?wy) ?w)
            (on '(?bx ?by) ?b))
     :if (
           (1 '(?bx ?by))
           (2 '(?dx ?dy))
           (:guard (or (and (= (? dx) (? bx) (? wx))
                            (= 1 (abs (- (? dy) (? by))))
                            (= 1 (abs (- (? wy) (? by))))
                            (not= (? dy) (? wy))
                            )
                       (and (= (? dy) (? by) (? wy))
                            (= 1 (abs (- (? dx) (? bx))))
                            (= 1 (abs (- (? wx) (? bx))))
                            (not= (? dx) (? wx))
                            )
                       )
                   )
           )
     :at '(?wx ?wy)
     :range ((2 '((:eval (- (? wx) 2)) ?wy))
             (1 '((:eval (- (? wx) 1)) ?wy))
             (1 '((:eval (+ (? wx) 1)) ?wy))
             (2 '((:eval (+ (? wx) 2)) ?wy))
             (2 '(?wx (:eval (- (? wy) 2))))
             (1 '(?wx (:eval (- (? wy) 1))))
             (1 '(?wx (:eval (+ (? wy) 1))))
             (2 '(?wx (:eval (+ (? wy) 2))))
             )
     :add {:world ((1 '(?dx ?dy)) (2 '(?bx ?by)))
           :state ((on '(?dx ?dy) ?b) (at '(?bx ?by) w0))}
     :del {:world ((2 '(?dx ?dy)) (1 '(?bx ?by)))
           :state ((on '(?bx ?by) ?b) (at '(?wx ?wy) w0))}
     :cmd ()
     :txt (?b pushed to '(?dx ?dy))
     :cost (:eval (+ (? cost) 1))
     }
    }
  )
  
(def lmg-ops
 '{:move
   {:name move
    :achieves ((at '(?dx ?dy) w0))
    :pre (
           (at '(?sx ?sy) w0)
           (on '(?dx ?dy) none)
           (:guard (or (and (= (? dx) (? sx))
                            (= 1 (abs (- (? dy) (? sy))))
                            )
                       (and (= (? dy) (? sy))
                            (= 1 (abs (- (? dx) (? sx))))
                            )
            )
           )
           )
    :add ((at '(?dx ?dy) w0))
    :del ((at '(?sx ?sy) w0))
    :cmd ()
    :txt (w0 moves to '(?dx ?dy))
    :cost 2
    }
   
   :push-box
   {:name push-box
    :achieves ((on '(?dx ?dy) ?b))
    :pre (
           (on '(?sx ?sy) ?b)
           (at '(?wx ?wy) w0)
           (on '(?dx ?dy) none)
           (isa ?b box)
           (:guard (or (and (= (? dx) (? sx) (? wx))
                            (= 1 (abs (- (? dy) (? sy))))
                            (= 1 (abs (- (? wy) (? sy))))
                            (not= (? dy) (? wy))
                            )
                       (and (= (? dy) (? sy) (? wy))
                            (= 1 (abs (- (? dx) (? sx))))
                            (= 1 (abs (- (? wx) (? sx))))
                            (not= (? dx) (? wx))
                            )
                       )
                   )
           )
    :add ((on '(?dx ?dy) ?b) (on '(?sx ?sy) none) (at '(?sx ?sy) w0))
    :del ((on '(?sx ?sy) ?b) (on '(?dx ?dy) none) (at '(?wx ?wy) w0))
    :cmd ()
    :txt (?b pushed to '(?dx ?dy))
    :cost 1
    }
   }
 )