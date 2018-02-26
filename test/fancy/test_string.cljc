(ns fancy.test-string
  (:require [clojure.test :as test :refer [deftest is are testing]]
            [fancy.string :refer [repeat-to-length pad-start pad-end]]))

(deftest test-repeat-to-length
  (is (= "11" (repeat-to-length "1" 2)))
  (is (= "11" (repeat-to-length \1 2)))
  (is (= "121" (repeat-to-length "12" 3)))
  (is (= "123" (repeat-to-length "1234" 3))))

(deftest test-pad-start
  (testing "Several ways to arrive at \" @\""
    (are [actual] (= " @" actual)
         (pad-start "@" 2)
         (pad-start "@" 2 \space)
         (pad-start "@" 2 " ")
         (pad-start " @" 2)))
  (testing "Several ways to arrive at \"-@\""
    (are [actual] (= "-@" actual)
         (pad-start "@" 2 \-)
         (pad-start "@" 2 "-")
         (pad-start "-@" 2)
         (pad-start "-@" 2 \-)))
  (testing "Padding with multiple characters"
    (is (= "a@" (pad-start "@" 2 "abc")))
    (is (= "ab@" (pad-start "@" 3 "abc")))
    (is (= "abc@" (pad-start "@" 4 "abc")))
    (is (= "abca@" (pad-start "@" 5 "abc"))))
  (testing "Guava examples"
    (is (= "007" (pad-start "7" 3 \0)))
    (is (= "2010" (pad-start "2010" 3 \0)))))

(deftest test-pad-end
  (testing "Several ways to arrive at \"@ \""
    (are [actual] (= "@ " actual)
         (pad-end "@" 2)
         (pad-end "@" 2 \space)
         (pad-end "@" 2 " ")
         (pad-end "@ " 2)))
  (testing "Several ways to arrive at \"@-\""
    (are [actual] (= "@-" actual)
         (pad-end "@" 2 \-)
         (pad-end "@" 2 "-")
         (pad-end "@-" 2)
         (pad-end "@-" 2 \-)))
  (testing "Padding with multiple characters"
    (is (= "@a" (pad-end "@" 2 "abc")))
    (is (= "@ab" (pad-end "@" 3 "abc")))
    (is (= "@abc" (pad-end "@" 4 "abc")))
    (is (= "@abca" (pad-end "@" 5 "abc"))))
  (testing "Guava examples"
    (is (= "4.000" (pad-end "4." 5 \0)))
    (is (= "2010" (pad-end "2010" 3 \!)))))
