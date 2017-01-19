var Marionette = require('backbone.marionette');
var Backbone = require('backbone');
var Application =require('Application');
var moment = require('moment');
var Radio = require('backbone.radio');
var alertChannel = Radio.channel('AlertChannel');

Application.requiresTemplate('#alerts.view');
Application.requiresTemplate('#alert.item.template');
var compare = function(model){
    return -(model.get('creationTime'));//Latest alerts show on top
};

var subscribedAlerts = {};

var AlertItem = Backbone.Model.extend({urlRoot:'api/sr/notification'});

var AlertCollection = Backbone.Collection.extend({

    initialize: function (models, options) {
        this.bind("add", function (item) {
            alertChannel.trigger('alert:' + options.channel + ':count:changed', this.length);
        });
        this.bind("remove", function (item) {
            alertChannel.trigger('alert:' + options.channel + ':count:changed', this.length);
        });
    }
});

var actionCollection = new AlertCollection(null, {model: AlertItem, comparator: compare, channel:'action'});
var errorCollection  = new AlertCollection(null, {model: AlertItem, comparator: compare, channel:'error'});
var infoCollection   = new AlertCollection(null, {model: AlertItem, comparator: compare, channel:'info'});

var AlertsItemView = Marionette.View.extend({
    tagName: 'div',
    template: '#alert.item.template',
    subscribedAlerts : subscribedAlerts,

    regions: {
        messageRegion: '#message'
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

var AlertsView = Marionette.View.extend({
    initialize : function(options){
        this.collectionView = options.collectionView;
        this.alerts = options.alerts;
        this.model = new Backbone.Model();
        this.model.set("title", options.title);
    },
    template : _.template(
                            '<div>' +
                                '<div id="alerts-handle" class="alertHandle glyphicon glyphicon-eye-close"></div>' +
                                '<div class="alertTitle"><p> <%-title%></p></div>' +
                            '</div>' +
                            '<div style="clear:both; border-bottom: 1px solid #ccc;"/>' +
                            '<div id ="col"></div>'
                        ),

    regions : {
        col : '#col'
    },

    onRender : function (){
        this.showChildView('col', new (this.collectionView)({collection : this.alerts}));
    },

    events: {
        'click #alerts-handle' : "hideContainer"
    },

    hideContainer : function(){
        alertChannel.trigger('alert:hide');
    }

});

var AlertCollectionView = Marionette.CollectionView.extend({
    childView: AlertsItemView
});

var AlertsPanel = Marionette.View.extend({
    tagName: 'div',
    template: '#alerts.view',

    initialize : function(){
        var that = this;
        alertChannel.on('alert:action:clicked', function() {
            that.showChildView('alertsContainer', new AlertsView({alerts: actionCollection, title:Localize("ui.alert.action"), collectionView : AlertCollectionView}));
            that.ui.alertsContainer.show();
        });
        alertChannel.on('alert:error:clicked', function() {
            that.showChildView('alertsContainer', new AlertsView({alerts: errorCollection, title:Localize("ui.alert.errors"), collectionView : AlertCollectionView}));
            that.ui.alertsContainer.show();
        });
        alertChannel.on('alert:info:clicked', function() {
            that.showChildView('alertsContainer', new AlertsView({alerts: infoCollection, title:Localize("ui.alert.info"), collectionView : AlertCollectionView}));
            that.ui.alertsContainer.show();
        });
        alertChannel.on('alert:hide', function() {
            that.ui.alertsContainer.hide();
        });
    },

    ui: {
        alertsContainer: "#alerts-container"
    },

    regions: {
        alertsContainer: '#alerts-container'
    }

});
var alertsPanel = new AlertsPanel();

/* This is the functionality that we export */
Application.Alerts = {
    registerAlertSubscriptions : function(){
        _.each(Application.getSubscribersForAlerts(), function(subscription){
            var notificationChannel = subscription.channel;

            subscribedAlerts[notificationChannel] = subscription.options;
            Application.subscribe(notificationChannel, function(message){
                var newAlerts = [];
                var alertCollection;
                if(message.data[0].alertType == 'ACTION') {alertCollection = actionCollection;alertChannel.trigger('alert:action:clicked');}
                if(message.data[0].alertType == 'ERROR') alertCollection = errorCollection;
                if(message.data[0].alertType == 'INFO') alertCollection = infoCollection;
                _.each(message.data, function(notification){

                    if(_.isUndefined(alertCollection.get(notification.id))){
                        notification['creationTimeMoment'] = moment(notification.creationTime).format(Application.getMomentFormatForDateTime());
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
};
Application.subscribe('/core/notificationCancel', function(message){
    var alertCollection;
    if(message.data[0].alertType == 'ACTION') alertCollection = actionCollection;
    if(message.data[0].alertType == 'ERROR') alertCollection = errorCollection;
    if(message.data[0].alertType == 'INFO') alertCollection = infoCollection;
    alertCollection.remove(message.data[0].id);

});



