define(['Application', 'marionette', 'backbone', 'alerts'], function (Application) {

    Application.Alerts.subscribe('/core/fyi', {
        itemView: '',
        showDismiss: true,
        showMoreInfo: false,
        showApprove: false,
        showReject: false,
    });
});
