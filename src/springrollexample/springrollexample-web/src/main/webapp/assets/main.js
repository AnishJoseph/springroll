require.config({
});
require(['springroll.js'],function(){

    var solutionModules = [
        'lib/modules/transactionTests',
        'lib/modules/m2',
        'lib/modules/m3s1',
        'lib/modules/m3s2'
    ];
    require(solutionModules, function() {
        var promises = [];
        promises.push(Application.loadTemplates());
        promises.push(Application.loadUser());
        promises.push(Application.loadLocaleMessages());
        $.ajaxSetup({
            cache: false,
            statusCode : {
                406:function(message){   //NOT_ACCEPTABLE
                    console.log("Main.js got - 406" );
                    Application.Indicator.showErrorMessage({message:message.responseText});
                },
            }
        });
        $.fn.datepicker.defaults.format = UIProperties.uiDateFormatJs;
        $.when.apply($, promises).always(function () {
            //Application.Alerts.registerAlertSubscriptions();
            //Application.CometD.init();
            Application.start();
        });
    });
});
