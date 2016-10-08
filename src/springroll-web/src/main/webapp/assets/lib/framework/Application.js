define(['marionette', 'backbone'], function (Marionette, Backbone) {
    console.log("TOP OF APPLICATION.JS  --------")
    var listeners = {};

    var SpringrollApplication = Marionette.Application.extend({
        region: '#root-element',

        onStart: function() {
            console.log("APP STARTED!!!!")
        }
    });

    var springrollApplication = new SpringrollApplication();
    return {
        start : function () {
            springrollApplication.start();
        },
        subscribe: function(service, callback){
            if(listeners[service] == undefined){
                listeners[service] = [callback];
                return;
            }
            listeners[service].push(callback);
        },
        getListeners : function(){
            return listeners;
        }
    }
});