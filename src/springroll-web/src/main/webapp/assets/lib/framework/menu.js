define(['Application', 'marionette'], function (Application, Marionette) {

    Application.MenuView = Marionette.View.extend({
        tagName: 'div',
        template: function(){
            var template = [];
            template.push('<ul class="nav nav-pills">');
            Object.keys(Application.getMenuItems()).forEach(function(key,index) {
               var menuItem = Application.getMenuItems()[key];
                var id = 'menuId' + menuItem.name;
                template.push('<li role="presentation"><a href="#" id="' + id + '">' + menuItem.name +'</a></li>');
            });
            template.push('</ul>');
            var  menuTemplate = template.join("");
            return menuTemplate;

        },
        events : function(){
            var events = {};
            var that = this;
            Object.keys(Application.getMenuItems()).forEach(function(key,index) {
                var menuItem = Application.getMenuItems()[key];
                var id = 'click #menuId' + menuItem.name;
                events[id] = function(){
                    Application.rootView.showBody(menuItem.view);
                }
            });
            return events;
        },

    });
});



