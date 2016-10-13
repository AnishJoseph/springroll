define(['marionette', 'backbone'], function (Marionette, Backbone) {
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
            url: '/api/sr/templates',
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
        return deferred.promise();
    }
    return Application;
});