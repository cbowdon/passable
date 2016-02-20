(ns passable.cli-test
  (:require [clojure.test :refer :all]
            [passable.io :refer [*env*]]
            [passable.cli :refer :all]))

(deftest keypair-parsing
  (testing "Can parse keypair"
    (are [user out] (= (parse-keypair ["-u" user
                                       "-o" out])
                       {:user user
                        :out out})
      "darlene" "/home/darlene/.passable/darlene"))
  (testing "Defaults taken from environment"
    (are [user home out] (= (binding [*env* {:user user
                                             :home home}]
                              (parse-keypair []))
                            {:user user
                             :out out})
      "darlene" "/home/darlene" "/home/darlene/.passable/darlene"))
  (testing "Throws on unexpected options"
    (is (thrown? java.lang.IllegalArgumentException
                 (parse-keypair ["-e" "evil"]))))
  (testing "Throws on help (with a usage message)"
    (is (thrown? java.lang.IllegalArgumentException
                 (parse-keypair ["-h"])))))

(deftest init-parsing
  (testing "Can parse init"
    (are [out key-file] (= (parse-init ["-o" out
                                        "-k" key-file])
                           {:out out
                            :key key-file})
      "~/dir/passable_store" "~/elliot/.passable/elliot"))
  (testing "Defaults taken from environment"
    (are [out user home key-file] (= (binding [*env* {:user user
                                                      :home home}]
                                       (parse-init []))
                                     {:out out
                                      :key key-file})
      "passable_store" "elliot" "/home/elliot" "/home/elliot/.passable/elliot"))
  (testing "Throws on unexpected options"
    (is (thrown? java.lang.IllegalArgumentException
                 (parse-init ["-e" "evil"]))))
  (testing "Throws on help (with a usage message)"
    (is (thrown? java.lang.IllegalArgumentException
                 (parse-init ["-h"])))))

