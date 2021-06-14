(ns typing.views
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [typing.events :as events]
   [typing.subs :as subs]
   [typing.words :as words]
   [goog.string :as gstring]))

(defn dispatch-keydown-rules []
  (re-frame/dispatch
   [::rp/set-keydown-rules
    {:event-keys [[[::events/set-re-pressed-example "Hello, world!"]
                   [{:keyCode 72} ;; h
                    {:keyCode 69} ;; e
                    {:keyCode 76} ;; l
                    {:keyCode 76} ;; l
                    {:keyCode 79} ;; o
                    ]]
                  [[::events/set-current-key " "] [{:keyCode 32}]]
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

                  [[::events/set-current-key "-"] [{:keyCode 189}]]]

     :clear-keys
     [[{:keyCode 27} ;; escape
       ]]
     :prevent-default-keys
[;; Ctrl+g
 {:keyCode   32}]
    ;; is pressed
     }]))

(defn rand-text [n]
  (str (apply str (interpose " " (take n (shuffle words/common-words)))) " "))

@(re-frame/subscribe [::subs/deltas])

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
     [:p
      [:span {:style {:font-size "40px"
                      :font-family "Georgia"}} 
       before]
      [:span#cursor {:style {:font-size "40px"}}  (if (= " " (nth @text @pos))
                                                    (gstring/unescapeEntities "&nbsp;")
                                                    (nth @text @pos))]
      [:span {:style {:font-size "40px"
                      :font-family "Georgia"}} after]]
     [:p
      [:span {:style {:font-size "40px"
                      :font-family "Georgia"}}
       @text2]]

     #_[:div
        [:button
         {:on-click (re-frame/dispatch [::events/set-text (rand-text 10)])}
         "Re-pressed text"]]

     
     #_[:p
      [:span
       "Average (ms): "]
      [:strong (str (.round js/Math (/ (reduce + (for [x (range (dec (count @presses)))]
                                                   (- (nth @presses (inc x))
                                                      (nth @presses x))))
                                       (count @presses))))]]
     
     #_[:p
      [:span
       "cps: "]
      [:strong (str (/ 1000 (/ (reduce + (for [x (range (dec (count @presses)))]
                                                           (- (nth @presses (inc x))
                                                              (nth @presses x))))
                                               (count @presses))))]]

#_[:p
 [:span
  "wps: "]
 [:strong (str (/ (/ 1000 (/ (reduce + (for [x (range (dec (count @presses)))]
                                         (- (nth @presses (inc x))
                                            (nth @presses x))))
                             (count @presses)))
                  5))]]
     
     [:p
      [:span
       "wpm: "]
      [:strong (str (.round js/Math (* 60 (/ (/ 1000 (/ (reduce + @(re-frame/subscribe [::subs/deltas]))
                                        (count @presses)))
                             5))))]]

 
     
     (when-let [rpe @re-pressed-example]
       [:div
        {:style {:padding          "16px"
                 :background-color "lightgrey"
                 :border           "solid 1px grey"
                 :border-radius    "4px"
                 :margin-top       "16px"}}
        rpe])]))

(defn main-panel []
  (let [text (re-frame/subscribe [::subs/text])]
    [:div
     [:h1
      "Re-pressed Typing Test"]
     [:p {:style {:font-size "40px"
                  :font-family "Georgia"}}]
     (dispatch-keydown-rules)
     [display-re-pressed-example]]))

@(re-frame/subscribe [::subs/text])
(js/Date.now)