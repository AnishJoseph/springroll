var Marionette = require('backbone.marionette');
var Backbone = require('backbone');
var Application =require('Application');
var moment = require('moment');

Application.requiresTemplate('#alerts.view');
Application.requiresTemplate('#alert.item.template');
var compare = function(model){
    return -(model.get('creationTime'));//Latest alerts show on top
}

var subscribedAlerts = {};

var AlertItem = Backbone.Model.extend({urlRoot:'/api/sr/notification'});

var AlertCollection = Backbone.Collection.extend({
    initialize: function (models, options) {
        this.bind("add", function (item) {
            Application.Alerts.getAlertPanelView().triggerMethod(options.channel + ":count:changed", this.length);
        });
        this.bind("remove", function (item) {
            Application.Alerts.getAlertPanelView().triggerMethod(options.channel + ":count:changed", this.length);
        });
    },
});

var actionCollection = new AlertCollection(null, {model: AlertItem, comparator: compare, channel:'action'});
var errorCollection = new AlertCollection(null, {model: AlertItem, comparator: compare, channel:'error'});
var infoCollection = new AlertCollection(null, {model: AlertItem, comparator: compare, channel:'info'});

var AlertsItemView = Marionette.View.extend({
    tagName: 'div',
    template: '#alert.item.template',
    subscribedAlerts : subscribedAlerts,

    regions: {
        messageRegion: '#message',
    },

    dismissClicked : function(destroyModel){
        if(destroyModel !== true && _.contains(_.functions(this.model.get('view')),"dismissClicked")){
            this.model.get('view').dismissClicked();
        } else {
            this.model.destroy({
                success: function(model, response) {
                }
            });
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
        this.model.set('view', new (options.view)({model:this.model, parentView : this}));
        this.showChildView('messageRegion', this.model.get('view'));
    }
});

var ActionCollectionView = Marionette.CollectionView.extend({
    childView: AlertsItemView,
    collection: actionCollection
});

var ErrorCollectionView = Marionette.CollectionView.extend({
    childView: AlertsItemView,
    collection: errorCollection
});

var InfoCollectionView = Marionette.CollectionView.extend({
    childView: AlertsItemView,
    collection: infoCollection
});


var AlertsPanel = Marionette.View.extend({
    tagName: 'div',
    template: '#alerts.view',
    events: {
        "click #alerts-handle": "toggleAlertContainer",
        "click #actionHandle": "showAction",
        "click #errorHandle": "showError",
        "click #infoHandle": "showInfo",
    },

    showAction : function(evt){
        this.showChildView('alertsContainer', new ActionCollectionView());
        this.toggleAlertContainer(evt, true);
    },
    showError : function(evt){
        this.showChildView('alertsContainer', new ErrorCollectionView());
        this.toggleAlertContainer(evt, true);
    },
    showInfo : function(evt){
        this.showChildView('alertsContainer', new InfoCollectionView());
        this.toggleAlertContainer(evt, true);
    },
    toggleAlertContainer : function(evt, show){
        if(show == undefined) {
            //Called when the toggle is clicked (i.e not called when a specific collection is requested
            if ($(this.ui.alertsContainer).is(':visible')) {
                $(this.ui.alertsHandle).removeClass('glyphicon-eye-close');
                $(this.ui.alertsHandle).addClass('glyphicon-eye-open');
            } else {
                $(this.ui.alertsHandle).removeClass('glyphicon-eye-open');
                $(this.ui.alertsHandle).addClass('glyphicon-eye-close');
            }
            this.ui.alertsContainer.toggle();
            return;
        }
        if ($(this.ui.alertsContainer).is(':visible')) return;
        $(this.ui.alertsHandle).removeClass('glyphicon-eye-open');
        $(this.ui.alertsHandle).addClass('glyphicon-eye-close');
        this.ui.alertsContainer.toggle();
    },

    ui: {
        alertsContainer: "#alerts-container",
        alertsHandle: "#alerts-handle",
        actionCount: "#action",
        errorCount : "#error",
        infoCount  : "#info"
    },

    regions: {
        alertsContainer: '#alerts-container',
    },

    onRender : function(){
        this.showChildView('alertsContainer', new ActionCollectionView());
    },
    onActionCountChanged : function(count){
        this.ui.actionCount.html(count);
    },
    onErrorCountChanged : function(count){
        this.ui.errorCount.html(count);
    },
    onInfoCountChanged : function(count){
        this.ui.infoCount.html(count);
    }
});
var alertsPanel = new AlertsPanel();

/* This is the functionality that we export */
Application.Alerts = {
    registerAlertSubscriptions : function(){
        _.each(Application.getSubscribersForAlerts(), function(subscription){
            var notificationChannel = subscription.channel;
            var options = subscription.options;

            subscribedAlerts[notificationChannel] = options;
            Application.subscribe(notificationChannel, function(message){
                var newAlerts = [];
                var alertCollection;
                if(message.data[0].channelType == 'ACTION') alertCollection = actionCollection;
                if(message.data[0].channelType == 'ERROR') alertCollection = errorCollection;
                if(message.data[0].channelType == 'INFO') alertCollection = infoCollection;
                _.each(message.data, function(notification){

                    if(_.isUndefined(alertCollection.get(notification.id))){
                        notification['creationTimeMoment'] = moment(notification.creationTime).format("DD MMM HH:mm");
                        newAlerts.push(notification);
                    }
                });
                if(newAlerts.length > 0)alertCollection.add(newAlerts);
            });
        });

    },

    getAlertPanelView : function (){
        return alertsPanel;
    }
}
Application.subscribe('/core/notificationCancel', function(message){
    var alertCollection;
    if(message.data[0].channelType == 'ACTION') alertCollection = actionCollection;
    if(message.data[0].channelType == 'ERROR') alertCollection = errorCollection;
    if(message.data[0].channelType == 'INFO') alertCollection = infoCollection;
    alertCollection.remove(message.data[0].id);

});



