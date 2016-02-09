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
