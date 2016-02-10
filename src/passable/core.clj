(ns passable.core
  (:require [passable.crypto :refer :all]
            [passable.io :refer :all]
            [passable.serialization :refer :all])
  (:gen-class))

;; (def example
;;   {:pass (.getBytes "hello")
;;    :user (.getBytes "world")
;;    :site "example.com"
;;    :notes (.getBytes "foobarbaz")})
;;
;; (def secret-key (generate-secret-key))
;; (def nonce (generate-nonce))
;;
;; (defn demo []
;;   (let [f "/Users/chris/Downloads/test.dat"]
;;     (->> example
;;          (serialize)
;;          (secretbox/encrypt secret-key nonce)
;;          (hexify)
;;          (spit f))))
;; (defn demo2 []
;;   (let [f "/Users/chris/Downloads/test.dat"]
;;     (->> (slurp f)
;;          (unhexify)
;;          (secretbox/decrypt secret-key nonce)
;;          (deserialize))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (printf "hello werld %s" args))
