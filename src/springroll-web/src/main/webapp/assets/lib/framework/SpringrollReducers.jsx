import { combineReducers } from 'redux';
import { AlertActions, AlertFilters, USER_CHANGED, MdmActions, GridReportActions} from 'SpringrollActionTypes'

function dedup(array1, array2){
    let allAlerts = array1.concat(array2);
    let existingAlertIds = [];
    let uniqAlerts = _.filter(allAlerts, function(alert){
        if(existingAlertIds.includes(alert.id)) return false;
        existingAlertIds.push(alert.id);
        return true;
    });
    return uniqAlerts;

}
function alertsReducer(state = {actions : [], info : [], errors : [], visibleAlertType : AlertFilters.ALERT_FILTER_NONE}, action) {
    switch (action.type) {
        case AlertActions.ADD_ACTION_ALERTS:
            return Object.assign({}, state, { actions :  dedup(state.actions, action.alerts)});
        case AlertActions.ADD_ERROR_ALERTS:
            return Object.assign({}, state, { errors :  dedup(state.errors, action.alerts)});
        case AlertActions.ADD_INFO_ALERTS:
            return Object.assign({}, state, { info :  dedup(state.info, action.alerts)});
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
function gridReportReducer(state = {}, action) {
    switch (action.type) {
        case GridReportActions.GRID_REPORT_PARAMS_RECEIVED:  {
            let gridInfoAboutThisGrid = state[action.gridName] || {};
            let updatedGridInfoAboutThisGrid = Object.assign({}, gridInfoAboutThisGrid, { params :  action.gridParams, gridName : action.gridName});
            let newState = Object.assign({}, state);
            newState[action.gridName] = updatedGridInfoAboutThisGrid;
            return Object.assign({}, state, newState);
        }
        case GridReportActions.GRID_REPORT_DATA_RECEIVED: {
            let gridInfoAboutThisGrid = state[action.gridName] || {};
            let that = this;
            let rows = _.map(action.gridData.data, function (rowData) {
                var row = {};
                for (var j = 0; j < action.gridData.columns.length; ++j) {
                    if (rowData[j] == undefined || rowData[j] == null) continue;
                    row[action.gridData.columns[j].title] = rowData[j];
                }
                return row;
            });
            action.gridData.data = rows;
            let updatedGridInfoAboutThisGrid = Object.assign({}, gridInfoAboutThisGrid, {gridData: action.gridData});
            let newState = Object.assign({}, state);
            newState[action.gridName] = updatedGridInfoAboutThisGrid;
            return Object.assign({}, state, newState);
        }
        case GridReportActions.GRID_REPORT_DATA_UPDATE_RECEIVED: {
            let gridInfoAboutThisGrid = state[action.gridName];
            if (gridInfoAboutThisGrid == undefined) {
                console.log("Received Update for Grid '" + action.gridName + "' but no data available for the grid as yet - discarding the update");
                return state;
            }

            let idOfRowThatChanged = action.updatedData[0];

            /* The incoming data is just an array of the data - we need to convert to JSON */
            let newRow = {};
            _.each(action.updatedData, function (colValue, index) {
                if (colValue == undefined || colValue == null) return;
                newRow[gridInfoAboutThisGrid.gridData.columns[index].title] = colValue;
            });


            let isNewRow = true;
            let newData = _.map(gridInfoAboutThisGrid.gridData.data, row => {
                if (row['ID'] === idOfRowThatChanged) {
                    isNewRow = false;
                    return newRow;
                }
                return row;
            });
            if (isNewRow) newData.push(newRow);

            let newGridData = Object.assign({}, gridInfoAboutThisGrid.gridData, {data: newData});
            let newGridInfo = Object.assign({}, gridInfoAboutThisGrid, {gridData: newGridData});
            let newState = Object.assign({}, state);
            newState[action.gridName] = newGridInfo;
            return Object.assign({}, state, newState);
        }
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
    gridReports : gridReportReducer
});


export default springrollReducers;