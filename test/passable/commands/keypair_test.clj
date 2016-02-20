(ns passable.commands.keypair-test
  (:import (java.io BufferedReader
                    StringReader))
  (:require [clojure.test :refer :all]
            [passable.io :refer [*console*]]
            [passable.io-test :refer [make-console]]
            [passable.commands.keypair :refer :all]))

(deftest keypair-test
  (testing "Generates file-creation task"
    (binding [*console* (make-console "password1")]
      (let [{:keys [task path contents]} (keypair "roger" "/Users/roger/")]
        (is (= 'write-file task))
        (is (= "/Users/roger/.passable/roger" path))
        (let [{:keys [user public secret]} contents]
          (is (= "roger" user))
          (is (some? public))
          (is (some? secret)))))))
