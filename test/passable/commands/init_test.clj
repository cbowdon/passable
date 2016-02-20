(ns passable.commands.init-test
  (:require [clojure.test :refer :all]
            [passable.crypto :refer [generate-keypair]]
            [passable.commands.init :refer :all]))

(deftest init-test

  (testing "Generates a write file task"
    (let [{:keys [public secret]} (generate-keypair)
          {:keys [task path]} (init "/home/stan"
                                    "stan"
                                    public
                                    secret)]
      (is (= task 'write-file))
      (is (= path "/home/stan/.passable"))))

  (testing "Generates an empty data structure"
    (let [{:keys [public secret]} (generate-keypair)
          contents (:contents (init "/home/stan"
                                    "stan"
                                    public
                                    secret))]
      (is (= (:data contents)
             {}))))

  (testing "Generates a user key and group key"
    (let [{:keys [public secret]} (generate-keypair)
          contents (:contents (init "/home/stan"
                                    "stan"
                                    public
                                    secret))
          user-keys (:user-keys contents)
          group-keys (:group-keys contents)]
      (is (some? (:stan user-keys)))
      (is (some? (:stan group-keys))))))
