var Marionette = require('backbone.marionette');
var Application =require('Application');
Application.requiresTemplate('#mdm.newrecs.moreinfo');
Application.requiresTemplate('#mdm.changedrecs.moreinfo');

var ChangedView = Marionette.View.extend({
    template: '#mdm.changedrecs.moreinfo'
});

var NewView = Marionette.View.extend({
    template: '#mdm.newrecs.moreinfo'
});

var MdmMoreInfoView = Marionette.View.extend({
    template : _.template('<div id="changedRecords"></div><div style="overflow: auto" id="newRecords"></div><div id="control"></div>'),
    regions : {
        changedRegion: '#changedRecords',
        newRegion: '#newRecords',
        controlRegion: '#control'
    },

    onRender : function(){
        var changedRecords = this.model.get('mdmChangesForReview')['changedRecords'];
        var newRecords = this.model.get('mdmChangesForReview')['newRecords'];
        if( changedRecords !== null && changedRecords.length > 0){
            this.showChildView('changedRegion', new ChangedView({'model' : this.model}));
        }
        if( newRecords !== null && newRecords.length > 0){
            this.showChildView('newRegion', new NewView({'model' : this.model}));
        }
    }
});


var ReviewItem = Backbone.Model.extend({urlRoot:'api/sr/reviewaction'});

var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-localMessage%>'),

    initialize : function(){
        this.model.set('localMessage', Localize(this.model.get("messageKey"), this.model.get("args")));
    },
    infoClicked : function(){
        var view = new MdmMoreInfoView({ 'model': this.model});
        Application.showModal(this.model.get('localMessage'), view, this);
    },
    acceptClicked : function(commentText){
        var reviewItem = new ReviewItem({reviewStepId: this.model.get('reviewStepId'), approved : true, 'reviewComment':commentText});
        var that = this;
        reviewItem.save({},{
            success : function () {
                that.model.trigger('destroy', that.model);
            },
            error : function(){
                //FIXME - handle success and failure
            }
        });
    },
    rejectClicked : function(commentText){
        var reviewItem = new ReviewItem({reviewStepId: this.model.get('reviewStepId'), approved : false, 'reviewComment':commentText});
        var that = this;
        reviewItem.save({},{
            success : function () {
                that.model.trigger('destroy', that.model);
            },
            error : function(){
                //FIXME - handle sucees and failure
            }
        });
    },
});

Application.subscribeForAlert('/core/mdmreview', {
    view: AlertsView,
    showDismiss: false,
    showInfo: true,
    showAccept: false,
    showReject: false,
});
