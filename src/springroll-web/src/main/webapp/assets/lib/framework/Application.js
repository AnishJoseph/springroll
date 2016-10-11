define(['marionette', 'backbone'], function (Marionette, Backbone) {

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
        console.log("Template ID is " + templateId);
        if(templateId.includes("m2"))return "<H1>M2</H1>";

        return "console";
    };

    /* Now comes all the application utility functions that are available to the modules */
    var subscribers = {};   //Holds the subscribers to the push notifications - filled in by modules calling subscribe
    var menuItems = [];     //Holds the list of menuItems - filled by modules calling addMenuItem

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