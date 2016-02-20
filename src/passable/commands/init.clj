(ns passable.commands.init
  (:require [passable.crypto :as crypto]
            [passable.io :refer [*env* make-path keywordize zero!]]))

(defn- box-secretbox-key
  [public-key secret-key]
  (let [sbk (crypto/generate-secret-key)
        ciphertext (crypto/box public-key secret-key sbk)]
    (zero! sbk)
    ciphertext))

(defn init
  "Initializes a store file in given directory."
  [dir user public-key secret-key]
  {:task 'write-file
   :path (make-path dir ".passable")
   :contents {:data {}
              :user-keys {(keywordize user) public-key}
              :group-keys {(keywordize user) (box-secretbox-key public-key
                                                                secret-key)}}})
