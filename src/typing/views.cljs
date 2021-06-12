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
    {:event-keys [[[::events/set-re-pressed-example "Hello, world!"]
                   [{:keyCode 72} ;; h
                    {:keyCode 69} ;; e
                    {:keyCode 76} ;; l
                    {:keyCode 76} ;; l
                    {:keyCode 79} ;; o
                    ]]
                  [[::events/advance-cursor "g"]
                   [{:keyCode 71}] ;; g
                   ]]

     :clear-keys
     [[{:keyCode 27} ;; escape
       ]]}]))

(defn rand-text [n]
  (apply str (interpose " " (take n (shuffle words/common-words)))))

(defn display-re-pressed-example []
  (let [re-pressed-example (re-frame/subscribe [::subs/re-pressed-example])
        text (re-frame/subscribe [::subs/text])
        pos (re-frame/subscribe [::subs/cursor-pos])
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
     [display-re-pressed-example]
     ]))

@(re-frame/subscribe [::subs/text])