(ns passable.core-test
  (:require [clojure.test :refer :all]
            [caesium.crypto.box :as box]
            [passable.core :refer :all]))

(def kp 
  (box/generate-keypair))
(deftest a-test
  (testing 
      (is (not kp)))

  (testing "FIXME, I fail."
    (is (= 0 1))))
