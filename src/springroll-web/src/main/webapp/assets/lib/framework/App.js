import React from 'react';
import ReactDOM from 'react-dom';
import Root from 'Root';
import CometD from 'messenger.cometd.js';
import { Router, Route, hashHistory } from 'react-router'

require("main.scss");
require("bootstrap/dist/css/bootstrap.min.css");

class Application {

    constructor() {
        this.subscribers = {};   //Holds the subscribers to the push notifications - filled in by modules calling subscribe
        this.menuDefns = [];     //Holds the list of menu definitions - filled by modules calling addMenuItem
        this.subscribersForAlerts = [];
        this.subscribersToAlerts = {};
        this.localeMessages = {"anish" : "joseph"};
        this.properties = {};
        this.promises = [];
        this.user = undefined;
        this.moduleMap = {};
    }

    setModule(name, module){
        this.moduleMap[name] = module;
    }
    getModuleMap(){
        return this.moduleMap;
    }
    start() {
        /*
        this.addMenu({
            name: 'ThirdMenu',
            title: 'ui.menu.third',
            index : 2,
            type : "menuitem"
        });
        this.addMenu({
            name: 'FourthMenu',
            title: 'ui.menu.fourth',
            index : 4,
            type : "submenu"
        });
        this.addMenu({
            name: 'FifthMenu',
            title: 'ui.menu.fifth',
            index : 4,
            type : "submenu"
        });

        this.addMenu({
            parent: 'FourthMenu',
            title: 'ui.SubItem2',
            name: 'subItem2',
            index : 2,
            type : "menuitem"
        });

        this.addMenu({
            parent: 'FourthMenu',
            title: 'ui.SubItem1',
            name: 'subItem1',
            index : 1,
            type : "menuitem"
        });

        this.addMenu({
            parent: 'FourthMenu',
            name: 'FourthMenuSub',
            title: 'ui.menu.fourthsub',
            index : 2,
            type : "submenu"
        });

        this.addMenu({
            parent: 'FourthMenuSub',
            title: 'ui.SubItem2sub',
            name: 'subItem2sub',
            index : 2,
            type : "menuitem"
        });

        this.addMenu({
            parent: 'FourthMenuSub',
            title: 'ui.SubItem1sub',
            name: 'subItem1sub',
            index : 1,
            type : "menuitem"
        });
        */
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

    addMenu(menuDefn){
        console.log("Adding menu item - " + menuDefn.name);
        this.menuDefns.push(menuDefn);
    }

    makeSubMenus(menus, allMenus){
        var that = this;
        _.each(menus, function(menuDefn) {
            if(menuDefn.type == 'menuitem')return;
            var subMenuItems = _.filter(allMenus, (aMenu) => (aMenu.parent == menuDefn.name)).sort((a,b) => (a.index - b.index));
            menuDefn['subMenuItems'] = subMenuItems;
            if(subMenuItems.length > 0) that.makeSubMenus(subMenuItems, allMenus);
        });
    }
    getMenuDefns(){
        var rootMenus = _.filter(this.menuDefns, (menuDefn) => (menuDefn.parent == undefined)).sort((a,b) => (a.index - b.index));
        this.makeSubMenus(rootMenus, this.menuDefns);
        /* filter out any menu of type submenu which have no menu items */
        var menusWithSubMenus =  _.reject(rootMenus, (menuDefn) => (menuDefn.type == 'submenu' && menuDefn.subMenuItems.length == 0));
        return menusWithSubMenus;
    }

    loadUser() {
        var deferred = $.Deferred();
        var that = this;
        $.ajax({
            url: 'api/sr/user',
            type: 'GET',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (user) {
                that.user = user;
                deferred.resolve();
            },
            error : function (jqXHR, textStatus, errorThrown ){
                console.error("Unable to load User - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
                deferred.resolve();
            }
        });
        this.addPromise(deferred.promise());
    }

    loadProperties() {
        var deferred = $.Deferred();
        var that = this;
        $.ajax({
            url: 'api//sr/properties',
            type: 'GET',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (props) {
                that.properties = props;
                deferred.resolve();
            },
            error : function (jqXHR, textStatus, errorThrown ){
                console.error("Unable to load Properties - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
                deferred.resolve();
            }
        });
        this.addPromise(deferred.promise());
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

    loadLocaleMessages() {
        var deferred = $.Deferred();
        var that = this;
        $.ajax({
            url: 'api/sr/localeMessages',
            type: 'GET',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (messages) {
                that.localeMessages = messages;
                deferred.resolve();
            },
            error : function (jqXHR, textStatus, errorThrown ){
                console.error("Unable to load Locale Messages - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
                deferred.resolve();
            }
        });
        this.addPromise(deferred.promise());
    }

    subscribeForAlert(channel, options){
        this.subscribersForAlerts.push({channel : channel, options : options});
    }

    getSubscribersForAlerts(){
        return this.subscribersForAlerts;
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

    getUser() {
        return this.user;
    }
    addSubscriberToAlerts(channel, component){
        this.subscribersToAlerts[channel] = component;
    }
    getSubscribersToAlerts(){
        return this.subscribersToAlerts;
    }

}

export default new Application();


