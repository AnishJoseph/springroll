var Marionette = require('backbone.marionette');
var Application =require('Application');


var ReviewItem = Backbone.Model.extend({urlRoot:'/api/sr/reviewaction'});

var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-localMessage%>'),

    initialize : function(){
        this.model.set('localMessage', Localize(this.model.get("messageKey"), this.model.get("args")));
    },
    infoClicked : function(){
        var violations = [];
        _.each(this.model.get('businessValidationResult'), function(violation){
            violations.push({violatedRule: Localize(violation.violatedRule), message : Localize(violation.messageKey, violation.args)});
        });
        var view = new Application.ReviewMoreInfoTableView({ collection: new Backbone.Collection(violations)});
        Application.showModal("Approval Required", view, this);
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

Application.subscribeForAlert('/core/review', {
    view: AlertsView,
    showDismiss: false,
    showInfo: true,
    showAccept: true,
    showReject: true,
});
