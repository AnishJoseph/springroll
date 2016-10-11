define(['Application', 'marionette'], function (Application, Marionette) {

    Application.subscribe('/core/review', function(message){
        console.log("Recevived message on REVIEW CHANNEL - user 1");
    });

    var View = Marionette.View.extend({
        tagName: 'h2',
        template: "#m1Template",

    });

    Application.addMenuItem({
        name: 'Item1',
        view : View
    });

});