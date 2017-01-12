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
     :cmd ((on '(?dx ?dy) ?b))
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
     :cmd ((on '(?dx ?dy) ?b))
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
     :cmd ((on '(?dx ?dy) ?b))
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

 (def lmg-ops
  '{:move
    {:name move
     :achieves (at ?next-patch w0)
     :when ()
     :post ()
     :pre ((at ?patch w0) (connects ?patch ?next-patch)(on ?next-patch none)(isa w0 worker))
     :add (at ?next-patch w0)
     :del (at ?patch w0)
     :cmd ()
     :txt (w0 moves to ?next-patch)
     :cost ()
     }
 }
  )