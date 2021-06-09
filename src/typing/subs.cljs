(ns typing.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::text
 (fn [db]
   (:text db)))

(re-frame/reg-sub
 ::cursor-pos
 (fn [db]
   (:cursor-pos db)))

(re-frame/reg-sub
 ::re-pressed-example
 (fn [db _]
   (:re-pressed-example db)))
