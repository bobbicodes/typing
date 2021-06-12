(ns typing.events
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [typing.db :as db]
   [typing.subs :as subs]
   ))

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
 ::set-current-key
 (fn [db [_ value]]
   (assoc db 
          :current-key value
          :cursor-pos (if (= value (nth @(re-frame/subscribe [::subs/text]) @(re-frame/subscribe [::subs/cursor-pos])))
                        (inc @(re-frame/subscribe [::subs/cursor-pos]))
                        @(re-frame/subscribe [::subs/cursor-pos]))
          )))

(re-frame/reg-event-db
 ::advance-cursor
 (fn [db [_ value]]
   (update db :cursor-pos inc)))