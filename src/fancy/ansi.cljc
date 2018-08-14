(ns fancy.ansi
  "Helpers for wrapping strings in ANSI escape codes")

(def ^:private ESC
  \u001b)

(def ^:private RESET
  (str ESC \[ \0 \m))

(def ^:private effect-names
  ["reset"      ; 0
   "bold"       ; 1
   "faint"      ; 2
   "italic"     ; 3
   "underline"  ; 4
   "blink"      ; 5
   "blink-fast" ; 6
   "inverse"     ; 7
   "conceal"    ; 8
   "crossout"]) ; 9

(def ^:private color-names
  ["black"   ; 0
   "red"     ; 1
   "green"   ; 2
   "yellow"  ; 3
   "blue"    ; 4
   "magenta" ; 5
   "cyan"    ; 6
   "white"]) ; 7

(defmacro ^:private create-helpers! []
  (list* 'do
    (concat
      ; define basic effects vars:
      ;   bold faint italic underline blink blink-fast inverse conceal crossout
      (for [[effect-name code] (next (map vector effect-names (range)))]
        `(defn ~(symbol effect-name)
           ~(str "Combine strings and apply " effect-name " effect.")
           {:arglists '([& ~'strings])}
           [& s#]
           (str ESC \[ ~code \m (apply str s#) RESET)))
      ; define foreground color vars:
      ;   black red green blue yellow magenta cyan white
      (for [[color-name code] (map vector color-names (iterate inc 30))]
        `(defn ~(symbol color-name)
           ~(str "Combine strings and color them " color-name ".")
           {:arglists '([& ~'strings])}
           [& s#]
           (str ESC \[ ~code \m (apply str s#) RESET)))
      ; define background color vars:
      ;   bg-black bg-red bg-green bg-yellow bg-blue bg-magenta bg-cyan bg-white
      (for [[color-name code] (map vector color-names (iterate inc 40))]
        `(defn ^{:arglists '([& strings])} ~(symbol (str "bg-" color-name))
           ~(str "Combine strings and color the background " color-name ".")
           {:arglists '([& ~'strings])}
           [& s#]
           (str ESC \[ ~code \m (apply str s#) RESET))))))
(create-helpers!)
