
;;; TODO: add common header

;;; TODO: add specific header

(ns think-concurrent.pingpong
  (:require [clojure.core.async
             :refer [>!! <!! chan thread]]))

(defn pingpong
  [ch kind]
  (thread (loop []
            (let [msg (<!! ch)]
              (print msg) (flush)
              (>!! ch kind)
              (recur)))))

(defn run-pingpong
  []
  (let [ch (chan)]
    (let [j (pingpong ch "Ping! ")
          _ (pingpong ch "Pong! ")]
      (>!! ch "Start! ") 
      (<!! j))))


(defn pingpong-fuel
  [ch kind fuel]
  (thread (loop [level fuel]
            (when (> level 0)
              (let [msg (<!! ch)]
                (print msg) (flush)
                (>!! ch kind)
                (recur (- level 1)))))))

(defn run-pingpong-fuel
  [fuel]
  (let [ch (chan)]
    (let [j (pingpong-fuel ch "Ping! " fuel)
          _ (pingpong-fuel ch "Pong! " fuel)] ;; or (+ fuel 1) ?
      (Thread/sleep 100) ;; non-determinism please !
      (>!! ch "Start! ") 
      (<!! j)
      (println "Joined!"))))




