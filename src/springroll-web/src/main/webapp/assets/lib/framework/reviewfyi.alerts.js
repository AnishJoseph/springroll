var Marionette = require('backbone.marionette');
var Application =require('Application');


var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-message%>'),

    infoClicked : function(){
        var violations = [];
        _.each(this.model.get('businessValidationResult'), function(violation){
            violations.push({violatedRule: Localize(violation.violatedRule), message : Localize(violation.messageKey, violation.args)});
        });
        var view = new Application.ReviewMoreInfoTableView({ collection: new Backbone.Collection(violations)});
        Application.showModal("For your information", view);
    },
});

Application.subscribeForAlert('/core/reviewfyi', {
    view: AlertsView,
    showDismiss: true,
    showInfo: true,
    showAccept: false,
    showReject: false,
});
