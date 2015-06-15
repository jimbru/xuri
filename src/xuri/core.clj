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

(defn- hex-digit?
  "Predicate true if character is a valid hexadecimal digit."
  [c]
  (let [ci (int c)]
    (or (<= (int \0) ci (int \9))
        (<= (int \A) ci (int \F))
        (<= (int \a) ci (int \f)))))

(defn percent-decode-char
  "Decodes a single percent-encoded octet. Properly encoded, each octet
  should be a three character sequence. Returns nil when a sequence that
  can't be decoded is encountered."
  [s]
  (assert (= (count s) 3))
  (when (and (= (first s) \%) (every? hex-digit? (rest s)))
    (-> (subs s 1)
        (Integer/valueOf 16)
        .byteValue
        list
        byte-array
        (String. "UTF-8"))))

(defn percent-decode
  "TODO"
  [s]
  (loop [s (str s)
         acc ""]
    (if (< (count s) 3)
      (str acc s)
      (if-let [decoded (percent-decode-char (subs s 0 3))]
        (recur (subs s 3) (str acc decoded))
        (recur (subs s 1) (str acc (first s)))))))
