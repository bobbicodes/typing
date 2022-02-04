(ns app.renderer.views
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [app.renderer.events :as events]
   [app.renderer.subs :as subs]
   [app.renderer.words :as words]
   [goog.string :as gstring]))

(defn button [label onclick]
  [:button
   {:on-click onclick}
   label])

(defn dispatch-keydown-rules []
  (re-frame/dispatch
   [::rp/set-keydown-rules
    {:event-keys [[[::events/set-current-key " "] [{:keyCode 32}]]
                  [[::events/set-current-key "A"] [{:keyCode 65 :shiftKey true}]]
                  [[::events/set-current-key "B"] [{:keyCode 66 :shiftKey true}]]
                  [[::events/set-current-key "C"] [{:keyCode 67 :shiftKey true}]]
                  [[::events/set-current-key "D"] [{:keyCode 68 :shiftKey true}]]
                  [[::events/set-current-key "E"] [{:keyCode 69 :shiftKey true}]]
                  [[::events/set-current-key "F"] [{:keyCode 70 :shiftKey true}]]
                  [[::events/set-current-key "G"] [{:keyCode 71 :shiftKey true}]]
                  [[::events/set-current-key "H"] [{:keyCode 72 :shiftKey true}]]
                  [[::events/set-current-key "I"] [{:keyCode 73 :shiftKey true}]]
                  [[::events/set-current-key "J"] [{:keyCode 74 :shiftKey true}]]
                  [[::events/set-current-key "K"] [{:keyCode 75 :shiftKey true}]]
                  [[::events/set-current-key "L"] [{:keyCode 76 :shiftKey true}]]
                  [[::events/set-current-key "M"] [{:keyCode 77 :shiftKey true}]]
                  [[::events/set-current-key "N"] [{:keyCode 78 :shiftKey true}]]
                  [[::events/set-current-key "O"] [{:keyCode 79 :shiftKey true}]]
                  [[::events/set-current-key "P"] [{:keyCode 80 :shiftKey true}]]
                  [[::events/set-current-key "Q"] [{:keyCode 81 :shiftKey true}]]
                  [[::events/set-current-key "R"] [{:keyCode 82 :shiftKey true}]]
                  [[::events/set-current-key "S"] [{:keyCode 83 :shiftKey true}]]
                  [[::events/set-current-key "T"] [{:keyCode 84 :shiftKey true}]]
                  [[::events/set-current-key "U"] [{:keyCode 85 :shiftKey true}]]
                  [[::events/set-current-key "V"] [{:keyCode 86 :shiftKey true}]]
                  [[::events/set-current-key "W"] [{:keyCode 87 :shiftKey true}]]
                  [[::events/set-current-key "X"] [{:keyCode 88 :shiftKey true}]]
                  [[::events/set-current-key "Y"] [{:keyCode 89 :shiftKey true}]]
                  [[::events/set-current-key "Z"] [{:keyCode 90 :shiftKey true}]]
                  [[::events/set-current-key "a"] [{:keyCode 65}]]
                  [[::events/set-current-key "b"] [{:keyCode 66}]]
                  [[::events/set-current-key "c"] [{:keyCode 67}]]
                  [[::events/set-current-key "d"] [{:keyCode 68}]]
                  [[::events/set-current-key "e"] [{:keyCode 69}]]
                  [[::events/set-current-key "f"] [{:keyCode 70}]]
                  [[::events/set-current-key "g"] [{:keyCode 71}]]
                  [[::events/set-current-key "h"] [{:keyCode 72}]]
                  [[::events/set-current-key "i"] [{:keyCode 73}]]
                  [[::events/set-current-key "j"] [{:keyCode 74}]]
                  [[::events/set-current-key "k"] [{:keyCode 75}]]
                  [[::events/set-current-key "l"] [{:keyCode 76}]]
                  [[::events/set-current-key "m"] [{:keyCode 77}]]
                  [[::events/set-current-key "n"] [{:keyCode 78}]]
                  [[::events/set-current-key "o"] [{:keyCode 79}]]
                  [[::events/set-current-key "p"] [{:keyCode 80}]]
                  [[::events/set-current-key "q"] [{:keyCode 81}]]
                  [[::events/set-current-key "r"] [{:keyCode 82}]]
                  [[::events/set-current-key "s"] [{:keyCode 83}]]
                  [[::events/set-current-key "t"] [{:keyCode 84}]]
                  [[::events/set-current-key "u"] [{:keyCode 85}]]
                  [[::events/set-current-key "v"] [{:keyCode 86}]]
                  [[::events/set-current-key "w"] [{:keyCode 87}]]
                  [[::events/set-current-key "x"] [{:keyCode 88}]]
                  [[::events/set-current-key "y"] [{:keyCode 89}]]
                  [[::events/set-current-key "z"] [{:keyCode 90}]]
                  [[::events/set-current-key "-"] [{:keyCode 189}]]
                  [[::events/set-current-key "'"] [{:keyCode 222}]]]

     :clear-keys
     [[{:keyCode 27} ;; escape
       ]]
     :prevent-default-keys
     [;; Ctrl+g
      {:keyCode   32}]
    ;; is pressed
     }]))

(defn rand-text [n]
  (str (apply str (interpose " " (take n (shuffle @(re-frame/subscribe [::subs/words]))))) " "))

(defn display-re-pressed-example []
  (let [re-pressed-example (re-frame/subscribe [::subs/re-pressed-example])
        text (re-frame/subscribe [::subs/text])
        text2 (re-frame/subscribe [::subs/text2])
        pos (re-frame/subscribe [::subs/cursor-pos])
        key (re-frame/subscribe [::subs/current-key])
        presses (re-frame/subscribe [::subs/presses])
        before (apply str (take @pos @text))
        after (apply str (drop (inc @pos) @text))]
    [:div
     #_[:p
        [:span
         "wpm: "]
        [:strong (str @(re-frame/subscribe [::subs/ave-wpm]))]]
     [:p {:style {:font-size "40px"
                  :font-family "Georgia"
                  :cursor "none"}}
      [:span
       before]
      [:span#cursor  {:style {:background "#fc199a"}} (if (= " " (nth @text @pos))
                                                        (gstring/unescapeEntities "&nbsp;")
                                                        (nth @text @pos))]
      [:span  after]
      [:p
       [:span
        @text2]]]




     (when-let [rpe @re-pressed-example]
       [:div
        {:style {:padding          "16px"
                 :background-color "lightgrey"
                 :border           "solid 1px grey"
                 :border-radius    "4px"
                 :margin-top       "16px"}}
        rpe])]))

(defn path [level]
  (let [ave (re-frame/subscribe [::subs/all-time-ave])]
    (str "M -0.0 -0.025 L 0.0 0.025 L "
         (- (Math/cos (* (* 180 (/ level (* 2 @ave))) (/ js/Math.PI 180))))
         " "
         (- (Math/sin (* (* 180 (/ level (* 2 @ave))) (/ js/Math.PI 180))))
         " Z")))

(defn gauge []
  [:svg {:width    "20%"
         :view-box (str "-1 -1.1 2 1")}
   [:g
    [:circle {:cx 0 :cy 0 :r 1 :stroke "blue" :stroke-width 0.05}]
    [:path {:d (path @(re-frame/subscribe [::subs/ave-wpm])) :stroke "red"
            :stroke-width 0.05}]]])

(defn zero-pad [n]
  (if (< n 10)
    (str "0" n)
    n))

(defn fmt-time [seconds]
  (let [minutes (quot seconds 60)
        hours (quot minutes 60)]
    (str (when (< 0 hours)
           (str hours ":"))
         (zero-pad (mod minutes 60)) ":"
         (zero-pad (mod seconds 60)))))

(defn ave-time [letter]
  (let [presses (sort-by key @(re-frame/subscribe [::subs/presses]))
        times
        (remove #(> % 5000)
                (for [n (range 1 (count presses))
                      :when (= (last (nth presses n)) letter)]
                  (- (first (nth presses n)) (first (nth presses (dec n))))))]
    (.round js/Math (/ (reduce + times) (count times)))))

(defn problem-keys [presses]
  (let [presses (sort-by key presses)
        keys (distinct (vals presses))]
    (reverse (sort-by last (for [letter keys]
                             [letter (ave-time letter)])))))

(def lowercase-letters
  (set (map char (range 97 123))))

(defn main-panel []
  (let [text (re-frame/subscribe [::subs/text])
        total (re-frame/subscribe [::subs/total-time])
        presses (re-frame/subscribe [::subs/presses])
        deltas (re-frame/subscribe [::subs/deltas])
        times (re-frame/subscribe [::subs/times])
        prob-keys (re-frame/subscribe [::subs/prob-keys])
        moving-ave (re-frame/subscribe [::subs/moving-ave])
        high-speed (re-frame/subscribe [::subs/high-speed])
        all-time-ave (re-frame/subscribe [::subs/all-time-ave])
        errors (re-frame/subscribe [::subs/errors])]
    [:div [:center
           [gauge]
           [:h3 (str @moving-ave " wpm")]
           (dispatch-keydown-rules)
           [display-re-pressed-example]]
     [:div
      [:p (str "Keypresses analyzed: " (count @presses))]
      [:p (str "Total time: " (fmt-time @total))]
      [:p (str "Average: " @all-time-ave " wpm")]
      [:p (str "High speed: " @high-speed " wpm")]
      [:div
       [:span "Problem keys (ave. ms): "]
       [:span (interpose ", " (for [key (take 4 (filter #(contains? lowercase-letters (first %)) @prob-keys))]
                                (str (first key) " - " (last key))))]
       [:p (str "Errors: " @errors "  ("
                (.round js/Math (* 100 (/ @errors (+ (count @presses) @errors))))
                "%)")]]]]))