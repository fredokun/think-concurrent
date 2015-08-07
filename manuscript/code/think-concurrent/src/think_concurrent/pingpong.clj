
;;; TODO: add common header

;;; TODO: add specific header

(ns think-concurrent.pingpong
  (:require [clojure.core.async
             :refer [>!! <!! chan thread]]))

(defn ping-pong
  [ch kind]
  (thread (loop []
            (let [msg (<!! ch)]
              (print msg) (flush)
              (>!! ch kind)
              (recur)))))

(defn run-ping-pong
  []
  (let [ch (chan)]
    (let [j (ping-pong ch "Ping! ")
          _ (ping-pong ch "Pong! ")]
      (>!! ch "Start! ") 
      (<!! j))))




