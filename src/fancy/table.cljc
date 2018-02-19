(ns fancy.table
  (:require [clojure.string :as str]
            [fancy.string :refer [pad-end]]
            [clojure.spec.alpha :as s]))

(defn- unique-keys
  "Create a sorted-set with all the keys from all the maps"
  [maps]
  (reduce into (sorted-set) (map keys maps)))

(s/fdef unique-keys
  :args (s/cat :maps (s/* map?))
  :ret set?)

(defn render-table
  "Eagerly evalutes `maps` to collect all the relevant keys, but lazily
  iterates over the lines of a left-aligned, mostly-markdown table."
  ([ks maps]
   (let [header (map #(str " " % " ") ks)
         rows (map (fn [m] (map #(str " " (get m %) " ") ks)) maps)
         rowswidths (map #(map count %) (cons header rows))
         widths (apply map max rowswidths)
         rules (map #(str/join (repeat % "-")) widths)]
     (for [cells (list* header rules rows)]
       (str/join "|" (map (fn [cell width] (pad-end cell width)) cells widths)))))
  ([maps]
   (render-table (unique-keys maps) maps)))

(s/fdef render-table
  :args (s/cat :keys (s/? coll?)
               :maps (s/* map?))
  :ret (s/* string?))

(defn print-table
  "Like clojure.pprint/print-table but left-aligns cell contents,
  and defaults `ks` with keys from all maps instead of just the first,
  among a few other aesthetic simplifications."
  ([ks maps]
   (run! println (render-table ks maps)))
  ([maps]
   (run! println (render-table maps))))

(s/fdef print-table
  :args (s/cat :keys (s/? coll?)
               :maps (s/* map?))
  :ret nil?)
