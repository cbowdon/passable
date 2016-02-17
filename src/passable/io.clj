(ns passable.io
  (:import java.nio.CharBuffer
           java.nio.charset.Charset
           java.util.Arrays)
  (:require [clojure.java.io :refer [file]]
            [clojure.string :as str]))

(defmulti zero!
  "Overwrites mutable array with zeros.
  Returns the same referenced array, having mutated it."
  type)

(defmethod zero! (class (char-array []))
  [c-array]
  (do
    (Arrays/fill c-array (char 0))
    c-array))

(defmethod zero! (class (byte-array []))
  [b-array]
  (do
    (Arrays/fill b-array (byte 0))
    b-array))

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
    "Read password into a byte-array. Ensure you zero the array as soon as possible."))

(defn chars->bytes [char-arr]
  (let [cb (CharBuffer/wrap char-arr)
        bb (.encode (Charset/forName "UTF-8") cb)
        result (Arrays/copyOfRange (.array bb)
                                   (.position bb)
                                   (.limit bb))]
    (zero! char-arr)
    (zero! (.array cb))
    (zero! (.array bb))
    result))

(deftype Console []
  PasswordReader
  (read-password [this]
    (let [char-pwd (.readPassword (System/console))
          byte-pwd (chars->bytes char-pwd)]
      (zero! char-pwd)
      byte-pwd)))

(def ^:dynamic *console*
  "The system console (i.e. for getting stdin)."
  (Console.))

(defn make-path [& paths]
  (.getPath (apply file paths)))
