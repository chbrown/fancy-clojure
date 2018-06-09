(ns fancy.test-table
  (:require [clojure.test :as test :refer [deftest is are testing]]
            [fancy.table :refer [render-table]]))

(deftest test-render-table
  (is (= ["" ""] (render-table nil)))
  (is (= ["" "" ""] (render-table [{}])))
  (let [maps [{:a 1 :b "short"}
              {:b "long" :c 99999}]]
    (is (= [" :a | :b    | :c    "
            "----|-------|-------"
            " 1  | short |       "
            "    | long  | 99999 "] (render-table maps)))
    (is (= [" :b    "
            "-------"
            " short "
            " long  "] (render-table [:b] maps)))))
