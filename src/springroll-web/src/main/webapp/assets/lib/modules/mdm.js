var Marionette = require('backbone.marionette');
var Application =require('Application');

var MDMData = Backbone.Model.extend({
    url : function(){
        var master = this.get('master');
        this.unset('master');
        return "/api/sr/mdm/" + master;

    }
});


var MdmController = Marionette.Object.extend({
    activate: function(master) {
        /* If this is invoked from the menu system then the master will have the value of the master - howver if this is invoked thru routing
           then master will be null - so get the name of the master from Backbone History
         */
        if(master == null || master == undefined) master = Backbone.history.getFragment();
        master = master.substring(master.lastIndexOf('/')+1);
/*
        var masterGridData = {
            colDefs : [
                {'name' : 'id'},
                {'name' : 'TextCol', "writeable": true, "type" : "text"},
                {'name' : 'DropDownColW', "writeable": true, "type" : "text", "lovList" : [{"name": "name1", "value": "val1"},{"name": "name2", "value": "val2"}] },
                {'name' : 'DropDownCol', "writeable": false, "type" : "text", "lovList" : [{"name": "name1", "value": "val1"},{"name": "name2", "value": "val2"}] },
                {'name' : 'DateCol', "writeable": true, "type" : "date"},
                {'name' : 'NumberCol', "writeable": true, "type" : "num", defaultValue: 1},
                {'name' : 'BooleanCol', "writeable": true, "type" : "boolean", "lovList" : [{"name": "Yes", "value": true},{"name": "No", "value": false}], defaultValue:true },

            ],
            data : [
                [1, "r1c2", "val2","val2","25/12/1985",100, true],
                [2, "r1c2", "val2","val2","15/12/1985",200, false],
            ]
        };
*/
        var  roles = new MDMData({master:master});
        roles.save(null,{
            success: function(model, masterGridData){
                var view = new Application.MasterView({masterGridData : masterGridData, url : '/api/sr/masters/roles/update', master : master});
                Application.rootView.showBody(view);
                Backbone.history.navigate('master/role');
            }
        });
    }
});


var mdmController = new MdmController();

var masters = ['role', 'reviewrules'];
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

var MasterRoleRouter = new Marionette.AppRouter({
    controller: mdmController,

    appRoutes: routes
});


