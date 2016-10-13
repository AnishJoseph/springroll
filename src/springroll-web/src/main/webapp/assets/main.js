require.config({
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
        "marionette": "vendor/marionette/backbone.marionette.min",
        "underscore": "vendor/marionette/underscore",
        "backbone": "vendor/marionette/backbone",
        "backbone.radio": "vendor/marionette/backbone.radio",
        "m1": "lib/modules/m1",
        "m2": "lib/modules/m2",
        "m3s1": "lib/modules/m3s1",
        "m3s2": "lib/modules/m3s2",
        "menu": "lib/framework/menu",
        "root.view": "lib/framework/root.view",
        "bootstrap": "vendor/bootstrap/js/bootstrap.min",
    }
});
require(['Application', 'menu', 'm1', 'm2', 'm3s1', 'm3s2', 'messenger', 'root.view', 'bootstrap'],function(Application){

    $.ajaxSetup({ cache: false });


    var promises = [];
    promises.push(Application.loadTemplates());
    $.when.apply($, promises).always(function () {
        Application.CometD.init();
        Application.start();
    });

});
