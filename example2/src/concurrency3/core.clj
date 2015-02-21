(ns concurrency3.core
  (:gen-class))

(require '[clojure.string :as str])  
  
(def counter (ref 0)) 

(def counter2 (ref 0))
  
(def tobac (ref 0))

(def papers (ref 0)) 
  
(def matches (ref 0))  
 
(defn smoker1 
	"I like smoking"
	[]
	
	(if (and(> @papers 0)(> @matches 0))  
	    (dosync(alter papers dec)(alter matches dec) (println "Smoker1 -> I build a cigarette"))
		(println "Smoker 1: no materials")
	)
	;(Thread/sleep (rand-int 4000))
	(Thread/sleep 2000)
	(smoker1)
) 

(defn smoker2 
	"I like smoking"
	[]
	(if (and(> @tobac 0)(> @matches 0))  
	    (dosync(alter tobac dec)(alter matches dec) (println "Smoker2 -> I build a cigarette"))
		(println "Smoker 2: no materials")
	)
	;(Thread/sleep (rand-int 4000))
	(Thread/sleep 2000)
	(smoker2)
)

(defn smoker3 
	"I like smoking"
	[]
	(if (and(> @tobac 0)(> @papers 0))  
	    (dosync(alter tobac dec)(alter papers dec) (println "Smoker3 -> I build a cigarette"))
		(println "Smoker 3: no materials")
	)
	;(Thread/sleep (rand-int 4000))
	(Thread/sleep 2000)
	(smoker3)
)

(defn dealer
	"I kill people"
	[]
	(dosync(ref-set counter (rand-int 3)))
	(dosync(ref-set counter2 (rand-int 3)))
	(if(= @counter 0) 
		(dosync(alter tobac inc)) 
		(if(= @counter 1)
			(dosync(alter papers inc)) 
			(if(= @counter 2) 
			(dosync(alter matches inc)
			 (ref-set counter 0))
			)
		)
	)
	
	(if(= @counter2 0) 
		(dosync(alter tobac inc)) 
		(if(= @counter2 1)
			(dosync(alter papers inc)) 
			(if(= @counter2 2) 
			(dosync(alter matches inc)
			 (ref-set counter2 0))
			)
		)
	)
  
	(println "Dealer delivered some materials" @tobac @papers @matches)
	(Thread/sleep 2000) 

	(dealer) 
)
 
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (future (dealer))
  (future (smoker1))
  (future (smoker2))
  (future (smoker3))
  
  (shutdown-agents)
)