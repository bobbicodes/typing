(ns typing.events
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [typing.db :as db]
   [typing.subs :as subs]
   [typing.words :as words]))

(defn rand-text [n]
  (str (apply str (interpose " " (take n (shuffle words/common-words)))) " "))

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
 ::set-text2
 (fn [db [_ value]]
   (assoc db :text2 value)))

(re-frame/reg-event-db
 ::insert-press
 (fn [db [_ value]]
   (update db :presses conj value)))

(re-frame/reg-event-db
 ::set-ave-wpm
 (fn [db [_ value]]
   (assoc db :ave-wpm value)))

(re-frame/reg-event-db
 ::clear-presses
 (fn [db [_ value]]
   (assoc db :presses [])))

(re-frame/reg-event-db
 ::set-current-key
 (fn [db [_ value]]
   (assoc db
          :current-key value
          :cursor-pos (if (= value
                             (nth @(re-frame/subscribe [::subs/text])
                                  @(re-frame/subscribe [::subs/cursor-pos])))
                        (do (re-frame/dispatch [::insert-press (js/Date.now)])
                            (if (= "" (apply str (drop (inc @(re-frame/subscribe [::subs/cursor-pos])) ; are we at the end?
                                                       @(re-frame/subscribe [::subs/text]))))
                              (do (re-frame/dispatch [::set-text @(re-frame/subscribe [::subs/text2])])
                                  (re-frame/dispatch [::set-text2 (rand-text 4)])
                                  0) ; reset counter and update text
                              (inc @(re-frame/subscribe [::subs/cursor-pos]))))
                        @(re-frame/subscribe [::subs/cursor-pos])))))

@(re-frame/subscribe [::subs/deltas])

(re-frame/reg-event-db
 ::advance-cursor
 (fn [db [_ value]]
   (update db :cursor-pos inc)))