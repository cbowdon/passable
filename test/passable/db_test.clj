(ns passable.db-test
  (:require [clojure.test :refer :all]
            [caesium.util :refer [array-eq]]
            [clojure.java.jdbc :refer [with-db-connection]]
            [passable.db :refer :all]))

(deftest create-schema-in-memory
  (testing "Can create without any errors"
    (is (= [0] (create-schema (spec ":memory:"))))))

(defmacro with-in-memory-db
  "Convenience macro for testing. 
  Creates an in-memory database bound to the given symbol."
  [binding & body]
  `(with-db-connection [~binding (spec ":memory:")]
     (create-schema ~binding)
     ~@body))

(deftest adding-users
  (testing "Can add and retrieve a user"
    (let [n "romero"
          pk (byte-array [1 1 1 1 1 1 1 1])]
      (with-in-memory-db db
        (is (< 0 (add-user db {:name n :public-key pk})))
        (is (array-eq pk (:public-key (get-user db {:name n}))))))))

(deftest adding-groups
  (testing "Can add and retrieve group"
    (let [n "romero"
          pk (byte-array [1 1 1 1 1 1 1 1])
          sk (byte-array [2 2 2 2 2 2 2 2])]
      (with-in-memory-db db
        (add-user db {:name n :public-key pk})
        (add-group db {:name n})
        (add-user-group db {:user-name n
                            :group-name n
                            :issuer-name n
                            :secret-key sk})
        (let [{:keys [secret-key public-key issuer]}
              (get-user-group-key db {:user-name n
                                      :group-name n})]
          (is (array-eq public-key pk))
          (is (= issuer n))
          (is (array-eq secret-key sk)))))))
