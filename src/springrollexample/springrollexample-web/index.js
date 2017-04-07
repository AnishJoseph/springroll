require('Springrollcore');
import Application from 'App';



$(function() {
    Application.setup();
    $.when.apply($, Application.getPromises()).then(function () {
        var modules = ["TransactionTests", "SubMenuSimple", "MDM", "SubmenuCommon", "GridTests", "JobDashboard"];

        /* Based on this users authorization start requiring the modules */
        if (_.contains(modules, "TransactionTests")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("TransactionTests");
                deferred.resolve();
            });
        }
        if (_.contains(modules, "SubMenuSimple")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("SubMenuSimple");
                deferred.resolve();
            });
        }
        if (_.contains(modules, "MDM")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("mdm/MDMContainer.jsx");
                deferred.resolve();
            });
        }
        if (_.contains(modules, "SubmenuCommon")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("SubmenuCommon");
                deferred.resolve();
            });
        }
        if (_.contains(modules, "GridTests")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("GridTests");
                deferred.resolve();
            });
        }
        if (_.contains(modules, "JobDashboard")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("JobDashboard.jsx");
                deferred.resolve();
            });
        }

        $.when.apply($, Application.getPromises()).then(function () {
            Application.start();
        });
    });
});
