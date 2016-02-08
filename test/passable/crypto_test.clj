(ns passable.crypto-test
  (:require [clojure.test :refer :all]
            [caesium.util :refer [hexify unhexify array-eq]]
            [passable.crypto :refer :all]))

(deftest secretboxes
  (testing "Plaintext != ciphertext"
    (are [plaintext] (let [secret-key (generate-secret-key)]
                       (not (array-eq plaintext
                                      (secretbox secret-key plaintext))))
      (.getBytes "Is this the real life?")
      (.getBytes "Is this just fantasy?")
      (.getBytes "Caught in a landslide")
      (.getBytes "No escape from reality")))
  (testing "No repeated ciphertexts (no nonce reuse)"
    (are [plaintext] (let [secret-key (generate-secret-key)]
                       (not (array-eq (secretbox secret-key plaintext)
                                      (secretbox secret-key plaintext))))
      (.getBytes "Is this the real life?")
      (.getBytes "Is this just fantasy?")
      (.getBytes "Caught in a landslide")
      (.getBytes "No escape from reality")))
  (testing "Can round trip"
    (are [plaintext] (let [secret-key (generate-secret-key)]
                       (array-eq plaintext
                                 (->> plaintext
                                      (secretbox secret-key)
                                      (secretbox-open secret-key))))
      (.getBytes "Is this the real life?")
      (.getBytes "Is this just fantasy?")
      (.getBytes "Caught in a landslide")
      (.getBytes "No escape from reality"))))

(deftest boxes
  (testing "Plaintext != ciphertext"
    (are [plaintext] (let [{pk1 :public sk1 :secret } (generate-keypair)  ;; recipient
                           {pk2 :public sk2 :secret } (generate-keypair)] ;; sender
                       (not (array-eq plaintext
                                      (box pk1 sk2 plaintext))))
      (.getBytes "Is this the real life?")
      (.getBytes "Is this just fantasy?")
      (.getBytes "Caught in a landslide")
      (.getBytes "No escape from reality")))
  (testing "No repeated ciphertexts (no nonce reuse)"
    (are [plaintext] (let [{pk1 :public sk1 :secret } (generate-keypair)  ;; recipient
                           {pk2 :public sk2 :secret } (generate-keypair)] ;; sender
                       (not (array-eq (box pk1 sk2 plaintext)
                                      (box pk1 sk2 plaintext))))
      (.getBytes "Is this the real life?")
      (.getBytes "Is this just fantasy?")
      (.getBytes "Caught in a landslide")
      (.getBytes "No escape from reality")))
  (testing "Can round trip"
    (are [plaintext] (let [{pk1 :public sk1 :secret } (generate-keypair)  ;; recipient
                           {pk2 :public sk2 :secret } (generate-keypair)] ;; sender
                       (array-eq plaintext
                                 (->> plaintext
                                      (box pk1 sk2)
                                      (box-open pk2 sk1))))
      (.getBytes "Is this the real life?")
      (.getBytes "Is this just fantasy?")
      (.getBytes "Caught in a landslide")
      (.getBytes "No escape from reality"))))

(deftest zeroing
  (let [zeros (byte-array [0 0 0 0 0 0 0 0])]
    (testing "Should return the cleared array"
      (are [nonzeros] (array-eq zeros (zero! nonzeros))
        (byte-array [1 2 3 4 5 6 7 8])
        (byte-array [9 0 1 1 3 0 1 1]))
      (testing "Should mutate the original array"
        (are [nonzeros] (let [orig nonzeros]
                          (zero! orig)
                          (array-eq orig zeros))
          (byte-array [1 2 3 4 5 6 7 8])
          (byte-array [9 0 1 1 3 0 1 1]))))))
