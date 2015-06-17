(ns xuri.core
  (:require [clojure.string :as string]))


; 1. Check for :// absolute
; 2. Check for // authority
; 3. Check for / ./ ../ ~ path-ish-ness
; 4. Check for leading : for scheme-first (check scheme-valid chars)
; 5. Leading segment looks like a domain?
; 6. Unknown...assume path?


(defn- looks-like-a-path? [s]
  (or (re-find #"^/[^/]" s)
      (re-find #"^\./" s)
      (re-find #"^\.\./" s)
      (re-find #"^~" s)))


(defn parse-as-relative-authority
  "TODO"
  [uri]
  nil
  )

(defn- split-scheme [uri]
  (string/split uri #"://" 2))

(defn looks-absolute?
  "TODO"
  [uri]
  (let [[scheme rel-part] (split-scheme uri)]
    (if (re-matches #"." ...
    ))

(defn parse-as-absolute
  "TODO"
  [uri]
  (let [[scheme relative-part] (split-scheme uri)
        result (parse-as-relative-authority relative-part)]
    (assoc result :scheme scheme)))

(defn parse
  "Parses a URI into its components."
  [uri]
  (cond
    ;; 1. Absolute
    (re-find #"://" uri)
      (parse-as-absolute uri)
    ;; 2. Authority relative
    (.startsWith uri "//")
      (parse-as-relative-authority uri)
    (looks-like-a-path? uri)
      nil  ;; starts with a single slash, dot, or dot-dot, it's a path
    :else
      nil  ;; must start with a path or later? what about URNs?
      ))
