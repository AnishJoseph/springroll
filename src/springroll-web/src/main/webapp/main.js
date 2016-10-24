//import './styles/application.css';
var Application =require('./assets/lib/framework/Application.js');
require('./assets/lib/framework/root.view.js');
require('./assets/lib/framework/menu.js');
require('./assets/lib/framework/indicator.js');
require('./assets/lib/framework/alerts.js');
require('./assets/lib/framework/fyi.alerts.js');
require('./assets/lib/framework/review.alerts.js');
//require('./assets/lib/framework/messenger.cometd.js');

document.addEventListener('DOMContentLoaded', () => {
    var promises = [];
    promises.push(Application.loadTemplates());
    promises.push(Application.loadUser());
    promises.push(Application.loadLocaleMessages());

    $.when.apply($, promises).always(function () {
        Application.Alerts.registerAlertSubscriptions();
        //Application.CometD.init();
        Application.start();
    });
});
