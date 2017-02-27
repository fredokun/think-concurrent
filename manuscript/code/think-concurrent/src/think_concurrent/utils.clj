
(ns think-concurrent.utils)

(def printer (agent nil))

(defn log [msg]
  (send printer (fn [& _] (println msg))))
