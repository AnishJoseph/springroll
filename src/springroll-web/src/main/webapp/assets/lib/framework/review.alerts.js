define(['Application', 'marionette', 'alerts'], function (Application, Marionette){
    var ReviewItem = Backbone.Model.extend({urlRoot:'/api/sr/reviewaction'});

    var AlertsView = Marionette.View.extend({
        tagName: 'div',
        template: _.template('<%-channel%>'),

        acceptClicked : function(){
            var reviewItem = new ReviewItem({reviewStepId: this.model.get('reviewStepId'), approved : true});
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

    Application.Alerts.subscribe('/core/review', {
        view: AlertsView,
        showDismiss: false,
        showInfo: false,
        showAccept: true,
        showReject: true,
    });

});
