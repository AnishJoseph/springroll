var Marionette =require('backbone.marionette');
var Application =require('Application');

var View = Marionette.View.extend({
    tagName: 'div',
    className : 'full-overflow',
    template: "#m3s2Template"
});

var M3S2Controller = Marionette.Object.extend({
    activate: function() {
        var view = new View();
        Application.rootView.showBody(view);
        Backbone.history.navigate('m3s2');
    }
});

var m3s2Controller = new M3S2Controller();

Application.addMenuItem({
    parent: 'ui.Item3',
    title: 'ui.SubItem2',
    name: 'subItem2',
    controller : m3s2Controller,
    index : 3,
    subIndex : 2
});

var M3S2Router = new Marionette.AppRouter({
    controller: m3s2Controller,

    appRoutes: {
        'm3s2': 'activate'
    }
});

Application.requiresTemplate("#m3s2Template");
