var Marionette = require('backbone.marionette');
var Application =require('Application');


var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-localMessage%>'),

    initialize : function(options){
        this.parentView = options.parentView;
        this.model.set('localMessage', Localize(this.model.get("messageKey"), this.model.get("args")));
    },

    dismissClicked : function() {
        this.parentView.dismissClicked(true);
    },
    infoClicked : function(){
        var violations = [];
        _.each(this.model.get('businessValidationResult'), function(violation){
            violations.push({violatedRule: Localize(violation.violatedRule), message : Localize(violation.messageKey, violation.args)});
        });
        var view = new Application.ReviewMoreInfoTableView({ violations: new Backbone.Collection(violations), model : this.model});
        Application.showModal(this.model.get('localMessage'), view, this);
    },
});

Application.subscribeForAlert('/core/reviewfyi', {
    view: AlertsView,
    showDismiss: true,
    showInfo: true,
    showAccept: false,
    showReject: false,
});
