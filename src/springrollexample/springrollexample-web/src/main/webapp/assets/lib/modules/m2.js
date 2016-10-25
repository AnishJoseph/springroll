define([], function () {

    var M2Controller = Marionette.Object.extend({
        activate: function() {
            var view = new Application.ReportView({"gridName":"TestGrid1" });
            Application.rootView.showBody(view);
            Backbone.history.navigate('m2');
        }
    });

    var m2Controller = new M2Controller();

    Application.addMenuItem({
        name: 'Item2',
        controller : m2Controller,
        index : 2
    });

    var M2Router = new Marionette.AppRouter({
        controller: m2Controller,

        appRoutes: {
            'm2': 'activate'
        }
    });

});