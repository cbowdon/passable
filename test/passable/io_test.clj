(ns passable.io-test
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
