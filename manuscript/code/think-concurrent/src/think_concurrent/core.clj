(ns think-concurrent.core
  (:gen-class)
  (:require [think-concurrent.pingpong
             :refer [run-pingpong run-pingpong-fuel]])
  (:require [think-concurrent.critsec
             :refer [run-critsec-bad run-critsec-good default-csfun]]))

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
          "critsec-bad" (run-critsec-bad 10 default-csfun)
          "critsec-good" (run-critsec-good 10 default-csfun)
          (println "This example is not available.")))
      (println "Please choose an example to run."))))
