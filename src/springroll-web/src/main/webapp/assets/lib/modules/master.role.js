var Marionette = require('backbone.marionette');
var Application =require('Application');

var Roles = Backbone.Model.extend({
    url: '/api/sr/masters/roles'
});


var MasterRoleController = Marionette.Object.extend({
    activate: function() {
        var masterGridData = {
            colDefs : [
                {'name' : 'id'},
                {'name' : 'TextCol', "writeable": true, "type" : "text"},
                {'name' : 'DropDownColW', "writeable": true, "type" : "text", "lovList" : [{"name": "name1", "value": "val1"},{"name": "name2", "value": "val2"}] },
                {'name' : 'DropDownCol', "writeable": false, "type" : "text", "lovList" : [{"name": "name1", "value": "val1"},{"name": "name2", "value": "val2"}] },
                {'name' : 'DateCol', "writeable": true, "type" : "date"},
                {'name' : 'NumberCol', "writeable": true, "type" : "num"},
                {'name' : 'BooleanCol', "writeable": true, "type" : "boolean", "lovList" : [{"name": "Yes", "value": true},{"name": "No", "value": false}] },

            ],
            data : [
                [1, "r1c2", "val2","val2","r1c5",100, true],
                [2, "r1c2", "val2","val2","r2c5",200, false],
                //[3, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[4, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[5, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[6, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[7, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[8, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[9, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[10, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[11, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[12, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[13, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[14, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[15, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[16, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[17, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[18, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[19, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[20, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[21, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[22, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[23, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[24, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[25, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[26, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[27, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[28, "r1c2", "val2","val2","r2c5","r2c6", false],
                //[29, "r1c2", "val2","val2","r2c5","r2c6", false]
            ]
        };
        var view = new Application.MasterView({masterGridData : masterGridData, url : '/api/sr/masters/roles/update'});
        Application.rootView.showBody(view);
        Backbone.history.navigate('master/role');
        //var  roles = new Roles();
        //var that = this;
        //roles.save(null,{
        //    success: function(model, response){
        //        var view = new RoleView({masterGridData : response});
        //        Application.rootView.showBody(view);
        //        Backbone.history.navigate('master/role');
        //    }
        //});
    }
});

var masterRoleController = new MasterRoleController();

Application.addMenuItem({
    parent: 'Masters',
    name: 'Role',
    controller : masterRoleController,
    index : 4,
    subIndex : 1
});

var MasterRoleRouter = new Marionette.AppRouter({
    controller: masterRoleController,

    appRoutes: {
        'master/role': 'activate'
    }
});


