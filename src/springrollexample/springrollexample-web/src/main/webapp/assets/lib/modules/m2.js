var Marionette = require('backbone.marionette');
var Application =require('Application');

var M2Controller = Marionette.Object.extend({
    activate: function() {
        var view = new Application.GridView({"gridName":"TestGrid1" });
        Application.rootView.showBody(view);
        Backbone.history.navigate('m2');
    }
});

var m2Controller = new M2Controller();

Application.addMenuItem({
    name: 'menuSecond',
    title: 'ui.menu.second',
    controller : m2Controller,
    index : 2
});

var M2Router = new Marionette.AppRouter({
    controller: m2Controller,

    appRoutes: {
        'm2': 'activate'
    }
});