(def goal-ops
  '{
;    :start
;    {:name start
;     :achieves (start ?p ?w)
;     :when ((worker ?w) (floor ?p) (at ?p ?w) (protected ?w [at ?p ?w]))
;     :post ()
;     :pre ()
;     :del ()
;     :add ()
;     :cmd ()
;     :txt ()
;     }
;    (planner (conj world-state object-state) '(at patch-4-8 worker-3) goal-ops)
    :move
    {:name move
     :achieves (at ?d ?w)
     :when ((worker ?w) (floor ?d) (at ?d none) (connects ?s ?d) (connects ?d ?s) (:not (last-patch ?d)))
     :post (at ?s ?w)
     :pre ()
     :del ((last-patch ?x) (at ?s ?w) (at ?d none))
     :add ((at ?s none) (at ?d ?w) (last-patch ?s))
     :cmd (move ?w ?d)
     :txt (?w moves to ?d)
     }
    
	   :protect-dest
	   {:name protect-dest
	    :achieves (destination ?x)
	    :add ((destination ?x))
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
