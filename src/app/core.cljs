(ns app.core
  (:require cljsjs.material
            [reagent.core :as r]))

(def upgrade-dom (.. js/componentHandler -upgradeDom))

(defn mdl [& children]
  (r/create-class
    {:display-name         "mdl-wrapper"
     :component-did-mount  (fn [] (upgrade-dom))
     :component-did-update (fn [] (upgrade-dom))
     :reagent-render       (fn [& children] (into [:div] children))}))

(def init-state
  {:user    {:email "hello@example.com"}
   :menu    [["Home" "home"]
             ["Inbox" "inbox"]
             ["Trash" "delete"]
             ["Spam" "report"]
             ["Forums" "forum"]
             ["Updates" "flag"]
             ["Promos" "local_offer"]
             ["Purchases" "shopping_cart"]
             ["Social" "people"]
             ["Help" "help_outline"]]
   :options ["Click per object"
             "Views per object"
             "Objects selected"
             "Objects viewed"]
   :charts  {:line [{:labels ["a" "b" "c"]
                     :series [[1 2 3 4 5]
                              [45 7 33 2 45]
                              [6 78 4 2 11]]}
                    {:labels ["a" "b" "c"]
                     :series [[1 2 3 4 5]
                              [45 7 33 2 45]
                              [6 78 4 2 11]]}]
             :pie  [{:labels [" " " " "90%"]
                     :series [9 1 0]}
                    {:labels [" " " " "70%"]
                     :series [7 3 0]}
                    {:labels [" " " " "60%"]
                     :series [6 4 0]}
                    {:labels [" " " " "40%"]
                     :series [4 6 0]}]}})

(def app-state (r/atom init-state))

(defn header []
  [:header.demo-header.mdl-layout__header.mdl-color--grey-100.mdl-color-text--grey-600
   [:div.mdl-layout__header-row
    [:span.mdl-layout-title "Home"]
    [:div.mdl-layout-spacer]
    [:div.mdl-textfield.mdl-js-textfield.mdl-textfield--expandable
     [:label.mdl-button.mdl-js-button.mdl-button--icon
      {:for "search"}
      [:i.material-icons "search"]]
     [:div.mdl-textfield__expandable-holder
      [:input#search.mdl-textfield__input {:type "text"}]
      [:label.mdl-textfield__label
       {:for "search"}
       "Enter your query..."]]]
    [:button#hdrbtn.mdl-button.mdl-js-button.mdl-js-ripple-effect.mdl-button--icon
     [:i.material-icons "more_vert"]]
    [:ul.mdl-menu.mdl-js-menu.mdl-js-ripple-effect.mdl-menu--bottom-right
     {:for "hdrbtn"}
     [:li.mdl-menu__item "About"]
     [:li.mdl-menu__item "Contact"]
     [:li.mdl-menu__item "Legal information"]]]])

(defn drawer []
  [:div.demo-drawer.mdl-layout__drawer.mdl-color--blue-grey-900.mdl-color-text--blue-grey-50
   [:header.demo-drawer-header
    [:img.demo-avatar {:src "images/user.jpg"}]
    [:div.demo-avatar-dropdown
     [:span (get-in @app-state [:user :email])]
     [:div.mdl-layout-spacer]
     [:button#accbtn.mdl-button.mdl-js-button.mdl-js-ripple-effect.mdl-button--icon
      [:i.material-icons {:role "presentation"} "arrow_drop_down"]
      [:span.visuallyhidden "Accounts"]]
     [:ul.mdl-menu.mdl-menu--bottom-right.mdl-js-menu.mdl-js-ripple-effect
      {:for "accbtn"}
      [:li.mdl-menu__item (get-in @app-state [:user :email])]
      [:li.mdl-menu__item "info@example.com"]
      [:li.mdl-menu__item
       [:i.material-icons "add"]
       "Add another account..."]]]]
   [:nav.demo-navigation.mdl-navigation.mdl-color--blue-grey-800
    (doall (for [[text icon] (get-in @app-state [:menu])]
             ^{:key icon}
             [:a.mdl-navigation__link
              {:href ""}
              [:i.mdl-color-text--blue-grey-400.material-icons
               {:role "presentation"} icon] text]))]])

(defn chart [constructor cursor opts]
  (r/create-class
    {:display-name         "Chartist"
     :component-did-mount  (fn [this]
                             (constructor. (r/dom-node this) (clj->js @cursor) (clj->js opts)))
     :component-did-update (fn [this]
                             (constructor. (r/dom-node this) (clj->js @cursor) (clj->js opts)))
     :reagent-render       (fn []
                             @cursor [:div])}))

(defn pie-charts []
  [:div.demo-charts.mdl-color--white.mdl-shadow--2dp.mdl-cell.mdl-cell--12-col.mdl-grid
   (doall (for [i (range (-> @app-state :charts :pie count))]
            ^{:key (str "pie-chart-" i)}
            [:div.demo-chart.mdl-cell.mdl-cell--4-col.mdl-cell--3-col-desktop
             [chart (.-Pie js/Chartist) (r/cursor app-state [:charts :pie i]) {:donut       true
                                                                               :donutWidth  15
                                                                               :labelOffset -70}]]))])

(defn line-charts []
  [:div.demo-graphs.mdl-shadow--2dp.mdl-color--white.mdl-cell.mdl-cell--8-col
   (doall (for [i (range (-> @app-state :charts :line count))]
            ^{:key (str "line-chart-" i)}
            [chart (.-Line js/Chartist) (r/cursor app-state [:charts :line i]) {:height 275}]))])

(defn cards []
  [:div.demo-cards.mdl-cell.mdl-cell--4-col.mdl-cell--8-col-tablet.mdl-grid.mdl-grid--no-spacing
   [:div.demo-updates.mdl-card.mdl-shadow--2dp.mdl-cell.mdl-cell--4-col.mdl-cell--4-col-tablet.mdl-cell--12-col-desktop
    [:div.mdl-card__title.mdl-card--expand.mdl-color--teal-300
     [:h2.mdl-card__title-text "Updates"]]
    [:div.mdl-card__supporting-text.mdl-color-text--grey-600
     "\n                Non dolore elit adipisicing ea reprehenderit consectetur culpa.\n              "]
    [:div.mdl-card__actions.mdl-card--border
     [:a.mdl-button.mdl-js-button.mdl-js-ripple-effect
      {:href "#"}
      "Read More"]]]
   [:div.demo-separator.mdl-cell--1-col]
   [:div.demo-options.mdl-card.mdl-color--deep-purple-500.mdl-shadow--2dp.mdl-cell.mdl-cell--4-col.mdl-cell--3-col-tablet.mdl-cell--12-col-desktop
    [:div.mdl-card__supporting-text.mdl-color-text--blue-grey-50
     [:h3 "View options"]
     [:ul
      (doall (for [opt (get-in @app-state [:options])]
               ^{:key opt}
               [:li
                [:label.mdl-checkbox.mdl-js-checkbox.mdl-js-ripple-effect
                 {:for opt}
                 [:input.mdl-checkbox__input {:id opt :type "checkbox"}]
                 [:span.mdl-checkbox__label opt]]]))]]
    [:div.mdl-card__actions.mdl-card--border
     [:a.mdl-button.mdl-js-button.mdl-js-ripple-effect.mdl-color-text--blue-grey-50
      {:href "#"}
      "Change location"]
     [:div.mdl-layout-spacer]
     [:i.material-icons "location_on"]]]])


(defn content []
  [:main.mdl-layout__content.mdl-color--grey-100
   [:div.mdl-grid.demo-content

    [pie-charts]
    [line-charts]
    [cards]

    ]])

(defn main []
  [mdl
   [:div.demo-layout.mdl-layout.mdl-js-layout.mdl-layout--fixed-drawer.mdl-layout--fixed-header
    [header]
    [drawer]
    [content]]])

(defn render-main []
  (r/render-component [main]
                      (.getElementById js/document "app")))

(defn init []
  (render-main))

(defn reload []
  (render-main))