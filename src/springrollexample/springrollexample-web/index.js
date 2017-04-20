import {includes}  from 'lodash';
require('Springrollcore');
import Application from 'App';



$(function() {
    Application.setup();
    $.when.apply($, Application.getPromises()).then(function () {
        var user = Application.getUser();
        var modules = user.authorizations;

        /* Based on this users authorization start requiring the modules */
        if (includes(modules, "TransactionTests")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("TransactionTests");
                deferred.resolve();
            });
        }
        if (includes(modules, "SubMenuSimple")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("SubMenuSimple");
                deferred.resolve();
            });
        }
        if (includes(modules, "MDM")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("mdm/MDMContainer.jsx");
                deferred.resolve();
            });
        }
        if (includes(modules, "SubmenuCommon")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("SubmenuCommon");
                deferred.resolve();
            });
        }
        if (includes(modules, "GridTests")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("GridTests");
                deferred.resolve();
            });
        }
        if (includes(modules, "JobDashboard")) {
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
