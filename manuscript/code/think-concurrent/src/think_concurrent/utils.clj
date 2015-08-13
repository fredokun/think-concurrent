
(ns think-concurrent.utils)

(def +printer+ (agent nil))

(defn log [msg]
  (send +printer+ #(println msg)))
