import Application from 'App';
import React from 'react';
import { connect } from 'react-redux';
import { mdmModuleActivated, mdmMasterChosen, mdmMasterChanged, mdmMasterUpdateRow, mdmMasterAddRow, mdmMasterDeleteRow, mdmMasterUpdateStarted} from 'SpringrollActionTypes';
import MDM from 'mdm/MDM.jsx';
import { bindActionCreators } from 'redux';
import { Router, Route } from 'react-router'


const mapStateToProps = (state) => {
    return {
        masterDefns : state.mdm.masterDefns,
        masterData : state.mdm.masterData,
        updateResponse : state.mdm.updateResponse,
        updateInProgress : state.mdm.updateInProgress,
        newRowData : state.mdm.newRowData,
        changedRowData : state.mdm.changedRowData
    };
};

const mapDispatchToProps = (dispatch) => bindActionCreators({
    onMdmModuleActivated : mdmModuleActivated,
    onMdmMasterChosen : mdmMasterChosen,
    onMdmMasterChanged : mdmMasterChanged,
    onMdmMasterRowChanged : mdmMasterUpdateRow,
    onMdmMasterAddRow : mdmMasterAddRow,
    onMdmMasterDeleteRow : mdmMasterDeleteRow,
    onMdmMasterUpdateStarted : mdmMasterUpdateStarted

}, dispatch);

const MDMContainer = connect(mapStateToProps, mapDispatchToProps)(MDM);
export default MDMContainer;

/*  This is to handle the case in MDM where the number of MDM masters are unknown at compile time.
    Couple of points here - regardless of the MDM master chosen the component is always MDMContainer.
    Here we define the routes but the actual push of the route happens in MDM.jsx when a master is chosen. The route
    itself is changed using router.push - this.props.router.push('/mdm/' + masterName);
 */

const routes = (
    <Route key="mdm" path="/mdm" component={MDMContainer}>
        <Route key="mdmMaster" path="/mdm/:master" component={MDMContainer}/>
    </Route>
);

Application.addMenu({
    title: 'ui.menu.mdm',
    index: 4,
    type: "menuitem",
    route : routes,
    component : MDMContainer,
    routeOnClick: 'mdm'
});