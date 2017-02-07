(ns nlp.lexicon)
;stores the words known
(def lexicon {:box noun ,   :block noun,  :square noun,:object noun, ;for box
              :move verb,   :push verb,   :walk verb,  :run verb         ;for moving
              :worker noun, :player noun, :agent noun, :man noun, :woman, :person noun, :guy noun, :character noun ;the player
              :gentle adjective , :force verb
              :block noun, :switch noun, :agent noun,
              :worker noun, })


(def colors (:red :orange :green));

;suffixes - note: if someone uses the word size, will it find ize in the suffixes list,
;           and wrongly deduce its meaning.
(def sufixes (:acy :al :ance :dom :er :or :ism :ist :ty
               :ment :ness :ship :sion :tion :ate :en
               :ify :fy :ize :ise :able :ible :al :esque
               :ful :ic :ical :ious :ous :ish :ive :less :y))
;taken from http://grammar.about.com/od/words/a/comsuffixes.htm

;prefixes - note, can convert this and suffixes into a different datatype using REPL.
;           should we be storing the values of these symbols in a map, with the value being the meaning
(defn prefixes {:anti {:no [:noun ] }
                {:de {:reverse [:verb :adverb]}
                 {:dis {:not [:adjective :verb :noun]}
                  {:en {:within [:adjective :noun]} ;"to put (something/someone) into <noun>",
                  :em {:within [:noun :adjective];http://www.dictionary.com/browse/en-
                  :fore ;{:before
                  :in
                  :im
                  :in

                 :im
                  :il
                  :ir
                  :inter
                  :mid
                  :mis
                  :non
                  :over
                  :pre
                  :re
                  :semi
                  :sub
                  :super
                  :trans
                  :un
                  :under})

;taken from http://teacher.scholastic.com/reading/bestpractices/vocabulary/pdf/prefixes_suffixes.pdf
;Considerations:
;-Group words by type; efficiency?
;-Need to scan lexicon fully every time, to pick up ambiguty.
;-the colors used in the nlp. these can change or be added to but
;-need to be fed in with the lexicon. Note that colours can be nouns or adjectives.
;-switch could be taken multiple ways