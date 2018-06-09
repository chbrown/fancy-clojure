(ns fancy.layout
  (:require [clojure.string :as str]
            [fancy.string :refer [repeat-to-length]]))

(defn- split-on
  "Return a lazy sequence of each consecutive segment of `coll` for which
  pred returns false for all elements."
  [pred coll]
  (remove #(every? pred %) (partition-by pred coll)))

(defprotocol CountableChars
  "Implemented by types that can be represented by a known number of characters"
  (count-chars [this]
    "Count the number of characters that `this` would require to `pr` it"))

(extend-protocol CountableChars
  clojure.lang.Seqable
  (count-chars [coll] (apply + (map count-chars coll)))
  CharSequence
  (count-chars [cs] (count cs))
  Character
  (count-chars [_] 1)
  nil
  (count-chars [_] 3))

(defn- measure-width
  [seqs]
  (->> seqs
       flatten
       (split-on #(= \newline %))
       (map count-chars)
       ; default to 0 if these seqs carry no width
       (apply max 0)))

(def ^:dynamic *wrap-width* 40)

(defn- indent-seqs
  [seqs offset]
  ; calculate the indentation this function inserts
  (let [width (measure-width seqs)]
    (if (> width *wrap-width*)
      (interpose (list \newline (str/join (repeat offset \space))) seqs)
      (interpose (list \space) seqs))))

(defprotocol FixedWidthLayout
  "A protocol for fixed-width layout of data structures for pretty-printing purposes."
  (pr-seq [this offset]
    "Return a seq (of characters, or strings, or seqs of strings, etc.) representing `this`.
    When called, the current line position is at `offset`, and if the result
    requires line breaks, it is the responsibility of the implementation to
    indent `offset` spaces after the line break"))

(defn- pr-seq-items
  "Helper for pr-seq implementations like IPersistentList and IPersistentVector."
  [items offset]
  (indent-seqs (map #(pr-seq % offset) items) offset))

(extend-protocol FixedWidthLayout
  clojure.lang.IPersistentMap
  (pr-seq [m offset]
    ; put each key-value pair on the same line,
    ; with a newline between each,
    ; and align right of the keys
    ; TODO: run the keys through pr-seq too
    (let [key-strings (map pr-str (keys m))
          key-width (apply max 0 (map count key-strings))
          ; key-offset adds 1 for the opening brace
          key-offset (+ offset 1)
          key-seqs (map (fn [k] (list k (repeat-to-length \space (- key-width (count k))))) key-strings)
          ; value-offset adds the key-width and 1 for the separator space
          value-offset (+ key-offset key-width 1)
          value-seqs (map #(pr-seq % value-offset) (vals m))]
      (list
       (list \{)
       (indent-seqs (map #(list %1 \space %2) key-seqs value-seqs) key-offset)
       (list \}))))
  clojure.lang.IPersistentList
  (pr-seq [l offset]
    (list
     (list \' \()
     ; increment offset for opening parens
     (pr-seq-items l (+ offset 2))
     (list \))))
  clojure.lang.IPersistentVector
  (pr-seq [v offset]
    ; separate each value on its own line
    (list
     (list \[)
     ; increment offset for opening bracket
     (pr-seq-items v (+ offset 1))
     (list \])))
  clojure.lang.IPersistentSet
  (pr-seq [s offset]
    ; separate each value on its own line
    (list
     (list \# \{)
     ; increment offset for opening set reader macro
     (pr-seq-items s (+ offset 2))
     (list \})))
  clojure.lang.Symbol
  (pr-seq [s _]
    (list (list \') (pr-str s)))
  Object
  (pr-seq [o _]
    (list (pr-str o)))
  nil
  (pr-seq [_ _]
    ; (pr-str nil) is always just "nil"
    (list "nil")))

(defprotocol Printable
  (-pr-raw [item]
    "Print a CharSequence (including Strings) or char directly to *out*"))

(extend-protocol Printable
  CharSequence
  (-pr-raw [cs]
    (.append *out* cs)) ; ^CharSequence
  Character
  (-pr-raw [c]
    (.append *out* c)) ; ^char
  #?(:clj Object :cljs default)
  (-pr-raw [item]
    (throw (ex-info "Unrecognized layout item" {:item item}))))

(defn pr-pretty
  "Like clojure.core/pr, but prettier, albeit less conservative with whitespace."
  ([] nil)
  ([x]
   (run! -pr-raw (flatten (pr-seq x 0))))
  ([x & more]
   (pr-pretty x)
   (.append *out* \space)
   (if-let [nmore (next more)]
     (recur (first more) nmore)
     (apply pr-pretty more))))

(defn prn-pretty
  "Same as pr-pretty followed by (newline)."
  [& more]
  (apply pr-pretty more)
  (newline)
  (when *flush-on-newline*
    (flush)))
