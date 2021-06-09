(ns typing.events
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [typing.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-re-pressed-example
 (fn [db [_ value]]
   (assoc db :re-pressed-example value)))
