(def goal-ops
  '{
;    (planner (conj world-state object-state) '(at patch-4-8 worker-3) goal-ops)

    :move-x
     {:name move-x
     :achieves (at '(?dx ?dy) ?w)
     :when (
             (at '(?wx ?wy) ?w)
             (on '(?sx ?sy) none)
             (on '(?dx ?dy) none)
             (connects '(?sx ?sy) '(?dx ?dy))
             (goal ?g)
             (:guard (and (>= (abs (- (? dx) (? wx)))
                             (abs (- (? dy) (? wy)))
                             )
                          (< (abs (- (? sx) (? wx)))
                             (abs (- (? dx) (? wx)))
                             )
                          )
                    )
             (available '(?sx ?sy))
             )
     :post ((unavailable '(?sx ?sy)) (destination '(?dx ?dy) ?g) (at '(?sx ?sy) ?w))
     :pre ((destination '(?dx ?dy) ?g))
     :del ((at '(?sx ?sy) ?w) (at '(?dx ?dy) none) (destination '(?sx ?sy) ?g))
     :add ((at '(?sx ?sy) none) (at '(?dx ?dy) ?w))
     :cmd (move ?w '(?dx ?dy))
     :txt (?w moves to '(?dx ?dy))
     }
    
     :move-y
     {:name move-y
     :achieves (at '(?dx ?dy) ?w)
     :when (
             (at '(?wx ?wy) ?w)
             (on '(?sx ?sy) none)
             (on '(?dx ?dy) none)
             (connects '(?sx ?sy) '(?dx ?dy))
             (goal ?g)
             (:guard (and (>= (abs (- (? dy) (? wy)))
                             (abs (- (? dx) (? wx)))
                             )
                          (< (abs (- (? sy) (? wy)))
                             (abs (- (? dy) (? wy)))
                             )
                          )
                    )
             (available '(?sx ?sy))
             )
     :post ((unavailable '(?sx ?sy)) (destination '(?dx ?dy) ?g) (at '(?sx ?sy) ?w))
     :pre ((destination '(?dx ?dy) ?g))
     :del ((at '(?sx ?sy) ?w) (at '(?dx ?dy) none) (destination '(?sx ?sy) ?g))
     :add ((at '(?sx ?sy) none) (at '(?dx ?dy) ?w))
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
             )
     :post ((unavailable '(?sx ?sy)) (destination '(?dx ?dy) ?g) (at '(?sx ?sy) ?w))
     :pre ((destination '(?dx ?dy) ?g))
     :del ((at '(?sx ?sy) ?w) (at '(?dx ?dy) none) (destination '(?sx ?sy) ?g))
     :add ((at '(?sx ?sy) none) (at '(?dx ?dy) ?w))
     :cmd (move ?w '(?dx ?dy))
     :txt (?w moves to '(?dx ?dy))
     }
    
    :start
    {:name start
     :achieves (at '(?dx ?dy) ?w)
     :post ((unavailable '(?dx ?dy)) (goal '(at '(?dx ?dy) ?w)) (at '(?dx ?dy) ?w))
     }
     
		:set-goal
		{:name set-goal
		 :achieves (goal ?g)
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
    
    
    
    
    
;    :move
;     {:name move
;      :achieves (at ?d ?w)
;      :when (
;              (worker ?w)
;              (floor ?d)
;              (floor ?s)
;              (at ?d nil)
;              (connects ?s ?d)
;              ;(:not (visited ?s ?w))
;              )
;      :post ( 
;              ;(protected ?w [at ?s ?w])
;              (at ?s ?w)
;              )
;      :pre (
;              (at ?s ?w)
;              (at ?d nil)              
;              (worker ?w)
;              (floor ?d)
;              (connects ?s ?d)
;              ;(:not (visited ?s ?w))
;              )
;      :del (
;             ;(protected ?w [at ?s ?w])
;             (at ?s ?w)
;             (at ?d nil)
;           )
;      :add (
;             (visited ?s ?w)
;             (at ?s nil)
;             (at ?d ?w)
;           )
;      :cmd [move ?w ?d]
;      :txt (?w moves to ?d)
;      }
;     
;     
;     :push-box
;     {:name push-box
;      :achieves (at ?dest-patch ?box)
;      :when (
;              (at ?agent-src-patch ?agent)
;              (at ?box-src-patch ?box)
;              (at ?dest-patch nil)
;              )
;      :post (
;              (box ?box)
;              (worker ?agent)
;              (floor ?dest-patch)
;              (connects ?box-src-patch ?dest-patch)
;              (connects ?agent-src-patch ?box-src-patch)
;              )
;      :pre (
;              (box ?box)
;              (worker ?agent)
;              (floor ?dest-patch)
;              (connects ?box-src-patch ?dest-patch)
;              (connects ?agent-src-patch ?box-src-patch)
;              (at ?dest-patch nil)
;              (at ?agent-src-patch ?agent)
;              (at ?box-src-patch ?box)
;              )
;      :del (
;             (at ?dest-patch nil)
;             (at ?agent-src-patch ?agent)
;             (at ?box-src-patch ?box)
;           )
;      :add (
;             (at ?dest-patch ?box)
;             (at ?agent-src-patch nil)
;             (at ?box-src-patch ?agent)
;           )
;      :cmd [push-box ?box]
;      :txt (?agent pushes ?box to ?dest-patch)
;     }
;
;     :load-bay
;     {:pre (
;              (floor ?box-patch)
;              (bay ?box-patch)
;              (unloaded ?box-patch)
;              (at ?box-patch ?box)
;              (at ?agent-patch ?agent)
;              (connects ?agent-patch ?box-patch)
;              (worker ?agent)
;              (box ?box)
;            )
;      :del (
;             (unloaded ?box-patch)
;           )
;      :add (
;             (unloaded ?box-patch)
;           )
;      :cmd [load-bay ?box-patch]
;      :txt (?box loads into ?box-patch)
;     }
;
;   unload-bay
;   {:pre (
;            (floor ?dest-patch)
;            (bay ?box-patch)
;            (loaded ?box-patch)
;            (at ?box-patch ?agent)
;            (worker ?agent)
;          )
;    :del (
;           (loaded ?box-patch)
;         )
;    :add (
;           (unloaded ?box-patch)
;         )
;    :cmd [unload-bay ?box-patch]
;    :txt (?box-patch no longer holds a box)
;   }
;   
;  }
;)
