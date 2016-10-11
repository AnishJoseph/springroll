define(['marionette', 'backbone'], function (Marionette, Backbone) {
    var subscribers = {};
    var menuItems = [];

    var SpringrollApplication = Marionette.Application.extend({
        region: '#root-element',

        onStart: function() {
            Backbone.history.start();
            Application.rootView = new Application.RootView();
            this.showView(Application.rootView);
        }
    });

    Marionette.TemplateCache.prototype.loadTemplate = function(templateId, options){
        console.log("Template ID is " + templateId);
        if(templateId.includes("m2"))return "<H1>M2</H1>";

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