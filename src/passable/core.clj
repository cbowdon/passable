(ns passable.core
  (:require [caesium.core :refer [sodium-init]]
            [passable.crypto :refer :all]
            [passable.io :refer :all]
            [passable.serialization :refer :all])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (sodium-init)
  (printf "hello werld %s" args))
