(ns think-concurrent.procring
  (:require [clojure.core.async
             :refer [>!! <!! >! <! chan thread go]]
            [think-concurrent.utils
             :refer [log]]))

(defn node-thread [id in out nb-turns leader?]
  (thread
    (loop [turn 1]
      (when (<= turn nb-turns)
        (let [tok (<!! in)]
          (log (str "[" id "] '" tok "' received at turn #" turn))
          (when (or (not leader?)
                    (not= turn nb-turns))
            (>!! out tok)
            (recur (inc turn))))))))

(defn run-ring-thread [nb-nodes nb-turns]
  (let [init (chan)
        exit (loop [id-node 1, in init]
               (if (< id-node nb-nodes)
                 (let [out (chan)]
                   (node-thread id-node in out nb-turns false)
                   (recur (inc id-node) out))
                 in))]
    ;; last process
    (let [lastproc (node-thread nb-nodes exit init nb-turns true)]
      ;; let's start
      (>!! init "token")
      (<!! lastproc))))



