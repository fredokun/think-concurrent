
;;; TODO: add common header

;;; TODO: add specific header

(ns think-concurrent.critsec
  (:require [clojure.core.async
             :refer [>!! >! <!! <! alts! chan go thread timeout]]))


(def +printer+ (agent nil))

(defn- log [msg]
  (send +printer+ (fn [_] (println msg))))

(def +cs-count+ (atom 0 :validator #(<= % 1)))

(defn critsec-bad
  [id enter csfun]
  (go
    (swap! +cs-count+ #(+ % 1))
    ;; Critical Section start
    (log (str ">>> Critical Section #" id " entered"))
    (csfun id)
    (log (str ">>> Critical Section #" id " left"))
    ;; Cristical Section end
    (swap! +cs-count+ #(- % 1))))

(defn critsec-good
  [id enter csfun]
  (go
    (let [leave (<! enter)]
      (swap! +cs-count+ #(+ % 1))
      ;; Critical Section start
      (log (str ">>> Critical Section #" id " entered"))
      (csfun id)
      (log (str ">>> Critical Section #" id " left"))
      ;; Cristical Section end
      (swap! +cs-count+ #(- % 1))
      (>! leave id))))

(defn controller
  [nb enter]
  (go (loop [nb nb]
        (when (> nb 0)
          (let [leave (chan)]
            (>! enter leave)
            (<! leave)
            (recur (- nb 1)))))))

(defn default-csfun
  [id]
  (Thread/sleep 20)) ;send +printer+ (fn [_] (println (str "   ==> in critical section #" id)))))

(defn run-critsec-bad
  [nb csfun]
  (let [enter (chan)]
    (loop [id 1]
      (when (<= id nb)
        (critsec-bad id enter csfun)
        (recur (+ id 1))))
    (let [j (controller nb enter)]
      (<!! j))))

(defn run-critsec-good
  [nb csfun]
  (let [enter (chan)]
    (loop [id 1]
      (when (<= id nb)
        (critsec-good id enter csfun)
        (recur (+ id 1))))
    (let [j (controller nb enter)]
      (<!! j))))



