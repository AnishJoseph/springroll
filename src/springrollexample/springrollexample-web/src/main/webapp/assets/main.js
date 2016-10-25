require.config({
});
require(['springroll.js'],function(){

    var solutionModules = [
        //'lib/modules/transactionTests',
        //'lib/modules/m2',
        'lib/modules/m3s1',
        'lib/modules/m3s2'
    ];
    require(solutionModules, function() {
        var promises = [];
        promises.push(Application.loadTemplates());
        promises.push(Application.loadUser());
        promises.push(Application.loadLocaleMessages());

        $.when.apply($, promises).always(function () {
            //Application.Alerts.registerAlertSubscriptions();
            //Application.CometD.init();
            Application.start();
        });
    });
});
