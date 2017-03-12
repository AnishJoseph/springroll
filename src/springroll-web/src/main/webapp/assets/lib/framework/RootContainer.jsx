import Application from 'App';
import { connect } from 'react-redux';
import { AlertFilters, addAlerts, deleteAlert, setAlertFilter } from 'SpringrollActionTypes';
import Root from 'Root.jsx';
import { bindActionCreators } from 'redux';


const mapStateToProps = (state) => {
    switch (state.visibleAlertType) {
        case AlertFilters.ALERT_FILTER_ACTION :
            return {currentAlertsTitle : Application.Localize('ui.alert.action.title'), currentAlerts : state.alerts.actions, alertType : state.visibleAlertType};
        case AlertFilters.ALERT_FILTER_ERROR:
            return {currentAlertsTitle : Application.Localize('ui.alert.errors.title'), currentAlerts : state.alerts.errors, alertType : state.visibleAlertType};
        case AlertFilters.ALERT_FILTER_INFO :
            return {currentAlertsTitle : Application.Localize('ui.alert.info.title'), currentAlerts : state.alerts.info, alertType : state.visibleAlertType};
        case AlertFilters.ALERT_FILTER_NONE :
            return {currentAlertsTitle : 'None', currentAlerts : undefined};
    }
};

const mapDispatchToProps = (dispatch) => bindActionCreators({
    onDeleteAlert : deleteAlert,
    onAddAlerts : addAlerts,
    onHideAlerts : setAlertFilter
}, dispatch);

const RootContainer = connect(mapStateToProps, mapDispatchToProps )(Root);
export default RootContainer;