(ns think-concurrent.core
  (:gen-class)
  (:require [think-concurrent.pingpong
             :refer [run-pingpong run-pingpong-fuel]] ))

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
          "hello" (println "If really you insist: Hello World !")
          "pingpong" (run-pingpong)
          "pingpong-fuel" (run-pingpong-fuel 10)
          (println "This example is not available.")))
      (println "Please choose an example to run."))))
