(ns user
  "REPL init"
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pprint]
            [clojure.string :as str]
            [clojure.java.io :as io]

            [fancy.ansi :as ansi :refer [bold blink inverse
                                         black red green blue yellow magenta cyan white]]
            [fancy.layout :as layout :refer [pr-seq pr-pretty prn-pretty]]
            [fancy.string :as string :refer [repeat-to-length pad-start pad-end]]
            [fancy.table :as table :refer [render-table print-table]]

            [clojure.tools.namespace.repl :refer [refresh]]))
