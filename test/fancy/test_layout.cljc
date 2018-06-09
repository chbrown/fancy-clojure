(ns fancy.test-layout
  (:require [clojure.test :as test :refer [deftest is are testing]]
            [clojure.string :as str]
            [fancy.layout :refer [-pr-raw pr-pretty prn-pretty]]))

(def example-map
  {:abcdefghij {"Name"           "Chris-v2"
                "Age"            0
                "Favorite Color" nil}
   :structures [#{'a 'b} '(1 2) nil]
   :z          :user/custom})

(def example-map-str
  (str/join \newline ["{:abcdefghij {\"Name\"           \"Chris-v2\""
                      "              \"Age\"            0"
                      "              \"Favorite Color\" nil}"
                      " :structures [#{'a 'b} '(1 2) nil]"
                      " :z          :user/custom}"]))

(deftest test--pr-raw
  (is (thrown-with-msg? #?(:clj Exception :cljs js/Error) #"Unrecognized layout item" (-pr-raw 1))))

(deftest test-pr-pretty
  (is (= "" (with-out-str (pr-pretty))))
  (is (= example-map-str (with-out-str (pr-pretty example-map))))
  (is (= "{}" (with-out-str (pr-pretty {}))))
  (is (= "\\A" (with-out-str (pr-pretty \A))))
  (is (= "'a :z" (with-out-str (pr-pretty 'a :z))))
  (is (= "1 2 3" (with-out-str (pr-pretty 1 2 3)))))

(deftest test-prn-pretty
  (is (= ":hello\n" (with-out-str (prn-pretty :hello)))))
