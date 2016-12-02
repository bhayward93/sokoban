(ns sokoban.core
  (:gen-class))

(load-file "../sock2/socket.clj")
(def s25 (startup-server 2222))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
