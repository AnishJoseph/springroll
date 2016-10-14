define(['Application', 'marionette', 'moment'], function (Application, Marionette, moment) {

    Application.requiresTemplate('#alerts.view');
    Application.requiresTemplate('#alert.item.template');

    var subscribedAlerts = {};
    var AlertItem = Backbone.Model.extend({urlRoot:'/api/sr/notification'});
    var AlertCollection = Backbone.Collection.extend({
        model: AlertItem,
    });
    var alertsCollection = new AlertCollection();

    var AlertsItemView = Marionette.View.extend({
        tagName: 'div',
        template: '#alert.item.template',
        subscribedAlerts : subscribedAlerts,

        regions: {
            messageRegion: '#message',
        },

        dismissClicked : function(){
            if(_.contains(_.functions(this.model.get('view')),"dismissClicked")){
                this.model.get('view').dismissClicked();
            } else {
                this.model.destroy({
                    success: function(model, response) {
                        console.log("Notification ACKED");
                    }
                });
                console.log("dismissClicked clicked");
            }
        },
        acceptClicked : function(){
            if(_.contains(_.functions(this.model.get('view')),"acceptClicked")){
                this.model.get('view').acceptClicked();
            } else {
                /* Some module has subscribed for this channel and has asked for the 'accept' icon to be displayed - however
                  no method exists in the view provided to handle the click
                 */
                console.error('For channel ' + this.model.get('channel') + " no method specified for event acceptClicked");
            }
        },
        rejectClicked : function(){
            if(_.contains(_.functions(this.model.get('view')),"rejectClicked")){
                this.model.get('view').rejectClicked();
            } else {
                /* Some module has subscribed for this channel and has asked for the 'reject' icon to be displayed - however
                 no method exists in the view provided to handle the click
                 */
                console.error('For channel ' + this.model.get('channel') + " no method specified for event rejectClicked");
            }
        },
        infoClicked : function(){
            if(_.contains(_.functions(this.model.get('view')),"infoClicked")){
                this.model.get('view').infoClicked();
            } else {
                /* Some module has subscribed for this channel and has asked for the 'info' icon to be displayed - however
                 no method exists in the view provided to handle the click
                 */
                console.error('For channel ' + this.model.get('channel') + " no method specified for event infoClicked");
            }
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
            if (options.showDismiss) this.ui.dismiss.show();
            if (options.showAccept) this.ui.accept.show();
            if (options.showReject) this.ui.reject.show();
            if (options.showInfo) this.ui.info.show();
            this.model.set('view', new (options.view)({model:this.model}));
            this.showChildView('messageRegion', this.model.get('view'));
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



