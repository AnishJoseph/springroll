define(['Application', 'marionette'], function (Application, Marionette) {

    Application.requiresTemplate("#transactionTestsTemplate");

    var SimpleTransaction = Backbone.Model.extend({
        url : '/api/testPipelineSimple'
    });

    var View = Marionette.View.extend({
        tagName: 'div',
        template: "#transactionTestsTemplate",

        events: {
            'click #simplePipeLine' : "simplePipeLine",
        },

        simplePipeLine : function(){
            console.log("simplePipeLine clicked");
            var simpleTransaction = new SimpleTransaction();
            simpleTransaction.set('testCase', 1);
            simpleTransaction.set('testLocation', 0);
            simpleTransaction.save();
        }

    });

    var M1Controller = Marionette.Object.extend({
        activate: function() {
            var view = new View();
            Application.rootView.showBody(view);
            Backbone.history.navigate('transactionTests');
        }
    });

    var m1Controller = new M1Controller();

    Application.addMenuItem({
        name: 'TransactionTests',
        controller : m1Controller,
        index : 1
    });

    var M1Router = new Marionette.AppRouter({
        controller: m1Controller,

        appRoutes: {
            'transactionTests': 'activate'
        }
    });

    $("#Test1").click(function(){
        //Application.CometD.publish('/service/hello', { name: 'World' });
        $.get("api/testPipelineSimple", function(data, status){
            //alert("Data: " + data + "\nStatus: " + status);
        });
    });
    $("#Test2").click(function(){
        $.get("api/testCompetingThreads", function(data, status){
//                alert("Data: " + data + "\nStatus: " + status);
        });
    });

});