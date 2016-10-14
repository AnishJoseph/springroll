define(['Application', 'marionette', 'backbone', 'alerts'], function (Application, Marionette) {

    var AlertsView = Marionette.View.extend({
        tagName: 'div',
        template: _.template('<%-messageKey%>'),
    });

    Application.Alerts.subscribe('/core/fyi', {
        view: AlertsView,
        showDismiss: true,
        showInfo: false,
        showAccept: false,
        showReject: false,
    });

});
