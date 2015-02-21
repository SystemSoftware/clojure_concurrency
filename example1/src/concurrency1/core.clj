(ns concurrency1.core
  (:gen-class))

(require '[clojure.string :as str])

(def counter (ref 0))

(defn next-counter [] (dosync (alter counter inc)))

(defn test1 [x]
    (if (> x 0) 
		((println(next-counter) "test1")(Thread/sleep 200)(test1 (dec x)))
		(println "ready")
	)
) 
(defn test2 [x]
     (if (> x 0) 
		((println(next-counter) "test2")(Thread/sleep 200)(test2 (dec x)))
		(println "ready")
	)
) 

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
 
  (future (test1 15))
  (future (test2 15))
  ;(println "result is" @counter)
  (shutdown-agents))
