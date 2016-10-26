var Application =require('./lib/framework/Application.js');
/* CSS files */

require("!style!css!bootstrap-select/dist/css/bootstrap-select.min.css");
require("!style!css!datatables.net-bs/css/dataTables.bootstrap.css");
require("!style!css!datatables.net-buttons-bs/css/buttons.bootstrap.css");

/* JS files */
require('AllSolutionModules');
require('./vendor/cometd/jquery.cometd.js');
require('./lib/framework/root.view.js');
require('./lib/framework/menu.js');
require('./lib/framework/indicator.js');
require('./lib/framework/alerts.js');
require('./lib/framework/fyi.alerts.js');
require('./lib/framework/review.alerts.js');
require('./lib/framework/grid.report.view.js');
require('./lib/framework/report.parameter.view.js');
require('./lib/framework/messenger.cometd.js');

$(function() {
    $.fn.datepicker.defaults.format = UIProperties.uiDateFormatJs;
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
