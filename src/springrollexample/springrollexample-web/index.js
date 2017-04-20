import {includes}  from 'lodash';
require('Springrollcore');
import Application from 'App';



$(function() {
    Application.setup();
    Promise.all(Application.getPromises()).then(function () {
        Application.clearPromises();
        var user = Application.getUser();
        var modules = user.authorizations;

        /* Based on this users authorization start requiring the modules */
        if (includes(modules, "TransactionTests")) {
            Application.addPromise(new Promise((resolve, reject) => {
                require.ensure([], function (require) {
                    require("TransactionTests");
                    resolve();
                });
            }));
        }
        if (includes(modules, "SubMenuSimple")) {
            Application.addPromise(new Promise((resolve, reject) => {
                require.ensure([], function (require) {
                    require("SubMenuSimple");
                    resolve();
                });
            }));
        }
        if (includes(modules, "MDM")) {
            Application.addPromise(new Promise((resolve, reject) => {
                require.ensure([], function (require) {
                    require("mdm/MDMContainer.jsx");
                    resolve();
                });
            }));
        }
        if (includes(modules, "SubmenuCommon")) {
            Application.addPromise(new Promise((resolve, reject) => {
                require.ensure([], function (require) {
                    require("SubmenuCommon");
                    resolve();
                });
            }));
        }
        if (includes(modules, "GridTests")) {
            Application.addPromise(new Promise((resolve, reject) => {
                require.ensure([], function (require) {
                    require("GridTests");
                    resolve();
                });
            }));
        }
        if (includes(modules, "SubmenuCommon")) {
            Application.addPromise(new Promise((resolve, reject) => {
                require.ensure([], function (require) {
                    require("SubmenuCommon");
                    resolve();
                });
            }));
        }

        if (includes(modules, "JobDashboard")) {
            let deferred = $.Deferred();
            Application.addPromise(deferred.promise());
            require.ensure([], function (require) {
                require("JobDashboard");
                deferred.resolve();
            });
        }
        /*  Wait for all promises to resolve. At that point all required JS files would have been loaded -
            go ahead and start the application
        */
        Promise.all(Application.getPromises()).then(() => Application.start());
    });
});
