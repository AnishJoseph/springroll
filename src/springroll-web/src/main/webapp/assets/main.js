var Application =require('Application');
/* CSS files */

require("!style!css!bootstrap-select/dist/css/bootstrap-select.min.css");
require("!style!css!datatables.net-bs/css/dataTables.bootstrap.css");
require("!style!css!datatables.net-buttons-bs/css/buttons.bootstrap.css");
require("!style!css!bootstrap-datepicker/dist/css/bootstrap-datepicker3.css");

/* JS files */
require( 'datatables.net' );
require( 'datatables.net-buttons' );
require('bootstrap-select');
require('bootstrap-datepicker');



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
require('./lib/framework/modal.js');

$(function() {
    $.ajaxSetup({
        cache: false,
        statusCode : {
            406:function(message){   //NOT_ACCEPTABLE
                Application.Indicator.showErrorMessage({message:message.responseText});
            },
            400:function(message){   //BAD_REQUEST BUSINESS VIOLATIONS
                _.each(message.responseJSON, function(violation){
                    /* Add the localized version of the field name at position 0 or the args (if the field exists) */
                    if(violation.field != undefined && violation.field != null) violation.args.unshift(Localize(violation.field));
                    Application.Indicator.showErrorMessage({message: Localize(violation.messageKey, violation.args)});
                });
            },
        }
    });

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
