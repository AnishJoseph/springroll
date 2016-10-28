var Marionette = require('backbone.marionette');
var Application =require('Application');


var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-message%>'),

    infoClicked : function(){
        //FIXME - i18n for this
        var violations = new Backbone.Collection(this.model.get('businessValidationResult'));
        var view = new Application.ReviewMoreInfoTableView({ collection: violations});
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
