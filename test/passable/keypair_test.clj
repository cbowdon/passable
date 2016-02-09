(ns passable.keypair-test
  (:import (java.io BufferedReader
                    StringReader))
  (:require [clojure.test :refer :all]
            [passable.keypair :refer :all]))

(deftest keypair-test
  (testing "Generates two file-creation tasks"
    (binding [*in* (BufferedReader. (StringReader. "password1"))]
      (let [tasks (keypair "roger" "/home/roger/")]
        (is (= 2 (count tasks)))
        (is (= '(write-file write-file) (map :task tasks)))
        (is (= #{"/Users/roger/.passable/roger.public-key"
                 "/Users/roger/.passable/roger.secret-key"}
               (set (map :path tasks))))
        (is (not (or (empty? (:contents (first tasks)))
                     (empty? (:contents (second tasks)))))))))
  (testing "Takes username and home from environment variable if not provided"
    (binding [*env* {:username "klaus"
                     :home "/home/klaus"}]
      (is (= #{"/home/klaus/.passable/klaus.public-key"
               "/home/klaus/.passable/klaus.secret-key"}
             (set (map :path (keypair))))))))
