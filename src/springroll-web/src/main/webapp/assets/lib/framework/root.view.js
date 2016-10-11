define(['Application', 'marionette'], function (Application, Marionette) {

    Application.RootView = Marionette.View.extend({
        tagName: 'div',
        template: _.template("<div id='main-menu'/><div id='main-body'/>"),
        regions: {
            menuRegion: '#main-menu',
            bodyRegion: '#main-body'
        },
        onRender: function() {
            this.showChildView('menuRegion', new Application.MenuView());
        },
        showBody : function(view){
            this.showChildView('bodyRegion', view);
        }
    });
});