(ns cljs-rte.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx dispatch subscribe]]
            [day8.re-frame.http-fx]
            [cemerick.url :as url]
            [ajax.core :as ajax]))

(reg-event-db
 :initialise-db
 (fn [_ _]
   {}))

(reg-event-db
 :cljs-rte-bad-user-response
 (fn [db [_ data response]]
   (assoc-in db [:global :enable-error] (select-keys data [:status :uri :last-method]))))

(reg-event-fx
 :cljs-rte-fetch-ajax
 (fn [_ [_ uri success-event]]
   {:http-xhrio {:method          :get
                 :uri             uri
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [success-event]
                 :on-failure      [:cljs-rte-bad-user-response]}}))

