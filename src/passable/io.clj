(ns passable.io
  (:require [clojure.string :as str]))

;; thanks environ!
;; https://github.com/weavejester/environ
(defn- keywordize [s]
  (-> (str/lower-case s)
      (str/replace "_" "-")
      (str/replace "." "-")
      (keyword)))

(defn- read-system-env []
  (->> (System/getenv)
       (map (fn [[k v]] [(keywordize k) v]))
       (into {})))

(def ^:dynamic *env*
  "A map of environment variables."
  (read-system-env))

(defprotocol PasswordReader
  (read-password [this]
    "Read password into a char-array. Ensure you zero the array as soon as possible."))
    ;; TODO convert into bytes without intermediate string
    ;; e.g. http://stackoverflow.com/a/20604909
  
(deftype Console []
  PasswordReader
  (read-password [this]
    (->> (System/console)
         (.readPassword))))

(def console (Console.))
