(ns typing.views
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [typing.events :as events]
   [typing.subs :as subs]
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

(defn display-re-pressed-example []
  (let [re-pressed-example (re-frame/subscribe [::subs/re-pressed-example])
        pos (re-frame/subscribe [::subs/cursor-pos])]
    [:div

     [:span {:style {:font-size "40px"
                     :font-family "Georgia"}} "There's the text before the cursor,"]
[:span#cursor {:style {:font-size "40px"}}  (gstring/unescapeEntities "&nbsp;")]
[:span {:style {:font-size "40px"
                :font-family "Georgia"}} "and the text after the cursor"]

     [:div
      [:button
       {:on-click dispatch-keydown-rules}
       "set keydown rules"]]

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
                 :margin-top       "16px"
                 }}
        rpe])]))

(defn main-panel []
  (let [text (re-frame/subscribe [::subs/text])]
    [:div
     [:h1
      "Re-pressed Typing Test"]
     [:p {:style {:font-size "40px"
                  :font-family "Georgia"}}
      @text]
     [display-re-pressed-example]
     ]))
