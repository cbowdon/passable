(ns passable.commands.keypair
  (:require [clojure.java.io :as io]
            [passable.io :refer [make-path
                                 *console*
                                 read-password
                                 zero!]]
            [passable.crypto :as crypto]))

(defn- encrypt-secret-key
  [sk password]
  (let [{:keys [salt secret-key]} (crypto/derive-secretbox-key password)
        encrypted-sk (crypto/secretbox secret-key sk)]
    (byte-array (concat salt encrypted-sk))))

(defn keypair
  "Create a new protected keypair file for the user in the given directory.

  Returns a write-file task."
  [user dir]
  (let [{ pk :public sk :secret } (crypto/generate-keypair)
        password (read-password *console*)
        encrypted-sk (encrypt-secret-key sk password)]
    (zero! sk)
    (zero! password)
    {:task 'write-file
     :path (make-path dir ".passable" user)
     :contents {:user user
                :public pk
                :secret encrypted-sk}}))

