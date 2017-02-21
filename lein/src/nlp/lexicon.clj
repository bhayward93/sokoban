(ns nlp.lexicon)
;stores the words known
(def lexicon {:box :noun    :square :noun   :object :noun, ;for box
              :move :verb   :push :verb   :walk :verb   :run :verb         ;for moving
              :worker :noun :player :noun :agent :noun :man :noun :woman :noun
              :person :noun :guy :noun :character :noun ;the player
              :gentle :adjective  :force :verb
              :block :noun :switch :noun
              })


(def colors (:red :orange :green));

;suffixes - note: if someone uses the word size, will it find ize in the suffixes list,
;           and wrongly deduce its meaning.
;(def sufixes (:acy :al :ance :dom :er :or :ism :ist :ty
;               :ment :ness :ship :sion :tion :ate :en
;               :ify :fy :ize :ise :able :ible :al :esque
;               :ful :ic :ical :ious :ous :ish :ive :less :y))
;taken from http://grammar.about.com/od/words/a/comsuffixes.htm

;prefixes - note, can convert this and suffixes into a different datatype using REPL.
;           should we be storing the values of these symbols in a map, with the value being the meaning
;      format:  {prefix {reworded [word type]}} - note word type feild may be off, and also require ordering.
(def prefixes {:anti {:no       [:noun ]}
                :de {:reverse   [:verb :adverb]}
               :dis {:not       [:adjective :verb :noun]}
                :en {:within    [:adjective :noun]}       ;"to put (something/someone) into <noun>",
                :em {:within    [:noun :adjective]}       ;http://www.dictionary.com/browse/en-
              :fore {:already   [:noun :adjective]}       ;{:before
                :in {:in        [:verb :adjective :noun]}
                :im {:in        [:verb :adjective :noun]}
                :il {:not       [:noun :adjective :verb]}
                :ir {:not       [:noun :adjective :verb]}
              :inter{:interior  [:noun :adjective :verb :adverb]}
               :mid {:middle    [:verb :adjective :verb]}
               :mis {:wrong     [:verb :noun :adjective]}
               :non {:not       [:adjective :verb :noun]}
              :over {:overly    [:adjective :adverb :prep]}
               :pre {:prior     [:verb :adjective :adverb :noun]} ;do before
                :re {:again     [:verb :adjective :noun :adverb]} ;do again
               :semi{:part      [:noun :adjective :verb :adverb :pronoun]}
               :sub {:underneath[:noun :ajective :adverb]}
              :super{:greater   [:noun :adjective :adverb]}
              :trans{:cross     [:noun :adjective :adverb :adverb]}
                 :un{:not       [:adjective :verb :pronoun :noun]}
              :under{:beneath   [:adverb :prep]}
          ;:--balancer--{:mapbalance[:map :balancing]}
                        })

;see Dictionary.com. Look up "re-" for the prefix re.

;taken from http://teacher.scholastic.com/reading/bestpractices/vocabulary/pdf/prefixes_suffixes.pdf
;Considerations:
;-Group words by type; efficiency?
;-Need to scan lexicon fully every time, to pick up ambiguty.
;-the colors used in the nlp. these can change or be added to but
;-need to be fed in with the lexicon. Note that colours can be nouns or adjectives.
;-switch could be taken multiple ways