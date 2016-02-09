(ns passable.io-test
  (:import java.io.Console)
  (:require [clojure.test :refer :all]
            [passable.io :refer :all]))

(deftest env-test
  (testing "Can mock environment variables easily"
    (binding [*env* {:user "Stan" :home "/home/stan"}]
      (is (= "Stan" (:user *env*)))
      (is (= "/home/stan" (:home *env*)))
      (is (nil? (:path *env*)))))
  (testing "Can retrieve actual environment variables"
    (is (not (empty? *env*)))
    (is (contains? *env* :path))))


(deftype ConsoleMock [^String password]
  PasswordReader
  (read-password [this] (.toCharArray password)))

(defn make-console [pw] (ConsoleMock. pw))

(deftest read-password-test
  (testing "Can mock console"
    (is (not (empty? (read-password (make-console "test")))))
    (is (= (seq (.toCharArray "test"))
           (seq (read-password (make-console "test")))))))

