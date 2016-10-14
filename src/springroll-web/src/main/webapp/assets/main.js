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
        "messenger" : { "deps" :['jquery','jquery.cometd'] }
    },
    paths: {
        "jquery": "vendor/jquery/jquery-3.1.1.min",
        //"jquery": "vendor/marionette/jquery",
        "jquery.cometd": "vendor/cometd/jquery.cometd",
        "messenger": "lib/framework/messenger.cometd",
        "Application": "lib/framework/Application",
        "alerts": "lib/framework/alerts",
        "fyi.alerts": "lib/framework/fyi.alerts",
        "review.alerts": "lib/framework/review.alerts",
        "marionette": "vendor/marionette/backbone.marionette.min",
        "underscore": "vendor/marionette/underscore",
        "backbone": "vendor/marionette/backbone",
        //"moment": "vendor/moment/moment-with-locales",
        "backbone.radio": "vendor/marionette/backbone.radio",
        "transactionTests": "lib/modules/transactionTests",
        "m2": "lib/modules/m2",
        "m3s1": "lib/modules/m3s1",
        "m3s2": "lib/modules/m3s2",
        "menu": "lib/framework/menu",
        "root.view": "lib/framework/root.view",
        "bootstrap": "vendor/bootstrap/js/bootstrap.min",
    }
});
require(['Application', 'menu', 'transactionTests', 'm2', 'm3s1', 'm3s2', 'messenger', 'root.view', 'bootstrap', 'fyi.alerts', 'review.alerts'],function(Application){

    $.ajaxSetup({ cache: false });


    var promises = [];
    promises.push(Application.loadTemplates());
    $.when.apply($, promises).always(function () {
        Application.CometD.init();
        Application.start();
    });

});
