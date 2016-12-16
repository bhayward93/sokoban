(def block-ops
  '{move
     {:name move
      :achieves (at ?dest-patch ?agent)
      :when ((at ?dest-patch nil))
      :post ((at ?src-patch ?agent))
      :pre (
              (worker ?agent)
              (floor ?dest-patch)
              (connects ?src-patch ?dest-patch)
              (at ?src-patch ?agent)
              (at ?dest-patch nil)
              )
      :del (
             (at ?src-patch ?agent)
             (at ?dest-patch nil)
           )
      :add (
             (at ?src-patch nil)
             (at ?dest-patch ?agent)
           )
      :cmd [move ?agent ?dest-patch]
      :txt (?agent moves to ?dest-patch)
     }

    push-box
     {:name push-box
      :achieves (at ?dest-patch ?agent)
      :when (
              (box ?box)
              (worker ?agent)
              (floor ?dest-patch)
              (connects ?box-src-patch ?dest-patch)
              (connects ?agent-src-patch ?box-src-patch)
              (at ?dest-patch nil)
              (at ?agent-src-patch ?agent)
              (at ?box-src-patch ?box)
              )
      :post ((at ?agent-src-patch ?agent))
      :pre (
              (box ?box)
              (worker ?agent)
              (floor ?dest-patch)
              (connects ?box-src-patch ?dest-patch)
              (connects ?agent-src-patch ?box-src-patch)
              (at ?dest-patch nil)
              (at ?agent-src-patch ?agent)
              (t?box-src-patch ?box)
              )
      :del (
             (at ?dest-patch nil)
             (at ?agent-src-patch ?agent)
             (at ?box-src-patch ?box)
           )
      :add (
             (at ?dest-patch ?box)
             (at ?agent-src-patch nil)
             (at ?box-src-patch ?agent)
           )
      :cmd [push-box ?box]
      :txt (?agent pushes ?box to ?dest-patch)
     }

   load-bay
   {:name load-bay
    :acheives (loaded ?box-patch)
    :when (
            (floor ?box-patch)
            (bay ?box-patch)
            (unloaded ?box-patch)
            (at ?box-patch ?box)
            (at ?agent-patch ?agent)
            (connects ?agent-patch ?box-patch)
            (worker ?agent)
            (box ?box)
          )
    :post ()
    :pre (
            (floor ?box-patch)
            (bay ?box-patch)
            (unloaded ?box-patch)
            (at ?box-patch ?box)
            (at ?agent-patch ?agent)
            (connects ?agent-patch ?box-patch)
            (worker ?agent)
            (box ?box)
          )
    :del (
           (unloaded ?box-patch)
         )
    :add (
           (unloaded ?box-patch)
         )
    :cmd [load-bay ?box-patch]
    :txt (?box loads into ?box-patch)
   }

   unload-bay
   {:name unload-bay
    :acheives (unloaded ?box-patch)
    :when (
            (floor ?dest-patch)
            (bay ?box-patch)
            (loaded ?box-patch)
            (at ?box-patch ?agent)
            (worker ?agent)
          )
    :post ()
    :pre (
            (floor ?dest-patch)
            (bay ?box-patch)
            (loaded ?box-patch)
            (at ?box-patch ?agent)
            (worker ?agent)
          )
    :del (
           (loaded ?box-patch)
         )
    :add (
           (unloaded ?box-patch)
         )
    :cmd [unload-bay ?box-patch]
    :txt (?box-patch no longer holds a box)
   }
   
   :protect-x
   {:name protect-x
    :acheives (protected ?x)
    :add ((protected ?x))
    }
  }
)
