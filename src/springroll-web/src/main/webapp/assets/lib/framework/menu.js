require('bootstrap-loader');
var Marionette = require('backbone.marionette');
var Application =require('Application');
var Radio = require('backbone.radio');

Application.MenuView = Marionette.View.extend({
    tagName: 'div',
    initialize :  function(){
        this.alertChannel = Radio.channel('AlertChannel');
    },

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
                    var id = 'menuId' + menuItem.name;
                    template.push('<li role="presentation"><a id="' + id + '">' + Localize(menuItem.title) + '</a></li>');
                });
                template.push(' </ul> </li>');
            } else {
                var menuItem = item.items;
                var id = 'menuId' + menuItem.name;
                template.push('<li role="presentation"><a id="' + id + '">' + Localize(menuItem.title) + '</a></li>');
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
        template.push('<div class="alertInfo navbar-right" data-toggle="tooltip" title="' + Localize('ui.view.info') + '">');
            template.push('<span class="alertLabel  glyphicon glyphicon-info-sign" aria-hidden="true"></span>');
            template.push('<span class="alertInfoCount alertCount">0</span>');
        template.push('</div>');

        template.push('<div class="alertError navbar-right" data-toggle="tooltip" title="' + Localize('ui.view.errors') + '">');
            template.push('<span class="alertLabel  glyphicon glyphicon-warning-sign" aria-hidden="true"/>');
            template.push('<span class="alertErrorCount alertCount">0</span>');
        template.push('</div>');

        template.push('<div class="alertAction navbar-right" data-toggle="tooltip" title="' + Localize('ui.view.approvals') + '">');
            template.push('<span  class="alertLabel   glyphicon glyphicon-ok-sign" aria-hidden="true"/>');
            template.push('<span class="alertActionCount alertCount">0</span>');
        template.push('</div>');

        template.push('</div></div></nav>');
        var  menuTemplate = template.join("");
        return menuTemplate;

    },

    ui : {
        alertInfoCount    : ".alertInfoCount",
        alertErrorCount : ".alertErrorCount",
        alertActionCount  : ".alertActionCount"
    },

    onRender : function(){
        var that = this;
        this.alertChannel.on('alert:action:count:changed', function(count) {
            that.ui.alertActionCount.html(count);
            if(count > 0){
                if(!that.ui.alertActionCount.hasClass('alertPresent')) that.ui.alertActionCount.addClass('alertPresent');
            } else {
                that.ui.alertActionCount.removeClass('alertPresent');
            }
        });
        this.alertChannel.on('alert:error:count:changed', function(count) {
            that.ui.alertErrorCount.html(count);
            if(count > 0){
                if(!that.ui.alertErrorCount.hasClass('alertPresent')) that.ui.alertErrorCount.addClass('alertPresent');
            } else {
                that.ui.alertErrorCount.removeClass('alertPresent');
            }
        });
        this.alertChannel.on('alert:info:count:changed', function(count) {
            that.ui.alertInfoCount.html(count);
            if(count > 0){
                if(!that.ui.alertInfoCount.hasClass('alertPresent')) that.ui.alertInfoCount.addClass('alertPresent');
            } else {
                that.ui.alertInfoCount.removeClass('alertPresent');
            }
        });
    },

    events : function(){
        var events = {};
        _.each(Application.getMenuItems(), function(item){
            if($.isArray(item.items)){
                _.forEach(item.items, function(menuItem){
                    var id = 'click #menuId' + menuItem.name;
                    events[id] = function () {
                        menuItem.controller.activate(menuItem.name);
                    }
                });
            } else {
                var menuItem = item.items;
                var id = 'click #menuId' + menuItem.name;
                events[id] = function () {
                    menuItem.controller.activate(menuItem.name);
                }
            }


        });

        events['click .alertInfo'] = function(){
            this.alertChannel.trigger('alert:info:clicked');
        };
        events['click .alertError'] = function(){
            this.alertChannel.trigger('alert:error:clicked');
        };
        events['click .alertAction'] = function(){
            this.alertChannel.trigger('alert:action:clicked');
        };
        events['click #logout'] = function(){
            $.ajax({
                type: "POST",
                url: "/logout",
            });
            window.location.href = '/';
        };
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
    }

});



