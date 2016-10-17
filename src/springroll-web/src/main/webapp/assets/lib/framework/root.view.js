define(['Application', 'marionette'], function (Application, Marionette) {

    Application.RootView = Marionette.View.extend({
        tagName: 'div',
        template: _.template("<div id='main-menu'/><div id='main-body'/><div id='alerts'/><div id='indicator' style='position: absolute; top:0px; align-content: center; width: 100%'/> "),
        regions: {
            menuRegion: '#main-menu',
            bodyRegion: '#main-body',
            alertsRegion: '#alerts',
            indicatorRegion: '#indicator'
        },
        onRender: function() {
            this.showChildView('menuRegion', new Application.MenuView());
            this.showChildView('alertsRegion', Application.Alerts.getAlertPanelView());
            this.showChildView('indicatorRegion', Application.Indicator.getView());
        },
        showBody : function(view){
            this.showChildView('bodyRegion', view);
        }
    });
});