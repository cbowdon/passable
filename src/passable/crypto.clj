(ns passable.crypto
  (:require [caesium.randombytes :as random]
            [caesium.crypto.box :as box]
            [caesium.util :refer [hexify unhexify]]
            [caesium.crypto.secretbox :as secretbox]))

;; I had to look up the key and nonce length in the C code:
;; https://github.com/jedisct1/libsodium/blob/master/src/libsodium/include/sodium/crypto_secretbox_xsalsa20poly1305.h
;; They are in Kalium though:
;; https://github.com/abstractj/kalium/blob/master/src/main/java/org/abstractj/kalium/NaCl.java
;; Should probably add these to caesium too
(def secretbox-key-length 32)
(def secretbox-nonce-length 24)

(defn generate-secret-key []
  (random/randombytes secretbox-key-length))

(defn generate-nonce []
  (random/randombytes secretbox-nonce-length))

(defn secretbox
  "`secretbox/encrypt` with a random nonce, prepended to the result."
  [key plaintext]
  (let [nonce (generate-nonce)]
    ;; TODO the prepending part
    (secretbox/encrypt key nonce plaintext)))

(defn secretbox-open
  "`secretbox/decrypt` where the ciphertext is prefixed with its nonce."
  [key ciphertext]
  nil)

(defn zero!
  "Overwrites mutable byte-array with zeros. 

  Returns the same referenced array, having mutated it."
  [^bytes b-array]
  (do
    (java.util.Arrays/fill b-array (byte 0))
    b-array))
