(ns passable.crypto-test
  (:require [clojure.test :refer :all]
            [caesium.util :refer [hexify unhexify array-eq]]
            [passable.crypto :refer :all]))

(deftest secretboxes

  (testing "Plaintext != ciphertext"
    (are [plaintext secret-key]
        (not (array-eq plaintext (secretbox secret-key plaintext)))
      (.getBytes "Is this the real life?") (generate-secret-key)
      (.getBytes "Is this just fantasy?") (generate-secret-key)
      (.getBytes "Caught in a landslide") (generate-secret-key)
      (.getBytes "No escape from reality") (generate-secret-key)))

  (testing "No repeated ciphertexts (no nonce reuse)"
    (are [plaintext secret-key]
        (not (array-eq (secretbox secret-key plaintext)
                       (secretbox secret-key plaintext)))
      (.getBytes "Is this the real life?") (generate-secret-key)
      (.getBytes "Is this just fantasy?") (generate-secret-key)
      (.getBytes "Caught in a landslide") (generate-secret-key)
      (.getBytes "No escape from reality") (generate-secret-key)))

  (testing "Can round trip"
    (are [plaintext secret-key]
        (array-eq plaintext
                  (->> plaintext
                       (secretbox secret-key)
                       (secretbox-open secret-key)))
      (.getBytes "Is this the real life?") (generate-secret-key)
      (.getBytes "Is this just fantasy?") (generate-secret-key)
      (.getBytes "Caught in a landslide") (generate-secret-key)
      (.getBytes "No escape from reality") (generate-secret-key))))

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
