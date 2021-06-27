(ns typing.db
  (:require
   [typing.words :as words]))

(defn rand-text [n]
  (str (apply str (interpose " " (take n (shuffle words/common-words)))) " "))

(def default-db
  {:text (rand-text 4)
   :text2 (rand-text 4)
   :current-key nil
   :cursor-pos 0
   :presses []
   :ave-wpm 0
   :prob-keys []
   :words words/common-words})
