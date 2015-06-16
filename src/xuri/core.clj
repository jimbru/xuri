(ns xuri.core
  (:require [clojure.string :as string]))

(def reserved-chars
  (set "!*'();:@&=+$,/?#[]"))

(def unreserved-chars
  (set "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~"))

(defn- char->encoded-octet
  "Translates a character to its percent-encoded octet. Unreserved characters
  represent themselves while other characters are encoded into a three-character
  sequence beginning with a percent sign and ending in a hex value."
  [c]
  (if (contains? unreserved-chars c)
    c
    (->> (.getBytes (str c) "UTF-8")
         (map (partial format "%%%02X"))
         (string/join))))

(defn percent-encode
  "Percent-encodes the given string (per RFC 3986), returning a string result.
  Use this function to properly encode URI components."
  [s]
  (string/join (map char->encoded-octet s)))

(defn- hex-digit?
  "Predicate true if character is a valid hexadecimal digit."
  [c]
  (let [ci (int c)]
    (or (<= (int \0) ci (int \9))
        (<= (int \A) ci (int \F))
        (<= (int \a) ci (int \f)))))

(defn- encoded-octet->byte
  "Translates a percent-encoded octet to its byte value. When encoded, an octet
  may be represented by one of the unreserved characters itself, or as a three-
  character string beginning with a percent sign and ending in a hex value."
  [s]
  (let [sc (count s)]
    (assert (or (= sc 1) (= sc 3)))
    (.byteValue
      (if (= sc 1)
        (int (first s))
        (Integer/valueOf (subs s 1) 16)))))

(defn- parse-encoded-octets
  "Consumes a sequence of percent-encoded characters and produces a lazy
  sequence of parsed octets. These octets take the form of strings, either
  of a single character, or of a three-character encoded sequence beginning
  with a percent sign and ending in a hex value."
  [s]
  (lazy-seq
    (when (seq s)
      (let [trigram (take 3 s)]
        (if (and (= (count trigram) 3)
                 (= (first trigram) \%)
                 (every? hex-digit? (rest trigram)))
          (cons (string/join trigram) (parse-encoded-octets (nthnext s 3)))
          (cons (str (first s)) (parse-encoded-octets (next s))))))))

(defn percent-decode
  "Percent-decodes the given string (per RFC 3986), returning a string result.
  Use this function to properly decode URI components."
  [s]
  (as-> (parse-encoded-octets s) $
        (map encoded-octet->byte $)
        (byte-array $)
        (String. $ "UTF-8")))
