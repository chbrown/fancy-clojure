(ns fancy.test-table
  (:require [clojure.test :as test :refer [deftest is are testing]]
            [clojure.string :as str]
            [fancy.table :refer [render-table print-table]]))

(def example-maps
  [{:a 1 :b "short"}
   {:b "long" :c 99999}])

(def example-maps-lines
  [" :a | :b    | :c    "
   "----|-------|-------"
   " 1  | short |       "
   "    | long  | 99999 "])

(def example-maps-b-lines
  [" :b    "
   "-------"
   " short "
   " long  "])

(deftest test-render-table
  (is (= ["" ""] (render-table nil)))
  (is (= ["" "" ""] (render-table [{}])))
  (is (= example-maps-lines (render-table example-maps)))
  (is (= example-maps-b-lines (render-table [:b] example-maps))))

(deftest test-print-table
  (is (= (str (str/join \newline example-maps-lines) \newline)
         (with-out-str (print-table example-maps))))
  (is (= (str (str/join \newline example-maps-b-lines) \newline)
         (with-out-str (print-table [:b] example-maps)))))
