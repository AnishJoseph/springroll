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
        if(templateId.includes("m3s1"))return "<H1>M3S1</H1>";
        if(templateId.includes("m3s2"))return "<H1>M3S2</H1>";

        return "M1";
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

    return Application;
});