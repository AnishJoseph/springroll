var Marionette = require('backbone.marionette');
var Application =require('Application');

var MDMMasters = Backbone.Model.extend({
    url : "api/sr/mdm/masters"
});
var MDMData = Backbone.Model.extend({
    url : function(){
        var master = this.get('master');
        this.unset('master');
        return "/api/sr/mdm/data/" + master;

    }
});


var MdmController = Marionette.Object.extend({
    activate: function(master) {
        /* If this is invoked from the menu system then the master will have the value of the master - howver if this is invoked thru routing
           then master will be null - so get the name of the master from Backbone History
         */
        if(master == null || master == undefined) master = Backbone.history.getFragment();
        master = master.substring(master.lastIndexOf('/')+1);
        var  roles = new MDMData({master:master});
        roles.save(null,{
            success: function(model, masterGridData){
                var view = new Application.MasterView({masterGridData : masterGridData, url : '/api/sr/mdm/update', master : master});
                Application.rootView.showBody(view);
                Backbone.history.navigate('master/' + master);
            }
        });
    }
});


var mdmController = new MdmController();

var mdmMasters = new MDMMasters();
var masterRoleRouter = new Marionette.AppRouter();

var deferred = $.Deferred();

$.ajax({
    url: 'api/sr/mdm/masters',
    type: 'GET',
    contentType: 'application/json; charset=utf-8',
    dataType: 'json',
    success: function (masters) {
        var routes = {};
        _.each(masters, function(masterName, index){
            Application.addMenuItem({
                parent: 'Masters',
                name: masterName,
                controller : mdmController,
                index : 4,
                subIndex : index
            });
            routes['master/' + masterName] = 'activate';

        });
        masterRoleRouter.processAppRoutes(mdmController, routes);
        deferred.resolve();
    },
});
Application.addPromise(deferred.promise());