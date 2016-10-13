define(['Application', 'marionette', 'backbone'], function (Application, Marionette, Backbone) {

    Application.subscribe('/core/review', function(message){
        console.log("Recevived message on REVIEW CHANNEL - user 1");
    });

    var View = Marionette.View.extend({
        tagName: 'h2',
        template: "#m1Template",

    });

    var M1Controller = Marionette.Object.extend({
        activate: function() {
            var view = new View();
            Application.rootView.showBody(view);
            Backbone.history.navigate('m1');
        }
    });

    var m1Controller = new M1Controller();

    Application.addMenuItem({
        name: 'Item1',
        controller : m1Controller,
        index : 1
    });

    var M1Router = new Marionette.AppRouter({
        controller: m1Controller,

        appRoutes: {
            'm1': 'activate'
        }
    });
    Application.requiresTemplate("#m1Template");


});