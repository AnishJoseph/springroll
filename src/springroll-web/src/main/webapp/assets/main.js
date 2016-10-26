var Application =require('./lib/framework/Application.js');
require('AllSolutionModules');
require('./lib/framework/root.view.js');
require('./lib/framework/menu.js');
require('./lib/framework/indicator.js');
require('./lib/framework/alerts.js');
require('./lib/framework/fyi.alerts.js');
require('./lib/framework/review.alerts.js');
require('./lib/framework/grid.report.view.js');
require('./lib/framework/report.parameter.view.js');
//require('./lib/framework/messenger.cometd.js');

document.addEventListener('DOMContentLoaded', () => { //FIXME make this jquery $.reaady??
    var promises = [];
    promises.push(Application.loadTemplates());
    promises.push(Application.loadUser());
    promises.push(Application.loadLocaleMessages());

    $.when.apply($, promises).always(function () {
        Application.Alerts.registerAlertSubscriptions();
//        Application.CometD.init();
        Application.start();
    });
});
