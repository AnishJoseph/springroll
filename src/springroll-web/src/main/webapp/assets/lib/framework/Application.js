define(['marionette', 'backbone'], function (Marionette, Backbone) {
    var subscribers = {};
    var menuItems = [];

    var SpringrollApplication = Marionette.Application.extend({
        region: '#root-element',

        onStart: function() {
            this.showView(new Application.MenuView());
        }
    });

    Marionette.TemplateCache.prototype.loadTemplate = function(templateId, options){
        console.log("Template ID is " + templateId)
        return "console";
    };

    var Application = new SpringrollApplication();

    Application.subscribe = function(service, callback){
        if(subscribers[service] == undefined){
            subscribers[service] = [callback];
            return;
        }
        subscribers[service].push(callback);
    };

    Application.getSubscribers = function(){
        return subscribers;
    };

    Application.getMenuItems = function(){
        return menuItems;
    };

    Application.addMenuItem = function(menuItem){
        menuItems.push(menuItem);
    };

    return Application;
});