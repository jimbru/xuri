(ns xuri.core
  (:require [clojure.string :as string]))

(def reserved-chars
  (set "!*'();:@&=+$,/?#[]"))

(def unreserved-chars
  (set "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~"))

(defn percent-encode-all
  "Percent-encodes all characters in the passed-in string (or character),
  returning a string result."
  [s]
  (->> (.getBytes (str s) "UTF-8")
       (map (partial format "%%%02X"))
       (string/join)))

(defn percent-decode-all
  "TODO"
  [s]
  s)

(defn percent-encode
  "Percent-encodes all not-unreserved (per RFC 3986) characters in the
  passed-in string (or character), returning a string result. Use this
  function to properly encode URI components."
  [s]
  (loop [s (str s)
         acc ""]
    (if-let [c (first s)]
      (recur (rest s) (str acc (if (contains? unreserved-chars c)
                                 c
                                 (percent-encode-all c))))
      acc)))

(defn percent-decode
  "TODO"
  [s]
  s)
