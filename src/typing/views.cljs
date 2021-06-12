(ns typing.views
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [typing.events :as events]
   [typing.subs :as subs]
   [typing.words :as words]
   [goog.string :as gstring]
   ))

(defn dispatch-keydown-rules []
  (re-frame/dispatch
   [::rp/set-keydown-rules
    {:event-keys [
                  [[::events/set-re-pressed-example "Hello, world!"]
                   [{:keyCode 72} ;; h
                    {:keyCode 69} ;; e
                    {:keyCode 76} ;; l
                    {:keyCode 76} ;; l
                    {:keyCode 79} ;; o
                    ]]
                  [[::events/advance-cursor "g"]
                   [{:keyCode 71}] ;; g
                   ]
                   [[::events/set-current-key " "] [{:keyCode 32}]]
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
                  ]

     :clear-keys
     [[{:keyCode 27} ;; escape
       ]]}]))

(defn rand-text [n]
  (apply str (interpose " " (take n (shuffle words/common-words)))))

(defn display-re-pressed-example []
  (let [re-pressed-example (re-frame/subscribe [::subs/re-pressed-example])
        text (re-frame/subscribe [::subs/text])
        pos (re-frame/subscribe [::subs/cursor-pos])
        key (re-frame/subscribe [::subs/current-key])
        before (apply str (take @pos @text))
        after (apply str (drop (inc @pos) @text))]
    [:div

     [:span {:style {:font-size "40px"
                     :font-family "Georgia"}} before]
     [:span#cursor {:style {:font-size "40px"}}  (if (= " " (nth @text @pos))
                                                   (gstring/unescapeEntities "&nbsp;")
                                                   (nth @text @pos))]
     [:span {:style {:font-size "40px"
                     :font-family "Georgia"}} after]

     #_[:div
      [:button
       {:on-click (re-frame/dispatch [::events/set-text (rand-text 10)])}
       "Re-pressed text"]]

     [:p
      [:span
       "Cursor is at position "]
      [:strong (str @pos)]]

[:p
 [:span
  "Current key: "]
 [:strong (str @key)]]
     
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
                  :font-family "Georgia"}}
      ]
     (dispatch-keydown-rules)
     [display-re-pressed-example]
     ]))

@(re-frame/subscribe [::subs/text])