(ns app.renderer.core
  (:require [reagent.core :refer [atom]]
            [reagent.dom :as rd]
            [re-frame.core :as re-frame]
            [re-pressed.core :as rp]
            [app.renderer.events :as events]
            [app.renderer.views :as views]))

(enable-console-print!)

(defonce state (atom 0))

(defn root-component []
  [views/main-panel])

(defn ^:dev/after-load start! []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  (rd/render
   [root-component]
   (js/document.getElementById "app-container")))
