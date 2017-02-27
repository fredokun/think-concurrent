(ns think-concurrent.pingpong
  (:require [clojure.core.async
             :refer [>!! <!! chan thread]]))

(defn pingpong [ch kind]
  (loop []
    (let [msg (<!! ch)]
      (print msg) (flush)
      (>!! ch kind)
      (recur))))

(defn run-pingpong []
  (let [ch (chan)]
    (thread (pingpong ch "Ping! "))
    (>!! ch "Start! ")
    (pingpong ch "Pong! ")))

(defn pingpong-fuel
  [ch kind fuel]
  (loop [level fuel]
    (when (> level 0)
      (let [msg (<!! ch)]
        (print msg) (flush)
        (>!! ch kind)
        (recur (- level 1))))))

(defn run-pingpong-fuel
  [fuel]
  (let [ch (chan)]
    (let [j (thread (pingpong-fuel ch "Ping! " fuel))
          _ (thread (pingpong-fuel ch "Pong! " (inc fuel)))]
      (Thread/sleep 100) ;; non-determinism please !
      (>!! ch "Start! ") 
      (<!! j)
      (println "Joined!"))))




