define(['Application', 'marionette'], function (Application, Marionette) {

    Application.RootView = Marionette.View.extend({
        tagName: 'div',
        template: _.template("<div id='main-menu' style='width: 100%;background-color: #2aabd2'/><div id='main-body' style='width: 100%'/><div id='alerts'></div>"),
        regions: {
            menuRegion: '#main-menu',
            bodyRegion: '#main-body',
            alertsRegion: '#alerts'
        },
        onRender: function() {
            this.showChildView('menuRegion', new Application.MenuView());
            this.showChildView('alertsRegion', new Application.AlertsView());
        },
        showBody : function(view){
            this.showChildView('bodyRegion', view);
        }
    });
});