import React from 'react';
import Application from 'App';
import GridReportContainer from 'GridReportContainer';
import {ReviewLogFormatterForDisplay, ReviewLogFormatterForExport} from 'ReviewLogFormatter';
import { gridDataUpdateReceived} from 'SpringrollActionTypes'

function jobDashboardUpdated(receivedPushData) {
    return gridDataUpdateReceived(receivedPushData.data[0].updatedData, 'JobDashboard');
}

function JobDashboard(props) {
    let options = {cache : true, formatters : {'reviewers' : ReviewLogFormatterForDisplay}, 'exportFormatters' : {'reviewers' : ReviewLogFormatterForExport}};
    return <GridReportContainer gridName="JobDashboard" options={options}/>;
}

Application.addMenu({
    title: 'JobDashboard',
    index: 6,
    type: "menuitem",
    route: 'JobDashboard',
    component : JobDashboard
});

Application.subscribeToPushTopic('/core/jobstatusupdate', jobDashboardUpdated);
