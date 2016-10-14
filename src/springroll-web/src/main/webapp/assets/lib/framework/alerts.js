define(['Application', 'marionette'], function (Application, Marionette) {

    Application.requiresTemplate('#alerts.view');
    Application.requiresTemplate('#alert.item.template');

    var AlertItem = Backbone.Model.extend({});
    var AlertCollection = Backbone.Collection.extend({
        model: AlertItem,
    });
    var alertsCollection = new AlertCollection();

    var AlertsItemView = Marionette.View.extend({
        tagName: 'div',
        template: '#alert.item.template',

        ui: {
            trash:  '#trash',
            accept: '#accept',
            reject: '#reject',
            info:   '#info'
        },

        onRender : function(){
            this.ui.trash.show();
        }
    });

    var AlertCollectionView = Marionette.CollectionView.extend({
        childView: AlertsItemView,
        collection: alertsCollection
    });

    Application.AlertsView = Marionette.View.extend({
        tagName: 'div',
        template: '#alerts.view',
        events: {
            "click #alerts-handle": "toggleAlertContainer",
        },

        ui: {
            alertsContainer: "#alerts-container"
        },

        regions: {
            alertsContainer: '#alerts-container',
        },

        toggleAlertContainer : function(){
            console.log("toggleAlertContainer");
            this.ui.alertsContainer.toggle();
        },
        onRender : function(){
            var alertCollectionView = new AlertCollectionView();
            this.showChildView('alertsContainer', new AlertCollectionView());
            console.log("Rendered");
        }
    });

    var subscribedAlerts = [];
    Application.Alerts = {
        subscribe : function(notificationChannel, options){
            subscribedAlerts[notificationChannel] = options;
            Application.subscribe(notificationChannel, function(message){
                var newAlerts = [];
                _.each(message.data, function(notification){
                    var isAlreadyPresentInCollection = alertsCollection.get(notification.id);
                    if(_.isUndefined(isAlreadyPresentInCollection))newAlerts.push(notification);
                });
                if(newAlerts.length > 0)alertsCollection.add(newAlerts);
            });
        }
    }
});



