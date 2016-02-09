(ns passable.keypair
  (:require [clojure.java.io :as io]
            [passable.io :refer [console read-password]]
            [passable.crypto :as crypto]))

(defn- public-key
  [^bytes pk username home]
  {:task 'write-file
   :path (io/file home ".passable" (format "%s.public-key" username))
   :contents pk})

(defn- secret-key
  [^bytes sk username home]
  (let [password (read-password console)
        encrypted-sk (crypto/secretbox password sk)]
    (crypto/zero! password)
    (crypto/zero! sk)
    encrypted-sk))

(defn keypair
  "Creates a new protected keypair in the user's home directory.

  Returns an unevaluated write-file task."
  ([] nil)
  ([username] nil)
  ([username home]
   (let [{ pk :public sk :secret } (crypto/generate-keypair)]
     #{(public-key pk username home)
       (secret-key sk username home)})))
   
