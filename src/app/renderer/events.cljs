(ns app.renderer.events
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [app.renderer.db :as db]
   [app.renderer.subs :as subs]
   [app.renderer.words :as words]))

(def lowercase-letters
  (set (map char (range 97 123))))

(defn rand-text [n]
  (let [prob-keys (filter #(contains? lowercase-letters (first %))
                          @(re-frame/subscribe [::subs/prob-keys]))
        rand-words (if (empty? prob-keys)
                     (take n (shuffle words/common-words))
                     (shuffle (map #(first (words/words-with % words/common-words))
                                   (map first (take n prob-keys)))))]
    (str (apply str (interpose " " rand-words)) " ")))

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

(re-frame/reg-event-db
 ::insert-time
 (fn [db [_ time]]
   (assoc db :last-press time)))

(re-frame/reg-event-db
 ::set-ave-wpm
 (fn [db [_ value]]
   (assoc db :ave-wpm value)))


(def mydb
  {:last-press 0
   :key-map {:a [150 1]}})

(update-in mydb [:key-map :a]
           (fn [[total-ms num-pressed]] [(+ total-ms (- (js/Date.now) @(re-frame/subscribe [::subs/last-press])))
                                         (inc num-pressed)]))

(js/Date.now)


(re-frame/reg-event-db
 ::update-key-map
 (fn [db [_ time key]]
   (update-in db [:key-map key]
              (fn [[total-ms num-pressed]] [(+ total-ms (- time (:last-press db)))
                                            (inc num-pressed)]))))

(defn ave-time [letter]
  (let [presses @(re-frame/subscribe [::subs/presses])
        times (remove #(> % 5000)
                (for [n (range 1 (count presses))
                      :when (= (:key (nth presses n)) letter)]
                  (- (:time (nth presses n)) (:time (nth presses (dec n))))))]
    (.round js/Math (/ (reduce + times) (count times)))))

(defn stats [letter presses]
  (let [times (remove #(> % 5000)
                      (for [n (range 1 (count presses))
                            :when (= (:key (nth presses n)) letter)]
                        (- (:time (nth presses n)) (:time (nth presses (dec n))))))]
    [(reduce + times)
     (count times)]))

(defn add-stat [m l]
  (assoc m
         (keyword l)
         (stats l @(re-frame/subscribe [::subs/presses]))))

(def speed-map
  (reduce add-stat {} lowercase-letters))

(comment
  (stats "p" @(re-frame/subscribe [::subs/presses]))
  (assoc {} 
         (keyword "h") 
         (stats "h" @(re-frame/subscribe [::subs/presses])))
  speed-map
  )

(defn problem-keys [presses]
  (let [keys (distinct (map :key presses))]
    (reverse (sort-by last (for [letter keys]
                             [letter (ave-time letter)])))))

(re-frame/reg-event-db
 ::analyze-prob-keys
 (fn [db [_ value]]
   (assoc db :prob-keys (problem-keys value))))

(re-frame/reg-event-db
 ::clear-presses
 (fn [db [_ value]]
   (assoc db :presses [])))

(re-frame/reg-event-db
 ::update-high-speed
 (fn [db [_ value]]
   (assoc db :high-speed value)))

(re-frame/reg-event-db
 ::inc-errors
 (fn [db [_ value]]
   (update db :errors inc)))

(re-frame/reg-event-db
 ::set-current-key
 (fn [db [_ value]]
   (let [presses (subvec (vec (map :time (:presses db)))
                         (max 0 (- (count (:presses db)) 50)))
         deltas (remove #(> % 5000)
                        (for [x (range (dec (count presses)))]
                          (- (nth presses (inc x))
                             (nth presses x))))
         moving-ave (.round js/Math  (* 60 (/ (/ 1000 (/ (reduce + deltas)
                                              (count deltas)))
                                   5)))]
   (assoc db
          :high-speed (max (:high-speed db) moving-ave)
          :current-key value
          :cursor-pos (if (= value (nth (:text db) (:cursor-pos db)))
                        (do (re-frame/dispatch [::insert-press (js/Date.now) value])
                            (re-frame/dispatch [::update-key-map (js/Date.now) value])
                            (re-frame/dispatch [::insert-time (js/Date.now)])
                            (if (= "" (apply str (drop (inc (:cursor-pos db)) ; are we at the end?
                                                       (:text db))))
                              (do (re-frame/dispatch [::set-text (:text2 db)])
                                  (re-frame/dispatch [::set-text2 (rand-text 4)])
                                  (re-frame/dispatch [::analyze-prob-keys
                                                      (subvec (:presses db) (- (count (:presses db)) (min (count (:presses db)) 1000)))
                                                      (vec (take 1000 (:presses db)))])
                                  (re-frame/dispatch [::set-words 
                                                      (words/must-include
                                                       words/common-words
                                                       (set (map first (take 5 (filter #(contains? lowercase-letters (first %))
                                                                                       (:prob-keys db))))))])
                                  0) ; reset counter and update text
                              (inc (:cursor-pos db))))
                        (do
                          (re-frame/dispatch [::inc-errors])
                          (:cursor-pos db)))))))

(re-frame/reg-event-db
 ::advance-cursor
 (fn [db [_ value]]
   (update db :cursor-pos inc)))