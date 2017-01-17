var Marionette =  require('backbone.marionette');
var Application = require('Application');

var dashboardView;
var JobDashboardController = Marionette.Object.extend({
    activate: function() {
        dashboardView = new Application.GridView({"gridName":"JobDashboard", "datamassager" : this.dataMassager });
        Application.rootView.showBody(dashboardView);
        Backbone.history.navigate('jobDashboard');
    },
    dataMassager : function(gridReport){
        _.each(gridReport.data, function(row){
            var reviewLogs = row[7];
            var reviewStr = [];
            if(reviewLogs != undefined){
                _.each(reviewLogs, function(reviewLog){
                    reviewStr.push(reviewLog.reviewer + ":" + (reviewLog.approved == true ? "Approved" : "Rejected"));
                });
                row[7] = reviewStr.join(", ");
            }
        });
        return gridReport;
    }
});

var jobDashboardController = new JobDashboardController();

Application.addMenuItem({
    name: 'JobDashboard',
    title: 'ui.JobDashboard',
    controller : jobDashboardController,
    index : 5
});

var JobDashboardRouter = new Marionette.AppRouter({
    controller: jobDashboardController,

    appRoutes: {
        'jobDashboard': 'activate'
    }
});

Application.subscribe('/core/jobstatusupdate', function(message){
    /*  If the dashboard is not currently displayed then just return - when the user chooses to see the dashboard
        (at a later point) the latest data from the server will in any case be fetched - no need to update the data now
     */
    if(dashboardView == undefined || dashboardView.isDestroyed())return;

    /* The dashboard is currently active - send the updated data to the GridView (which expects an array of changes) */
    /* First convert the reviewlog object into a string */
    var reviewStr = [];
    _.each(message.data[0].updatedData[7], function(reviewLog){
        reviewStr.push(reviewLog.reviewer + ":" + (reviewLog.approved == true ? "Approved" : "Rejected"));

    });
    message.data[0].updatedData[7] = reviewStr.join(", ");
    dashboardView.updateData([message.data[0].updatedData]);
});