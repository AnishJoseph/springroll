var Marionette = require('backbone.marionette');
var Application =require('Application');

var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-messageKey%>'),
});

Application.subscribeForAlert('/core/fyi', {
    view: AlertsView,
    showDismiss: true,
    showInfo: false,
    showAccept: false,
    showReject: false,
});
