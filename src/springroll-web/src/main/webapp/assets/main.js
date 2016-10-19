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
        "bootstrap.select" : { "deps" :['jquery'] },
        "bootstrap.datetimepicker" : { "deps" :['jquery', 'moment', 'bootstrap'] },
        // Put all the vendor javascript files here
        "Application" : {"deps": ['bootstrap', 'jquery', 'jquery.cometd', 'marionette', 'underscore',
            'backbone', 'backbone.radio', 'moment', 'bootstrap.select', 'bootstrap.datetimepicker'
        ]}
    },
    paths: {
        "jquery": "vendor/jquery/jquery-3.1.1.min",
        "jquery.cometd": "vendor/cometd/jquery.cometd",
        "marionette": "vendor/marionette/backbone.marionette.min",
        "underscore": "vendor/marionette/underscore",
        "backbone": "vendor/marionette/backbone",
        //"moment": "vendor/moment/moment-with-locales",
        "backbone.radio": "vendor/marionette/backbone.radio",
        "bootstrap": "vendor/bootstrap/js/bootstrap.min",
        "bootstrap.select": "vendor/bootstrap-plugins/js/bootstrap-select.min",
        "bootstrap.datetimepicker": "vendor/bootstrap-plugins/js/bootstrap-datetimepicker.min",

        "Application": "lib/framework/Application",
    }
});
require(['Application'],function(Application){

    var frameworkModules = [
        'lib/framework/menu',
        'lib/framework/fyi.alerts',
        'lib/framework/review.alerts',
        'lib/framework/indicator',
        'lib/framework/root.view',
        'lib/framework/messenger.cometd',
        'lib/framework/alerts',
        'lib/framework/report.view',
    ];

    var solutionModules = [
        'lib/modules/transactionTests',
        'lib/modules/m2',
        'lib/modules/m3s1',
        'lib/modules/m3s2',
    ];
    /*
       Step 1 - Load all the framework modules
       Step 2 - Load the solution modules
       Step 3 - Load data from the server - templates, user, i18n bundles, masters etc
       Step 4 - Start up cometd and and then start the application
    */
    require(frameworkModules, function() {
        require(solutionModules, function() {

            $.ajaxSetup({ cache: false });

            var promises = [];
            promises.push(Application.loadTemplates());
            promises.push(Application.loadUser());
            promises.push(Application.loadLocaleMessages());

            $.when.apply($, promises).always(function () {
                Application.Alerts.registerAlertSubscriptions();
                Application.CometD.init();
                Application.start();
            });
        });
    });
});
