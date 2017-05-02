import {includes}  from 'lodash';
import Application from 'App';



document.addEventListener("DOMContentLoaded", function(event) {
    Application.setup();
    Promise.all(Application.getPromises()).then(function () {
        /* Once the app is setup - get the core JS and CSS files */
        require("Springrollcore");
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
            Application.addPromise(new Promise((resolve, reject) => {
                require.ensure([], function (require) {
                    require("JobDashboard");
                    resolve();
                });
            }));
        }

        /*  Wait for all promises to resolve. At that point all required JS files would have been loaded -
            go ahead and start the application
        */
        Promise.all(Application.getPromises()).then(() => Application.start());
    });
});
