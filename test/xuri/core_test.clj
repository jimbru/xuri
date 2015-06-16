(ns xuri.core-test
  (:require [clojure.string :as string]
            [clojure.test :refer :all]
            [xuri.core :refer :all]))

(deftest percent-encode-all-test
  (testing "empty strings"
    (is (= (percent-encode-all nil) ""))
    (is (= (percent-encode-all "") "")))
  (testing "characters"
    (is (= (percent-encode-all \a) "%61")))
  (testing "ASCII strings"
    (is (= (percent-encode-all "a") "%61"))
    (is (= (percent-encode-all " ") "%20"))
    (is (= (percent-encode-all "%") "%25"))
    (is (= (percent-encode-all "A b") "%41%20%62")))
  (testing "UTF-8 strings"
    (is (= (percent-encode-all "£") "%C2%A3"))
    (is (= (percent-encode-all "€") "%E2%82%AC"))
    (is (= (percent-encode-all "A%b €£") "%41%25%62%20%E2%82%AC%C2%A3"))))

(deftest percent-encode-test
  (testing "empty strings"
    (is (= (percent-encode nil) ""))
    (is (= (percent-encode "") "")))
  (testing "characters"
    (is (= (percent-encode \a) "a"))
    (is (= (percent-encode \space) "%20")))
  (testing "ASCII strings"
    (is (= (percent-encode "a") "a"))
    (is (= (percent-encode " ") "%20"))
    (is (= (percent-encode "%") "%25"))
    (is (= (percent-encode "A b") "A%20b"))
    (is (= (percent-encode ",-./09:@AZ[^_`az}~")
           "%2C-.%2F09%3A%40AZ%5B%5E_%60az%7D~")))
  (testing "UTF-8 strings"
    (is (= (percent-encode "£") "%C2%A3"))
    (is (= (percent-encode "€") "%E2%82%AC"))
    (is (= (percent-encode "A%b €£") "A%25b%20%E2%82%AC%C2%A3"))))

(deftest percent-decode-test
  (testing "empty strings"
    (is (= (percent-decode nil) ""))
    (is (= (percent-decode "") "")))
  (testing "ASCII strings"
    (is (= (percent-decode "a") "a"))
    (is (= (percent-decode "%25") "%"))
    (is (= (percent-decode "A%20b") "A b"))
    (is (= (percent-decode "%2C-.%2F09%3A%40AZ%5B%5E_%60az%7D~")
           ",-./09:@AZ[^_`az}~")))
  (testing "UTF-8 strings"
    (is (= (percent-decode "%C2%A3") "£"))
    (is (= (percent-decode "%c2%a3") "£"))
    (is (= (percent-decode "%E2%82%AC") "€"))
    (is (= (percent-decode "A%25b%20%E2%82%AC%C2%A3") "A%b €£")))
  (testing "busted strings"
    (is (= (percent-decode "%") "%"))
    (is (= (percent-decode "%2") "%2"))
    (is (= (percent-decode "%25%") "%%"))
    (is (= (percent-decode "%%25") "%%"))
    (is (= (percent-decode "fo%6f b%61r") "foo bar"))))
