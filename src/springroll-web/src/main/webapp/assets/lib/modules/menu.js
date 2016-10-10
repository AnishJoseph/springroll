define(['Application', 'marionette'], function (Application, Marionette) {
    console.log("MENU VIEW Init!!!");
    Application.MenuView = Marionette.View.extend({
        tagName: 'div',
        template: function(){
            var template = [];
            template.push('<ul class="nav nav-pills style="border-bottom: none; border: none; background-color: #d3d8e0">');
            Object.keys(Application.getMenuItems()).forEach(function(key,index) {
               var menuItem = Application.getMenuItems()[key];
                var id = 'menuId' + menuItem.name;
                template.push('<li role="presentation"><a href="#" id="' + id + '">' + menuItem.name +'</a></li>');
            });

                //_.each(_.keys(Application.getMenuItems()), function (menuItem) {
            //});
            template.push('</ul>');
            var  xx = template.join("");
            return xx;

        },
        events : function(){
            var events = {};
            Object.keys(Application.getMenuItems()).forEach(function(key,index) {
                var menuItem = Application.getMenuItems()[key];
                var id = 'click #menuId' + menuItem.name;
                events[id] = menuItem.cb;
            });
            return events;
        },

        menuClicked : function(a,b,c){
            console.log("Menu Item clicked");
        }

    });


});