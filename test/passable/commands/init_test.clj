(ns passable.commands.init-test
  (:require [clojure.test :refer :all]
            [passable.io :refer [*env*]]
            [passable.crypto :refer [generate-keypair]]
            [passable.commands.init :refer :all]))

(deftest init-test

  (testing "Generates a write file task"
    (let [{:keys [public secret]} (generate-keypair)
          {:keys [task path]} (init :dir "/home/stan"
                                    :user "stan"
                                    :public-key public
                                    :secret-key secret)]
      (is (= task 'write-file))
      (is (= path "/home/stan/.passable"))))

  (testing "Creates path using $HOME if none provided"
    (binding [*env* {:home "/home/darlene"
                     :user "darlene"}]
      (let [{:keys [public secret]} (generate-keypair)
            {:keys [task path]} (init :public-key public
                                      :secret-key secret)]
        (is (= path "/home/darlene/.passable")))))

  (testing "Generates an empty data structure"
    (let [{:keys [public secret]} (generate-keypair)
          contents (:contents (init :dir "/home/stan"
                                    :user "stan"
                                    :public-key public
                                    :secret-key secret))]
      (is (= (:data contents)
             {}))))

  (testing "Generates a user key and group key"
    (let [{:keys [public secret]} (generate-keypair)
          contents (:contents (init :dir "/home/stan"
                                    :user "stan"
                                    :public-key public
                                    :secret-key secret))
          user-keys (:user-keys contents)
          group-keys (:group-keys contents)]
      (is (:stan user-keys))
      (is (:stan group-keys)))))

