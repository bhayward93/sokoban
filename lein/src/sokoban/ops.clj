(def block-ops
  '{move
     {:pre (
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
     {:pre (
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
   {:pre (
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
   {:pre (
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
