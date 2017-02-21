(ns nlp.morphologicalprocessor
 (use [clojure.string :only (join split)]
      [clojure.string :as s]
      [nlp.lexicon]
      [clojure.walk]))

;Remove this in final build and let core.clj load it
(load-file "./src/nlp/lexicon.clj") 
;Gets the users input
(defn get-input [x])

;will call get-input when everything is linked up.
;iterate through words in the input
;(prefix-remover "nonbeliever person")
;(map s/includes? (map first (stringify-keys prefixes)) "anti" )
(defn prefix-remover [input]   
     (let [split-sent (split input #"\s+")] ;split the string into words
              (fn [splitting]          ;quick anon fn for recursion 
                 (if (.contains (map first (stringify-keys prefixes)) "anti") ;if prefixes includes first word See footnote [1]
                     (print  "inside the cond"))
                    
                     (not= (count splitting) 1) ;if word count is greater than 1
                     (recur (rest splitting))  ;recur
                                 
              )
     )
     (print "Finished")
)
   

;replace that part of input with the alternative meaning recur
;(prefix-remover "non")  

;(defn find-match-in-word [query lex]
; (re-find #ery lex))


;Example input: '(Push the blue block forward)
;pick apart words by looking for common suffixes and
;prefixes, and then transform the words accordingly

;[1] Had to use java here. So much simpler. Alternative is 
;    a lot of map/reducing type stuff. Example of clojure below
;    (map s/includes? (map first (stringify-keys prefixes)) "anti" )
;    http://stackoverflow.com/questions/26386766/check-if-string-contains-substring-in-clojure