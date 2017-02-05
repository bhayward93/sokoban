(ns nlp.lexicon)
;stores the words known
(def lexicon {:box noun , :move verb , :push verb , :gently adjective ,
              :block noun, :switch noun, :agent noun,
              :worker noun, })

;the colors used in the nlp. these can change or be added to but
;need to be fed in with the lexicon. Note that colours can be nouns or adjectives.
(def colors (:red :orange :green))

(def sufixes (:ly :dy)) ;ect

;note, switch could be taken multiple ways