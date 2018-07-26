(ns fancy.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [fancy.test-layout]
            [fancy.test-string]
            [fancy.test-table]))

(doo-tests 'fancy.test-layout
           'fancy.test-string
           'fancy.test-table)
