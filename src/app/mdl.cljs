(ns app.mdl
  (:require [cljsjs.material]
            [reagent.core :as r]))

(def upgrade-elem (.. js/componentHandler -upgradeElement))
(def upgrade-dom (.. js/componentHandler -upgradeDom))

(defn mdl-upgrader [display-name render-func]
  (r/create-class
    {:display-name        display-name
     :component-did-mount  #(upgrade-elem (r/dom-node %))
     ;:component-did-mount (fn [] (upgrade-dom))
     :reagent-render      render-func}))

(defn div []
  (mdl-upgrader "mdl-div"
                (fn [& c] (into [:div] c))))

(defn spinner []
  (mdl-upgrader "mdl-spinner"
                (fn [] [:div.mdl-spinner.mdl-js-spinner.is-active])))

(defn card [& _]
  (mdl-upgrader "mdl-card"
                (fn [& c]
                  (into [:div.mdl-card.mdl-shadow--2dp.mdl-cell.mdl-cell--4-col] c))))

(defn button
  "Creates an MDL button. Provide a map m and some optional children.
  The map m can take an :options key, a seq with possible keys:
    :ripple :raised"
  [& _]
  (let [opt {:ripple "mdl-js-ripple-effect"
             :raised "mdl-button--raised"
             :colored "mdl-button--colored"}]
    (mdl-upgrader "mdl-button"
                  (fn [m & c]
                    (into [:button.mdl-button.mdl-js-button (merge {:id    (:id m)
                                                                    :class (apply str (interpose " " (map opt (:options m))))}
                                                                   (dissoc m :options))] c)))))