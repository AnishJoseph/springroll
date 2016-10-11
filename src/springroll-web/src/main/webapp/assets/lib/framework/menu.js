define(['Application', 'marionette'], function (Application, Marionette) {
    //var xx = '<ul class="nav nav-pills"> <li role="presentation"><a href="#" id="menuIdItem1">Item1</a></li> <li role="presentation"><a href="#" id="menuIdItem2">Item2</a></li> <li role="presentation" class="dropdown"> <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Item3<span class="caret"></span></a> <ul class="dropdown-menu"> <li role="presentation"><a href="#" id="menuIdsubItem1">subItem1</a></li> <li role="presentation"><a href="#" id="menuIdsubItem2">subItem2</a></li> </ul> </li> </ul>';
    Application.MenuView = Marionette.View.extend({
        tagName: 'div',
        template: function(){
            var template = [];
            template.push('<ul class="nav nav-pills">');
            /* Sort the menuItems by index before adding the menu item */
            _.each(_.sortBy(Application.getMenuItems(), 'index'), function(menuItem){
                var id = 'menuId' + menuItem.name;
                template.push('<li role="presentation"><a id="' + id + '">' + menuItem.name +'</a></li>');
            });
            template.push('</ul>');
            var  menuTemplate = template.join("");
            return menuTemplate;

        },
        events : function(){
            var events = {};
            _.each(Application.getMenuItems(), function(menuItem){
                var id = 'click #menuId' + menuItem.name;
                events[id] = function(){
                    menuItem.controller.activate();
                }
            });
            return events;
        },

    });
});



