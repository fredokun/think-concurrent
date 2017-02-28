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

(defn pingpong-fuel [ch kind fuel]
  (thread
    (dotimes [level fuel]
      (let [msg (<!! ch)]
        (print msg) (flush)
        (>!! ch kind)))))

(defn run-pingpong-fuel [fuel]
  (let [ch (chan)]
    (let [join (pingpong-fuel ch "Ping! " fuel)
          _ (pingpong-fuel ch "Pong! " fuel)] ;; or it is (inc fuel) ?
      (Thread/sleep 100) ;; non-determinism please !
      (>!! ch "Start! ") 
      (<!! join)
      (println "Joined!"))))




