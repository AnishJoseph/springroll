import React from 'react';
import ReactDOM from 'react-dom';
import CometD from 'messenger.cometd.js';
import { Router, Route, hashHistory,IndexRoute } from 'react-router'
import { Provider } from 'react-redux';
import { createStore, applyMiddleware, compose } from 'redux';
import springrollReducers from 'SpringrollReducers.jsx';
import Root from 'Root.jsx';
import { setUser, addAlerts, deleteAlert, AlertActions, setAlertFilter, AlertFilters} from 'SpringrollActionTypes';
import thunkMiddleware from 'redux-thunk'
import {each, find, filter, reject}  from 'lodash';
var moment = require('moment');
import axios from 'axios';




class Application {

    constructor() {
        this.pushTopicSubscribers = {};   //Holds the subscribers to the push notifications - filled in by modules calling subscribe
        this.subscribers = {};   //Holds the subscribers to the push notifications - filled in by modules calling subscribe
        this.menuDefns = [];     //Holds the list of menu definitions - filled by modules calling addMenuItem
        this.subscribersToAlerts = {};
        this.localeMessages = {};
        this.properties = {};
        this.promises = [];
        this.user = undefined;
        this.index = 0;
    }
    getUser () {
        return this.user;
    }
    dispatchActionsOnReceiptOfPushTopic(receivedPushData, eventCreators){
        var that = this;
        console.log("Received data on channel " + receivedPushData.channel);
        each(eventCreators, eventCreator =>
            that.store.dispatch(eventCreator(receivedPushData)));
    }
    notificationCancelled(cancelMessage) {
        let notificationCancellationMessage = cancelMessage.data[0];
        if (notificationCancellationMessage.alertType == 'ACTION') {
            return deleteAlert(notificationCancellationMessage.id, AlertFilters.ALERT_FILTER_ACTION);
        } else if (notificationCancellationMessage.alertType == 'ERROR') {
            return deleteAlert(notificationCancellationMessage.id, AlertFilters.ALERT_FILTER_ERROR);
        } else if (notificationCancellationMessage.alertType == 'INFO') {
            return deleteAlert(notificationCancellationMessage.id, AlertFilters.ALERT_FILTER_INFO);
        }
    }

    start() {
        var that = this;
        /*  Subscribe to the notification cancellation channel. When an alert is no longer valid the server pushes a message
         on thus channel. All we need to do is to dispatch a delete alert action. Note this will cause the alert to
         vanish from the alert panel (even if the user is watching it )
         */
        this.subscribeToPushTopic('/core/notificationCancel', this.notificationCancelled);

        each(Object.keys(that.getSubscribersToAlerts()), function (channel) {
            that.subscribe(channel, (response) => {
                if (response.data[0].alertType == 'ACTION') {
                    that.store.dispatch(addAlerts(AlertActions.ADD_ACTION_ALERTS, response.data));
                    that.store.dispatch(setAlertFilter(AlertFilters.ALERT_FILTER_ACTION));
                } else if (response.data[0].alertType == 'ERROR') {
                    that.store.dispatch(addAlerts(AlertActions.ADD_ERROR_ALERTS, response.data));
                } else if (response.data[0].alertType == 'INFO') {
                    that.store.dispatch(addAlerts(AlertActions.ADD_INFO_ALERTS, response.data));
                }
            });
        });
        each(Object.keys(this.getSubscribersToPushTopics()), function (channel) {
            let eventCreators = that.getSubscribersToPushTopics()[channel];
            that.subscribe(channel, function(response){
                that.dispatchActionsOnReceiptOfPushTopic(response, eventCreators);
            });
        });

        ReactDOM.render(
            <Provider store={that.store}>
                <Router history={hashHistory}>
                    <Route path="/" component={Root}>
                        <IndexRoute component={that.getMenuItems()[0].component}/>
                        {that.getMenuItems().map((menuDefn, index) => {
                            if (typeof menuDefn.route === 'string')
                                return (<Route key={index} component={menuDefn.component}
                                               path={"/" + menuDefn.route}/>);
                            return menuDefn.route;
                        })}
                    </Route>
                </Router>
            </Provider>,
            document.getElementById('app')
        );
        CometD.init();
    }

    subscribe(service, callback){
        if(this.subscribers[service] == undefined){
            this.subscribers[service] = [callback];
            return;
        }
        this.subscribers[service].push(callback);
    }

    getSubscribers(){
        return this.subscribers;
    }
    subscribeToPushTopic(service, eventCreator){
        if(this.pushTopicSubscribers[service] == undefined){
            this.pushTopicSubscribers[service] = [];
        }
        this.pushTopicSubscribers[service].push(eventCreator);
    }

    getSubscribersToPushTopics(){
        return this.pushTopicSubscribers;
    }

    addMenu(menuDefn){
        console.log("Adding menu item - '" + menuDefn.title + "' on Route '" + menuDefn.route + "' with index " + menuDefn.index);
        this.menuDefns.push(menuDefn);
        if(menuDefn.parentTitle == undefined)
            return;
        var foundParent = find(this.menuDefns, function(storedMenuDefn){
            return menuDefn.parentTitle == storedMenuDefn.title;
        });
        if(foundParent)return;
        this.menuDefns.push({
            title: menuDefn.parentTitle,
            index: menuDefn.parentIndex,
            type: "submenu"
        });
    }

    makeSubMenus(menus, allMenus){
        var that = this;
        each(menus, function(menuDefn) {
            if(menuDefn.type == 'menuitem')return;
            var subMenuItems = filter(allMenus, (aMenu) => (aMenu.parentTitle == menuDefn.title)).sort((a,b) => (a.index - b.index));
            menuDefn['subMenuItems'] = subMenuItems;
            if(subMenuItems.length > 0) that.makeSubMenus(subMenuItems, allMenus);
        });
    }
    getMenuDefns(){
        var rootMenus = filter(this.menuDefns, (menuDefn) => (menuDefn.parentTitle == undefined)).sort((a,b) => (a.index - b.index));
        this.makeSubMenus(rootMenus, this.menuDefns);
        /* filter out any menu of type submenu which have no menu items */
        var validMenus =  reject(rootMenus, (menuDefn) => (menuDefn.type == 'submenu' && menuDefn.subMenuItems.length == 0));
        return validMenus;
    }
    getMenuItems (){
        var menuItems = filter(this.menuDefns, (menuDefn) => (menuDefn.type == 'menuitem'));
        return menuItems;
    }

    loadUser() {
        var that = this;
        let promise =  axios.get('api/sr/user')
            .then(function (response) {
                that.store.dispatch(setUser(response.data));
                that.user = response.data;
            })
            .catch(function (error) {
                console.error("Unable to load User - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            });
        this.addPromise(promise);
    }

    loadProperties() {
        var that = this;
        let promise =  axios.get('api/sr/properties')
            .then(function (response) {
                that.properties = response.data;
            })
            .catch(function (error) {
                console.error("Unable to load Properties - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            });
        this.addPromise(promise);
    }
    
    loadLocaleMessages() {
        var that = this;

        let promise =  axios.get('api/sr/localeMessages')
            .then(function (response) {
                that.localeMessages = response.data;
            })
            .catch(function (error) {
                console.error("Unable to load Locale Messages - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            });
        this.addPromise(promise);
    }

    getProperty(propertyName){
        return this.properties[propertyName];
    }

    getMomentFormatForDate(){
        return this.getProperty('ui.moment.date.format.js');
    }

    getMomentFormatForDateTime(){
        return this.getProperty('ui.moment.datetime.format.js');
    }

    showInfoNotification (message) {
        this.notificationSystem.addNotification({
            message: message,
            level: 'success',
            position : 'tc',
            autoDismiss : 5
        });

    }
    showErrorNotification (message, children, title) {
        this.notificationSystem.addNotification({
            title: title,
            message: message,
            level: 'error',
            position : 'tc',
            autoDismiss : 0,
            children : children
        });
    }

    setNotificationSystem (notificationSystem){
        this.notificationSystem = notificationSystem;
    }

    /* Call this with arguments to replace {n} parameters */
    getLocaleMessage(messageKey){
        return (this.localeMessages[messageKey] || messageKey).replace(/\{(\d+)\}/g, (function(args){
            return function (pattern, index){
                return args[parseInt(index)+1] || pattern;
            }
        })(arguments));
    }

    /*
     Case 1 : Localize(messageKey) -  with no arguments
     Case 2 : Localize(messageKey, arg1, arg2.. argn)  - variable number of arguments
     Case 3 : Localize(messageKey, [arg1, arg2.. argn]) - arguments as an array
     */
    Localize(messageKey, args){
        if(args == undefined)return this.getLocaleMessage(messageKey);   //Case 1
        if(args instanceof Array ) {    //Case 3
            var argsToUse = args.slice(); //Make a copy of the array
            argsToUse.unshift(messageKey);
            return this.getLocaleMessage.apply(this, argsToUse);
        }
        return this.getLocaleMessage.apply(this, arguments); //Case 2
    }

    /**
     *
     * @param title - title to show in the Modal window
     * @param viewToShow - the view to show in the Modal window
     * @param viewOfCaller - view of the caller - is used to call methods in that view when the dismiss, accept or reject buttons are clicked (NOTE: these buttons are ONLY
     *                       shown if the caller view has methods like dismissClicked etc
     * @param detachViewOnHide - normally the view (viewToShow) will not be detached when the modal window in hidden. As a desiarable side effect when another modal window
     *                           is opened the original modal view is destroyed and also destroys any child views (in this case the viewToShow). However if you dont
     *                           want the viewToShow to be destroyed then set this flag to true. IF YOU DO THIS YOU MUST TAKE CARE OF DESTROYING THIS VIEW YOURSELF
     * @param hinderClose - prevents the Modal window from closing on clicking outside the modal or the ESC key
     */
    showModal(title, viewToShow, viewOfCaller, detachViewOnHide, hinderClose){
        this.rootView.showModal(title, viewToShow, viewOfCaller, detachViewOnHide || false, hinderClose || false);
    }

    hideModal(){
        this.rootView.hideModal();
    } ;


    addPromise(promise){
        this.promises.push(promise);
    }

    getPromises(){
        return this.promises;
    }

    clearPromises() {
        this.promises = [];
    }

    addSubscriberToAlerts(channel, component){
        this.subscribersToAlerts[channel] = component;
    }
    getSubscribersToAlerts(){
        return this.subscribersToAlerts;
    }

    setup(){
        let metas = document.querySelectorAll("meta");
        let metaValues = {};
        each(metas,  meta => {
            let name = meta.getAttribute("name");
            let value = meta.getAttribute("content");
            metaValues[name] = value;
        });
        let token = metaValues._csrf;
        let header = metaValues._csrf_header;
        var headers = {};
        headers[header] = token;
        var that = this;
        // axios.defaults.baseURL = 'api/';
        axios.defaults.headers.common
        axios.defaults.headers.common[header] = token;

        axios.interceptors.response.use(function (response) {
            return response;
        }, function (error) {
            if(error.response == null){
                /* Could not contact the server */
                that.showErrorNotification(that.Localize("ui.server.notreachable"));
                return Promise.reject(error);
            }
            if(error.response.status == 403){
                /* We don't have an authenticated session */
            }
            if(error.response.status == 406){
                /* This handles RuntimeException and SpringrollException - both of which return HTTP status 406  i.e. NOT_ACCEPTABLE */
                that.showErrorNotification(error.response.data, error.config.method.toUpperCase() + ":" + error.config.url, that.Localize('ui.406Error'));
                return Promise.reject(error);
            }
            return Promise.reject(error);
        });

        axios.interceptors.request.use(function (config) {
            if(config.method === 'get'){
                let params = config.params || {};
                params['__'] = that.index++;
                config.params = params;
            }
            return config;
        });
        
        /* Need to handle the following HTTP status
         406:function(message){   //NOT_ACCEPTABLE
         409:function(message){   //CONFLICT BUSINESS VIOLATIONS
         400:function(message){   //BAD_MESSAGE / BAD_REQUEST CONSTRAINT VIOLATIONS
         Also do a catch all 
         */

        const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
        this.store = createStore(springrollReducers, {}, composeEnhancers(
            applyMiddleware(thunkMiddleware)
        ));
        this.loadUser();
        this.loadProperties();
        this.loadLocaleMessages();
    }

}

export default new Application();


