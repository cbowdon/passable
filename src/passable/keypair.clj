(ns passable.keypair
  (:require [clojure.string :as str]
            [passable.crypto :as crypto]))

(defn keypair
  "Creates a new protected keypair in the user's home directory.

  Returns an unevaluated task."
  ([] nil)
  ([username] nil)
  ([username home] nil))
