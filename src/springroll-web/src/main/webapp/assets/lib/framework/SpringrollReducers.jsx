import { combineReducers } from 'redux';
import { AlertActions, AlertFilters, USER_CHANGED, MdmActions} from 'SpringrollActionTypes'

function alertsReducer(state = {actions : [], info : [], errors : [], visibleAlertType : AlertFilters.ALERT_FILTER_NONE}, action) {
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
function userReducer(state = {}, action) {
    switch (action.type) {
        case USER_CHANGED:
            return action.user;
        default:
            return state;
    }
}
function removeAlert(alertCollection, alertId){
    return alertCollection.filter(alert => (alert.id !== alertId));
}

function mdmReducer(state = {}, action) {
    switch (action.type) {
        case MdmActions.MDM_MASTER_METADATA_RECEIVED:
            return Object.assign({}, state, { masterDefns :  action.masterDefns});
        case MdmActions.MDM_MASTER_DATA_RECEIVED:
            let updatedMasterData = {recIdsUnderReview : action.masterData.recIdsUnderReview,
                data : action.masterData.data,
                colDefs : action.masterData.colDefs,
                master : action.masterData.master
            };
            return Object.assign({}, state, { masterData :  updatedMasterData, updateStatus: undefined});
        case MdmActions.MDM_MASTER_UPDATE_COMPLETE:
            return Object.assign({}, state, { updateStatus :  action.status, updateResponse : action.response});
        default:
            return state;
    }
}

const springrollReducers = combineReducers({
    alerts : alertsReducer,
    user : userReducer,
    mdm : mdmReducer,
});


export default springrollReducers;