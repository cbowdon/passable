(ns passable.cli
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]
            [passable.io :refer [*env* make-path]]))

(defn- usage [action summary]
  (->> [(format "passable %s" action)
        ""
        "Options:"
        summary]
       (string/join \newline)))

(defn- assert-no-errors
  "Throw if `errors` are present, otherwise return input."
  [action opts]
  (let [{:keys [options errors summary]} opts]
    (if (and (empty? errors)
             (not (:help options)))
      opts
      (->> (format "%s\n%s" (string/join errors) (usage action summary))
           (java.lang.IllegalArgumentException.)
           (throw)))))

(defn- default-keyfile-path
  [home user]
  (make-path home ".passable" user))

(defn parse-keypair [args]
  (->> [["-u" "--user USER" "Username"
         :default (:user *env*)]
        ["-o" "--out OUT" "Keyfile destination"
         :default (default-keyfile-path (:home *env*) (:user *env*))]
        ["-h" "--help"]]
       (parse-opts args)
       (assert-no-errors "keypair")
       (:options)))

(defn parse-init [args]
  (->> [["-k" "--key KEY" "Keyfile"
         :default (default-keyfile-path (:home *env*) (:user *env*))]
        ["-o" "--out OUT" "Store destination"
         :default "passable_store"]
        ["-h" "--help"]]
       (parse-opts args)
       (assert-no-errors "init")
       (:options)))
