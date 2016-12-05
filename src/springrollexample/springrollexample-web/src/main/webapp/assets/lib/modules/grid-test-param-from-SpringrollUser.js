var Marionette = require('backbone.marionette');
var Application =require('Application');

var TestGridParamFromSpringrollUserController = Marionette.Object.extend({
    activate: function() {
        var view = new Application.GridView({"gridName":"TestGridParamFromSpringrollUser" });
        Application.rootView.showBody(view);
        Backbone.history.navigate('TestGridParamFromSpringrollUser');
    }
});

var controller = new TestGridParamFromSpringrollUserController();

Application.addMenuItem({
    parent: 'GridTests',
    name: 'ParamFromSpringrollUser',
    controller : controller,
    index : 5,
    subIndex : 2
});

var TestGridAllTypesRouter = new Marionette.AppRouter({
    controller: controller,

    appRoutes: {
        'TestGridParamFromSpringrollUser': 'activate'
    }
});