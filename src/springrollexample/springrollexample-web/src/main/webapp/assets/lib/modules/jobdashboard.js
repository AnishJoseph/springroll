var Marionette =  require('backbone.marionette');
var Application = require('Application');

var dashboardView;
var JobDashboardController = Marionette.Object.extend({
    activate: function() {
        dashboardView = new Application.GridView({"gridName":"JobDashboard" });
        Application.rootView.showBody(dashboardView);
        Backbone.history.navigate('jobDashboard');
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
    dashboardView.updateData([message.data[0].updatedData]);
});