var Marionette = require('backbone.marionette');
var Application =require('Application');

Application.RootView = Marionette.View.extend({
    tagName: 'div',
    template: _.template(
        "<div id='main-menu' class='root-menu'/>" +
        "<div id='main-body' class='main-body'>" +
            "<div id='content' class='main-body-content' ></div>" +
        "</div>" +
        "<div id='alerts'    class='alertsPanel'/>" +
        "<div id='indicator' class='alertsIndicator'/> " +
        "<div id='modal'/> "
    ),
    regions: {
        menuRegion: '#main-menu',
        bodyRegion: '#content',
        alertsRegion: '#alerts',
        indicatorRegion: '#indicator',
        modalRegion: '#modal'
    },
    onRender: function() {
        this.showChildView('menuRegion', new Application.MenuView());
        this.showChildView('alertsRegion', Application.Alerts.getAlertPanelView());
        this.showChildView('indicatorRegion', Application.Indicator.getView());
    },
    showBody : function(view){
        this.showChildView('bodyRegion', view);
    },
    onShowModal : function(title, viewToShow, viewOfCaller, detachViewOnHide ){
        this.showChildView('modalRegion', new Application.ModalView({model: new Backbone.Model({title:title}), viewToShow : viewToShow, viewOfCaller : viewOfCaller, "detachViewOnHide" : detachViewOnHide}));
    },

    onHideModal : function(){
        var view = this.getChildView('modalRegion');
        view.hide();
    }
});