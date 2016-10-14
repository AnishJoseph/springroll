define(['Application', 'marionette', 'backbone', 'alerts'], function (Application) {

    Application.Alerts.subscribe('/core/fyi', {
        itemView: '',
        showDismiss: true,
        showInfo: true,
        showAccept: true,
        showReject: true,
    });
});