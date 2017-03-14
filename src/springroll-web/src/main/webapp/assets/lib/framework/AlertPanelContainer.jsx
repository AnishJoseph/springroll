import Application from 'App';
import { connect } from 'react-redux';
import { AlertFilters, setAlertFilter, dismissAlert, sendToServerAndDismissAlert } from 'SpringrollActionTypes';
import AlertPanel from 'AlertPanel.jsx';
import { bindActionCreators } from 'redux';


const mapStateToProps = (state) => {
    switch (state.alerts.visibleAlertType) {
        case AlertFilters.ALERT_FILTER_ACTION :
            return {currentAlertsTitle : Application.Localize('ui.alert.action.title'), currentAlerts : state.alerts.actions, alertType : state.alerts.visibleAlertType};
        case AlertFilters.ALERT_FILTER_ERROR:
            return {currentAlertsTitle : Application.Localize('ui.alert.errors.title'), currentAlerts : state.alerts.errors, alertType : state.alerts.visibleAlertType};
        case AlertFilters.ALERT_FILTER_INFO :
            return {currentAlertsTitle : Application.Localize('ui.alert.info.title'), currentAlerts : state.alerts.info, alertType : state.alerts.visibleAlertType};
        case AlertFilters.ALERT_FILTER_NONE :
            return {currentAlertsTitle : 'None', currentAlerts : undefined};
    }
};

const mapDispatchToProps = (dispatch) => bindActionCreators({
    onHideAlerts : setAlertFilter,
    onDismissAlert : dismissAlert,
    onSendToServerAndDismissAlert : sendToServerAndDismissAlert
}, dispatch);

const AlertPanelContainer = connect(mapStateToProps, mapDispatchToProps )(AlertPanel);
export default AlertPanelContainer;