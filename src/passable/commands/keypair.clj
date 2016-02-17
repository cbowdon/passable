(ns passable.commands.keypair
  (:require [clojure.java.io :as io]
            [passable.io :refer [make-path
                                 *env*
                                 *console*
                                 read-password
                                 zero!]]
            [passable.crypto :as crypto]))

(defn- public-key
  "Create a public-key file task"
  [^bytes pk username home]
  {:task 'write-file
   :path (make-path home ".passable" (format "%s.public-key" username))
   :contents pk})

(defn- secret-key
  "Create an encrypted secret-key file task"
  [^bytes sk username home]
  (let [password (read-password *console*)
        {salt :salt sbk :secret-key} (crypto/derive-secretbox-key password)
        encrypted-sk (crypto/secretbox sbk sk)]
    (zero! password)
    (zero! sk)
    {:task 'write-file
     :path (make-path home ".passable" (format "%s.secret-key" username))
     :contents (byte-array (concat salt encrypted-sk))}))

(defn keypair
  "Creates a new protected keypair in the user's home directory.

  Returns unevaluated write-file tasks."
  ([]
   (keypair (:user *env*) (:home *env*)))
  ([username]
   (keypair (:home *env*)))
  ([username home]
   (let [{ pk :public sk :secret } (crypto/generate-keypair)]
     #{(public-key pk username home)
       (secret-key sk username home)})))

