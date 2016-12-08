var Marionette = require('backbone.marionette');
var cachedTemplates;
var SpringrollApplication = Marionette.Application.extend({
    region: '#root-element',

    onStart: function() {
        Application.rootView = new Application.RootView();
        this.showView(Application.rootView);
        Backbone.history.start();
    }
});

var Application = new SpringrollApplication();

Marionette.TemplateCache.prototype.loadTemplate = function(templateId, options){
    if(cachedTemplates == undefined || cachedTemplates[templateId] == undefined){
        console.error("Unable to find template with ID " + templateId);
        return '<h1>Unable to find template with ID ' + templateId;
    }
    return cachedTemplates[templateId];
};

/* Now comes all the application utility functions that are available to the modules */
var subscribers = {};   //Holds the subscribers to the push notifications - filled in by modules calling subscribe
var menuItems = [];     //Holds the list of menuItems - filled by modules calling addMenuItem
var requiredTemplates = [];     //Holds the list of templates required by the modules - filled by modules calling requiresTemplate

Application.subscribe = function(service, callback){
    if(subscribers[service] == undefined){
        subscribers[service] = [callback];
        return;
    }
    subscribers[service].push(callback);
};

Application.requiresTemplate = function(templateName){
    requiredTemplates.push(templateName);
}

Application.getSubscribers = function(){
    return subscribers;
};

Application.getMenuItems = function(){
    return menuItems;
};

Application.addMenuItem = function(menuItem){
    if(menuItem.parent == undefined) {
        menuItems.push({'index': menuItem.index, 'items': menuItem});
        return;
    }
    var existing = _.find(menuItems, function(item){ return item.index == menuItem.index});
    if(existing == undefined){
        menuItems.push({'index': menuItem.index, 'items': [menuItem]});
        return;
    }
    existing.items.push(menuItem);
};

Application.loadTemplates = function() {
    var data = JSON.stringify(requiredTemplates);

    var deferred = $.Deferred();

    $.ajax({
        url: 'api/sr/templates',
        type: 'POST',
        data: data,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function (templateData) {
            console.log("Templates loaded.");
            cachedTemplates = templateData;
            deferred.resolve();
        },
        error : function (jqXHR, textStatus, errorThrown ){
            console.error("Unable to load templates - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            deferred.resolve();
        }
    });
    Application.addPromise(deferred.promise());
}
Application.loadUser = function() {
    var deferred = $.Deferred();
    $.ajax({
        url: 'api/sr/user',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function (user) {
            Application.user = user;
            deferred.resolve();
        },
        error : function (jqXHR, textStatus, errorThrown ){
            console.error("Unable to load User - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            deferred.resolve();
        }
    });
    Application.addPromise(deferred.promise());
}
Application.loadProperties = function() {
    var deferred = $.Deferred();
    $.ajax({
        url: 'api//sr/properties',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function (props) {
            properties = props;
            deferred.resolve();
        },
        error : function (jqXHR, textStatus, errorThrown ){
            console.error("Unable to load Properties - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            deferred.resolve();
        }
    });
    Application.addPromise(deferred.promise());
}

Application.getProperty = function(propertyName){
    return properties[propertyName];
}

Application.getMomentFormatForDate = function(){
    return Application.getProperty('ui.moment.date.format.js');
}
Application.getMomentFormatForDateTime = function(){
    return Application.getProperty('ui.moment.datetime.format.js');
}
Application.loadLocaleMessages = function() {
    var deferred = $.Deferred();
    $.ajax({
        url: 'api/sr/localeMessages',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function (messages) {
            localeMessages = messages;
            deferred.resolve();
        },
        error : function (jqXHR, textStatus, errorThrown ){
            console.error("Unable to load Locale Messages - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            deferred.resolve();
        }
    });
    Application.addPromise(deferred.promise());
}

var subscribersForAlerts = [];
Application.subscribeForAlert = function (channel, options){
    subscribersForAlerts.push({channel : channel, options : options});
}

Application.getSubscribersForAlerts = function (){
    return subscribersForAlerts;
}

var localeMessages = {};
var properties = {};

/* Call this with arguments to replace {n} parameters */
var getLocaleMessage = function(messageKey){
    return (localeMessages[messageKey] || messageKey).replace(/\{(\d+)\}/g, (function(args){
        return function (pattern, index){
            return args[parseInt(index)+1] || pattern;
        }
    })(arguments));
};

/*
    Case 1 : Localize(messageKey) -  with no arguments
    Case 2 : Localize(messageKey, arg1, arg2.. argn)  - variable number of arguments
    Case 3 : Localize(messageKey, [arg1, arg2.. argn]) - arguments as an array
 */
Application.Localize = function(messageKey, args){
    if(args == undefined)return getLocaleMessage(messageKey);   //Case 1
    if(args instanceof Array ) {    //Case 3
        var argsToUse = args.slice(); //Make a copy of the array
        argsToUse.unshift(messageKey);
        return getLocaleMessage.apply(this, argsToUse);
    }
    return getLocaleMessage.apply(this, arguments); //Case 2
};

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
Application.showModal = function(title, viewToShow, viewOfCaller, detachViewOnHide, hinderClose){
    Application.rootView.triggerMethod("show:modal", title, viewToShow, viewOfCaller, detachViewOnHide || false, hinderClose || false);
} ;
Application.hideModal = function(){
    Application.rootView.triggerMethod("hide:modal");
} ;

var promises = [];

Application.addPromise = function(promise){
    promises.push(promise);
};
Application.getPromises = function(){
    return promises;
};

window.Localize = Application.Localize;
module.exports = Application;