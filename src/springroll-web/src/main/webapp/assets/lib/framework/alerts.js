define(['Application', 'marionette', 'moment'], function (Application, Marionette, moment) {

    Application.requiresTemplate('#alerts.view');
    Application.requiresTemplate('#alert.item.template');

    var subscribedAlerts = {};
    var AlertItem = Backbone.Model.extend({});
    var AlertCollection = Backbone.Collection.extend({
        model: AlertItem,
    });
    var alertsCollection = new AlertCollection();

    var AlertsItemView = Marionette.View.extend({
        tagName: 'div',
        template: '#alert.item.template',
        subscribedAlerts : subscribedAlerts,

        dismissClicked : function(){
            console.log("dismissClicked clicked");
        },
        acceptClicked : function(){
            console.log("acceptClicked clicked");
        },
        rejectClicked : function(){
            console.log("rejectClicked clicked");
        },
        infoClicked : function(){
            console.log("info clicked");
        },
        events: {
            'click #dismiss' : "dismissClicked",
            'click #accept'  : "acceptClicked",
            'click #reject'  : "rejectClicked",
            'click #info'    : "infoClicked"
        },

        ui: {
            dismiss:  '#dismiss',
            accept: '#accept',
            reject: '#reject',
            info:   '#info'
        },

        onRender : function(){
            var options = this.subscribedAlerts[this.model.get('channel')];
            if (options.showDismiss) {
                this.ui.dismiss.show();
            }

            if (options.showAccept) {
                this.ui.accept.show();
            }

            if (options.showReject) {
                this.ui.reject.show();
            }

            if (options.showInfo) {
                this.ui.info.show();
            }
        }
    });

    var AlertCollectionView = Marionette.CollectionView.extend({
        childView: AlertsItemView,
        collection: alertsCollection
    });

    var AlertsView = Marionette.View.extend({
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
        }
    });

    Application.Alerts = {
        subscribe : function(notificationChannel, options){
            subscribedAlerts[notificationChannel] = options;
            Application.subscribe(notificationChannel, function(message){
                var newAlerts = [];
                _.each(message.data, function(notification){

                    if(_.isUndefined(alertsCollection.get(notification.id))){
                        notification['creationTimeMoment'] = moment(notification.creationTime).format("DD MMM HH:mm");
                        //notification['creationTimeMoment'] = '10:10:10';
                        newAlerts.push(notification);
                    }
                });
                if(newAlerts.length > 0)alertsCollection.add(newAlerts);
            });
        },

        getAlertView : function (){
            return AlertsView;
        }
    }
});



