var Marionette = require('backbone.marionette');
var Application =require('Application');
Application.requiresTemplate('#mdm.moreinfo');

var ChangedView = Marionette.View.extend({
    template : _.template('<div id="changedRecords">CHANGED VIEW</div>'),

    onRender : function(){
        console.log("CHANGED");
    }

});

var NewView = Marionette.View.extend({
    template: '#mdm.moreinfo',

    onRender : function(){
        console.log("NEW");
    }

});

var MdmMoreInfoView = Marionette.View.extend({
    template : _.template('<div id="changedRecords"></div><div id="newRecords"></div><div id="control"></div>'),
    regions : {
        changedRegion: '#changedRecords',
        newRegion: '#newRecords',
        controlRegion: '#control'
    },

    onRender : function(){
        var changedRecords = this.model.get('changedRecords');
        var newRecords = this.model.get('newRecords');
        if( changedRecords !== null && changedRecords.length > 0){
            this.showChildView('changedRegion', new ChangedView({'model' : new Backbone.Model(changedRecords)}));
        }
        if( newRecords !== null && newRecords.length > 0){
            this.showChildView('newRegion', new NewView({'model' : new Backbone.Model({'newRecords' : newRecords})}));
        }
    }
});


var ReviewItem = Backbone.Model.extend({urlRoot:'/api/sr/reviewaction'});
var MdmMoreInfo = Backbone.Model.extend({
    url: function () {
        var reviewStepId = this.get('reviewStepId');
        this.unset('reviewStepId');
        return "/api/sr/mdm/moreinfo?reviewStepId=" + reviewStepId;
    }
});
var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-localMessage%>'),

    initialize : function(){
        this.model.set('localMessage', Localize(this.model.get("messageKey"), this.model.get("args")));
    },
    infoClicked : function(){
        var mdmMoreInfo = new MdmMoreInfo({reviewStepId : this.model.get('reviewStepId')});
        var that = this;
        mdmMoreInfo.fetch({
            success : function(model, data){
                var view = new MdmMoreInfoView({ 'model': model});
                Application.showModal("Approval Required", view, that);
            }
        });

    },
    acceptClicked : function(){
        var reviewItem = new ReviewItem({reviewStepId: this.model.get('reviewStepId'), approved : true});
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
    rejectClicked : function(){
        var reviewItem = new ReviewItem({reviewStepId: this.model.get('reviewStepId'), approved : false});
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
    showAccept: true,
    showReject: true,
});
