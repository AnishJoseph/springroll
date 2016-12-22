var Marionette = require('backbone.marionette');
var Application =require('Application');

var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-localMessage%>'),

    initialize : function(){
        this.model.set('localMessage', Localize(this.model.get("messageKey"), this.model.get("args")));
    }
});

Application.subscribeForAlert('/core/fyi', {
    view: AlertsView,
    showDismiss: true,
    showInfo: false,
    showAccept: false,
    showReject: false,
});
Application.subscribeForAlert('/core/springrollexception', {
    view: AlertsView,
    showDismiss: true,
    showInfo: false,
    showAccept: false,
    showReject: false,
});
