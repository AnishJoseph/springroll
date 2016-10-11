require.config({
    paths: {
        "jquery": "vendor/jquery/jquery-3.1.1.min",
        "jquery.cometd": "vendor/cometd/jquery.cometd",
        "messenger": "lib/framework/messenger.cometd",
        "Application": "lib/framework/Application",
        "marionette": "vendor/marionette/backbone.marionette.min",
        "underscore": "vendor/marionette/underscore",
        "backbone": "vendor/marionette/backbone",
        "backbone.radio": "vendor/marionette/backbone.radio",
        "m1": "lib/modules/m1",
        "m2": "lib/modules/m2",
        "menu": "lib/framework/menu",
        "root.view": "lib/framework/root.view",
    }
});
require(['Application', 'menu', 'm1', 'm2', 'messenger', 'root.view'],function(Application, m1){

    $.ajaxSetup({ cache: false });

    Application.CometD.init();

    Application.start();

});
