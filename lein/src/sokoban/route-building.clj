(declare get-goals try-goal get-box-goals get-worker-goals get-route build-worker-routes)

(defn get-goals [state]
  (get (planner state '(complete) goal-ops) :cmds)
  )


(defn try-goal [goal state cmds]
  (get-route goal state)
  )

(defn get-route [goal state]
  (let [box-goals (get-box-goals goal state)
        worker-goals (get-worker-goals box-goals state)]
    (build-worker-routes state worker-goals '() step-ops step-ops)
        )
  )

(defn get-box-goals [goal state]
  (get (planner state goal box-ops) :cmds)
  )
  
 (defn get-worker-goals [box-goals state]
  (build-worker-routes state box-goals '() worker-ops box-ops)
  )

;(defn get-routes [goals state route-list]
;  (cond
;    (empty? goals) route-list
;    :else (recur (rest goals) (concat (get route-list (planner state (first goals) box-ops) :cmds)))
;    )
;  )

(defn build-worker-routes [state cmds new-cmds ops1 ops2]
  (cond
    (empty? cmds) new-cmds
    :else (mif ['(at '(?x ?y) ?w) (first cmds)]
               (let [result (planner state (first cmds) ops1)
                     new-state (clear-goals (get result :state) (find-goals (get result :state)))
                     worker-cmds (get result :cmds)]
                 (recur (union new-state current-state) (rest cmds) (concat new-cmds worker-cmds) ops1 ops2)
                 )
               (let [result (planner state (first cmds) ops2)
                     new-state (clear-goals (get result :state) (find-goals (get result :state)))
                     worker-cmds (get result :cmds)]
                 (recur (union new-state current-state) (rest cmds) (concat new-cmds worker-cmds) ops1 ops2)
                 )
               )
    )
  )

;(defn try-route [route state cmds]
;  (cond
;    (empty? route) cmds
;    :else (let [result (planner state (first route) step-ops)
;                new-state (clear-goals (get result :state) (find-goals (get result :state)))
;                new-cmds (cons (get result :cmds) cmds)]
;            (recur (rest route) (union new-state target-state) new-cmds)
;            )
;    )
;  )