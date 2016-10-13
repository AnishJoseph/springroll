define(['Application', 'marionette', 'backbone'], function (Application, Marionette, Backbone) {

    Application.subscribe('/core/review', function(message){
        console.log("Recevived message on REVIEW CHANNEL - user 2");
    });

    var View = Marionette.View.extend({
        tagName: 'h2',
        template: "#m2Template",

    });

    var M2Controller = Marionette.Object.extend({
        activate: function() {
            var view = new View();
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
    Application.requiresTemplate("#m2Template");

});