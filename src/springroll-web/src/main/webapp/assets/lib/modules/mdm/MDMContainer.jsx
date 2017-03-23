import Application from 'App';
import React from 'react';
import { connect } from 'react-redux';
import { mdmModuleActivated, mdmMasterChosen, mdmMasterChanged} from 'SpringrollActionTypes';
import MDM from 'mdm/MDM.jsx';
import { bindActionCreators } from 'redux';
import { Router, Route } from 'react-router'


const mapStateToProps = (state) => {
    return {masterDefns : state.mdm.masterDefns, masterData : state.mdm.masterData, updateResponse : state.mdm.updateResponse, updateStatus : state.mdm.updateStatus};
};

const mapDispatchToProps = (dispatch) => bindActionCreators({
    onMdmModuleActivated : mdmModuleActivated,
    onMdmMasterChosen : mdmMasterChosen,
    onMdmMasterChanged : mdmMasterChanged
}, dispatch);

const MDMContainer = connect(mapStateToProps, mapDispatchToProps)(MDM);
export default MDMContainer;

const routes = (<Route key="mdm" path="/mdm" component={MDMContainer}>
        <Route key="mm" path="/mdm/:master" component={MDMContainer}/>
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