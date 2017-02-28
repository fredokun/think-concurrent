(ns think-concurrent.core
  (:gen-class)
  (:require [think-concurrent.utils :refer [*log-enabled*]]
            [think-concurrent.pingpong
             :refer [run-pingpong run-pingpong-fuel]]
            [think-concurrent.procring
             :refer [run-ring-thread]]
            [think-concurrent.critsec
             :refer [run-critsec-bad run-critsec-good default-csfun]]))


(def examples
  {"hello" #(println "If really you insist: Hello World !")
   "pingpong" run-pingpong
   "pingpong-fuel" #(run-pingpong-fuel 10)
   "ring-thread" #(run-ring-thread 5 2)
   "critsec-bad" #(run-critsec-bad 10 default-csfun)
   "critsec-good" #(run-critsec-good 10 default-csfun)})

(defn -main
  [& args]
  (let [example (first args)]
    (if example
      (do 
        (println "--------------------------------------------")
        (println (str "Example: " example))
        (println "--------------------------------------------")
        (if-let [runf (get examples example)]
          (runf)
          (println "This example is not available.")))
      (do (println "Please choose an example to run, among:")
          (println (clojure.string/join " " (keys examples)))))))
