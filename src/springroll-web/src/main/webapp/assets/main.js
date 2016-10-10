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
        "menu": "lib/modules/menu",
    }
});
require(['Application', 'menu', 'm1', 'm2', 'messenger'],function(Application, m1){

    $.ajaxSetup({ cache: false });
    Application.M1.f1();
    Application.M2.f1();

    Application.CometD.init();

    Application.start();

});
