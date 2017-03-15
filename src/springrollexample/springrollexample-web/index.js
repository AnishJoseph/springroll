require('Springrollcore');
import Application from 'App';



$(function() {
    Application.setup();
    $.when.apply($, Application.getPromises()).then(function () {
        var modules = ["Module1", "Module2", "Module3_1", "MDM", "Module3_3"];

        /* Based on this users authorization start requiring the modules */
        if (_.contains(modules, "Module1")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("Module1");
                deferred.resolve();
            });
        }
        if (_.contains(modules, "Module2")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("Module2");
                deferred.resolve();
            });
        }
        if (_.contains(modules, "Module3_1")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("Module3_1");
                deferred.resolve();
            });
        }
        if (_.contains(modules, "MDM")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("MDM");
                deferred.resolve();
            });
        }
        if (_.contains(modules, "Module3_3")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("Module3_3");
                deferred.resolve();
            });
        }

       Application.start();
    });
});
