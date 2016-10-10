define(['marionette', 'backbone'], function (Marionette, Backbone) {
    console.log("TOP OF APPLICATION.JS  --------")
    var subscribers = {};
    var menuItems = [];

    var SpringrollApplication = Marionette.Application.extend({
        region: '#root-element',

        onStart: function() {
            console.log("APP STARTED!!!!");

            this.showView(new Application.MenuView());

        }
    });
    Marionette.TemplateCache.prototype.loadTemplate = function(templateId, options){
        // load your template here, returning the data needed for the compileTemplate
        // function. For example, you have a function that creates templates based on the
        // value of templateId
        console.log("Template ID is " + templateId)
        return "console";
    };
    var springrollApplication = new SpringrollApplication();
    var Application =  {
        start : function () {
            springrollApplication.start();
        },
        subscribe: function(service, callback){
            if(subscribers[service] == undefined){
                subscribers[service] = [callback];
                return;
            }
            subscribers[service].push(callback);
        },
        getSubscribers : function(){
            return subscribers;
        },
        getMenuItems : function(){
            return menuItems;
        },
        addMenuItem : function(menuItem){
            menuItems.push(menuItem);
        }
    }
    return Application;
});