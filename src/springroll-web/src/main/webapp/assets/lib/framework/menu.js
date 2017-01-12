require('bootstrap-loader');
var Marionette = require('backbone.marionette');
var Application =require('Application');

Application.MenuView = Marionette.View.extend({
    tagName: 'div',
    template: function() {
        var template = [];

        template.push('<nav class="navbar navbar-default spaRootElement">');
        template.push('<div class="container-fluid">');
        template.push('<div id="navbar" class="navbar-collapse collapse"> ');

        template.push('<ul class="nav navbar-nav nav-pills">');
        /* Sort the menuItems by index before adding the menu item */
        _.each(_.sortBy(Application.getMenuItems(), 'index'), function (item) {
            if ($.isArray(item.items)) {
                template.push('<li role="presentation" class="dropdown"> ');
                template.push('<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">' + Localize(item.items[0].parent) + '<span class="caret"></span></a> ');
                template.push('<ul class="dropdown-menu">');
                _.each(_.sortBy(item.items, 'subIndex'), function (menuItem) {
                    var id = 'menuId' + menuItem.name.replace(/\./g,'');
                    template.push('<li role="presentation"><a id="' + id + '">' + Localize(menuItem.name) + '</a></li>');
                });
                template.push(' </ul> </li>');
            } else {
                var menuItem = item.items;
                var id = 'menuId' + menuItem.name.replace(/\./g,'');
                template.push('<li role="presentation"><a id="' + id + '">' + Localize(menuItem.name) + '</a></li>');
            }
        });
        template.push('</ul>');
        template.push('<p data-toggle="tooltip" title="' + Localize('ui.logout') + '" class="navbar-text navbar-right glyphicon glyphicon-eject" aria-hidden="true" id="logout"/>');

        if (Application.user.delegators == null || Application.user.delegators.length == 0) {
            template.push('<p class="navbar-text navbar-right ">' + Application.user.displayName + '</p>');
        } else {
            template.push('<ul class="nav navbar-nav nav-pills navbar-right">');
            template.push('<li role="presentation" class="dropdown"> ');
            template.push('<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">' + Application.user.displayName + '<span class="caret"></span></a> ');
            template.push('<ul class="dropdown-menu">');
            _.each(Application.user.delegators, function (delegator) {
                var id = 'delegator' + delegator;
                template.push('<li role="presentation"><a id="' + id + '">' + delegator + '</a></li>');
            });
            template.push('</ul></li> </ul>');
        }

        template.push('</div></div></nav>');
        var  menuTemplate = template.join("");
        return menuTemplate;

    },
    events : function(){
        var events = {};
        _.each(Application.getMenuItems(), function(item){
            if($.isArray(item.items)){
                _.forEach(item.items, function(menuItem){
                    var id = 'click #menuId' + menuItem.name.replace(/\./g,'');
                    events[id] = function () {
                        menuItem.controller.activate(menuItem.name);
                    }
                });
            } else {
                var menuItem = item.items;
                var id = 'click #menuId' + menuItem.name.replace(/\./g,'');
                events[id] = function () {
                    menuItem.controller.activate(menuItem.name);
                }
            }


        });
        events['click #logout'] = function(){
            $.ajax({
                type: "POST",
                url: "/logout",
            });
            window.location.href = '/';
        }
        _.each(Application.user.delegators, function(delegator){
            var id = 'click #delegator' + delegator;
            events[id] = function(){
                if(Application.user.delegator != null && Application.user.delegator == delegator){
                    window.location.href = 'logout/impersonate';
                } else {
                    window.location.href = 'login/impersonate?username=' + delegator;
                }
            }
        });

        return events;
    },

});



