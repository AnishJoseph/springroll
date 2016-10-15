define(['Application', 'marionette'], function (Application, Marionette) {

    Application.RootView = Marionette.View.extend({
        tagName: 'div',
        template: _.template("<div id='main-menu'/><div id='main-body'/><div id='alerts'></div>"),
        regions: {
            menuRegion: '#main-menu',
            bodyRegion: '#main-body',
            alertsRegion: '#alerts'
        },
        onRender: function() {
            this.showChildView('menuRegion', new Application.MenuView());
            this.showChildView('alertsRegion', Application.Alerts.getAlertPanelView());
        },
        showBody : function(view){
            this.showChildView('bodyRegion', view);
        }
    });
});