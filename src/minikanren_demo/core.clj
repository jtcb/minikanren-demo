(ns minikanren-demo.core
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic]
        [clojure.repl]))

(defn nato [n]                 ; define the relation (nato n); "natural number"
  (conde                       ; conde is logical disjunction
    [(== n 0)]                 ; unify n with 0; "zero is a natural number"
    [(fresh [n-1]              ; create new logic variable n-1
            (== n `(~'S ~n-1)) ; unify n with (S n-1) "successsor of n-1"
            (nato n-1))]))     ; and n-1 is a natural number

(defn pluso [n1 n2 n3]               ; (pluso n1 n2 n3); "n1 + n2 = n3"
  (conde
    [(== n1 0) (== n2 n3)]           ; base case: n1 = 0 and n2 = n3
    [(fresh [n1-1 n3-1]              ; fresh lvars n1-1 and n3-1
            (== n1 `(~'S ~n1-1))     ; unify n1 with (S n1-1)
            (== n3 `(~'S ~n3-1))     ; unify n3 with (S n3-1)
            (pluso n1-1 n2 n3-1))])) ; "n1-1 + n2 = n3-1"

; "Start the repl"
; $ lein repl

; "Give me 3 instantiations of q such that q is a natural number"
; => (run 3 [q] (nato q))
; (0 (S 0) (S (S 0)))

; "Give me all q such that 0 + 1 = q"
; => (run* [q] (pluso 0 '(S 0) q))
; ((S 0))

; "Give me all q such that q + 1 = 2"
; => (run* [q] (pluso q '(S 0) '(S (S 0))))

; "a + b = 1 and b + 1 = c"
; (run 1 [q] (fresh [a b c]
;                   (== q {:a a :b b :c c})
;                   (pluso a b '(S 0))
;                   (pluso b '(S 0) c)))
; => ({:a 0, :b (S 0), :c (S (S 0))})

