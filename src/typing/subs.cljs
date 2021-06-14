(ns typing.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::text
 (fn [db]
   (:text db)))

(re-frame/reg-sub
 ::text2
 (fn [db]
   (:text2 db)))

(re-frame/reg-sub
 ::cursor-pos
 (fn [db]
   (:cursor-pos db)))

(re-frame/reg-sub
 ::presses
 (fn [db]
   (:presses db)))

(re-frame/reg-sub
 ::deltas
 (fn [db]
   (let [presses (:presses db)]
     (remove #(> % 1000)
             (for [x (range (dec (count presses)))]
               (- (nth presses (inc x))
                  (nth presses x)))))))

(re-frame/reg-sub
 ::ave-wpm
 (fn [db]
   (:ave-wpm db)))

(re-frame/reg-sub
 ::current-key
 (fn [db]
   (:current-key db)))

(re-frame/reg-sub
 ::re-pressed-example
 (fn [db _]
   (:re-pressed-example db)))
