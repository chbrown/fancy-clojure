(ns fancy.string
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]))

(defn repeat-to-length
  "Repeat the string or character `x` as needed to fill out a string of `target-length` characters,
  truncating from the end if the final repeat of `x` extends past `target-length`."
  [x target-length]
   ; TODO: fail if (empty? x)
  (let [x-length (if (string? x) (count x) 1)
        n (quot target-length x-length)
        underflow (rem target-length x-length)]
    (cond-> (str/join (repeat n x))
      ; append a substring of x if there's underflow
      (pos? underflow) (str (subs x 0 underflow)))))

(s/fdef repeat-to-length
  ; TODO: in the string? case, check that it's not empty
  :args (s/cat :x (s/or :string string? :chr map?))
  :ret string?)

(defn pad-start
  "left-pad (right-align) `string` to a total length of `target-length`
  by repeating the string or character `padding-item` at the beginning.
  When not provided, `padding-item` defaults to a single space."
  ([string target-length]
   (pad-start string target-length \space))
  ([string target-length padding-item]
   (let [padding-length (- target-length (count string))]
     (str (repeat-to-length padding-item padding-length) string))))

(s/fdef pad-start
  ; TODO: check that string is not already longer than target-length
  ;       (and decide what to do in that case)
  :args (s/cat :string string?
               :target-length nat-int?
               :padding-item (s/? (s/or :string string? :chr map?)))
  :ret string?)

(defn pad-end
  "right-pad (left-align) `string` to a total length of `target-length`
  by repeating the string or character `padding-item` at the end.
  When not provided, `padding-item` defaults to a single space."
  ([string target-length]
   (pad-end string target-length \space))
  ([string target-length padding-item]
   (let [padding-length (- target-length (count string))]
     (str string (repeat-to-length padding-item padding-length)))))

(s/fdef pad-end
  ; TODO: check that string is not already longer than target-length
  ;       (and decide what to do in that case)
  :args (s/cat :string string?
               :target-length nat-int?
               :padding-item (s/? (s/or :string string? :chr map?)))
  :ret string?)
