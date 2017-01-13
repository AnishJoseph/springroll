var Marionette = require('backbone.marionette');
var Application =require('Application');

var View = Marionette.View.extend({
    tagName: 'h2',
    template: "#m3s1Template",
});

var M3S1Controller = Marionette.Object.extend({
    activate: function() {
        var view = new View();
        Application.rootView.showBody(view);
        Backbone.history.navigate('m3s1');
    }
});

var m3s1Controller = new M3S1Controller();

Application.addMenuItem({
    parent: 'ui.Item3',
    name: 'subItem1',
    title: 'ui.SubItem1',
    controller : m3s1Controller,
    index : 3,
    subIndex : 1
});

var M3S1Router = new Marionette.AppRouter({
    controller: m3s1Controller,

    appRoutes: {
        'm3s1': 'activate'
    }
});
Application.requiresTemplate("#m3s1Template");