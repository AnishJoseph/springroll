import { combineReducers } from 'redux';
import { AlertActions, AlertFilters} from 'SpringrollActionTypes'

function alertsReducer(state = {actions : [], info : [], errors : []}, action, visibleAlertType :AlertFilters.ALERT_FILTER_NONE ) {
    switch (action.type) {
        case AlertActions.ADD_ACTION_ALERTS:
            return Object.assign({}, state, { actions :  state.actions.concat(action.alerts)});
        case AlertActions.ADD_ERROR_ALERTS:
            return Object.assign({}, state, { errors :  state.errors.concat(action.alerts)});
        case AlertActions.ADD_INFO_ALERTS:
            return Object.assign({}, state, { info :  state.info.concat(action.alerts)});
        case AlertActions.ALERT_DELETE:
            switch (action.alertType) {
                case AlertFilters.ALERT_FILTER_ACTION:
                    return Object.assign({}, state, { actions :  removeAlert(state.actions, action.id)});
                case AlertFilters.ALERT_FILTER_ERROR:
                    return Object.assign({}, state, { errors :  removeAlert(state.errors, action.id)});
                case AlertFilters.ALERT_FILTER_INFO:
                    return Object.assign({}, state, { info :  removeAlert(state.info, action.id)});
            }
        case AlertActions.SET_ALERT_FILTER:
            return Object.assign({}, state, { visibleAlertType :  action.alertFilter});
        default:
            return state;
    }
}

function removeAlert(alertCollection, alertId){
    return alertCollection.filter(alert => (alert.id !== alertId));
}

const springrollReducers = combineReducers({
    alerts : alertsReducer,
});


export default springrollReducers;