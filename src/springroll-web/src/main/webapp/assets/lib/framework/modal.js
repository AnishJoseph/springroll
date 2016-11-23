var Marionette = require('backbone.marionette');
var Application = require('Application');
Application.requiresTemplate('#modal');

Application.ModalView = Marionette.View.extend({
    tagName: 'div',
    template: "#modal",

    initialize : function(options){
        this.viewToShow = options.viewToShow;
        this.viewOfCaller = options.viewOfCaller;
        this.commentText = '';
    },

    regions : {
        modalBody : '#modal-body'
    },
    ui : {
        mymodal : '#mymodal',
        accept : '#accept',
        reject : '#reject',
        dismiss : '#dismiss',
        comments : '#comments'
    },

    events: {
        'click #dismiss' : "dismissClicked",
        'click #accept'  : "acceptClicked",
        'click #reject'  : "rejectClicked",
        'change #comments'  : "commentChanged",
    },

    commentChanged : function(){
        this.commentText = $(this.ui.comments).val();
    },
    dismissClicked : function(){
        this.viewOfCaller.dismissClicked();
        $(this.ui.mymodal).modal('hide');
    },
    acceptClicked : function(){
        this.viewOfCaller.acceptClicked(this.commentText);
        $(this.ui.mymodal).modal('hide');
    },
    rejectClicked : function(){
        this.viewOfCaller.rejectClicked(this.commentText);
        $(this.ui.mymodal).modal('hide');
    },


    onRender : function() {
        $(this.ui.mymodal).modal();
        this.showChildView('modalBody', this.viewToShow);
        if (_.contains(_.functions(this.viewOfCaller), "acceptClicked")) {
            $(this.ui.accept).removeClass('hidden');
        }
        if (_.contains(_.functions(this.viewOfCaller), "rejectClicked")) {
            $(this.ui.reject).removeClass('hidden');
        }
        if (_.contains(_.functions(this.viewOfCaller), "dismissClicked")) {
            $(this.ui.dismiss).removeClass('hidden');
        }
    }
});