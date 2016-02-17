(ns passable.crypto
  (:import java.util.Arrays)
  (:require [caesium.randombytes :as random]
            [caesium.crypto.pwhash :as pwhash]
            [caesium.crypto.box :as box]
            [caesium.util :refer [hexify unhexify]]
            [caesium.crypto.secretbox :as secretbox]))

(defn generate-secret-key []
  (random/randombytes (secretbox/key-length)))

(defn generate-secretbox-nonce []
  (random/randombytes (secretbox/nonce-length)))

(defn generate-box-nonce []
  (random/randombytes (secretbox/nonce-length))) ;; secretbox & box nonce length are the same

(defn generate-keypair []
  (box/generate-keypair))

(defn secretbox
  "`secretbox/encrypt` with a random nonce, prepended to the result."
  [secret-key plaintext]
  (let [nonce (generate-secretbox-nonce)
        ciphertext (secretbox/encrypt secret-key nonce plaintext)]
    (byte-array (concat nonce ciphertext))))

(defn secretbox-open
  "`secretbox/decrypt` where the ciphertext is prefixed with its nonce."
  [secret-key message]
  (let [nonce (byte-array (Arrays/copyOfRange message 0 (secretbox/nonce-length)))
        ciphertext (byte-array (Arrays/copyOfRange message (secretbox/nonce-length) (alength message)))]
    (secretbox/decrypt secret-key
                       nonce
                       ciphertext)))

(defn box
  "`box/encrypt` with a random nonce, prepended to the result."
  [recipient-pk sender-sk plaintext]
  (let [nonce (generate-box-nonce)
        ciphertext (box/encrypt recipient-pk sender-sk nonce plaintext)]
    (byte-array (concat nonce ciphertext))))

(defn box-open
  "`box/decrypt` where the ciphertext is prefixed with its nonce."
  [sender-pk recipient-sk message]
  (let [nonce (byte-array (Arrays/copyOfRange message 0 (secretbox/nonce-length)))
        ciphertext (byte-array (Arrays/copyOfRange message (secretbox/nonce-length) (alength message)))]
    (box/decrypt sender-pk
                 recipient-sk
                 nonce
                 ciphertext)))

(defn derive-secretbox-key
  "`pwhash/derive-key` configured for secretbox keys."
  [password]
  (let [salt (random/randombytes (pwhash/salt-length))]
    {:salt salt
     :secret-key (pwhash/derive-key (secretbox/key-length)
                                    password
                                    salt)}))
