import { combineReducers } from 'redux';
import { AlertActions, AlertFilters, USER_CHANGED, MdmActions, GridReportActions} from 'SpringrollActionTypes'
import { reducer as formReducer } from 'redux-form'

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

function mdmReducer(state = {updateInProgress: false}, action) {
    switch (action.type) {
        case MdmActions.MDM_MASTER_SWITCHING:
            return Object.assign({}, state, { updateInProgress: false, updateResponse : undefined, changedRowData : undefined, newRowData : undefined});
        case MdmActions.MDM_MASTER_METADATA_RECEIVED:
            return Object.assign({}, state, { masterDefns :  action.masterDefns});
        case MdmActions.MDM_MASTER_DATA_RECEIVED:

            let that = this;
            let rows = _.map(action.masterData.data, function (rowData, index) {
                var row = {};
                for (var j = 0; j < action.masterData.colDefs.length; ++j) {
                    if (rowData[j] == undefined || rowData[j] == null) continue;
                    row[action.masterData.colDefs[j].name] = rowData[j];
                }
                row['cid'] = index;
                return row;
            });

            let updatedMasterData = {
                recIdsUnderReview : action.masterData.recIdsUnderReview,
                data : rows,
                colDefs : action.masterData.colDefs,
                master : action.masterData.master
            };
            return Object.assign({}, state, { masterData :  updatedMasterData});
        case MdmActions.MDM_MASTER_UPDATE_COMPLETE: {
            let hasErrors = false;
            let errors = {};
            _.each(action.response || [], error => {
                let existingErrorsForRow = errors[error.cookie] || {};
                existingErrorsForRow[error.field] = error.message;
                errors[error.cookie] = existingErrorsForRow;
                hasErrors = true;
            });
            let newState =  {updateInProgress: false, updateResponse :  errors}

            if(!hasErrors){
                newState['newRowData'] = undefined;
                newState['changedRowData'] = undefined;
            }

            return Object.assign({}, state, newState);
        }
        case MdmActions.MDM_MASTER_UPDATE_STARTED: {
            return Object.assign({}, state, {updateInProgress: true});
        }
        case MdmActions.MDM_MASTER_UPDATE_ROW:
            let update = {};

            if(action.row['id'] === -1 ) {
                /* If this is a new row then update the newRowData for this row */
                let updatedNewRowData = Object.assign({}, state.newRowData);
                let updatedDataForThisNewRow = updatedNewRowData[action.row['cid']];
                updatedDataForThisNewRow[action.row.cellName] = action.row.cellValue;
                update['newRowData'] = updatedNewRowData;
            } else {
                /* The update is for an existing row */
                let updatedChangedRowData = Object.assign({}, state.changedRowData);
                let existingChangesForId = updatedChangedRowData[action.row['cid']] || { id : state.masterData.data[action.row['cid']]['id']};
                let existingChangesForThisFld = existingChangesForId[action.row.cellName];

                if(existingChangesForThisFld == undefined){
                    existingChangesForThisFld = {prevVal : state.masterData.data[action.row['cid']][action.row.cellName]}
                }
                existingChangesForThisFld['val'] = action.row.cellValue
                existingChangesForId[action.row.cellName] = existingChangesForThisFld;
                updatedChangedRowData[action.row['cid']] =  existingChangesForId;

                /* Now check if the user has put back the original value */
                if(existingChangesForThisFld['prevVal'] === existingChangesForThisFld['val']){
                    /* Ah hah - the user put back the original value */
                    delete existingChangesForId[action.row.cellName];
                    if(Object.keys(existingChangesForId).length === 1){
                        /* there are no changes for this row - remove the row itself */
                        delete updatedChangedRowData[action.row['cid']];
                    }
                }
                update['changedRowData'] = Object.keys(updatedChangedRowData).length === 0? undefined : updatedChangedRowData;
            }

            /*  update the data with the changed data */
            let newRows = state.masterData.data.slice();
            let row = newRows[action.row['cid']];
            row[action.row.cellName] = action.row.cellValue;
            let updatedRowData = Object.assign({}, state.masterData, {data: newRows});

            update['masterData'] =  updatedRowData;

            return Object.assign({}, state, update);
        case MdmActions.MDM_MASTER_ADD_ROW: {
            /* When a new row is added we need to do 2 things
                1) Create an object representing the new row - fill in this object with default values, if any and
                   set the cid to the length of data as we are going to add this object to the end of the data array
                2) add the object to the newRowData - needed later when save happens to push this to the server - maybe
                   we should not duplicate - need to think about this - FIXME
             */
            let newRows = state.masterData.data.slice();
            let newRowData = {id: -1, cid: newRows.length, rowIsNew: true};
            _.each(state.masterData.colDefs, colDef => {
                if(colDef.defVal !== undefined && colDef.defVal !== null){
                    newRowData[colDef.name] = colDef.defVal;
                }
            });
            newRows.push(newRowData);
            let updatedRowData = Object.assign({}, state.masterData, {data: newRows});


            let updatedNewRowData = Object.assign({}, state.newRowData || {});
            updatedNewRowData[newRowData['cid']] =  newRowData;
            return Object.assign({}, state, {masterData: updatedRowData, newRowData : updatedNewRowData});
        }
        case MdmActions.MDM_MASTER_DELETE_ROW: {
            /* First delete the row in the data */
            let rows = state.masterData.data.slice();
            rows[action.cid] = null;
            let updatedRowData = Object.assign({}, state.masterData, {data: rows});

            /* Now delete the row from the newRowData */
            let updatedNewRowData = Object.assign({}, state.newRowData);
            delete updatedNewRowData[action.cid];
            /* if there are no more new rows then set newRowData to undefined */
            if(Object.keys(updatedNewRowData).length === 0)
                updatedNewRowData = undefined;

            console.log(updatedNewRowData);
            return Object.assign({}, state, {masterData: updatedRowData, newRowData : updatedNewRowData});
        }
        default:
            return state;
    }
}

const springrollReducers = combineReducers({
    alerts : alertsReducer,
    user : userReducer,
    mdm : mdmReducer,
    gridReports : gridReportReducer,
    form: formReducer
});


export default springrollReducers;