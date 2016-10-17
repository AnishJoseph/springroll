define(['Application', 'marionette'], function (Application, Marionette) {
    //var xx = '<ul class="nav nav-pills"> <li role="presentation"><a href="#" id="menuIdItem1">Item1</a></li> <li role="presentation"><a href="#" id="menuIdItem2">Item2</a></li> <li role="presentation" class="dropdown"> <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Item3<span class="caret"></span></a> <ul class="dropdown-menu"> <li role="presentation"><a href="#" id="menuIdsubItem1">subItem1</a></li> <li role="presentation"><a href="#" id="menuIdsubItem2">subItem2</a></li> </ul> </li> </ul>';
    Application.MenuView = Marionette.View.extend({
        tagName: 'div',
        template: function(){
            var template = [];

            template.push('<nav class="navbar navbar-default">');
            template.push('<div class="container-fluid">');
            template.push('<div id="navbar" class="navbar-collapse collapse"> ');

            template.push('<ul class="nav navbar-nav nav-pills">');
            /* Sort the menuItems by index before adding the menu item */
            _.each(_.sortBy(Application.getMenuItems(), 'index'), function(item){
                if($.isArray(item.items)){
                    template.push('<li role="presentation" class="dropdown"> ');
                    template.push('<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Item3<span class="caret"></span></a> ');
                    template.push('<ul class="dropdown-menu">');
                    _.each(_.sortBy(item.items, 'subIndex'), function(menuItem){
                        var id = 'menuId' + menuItem.name;
                        template.push('<li role="presentation"><a id="' + id + '">' + menuItem.name +'</a></li>');
                    });
                    template.push(' </ul> </li>');
                } else {
                    var menuItem = item.items;
                    var id = 'menuId' + menuItem.name;
                    template.push('<li role="presentation"><a id="' + id + '">' + menuItem.name +'</a></li>');
                }
            });
            template.push('</ul>');
            //template.push('<div class="navbar-text navbar-right ">');
            template.push('<p data-toggle="tooltip" title="' + Localize('logout') + '" class="navbar-text navbar-right glyphicon glyphicon-eject" aria-hidden="true" id="logout"/>');
            //template.push('<p class="navbar-text navbar-right " id="logout">Logout</p>');
            template.push('<p class="navbar-text navbar-right ">' + Application.user.displayName + '</p>');
            //template.push('</div>');
            template.push('</div></div></nav>');
            var  menuTemplate = template.join("");
            return menuTemplate;

        },
        events : function(){
            var events = {};
            _.each(Application.getMenuItems(), function(item){
                if($.isArray(item.items)){
                    _.forEach(item.items, function(menuItem){
                        var id = 'click #menuId' + menuItem.name;
                        events[id] = function () {
                            menuItem.controller.activate();
                        }
                    });
                } else {
                    var menuItem = item.items;
                    var id = 'click #menuId' + menuItem.name;
                    events[id] = function () {
                        menuItem.controller.activate();
                    }
                }
                events['click #logout'] = function(){
                    window.location.href = 'logout';
                }
            });
            return events;
        },

    });
});



