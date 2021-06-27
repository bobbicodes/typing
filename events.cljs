(ns typing.events
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [typing.db :as db]
   [typing.subs :as subs]
   [typing.words :as words]))

(def lowercase-letters #{"a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z"})

(defn rand-text [n]
  (str (apply str (interpose " " (shuffle (map #(first (words/words-with % words/common-words))
                                               (map first (take n (filter #(contains? lowercase-letters (first %))
                                                                          @(re-frame/subscribe [::subs/prob-keys])))))))) " "))


(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-re-pressed-example
 (fn [db [_ value]]
   (assoc db :re-pressed-example value)))

(re-frame/reg-event-db
 ::set-text
 (fn [db [_ value]]
   (assoc db :text value)))

(re-frame/reg-event-db
 ::set-words
 (fn [db [_ value]]
   (assoc db :words value)))

(re-frame/reg-event-db
 ::set-text2
 (fn [db [_ value]]
   (assoc db :text2 value)))

(re-frame/reg-event-db
 ::insert-press
 (fn [db [_ time key]]
   (update db :presses #(conj % {:time time :key key}))))

(re-frame/dispatch [::insert-press (js/Date.now) "h"])

(re-frame/reg-event-db
 ::set-ave-wpm
 (fn [db [_ value]]
   (assoc db :ave-wpm value)))

(defn ave-time [letter]
  (let [presses @(re-frame/subscribe [::subs/presses])
        times
        (remove #(> % 5000)
                (for [n (range 1 (count presses))
                      :when (= (:key (nth presses n)) letter)]
                  (- (:time (nth presses n)) (:time (nth presses (dec n))))))]
    (.round js/Math (/ (reduce + times) (count times)))))

(defn problem-keys [presses]
  (let [keys (distinct (map :key presses))]
    (reverse (sort-by last (for [letter keys]
                             [letter (ave-time letter)])))))

@(re-frame/subscribe [::subs/presses])

(re-frame/reg-event-db
 ::analyze-prob-keys
 (fn [db [_ value]]
   (assoc db :prob-keys (problem-keys value))))

(re-frame/reg-event-db
 ::clear-presses
 (fn [db [_ value]]
   (assoc db :presses [])))



(re-frame/reg-event-db
 ::set-current-key
 (fn [db [_ value]]
   (assoc db
          :current-key value
          :cursor-pos (if (= value (nth (:text db) (:cursor-pos db)))
                        (do (re-frame/dispatch [::insert-press (js/Date.now) value])
                            (if (= "" (apply str (drop (inc (:cursor-pos db)) ; are we at the end?
                                                       (:text db))))
                              (do (re-frame/dispatch [::set-text (:text2 db)])
                                  (re-frame/dispatch [::set-text2 (rand-text 4)])
                                  (re-frame/dispatch [::analyze-prob-keys (:presses db)])
                                  (re-frame/dispatch [::set-words 
                                                      (words/must-include
                                                       words/common-words
                                                       (set (map first (take 5 (filter #(contains? lowercase-letters (first %))
                                                                                       (:prob-keys db))))))])
                                  0) ; reset counter and update text
                              (inc (:cursor-pos db))))
                        (:cursor-pos db)))))

(re-frame/reg-event-db
 ::advance-cursor
 (fn [db [_ value]]
   (update db :cursor-pos inc)))