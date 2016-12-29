var Marionette = require('backbone.marionette');
var Application =require('Application');
Application.requiresTemplate('#springroll.exception.moreinfo');

var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-localMessage%>'),

    initialize : function(){
        this.model.set('localMessage', Localize(this.model.get("messageKey"), this.model.get("args")));
    }
});


var SpringrollExceptionMoreInfoView = Marionette.View.extend({
    template: '#springroll.exception.moreinfo',
});

var SpringrollExceptionAlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-serviceMessage%>'),
    infoClicked : function(){
        var view = new SpringrollExceptionMoreInfoView({model : this.model});
        Application.showModal(Localize("Exception Details"), view, this);
    },

    initialize : function(){
        this.model.set('serviceMessage', Localize(this.model.get("serviceMessageKey"), this.model.get("serviceMessageArgs")));
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
    view: SpringrollExceptionAlertsView,
    showDismiss: true,
    showInfo: true,
    showAccept: false,
    showReject: false,
});
