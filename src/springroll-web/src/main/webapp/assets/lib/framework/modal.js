var Marionette = require('backbone.marionette');
var Application = require('Application');
Application.requiresTemplate('#modal');

Application.ModalView = Marionette.View.extend({
    tagName: 'div',
    template: "#modal",

    initialize : function(options){
        this.bodyView = options.view;
    },

    regions : {
        modalBody : '#modal-body'
    },
    ui : {
        mymodal : '#mymodal'
    },

    onRender : function(){
        $(this.ui.mymodal).modal();
        this.showChildView('modalBody', this.bodyView);
    }
});