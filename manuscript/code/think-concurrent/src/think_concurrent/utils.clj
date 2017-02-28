
(ns think-concurrent.utils)

(def ^:dynamic *log-enabled* true)

(def printer (agent nil))

(defn log [msg]
  (when *log-enabled*
    (send printer (fn [& _] (println msg)))))
