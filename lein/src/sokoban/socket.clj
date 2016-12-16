(load-file "./src/sock2/socket.clj")

(def s25 (startup-server 2222))

(defn receive-state
  [state-list]
    (let [new-state (socket-read s25)]
      (println new-state)
      (if (= new-state -1)
        state-list
        (receive-state (conj state-list new-state))
        )
      )
  )

(println "Awaiting new world state setup commands...")
(def world-state (receive-state '#{}))
(println "Awaiting new object state setup commands...")
(def object-state (receive-state '#{}))
