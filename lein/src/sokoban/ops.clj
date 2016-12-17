(def block-ops
  '{:start
    {:name start
     :acheives (start)
     :when ((worker ?w) (floor ?p) (at? ?p ?w) (protected [at ?p ?w]))
     }
    
    :move
    {:name move
     :achieves (at ?d ?w)
     :when ((worker ?w) (floor ?d) (at ?d nil) (connects ?s ?d) (:not (protected ?w [dest ?s ?w])))
     :post ((protected ?w [dest ?d ?w]) (protected ?w [at ?s ?w]) (start) (at ?s ?w))
     :pre ((worker ?w) (floor ?d) (at ?d nil) (connects ?s ?d) (:not (protected ?w [dest ?s ?w])))
     :del ((protected ?w [dest ?s ?w]) (protected ?w [at ?_ ?w]) (at ?s ?w) (at ?d nil))
     :add ((at ?s nil) (at ?d ?w))
     :cmd [move ?w ?d]
     :txt (?w moves to ?d)
     }
    
	   :protect-x
	   {:name protect-x
	    :achieves (protected ?x ?c)
	    :add ((protected ?x ?c))
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
