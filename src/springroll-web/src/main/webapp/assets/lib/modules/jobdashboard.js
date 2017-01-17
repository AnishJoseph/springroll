var Marionette =  require('backbone.marionette');
var Application = require('Application');

var dashboardView;
var indexOfReviewLog = undefined;
var JobDashboardController = Marionette.Object.extend({
    activate: function() {
        dashboardView = new Application.GridView({"gridName":"JobDashboard", "datamassager" : this.dataMassager });
        Application.rootView.showBody(dashboardView);
        Backbone.history.navigate('jobDashboard');
    },
    dataMassager : function(gridReport){
        for(var i = 0; i < gridReport.columns.length; i++){
            var column = gridReport.columns[i];
            if(column.type == 'reviewlog'){
                column.type = 'text';
                indexOfReviewLog = i;
                break;
            }
        }
        if(indexOfReviewLog == undefined)return gridReport;

        _.each(gridReport.data, function(row){
            var reviewLogs = row[indexOfReviewLog];
            if(reviewLogs == undefined)return;
            row[indexOfReviewLog] = convertReviewLogToStr(reviewLogs);
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

function convertReviewLogToStr(reviewLogs){
    var reviewStr = [];
    _.each(reviewLogs, function(reviewLog){
        reviewStr.push(reviewLog.reviewer + ":" + (reviewLog.approved == true ? "Approved" : "Rejected"));
    });
    return reviewStr.join(", ");
}
Application.subscribe('/core/jobstatusupdate', function(message){
    /*  If the dashboard is not currently displayed then just return - when the user chooses to see the dashboard
        (at a later point) the latest data from the server will in any case be fetched - no need to update the data now
     */
    if(dashboardView == undefined || dashboardView.isDestroyed())return;

    /* First convert the reviewlog object into a string IF necessary */
    if(indexOfReviewLog != undefined) {
        var reviewLogs = message.data[0].updatedData[indexOfReviewLog];
        if(reviewLogs != undefined)
            message.data[0].updatedData[indexOfReviewLog] = convertReviewLogToStr(reviewLogs);
    }

    /* The dashboard is currently active - send the updated data to the GridView (which expects an array of changes) */
    dashboardView.updateData([message.data[0].updatedData]);
});