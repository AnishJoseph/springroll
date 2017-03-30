import Application from 'App';
import React from 'react';
import { connect } from 'react-redux';

import { gridParamRequest, gridDataRequest} from 'SpringrollActionTypes';

import GridReport from 'GridReport.jsx';
import { bindActionCreators } from 'redux';
import { Router, Route } from 'react-router'


const mapStateToProps = (state, ownProps) => {
    let params =  state.gridReports[ownProps.gridName] === undefined ? [] : state.gridReports[ownProps.gridName].params;
    let gridData =  state.gridReports[ownProps.gridName] === undefined ? undefined : state.gridReports[ownProps.gridName].gridData;
    return Object.assign({gridParams : params, gridData : gridData}, ownProps);
};

const mapDispatchToProps = (dispatch) => bindActionCreators({
    onGridParamRequest : gridParamRequest,
    onGridDataRequest : gridDataRequest,
}, dispatch);

const GridReportContainer = connect(mapStateToProps, mapDispatchToProps)(GridReport);
export default GridReportContainer;
