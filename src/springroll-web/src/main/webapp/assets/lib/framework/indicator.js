define(['Application', 'marionette'], function (Application, Marionette) {

    Application.requiresTemplate('#indicator.item.template');

    var collection = new Backbone.Collection();

    var ItemView = Marionette.View.extend({
        tagName: 'div',
        template: '#indicator.item.template',

        events: {
            'click #trash' : "trashClicked",
        },

        ui: {
            id:  '#id',
        },
        trashClicked : function(){
            this.remove();
        },
        onRender : function(){
            if(this.model.get('type') == 'E'){
                this.$el.children().addClass('alert-danger');
                this.$el.children().removeClass('alert-success');
            } else {
                var that = this;
                setTimeout(function(){
                    that.remove();
                }, 3000);
            }
        },
    });

    var CollectionView = Marionette.CollectionView.extend({
        childView: ItemView,
        collection: collection
    });

    var collectionView = new CollectionView();

    Application.Indicator = {
        showSuccessMessage : function(message){
            message.type = 'S';
            collection.add(message);
        },
        showErrorMessage : function(message){
            message.type = 'E';
            collection.add(message);
        },

        getView : function(){
            return  collectionView;
        }

    };

});