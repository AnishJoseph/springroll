var Marionette = require('backbone.marionette');
var Application =require('Application');

Application.RootView = Marionette.View.extend({
    tagName: 'div',
    template: _.template(
        "<div id='main-menu' style='width: 100%; height:50px; position: fixed; top: 0;left:0;z-index: 100'/>" +
        "<div id='main-body' style='width: 100%; position: fixed; top: 50px;left:0;bottom: 0;z-index: 1'>" +
            "<div id='content' style='overflow: auto; margin: 5px;height: calc(100% - 20px)' ></div>" +
        "</div>" +
        "<div id='alerts'    style='position: fixed; top:100px; right: 0;z-index: 50'/>" +
        "<div id='indicator' style='position: fixed; top:0; align-content: center; width: 100%;z-index: 900'/> " +
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
    onShowModal : function(title, viewToShow, viewOfCaller ){
        this.showChildView('modalRegion', new Application.ModalView({model: new Backbone.Model({title:title}), viewToShow : viewToShow, viewOfCaller : viewOfCaller}));
    }
});