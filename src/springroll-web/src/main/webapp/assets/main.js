var Application =require('Application');

/* Put CSS requires in app.main.js in the application - for some reason it does not when placed here */

/* JS files */
require( 'datatables.net' );
require( 'datatables.net-buttons' );
require('bootstrap-select');
require('moment');
require('eonasdan-bootstrap-datetimepicker');



require('AllSolutionModules');
require('jquery.cometd.js');
require('root.view.js');
require('menu.js');
require('indicator.js');
require('alerts.js');
require('fyi.alerts.js');
require('review.alerts.js');
require('mdm.review.alerts.js');
require('reviewfyi.alerts.js');
require('grid.report.view.js');
require('report.parameter.view.js');
require('messenger.cometd.js');
require('modal.js');
require('review.moreinfo.js');
require('mdm.js');
require('mdm.editor.js');
require('utils.js');

$(function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var headers = {};
    headers[header] = token;
    $.ajaxSetup({
        headers: headers,
        cache: false,
        statusCode : {
            406:function(message){   //NOT_ACCEPTABLE
                // Check if this is already handled in the business logic
                if(message.errorHandled == undefined) {
                    Application.Indicator.showErrorMessage({message:message.responseText});
                }
            },
            409:function(message){   //CONFLICT BUSINESS VIOLATIONS
                // Check if this is already handled in the business logic
                if(message.errorHandled == undefined) {
                    _.each(message.responseJSON, function (violation) {
                        Application.Indicator.showErrorMessage({message:violation.field + ': ' + violation.message});
                    });
                }
            },
            400:function(message){   //BAD_MESSAGE CONSTRAINT VIOLATIONS
                // Check if this is already handled in the business logic
                if(message.errorHandled == undefined) {
                    _.each(message.responseJSON, function (violation) {
                        _.each(Object.keys(violation), function(field) {
                            /* First localize the field name */
                            var localizedFieldName = Localize(field);
                            if (violation[field].includes('{0}')){
                                /* if the error message is a custom message which includes  the field name  i.e message string contains '{0}'
                                 then dont show the field name separately - just create the message as per the template
                                 */
                                Application.Indicator.showErrorMessage({message: Localize(violation[field], [localizedFieldName])});
                            } else {
                                Application.Indicator.showErrorMessage({message: localizedFieldName +  " : " + Localize(violation[field])});
                            }
                        });
                    });
                }
            },
        }
    });

    Application.loadTemplates();
    Application.loadUser();
    Application.loadProperties();
    Application.loadLocaleMessages();
    $.when.apply($, Application.getPromises()).then(function () {
        Application.Alerts.registerAlertSubscriptions();
        Application.CometD.init();
        Application.start();
    });
});
