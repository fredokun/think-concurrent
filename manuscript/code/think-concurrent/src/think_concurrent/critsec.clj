
(ns think-concurrent.critsec
  (:require [clojure.core.async
             :refer [>!! >! <!! <! alts! chan go thread timeout]])
  (:require [think-concurrent.utils
             :refer [log]]))

(def +cs-count+ (atom 0 :validator #(<= % 1)))

(defn critsec-bad [id csfun]
  (go
    #_(swap! +cs-count+ #(inc %))
    (log (str ">>> Critical Section #" id " entered"))
    (csfun id)
    (log (str "<<< Critical Section #" id " left"))
    #_(swap! +cs-count+ #(dec %))))

(defn default-csfun [id]
  (Thread/sleep 20))

(defn run-critsec-bad [nb csfun]
  (loop [id 1]
    (when (<= id nb)
      (critsec-bad id csfun)
      (recur (+ id 1))))
  ;; no need for a join, bad things happen fast !
  (Thread/sleep 1000))

(defn controller [nb enter]
  (go (loop [nb nb]
        (when (> nb 0)
          (let [leave (chan)]
            (>! enter leave)
            (<! leave)
            (recur (- nb 1)))))))

(defn critsec-good [id enter csfun]
  (go
    (let [leave (<! enter)]
      (swap! +cs-count+ #(inc %))
      (log (str ">>> Critical Section #" id " entered"))
      (csfun id)
      (log (str "<<< Critical Section #" id " left"))
      (swap! +cs-count+ #(dec %))
      (>! leave id))))

(defn run-critsec-good
  [nb csfun]
  (let [enter (chan)]
    (loop [id 1]
      (when (<= id nb)
        (critsec-good id enter csfun)
        (recur (+ id 1))))
    (let [j (controller nb enter)]
      (<!! j))))


