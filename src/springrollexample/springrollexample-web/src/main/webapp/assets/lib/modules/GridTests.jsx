import React from 'react';
import Application from 'App';
import GridReportContainer from 'GridReportContainer';

function TestGrid1(props) {
    var p = {completeStatus : true, userIds : ['ANISH']};
    return <GridReportContainer gridName="TestGrid1" parameterFirst={true} params={p}/>;
}
function TestGridParamFromSpringrollUser(props) {
    return <GridReportContainer gridName="TestGridParamFromSpringrollUser" parameterFirst={true}/>;
}
function TestGridAllTypes(props) {
    return <GridReportContainer gridName="TestGridAllTypes" parameterFirst={true}/>;
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
