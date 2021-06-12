(ns typing.db
  (:require
   [typing.words :as words]))

(defn rand-text [n]
  (apply str (interpose " " (take n (shuffle words/common-words)))))

(def default-db
  {:text (rand-text 10)
   :current-key nil
   :cursor-pos 0})
