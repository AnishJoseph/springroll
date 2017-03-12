import { combineReducers } from 'redux';
import { AlertActions, AlertFilters, SET_ALERT_FILTER} from 'SpringrollActionTypes'

function alertsReducer(state = {actions : [], info : [], errors : []}, action) {
    switch (action.type) {
        case AlertActions.ACTION_ALERTS:
            return Object.assign({}, state, { actions :  state.actions.concat(action.alerts)});
        case AlertActions.ERROR_ALERTS:
            return Object.assign({}, state, { actions :  state.errors.concat(action.alerts)});
        case AlertActions.INFO_ALERTS:
            return Object.assign({}, state, { actions :  state.info.concat(action.alerts)});
        case AlertActions.ALERT_DELETE:
            switch (action.alertType) {
                case AlertFilters.ALERT_FILTER_ACTION:
                    return Object.assign({}, state, { actions :  removeAlert(state.actions, action.id)});
                case AlertFilters.ALERT_FILTER_ERROR:
                    return Object.assign({}, state, { errors :  removeAlert(state.errors, action.id)});
                case AlertFilters.ALERT_FILTER_INFO:
                    return Object.assign({}, state, { info :  removeAlert(state.info, action.id)});
            }
        default:
            return state;
    }
}

function removeAlert(alertCollection, alertId){
    return alertCollection.filter(alert => (alert.id !== alertId));
}

function setVisibleAlert(state = AlertFilters.ALERT_FILTER_NONE, action) {
    switch (action.type) {
        case SET_ALERT_FILTER:
            return action.alertFilter;
        default:
            return state;
    }
}

const springrollReducers = combineReducers({
    alerts : alertsReducer,
    visibleAlertType : setVisibleAlert
});


export default springrollReducers;