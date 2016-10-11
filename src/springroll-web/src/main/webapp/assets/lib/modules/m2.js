define(['Application', 'marionette'], function (Application, Marionette) {
    Application.subscribe('/core/review', function(message){
        console.log("Recevived message on REVIEW CHANNEL - user 2");
    });

    var View = Marionette.View.extend({
        tagName: 'h2',
        template: "#m2Template"
    });

    Application.addMenuItem({
        name: 'Item2',
        view : View
    });

});