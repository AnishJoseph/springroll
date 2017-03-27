import Application from 'App';
import React from 'react';
import { connect } from 'react-redux';

import { gridParamRequest, gridDataRequest} from 'SpringrollActionTypes';

import GridReport from 'GridReport.jsx';
import { bindActionCreators } from 'redux';
import { Router, Route } from 'react-router'


const mapStateToProps = (state, ownProps) => {
    return Object.assign({gridParams : state.gridReport.params, gridData : state.gridReport.gridData}, ownProps);
};

const mapDispatchToProps = (dispatch) => bindActionCreators({
    onGridParamRequest : gridParamRequest,
    onGridDataRequest : gridDataRequest,
}, dispatch);

const GridReportContainer = connect(mapStateToProps, mapDispatchToProps)(GridReport);
export default GridReportContainer;
