
;;; TODO: add common header

;;; TODO: add specific header

(ns think-concurrent.pingpong
  (:require [clojure.core.async
             :refer [>!! <!! chan thread]]))

(defn ping-pong
  [in out kind]
  (thread
    (loop []
      (let [msg (<!! in)]
        (print msg) (flush)
        (>!! out kind)
        (recur)))))

(defn run-ping-pong
  []
  (let [ping (chan)
        pong (chan)]
    (let [j (ping-pong ping pong "Ping ! ")
          _ (ping-pong pong ping "Pong ! ")]
      (>!! ping "Start ! ")
      (<!! j))))



