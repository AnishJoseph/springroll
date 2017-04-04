import React from 'react';
import Application from 'App';
import GridReportContainer from 'GridReportContainer';
import ReviewLogFormatter from 'ReviewLogFormatter';
import { gridDataUpdateReceived} from 'SpringrollActionTypes'

function jobDashboardUpdated(receivedPushData) {
    return gridDataUpdateReceived(receivedPushData.data[0].updatedData, 'JobDashboard');
}

function JobDashboard(props) {
    let formatters = {'reviewlog' : ReviewLogFormatter};
    return <GridReportContainer gridName="JobDashboard" formatters={formatters}/>;
}

Application.addMenu({
    title: 'JobDashboard',
    index: 6,
    type: "menuitem",
    route: 'JobDashboard',
    component : JobDashboard
});

Application.subscribeToPushTopic('/core/jobstatusupdate', jobDashboardUpdated);
