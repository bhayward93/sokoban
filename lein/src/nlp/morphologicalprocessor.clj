(ns nlp.morphologicalprocessor)

;Gets the users input
(defn get-input []

  )

;will call get-input when everything is linked up.
;iterate through words in the input

(defn prefix-remover [input]
   (let split (split input #"\s+") ;split the string into words
              (fn [split]          ;quick anon fn for recursion
              (first(split))       ;check if starts with a prefix - if 1st letter is a prefix, if second if third ect
                                   ;replace that part of input with the alternative meaning recur(rest split)))
  )



;Example input: '(Push the blue block forward)
;pick apart words by looking for common suffixes and
;prefixes, and then transform the words accordingly

