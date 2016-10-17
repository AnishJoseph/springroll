require.config({
    packages: [{
        name: 'moment',
        // This location is relative to baseUrl. Choose bower_components
        // or node_modules, depending on how moment was installed.
        location: 'vendor/moment',
        main: 'moment'
    }],

    shim : {
        "bootstrap" : { "deps" :['jquery'] },
        //"messenger" : { "deps" :['jquery','jquery.cometd'] },
        // Put all the vendor javascript files here
        "Application" : {"deps": ['bootstrap', 'jquery', 'jquery.cometd', 'marionette', 'underscore', 'backbone', 'backbone.radio']}
    },
    paths: {
        "jquery": "vendor/jquery/jquery-3.1.1.min",
        "jquery.cometd": "vendor/cometd/jquery.cometd",
        "messenger": "lib/framework/messenger.cometd",
        "marionette": "vendor/marionette/backbone.marionette.min",
        "underscore": "vendor/marionette/underscore",
        "backbone": "vendor/marionette/backbone",
        //"moment": "vendor/moment/moment-with-locales",
        "backbone.radio": "vendor/marionette/backbone.radio",
        "bootstrap": "vendor/bootstrap/js/bootstrap.min",

        "Application": "lib/framework/Application",
        "alerts": "lib/framework/alerts",
        "fyi.alerts": "lib/framework/fyi.alerts",
        "review.alerts": "lib/framework/review.alerts",

        "transactionTests": "lib/modules/transactionTests",
        "m2": "lib/modules/m2",
        "m3s1": "lib/modules/m3s1",
        "m3s2": "lib/modules/m3s2",
        "menu": "lib/framework/menu",
        "indicator": "lib/framework/indicator",
        "root.view": "lib/framework/root.view",
    }
});
require(['Application', 'menu', 'transactionTests', 'm2', 'm3s1', 'm3s2', 'messenger', 'root.view', 'fyi.alerts', 'review.alerts', 'indicator'],function(Application){

    $.ajaxSetup({ cache: false });


    var promises = [];
    promises.push(Application.loadTemplates());
    promises.push(Application.loadUser());
    promises.push(Application.loadLocaleMessages());
    $.when.apply($, promises).always(function () {
        Application.CometD.init();
        Application.start();
    });

});
