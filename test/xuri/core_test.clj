(ns xuri.core-test
  (:require [clojure.test :refer :all]
            [xuri.core :refer :all]))

(def rfc3986-examples
  [
   ;; Section 1.1.2
   "ftp://ftp.is.co.za/rfc/rfc1808.txt"
   "http://www.ietf.org/rfc/rfc2396.txt"
   "ldap://[2001:db8::7]/c=GB?objectClass?one"
   "mailto:John.Doe@example.com"
   "news:comp.infosystems.www.servers.unix"
   "tel:+1-816-555-1212"
   "telnet://192.0.2.16:80/"
   "urn:oasis:names:specification:docbook:dtd:xml:4.1.2"
   ;; Section 3
   "foo://example.com:8042/over/there?name=ferret#nose"
   "urn:example:animal:ferret:nose"
   ;; Section 5.2.4
   "/a/b/c/./../../g"
   "mid/content=5/../6"
   ;; Section 5.4
   "http://a/b/c/d;p?q"   ;; see additional relative resolutions
   ;; Appendix B
   "http://www.ics.uci.edu/pub/ietf/uri/#Related"
  ])

(def real-world-examples
  [
   "example.com"
   "example.com:8080"
   "example.com:80/foobar"
   "/foobar"
   "./foobar"
   "foobar"   ;; consider as path or domain?
   "ftp://username:password@example.com/"
  ])

(deftest parse-test
  (is (= (parse "http://example.com") {}))
  )
