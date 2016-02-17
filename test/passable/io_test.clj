(ns passable.io-test
  (:import java.io.Console)
  (:require [clojure.test :refer :all]
            [caesium.util :refer [array-eq]]
            [passable.io :refer :all]))

(deftest zeroing-test
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

(deftest env-test
  (testing "Can mock environment variables easily"
    (binding [*env* {:user "Stan" :home "/home/stan"}]
      (is (= "Stan" (:user *env*)))
      (is (= "/home/stan" (:home *env*)))
      (is (nil? (:path *env*)))))
  (testing "Can retrieve actual environment variables"
    (is (not (empty? *env*)))
    (is (contains? *env* :path))))

(deftest chars->bytes-test
  (testing "Can convert char array to bytes"
    (are [expected char-arr] (not (empty? (chars->bytes char-arr)))
      ;; TODO not most rigorous test
      (.toCharArray "test")
      (.toCharArray "我的密码是UTF-8"))))

(deftype ConsoleMock [^String password]
  PasswordReader
  (read-password [this] (.getBytes password)))

(defn make-console [pw] (ConsoleMock. pw))

(deftest read-password-test
  (testing "Can mock console"
    (binding [*console* (make-console "test")]
      (is (not (empty? (read-password *console*))))
      (is (= (seq (.getBytes "test"))
             (seq (read-password *console*)))))))

