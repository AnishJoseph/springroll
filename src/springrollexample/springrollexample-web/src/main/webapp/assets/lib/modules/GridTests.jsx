import React from 'react';
import Application from 'App';
import GridReportContainer from 'GridReportContainer';
import ReviewLogFormatter from 'ReviewLogFormatter';

function TestGrid1(props) {
    return <GridReportContainer gridName="TestGrid1" parameterFirst={true}/>;
}
function TestGridParamFromSpringrollUser(props) {
    return <GridReportContainer gridName="TestGridParamFromSpringrollUser" parameterFirst={true}/>;
}
function TestGridAllTypes(props) {
    return <GridReportContainer gridName="TestGridAllTypes" parameterFirst={true}/>;
}
function JobDashboard(props) {
    let formatters = {'reviewlog' : ReviewLogFormatter};
    return <GridReportContainer gridName="JobDashboard" formatters={formatters}/>;
}

Application.addMenu({
    title: 'Grid Tests 1',
    index: 1,
    type: "menuitem",
    route: 'grids/TestGrid1',
    component : TestGrid1,
    parentTitle: 'Grid Tests',
    parentIndex : 5
});
Application.addMenu({
    title: 'TestGridParamFromSpringrollUser',
    index: 2,
    type: "menuitem",
    route: 'grids/TestGridParamFromSpringrollUser',
    component : TestGridParamFromSpringrollUser,
    parentTitle: 'Grid Tests',
    parentIndex : 5
});
Application.addMenu({
    title: 'TestGridAllTypes',
    index: 3,
    type: "menuitem",
    route: 'grids/TestGridAllTypes',
    component : TestGridAllTypes,
    parentTitle: 'Grid Tests',
    parentIndex : 5
});

Application.addMenu({
    title: 'JobDashboard',
    index: 6,
    type: "menuitem",
    route: 'grids/JobDashboard',
    component : JobDashboard,
});





