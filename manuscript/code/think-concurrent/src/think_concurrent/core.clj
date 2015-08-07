(ns think-concurrent.core
  (:gen-class)
  (:require [think-concurrent.pingpong :refer [run-ping-pong]] ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [example (first args)]
    (if example
      (do 
        (println "--------------------------------------------")
        (println (str "Example: " example))
        (println "--------------------------------------------")
        (case example
          "hello" (println "Only because you insist: Hello World !")
          "pingpong" (run-ping-pong)
          (println "This example is not available.")))
      (println "Please choose an example to run."))))
