var Marionette = require('backbone.marionette');
var Application =require('Application');

var TestGridAllTypesController = Marionette.Object.extend({
    activate: function() {
        var view = new Application.GridView({"gridName":"TestGridAllTypes" });
        Application.rootView.showBody(view);
        Backbone.history.navigate('TestGridAllTypes');
    }
});

var testGridAllTypesController = new TestGridAllTypesController();

Application.addMenuItem({
    parent: 'GridTests',
    name: 'AllTypes',
    controller : testGridAllTypesController,
    index : 5,
    subIndex : 1
});

var TestGridAllTypesRouter = new Marionette.AppRouter({
    controller: testGridAllTypesController,

    appRoutes: {
        'TestGridAllTypes': 'activate'
    }
});