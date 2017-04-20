import Application from 'App.js';
import axios from 'axios';

export const USER_CHANGED = 'USER_CHANGED';

export const AlertFilters = {
    ALERT_FILTER_ACTION : 'ALERT_FILTER_ACTION',
    ALERT_FILTER_ERROR  : 'ALERT_FILTER_ERROR',
    ALERT_FILTER_INFO   : 'ALERT_FILTER_INFO',
    ALERT_FILTER_NONE   : 'ALERT_FILTER_NONE'
};

export const AlertActions = {
    ADD_INFO_ALERTS   : 'ADD_INFO_ALERTS',
    ADD_ERROR_ALERTS  : 'ADD_ERROR_ALERTS',
    ADD_ACTION_ALERTS : 'ADD_ACTION_ALERTS',
    ALERT_DELETE  : 'ALERT_DELETE',
    ALERT_DISMISS : 'ALERT_DISMISS',
    SET_ALERT_FILTER : 'SET_ALERT_FILTER'
};

export const MdmActions = {
    MDM_MASTER_METADATA_RECEIVED : 'MDM_MASTER_METADATA_RECEIVED',
    MDM_MASTER_DATA_RECEIVED : 'MDM_MASTER_DATA_RECEIVED',
    MDM_MASTER_UPDATE_COMPLETE : 'MDM_MASTER_UPDATE_COMPLETE',
    MDM_MASTER_UPDATE_ROW : 'MDM_MASTER_UPDATE_ROW',
    MDM_MASTER_ADD_ROW : 'MDM_MASTER_ADD_ROW',
    MDM_MASTER_DELETE_ROW : 'MDM_MASTER_DELETE_ROW',
    MDM_MASTER_UPDATE_STARTED : 'MDM_MASTER_UPDATE_STARTED',
    MDM_MASTER_SWITCHING : 'MDM_MASTER_SWITCHING'
};
export const GridReportActions = {
    GRID_REPORT_PARAMS_RECEIVED : 'GRID_REPORT_PARAMS_RECEIVED',
    GRID_REPORT_DATA_UPDATE_RECEIVED : 'GRID_REPORT_DATA_UPDATE_RECEIVED',
    GRID_REPORT_DATA_RECEIVED : 'GRID_REPORT_DATA_RECEIVED'
};

/* Action Creators */

/* ALERT RELATED */
export function deleteAlert(id, alertType) {
    return {
        type: AlertActions.ALERT_DELETE,
        id: id,
        alertType : alertType
    }
}

export function logout() {
    return function (dispatch) {
        axios.post("/logout");
        window.location.href = '/';
    }
}

export function switchUser(userToSwitchTo, realLoggedInUser) {
    return function (dispatch) {
        if (realLoggedInUser != null && realLoggedInUser == userToSwitchTo) {
            /* If I am currently running as a delegate and I am switching back to myself
            *  realLoggedInUser will be NULL if i am NOT running as a delegate for someone else */
            window.location.href = 'logout/impersonate';
        } else {
            /* Either i am running as myself, OR i am running as a delegate and I am switching to some other delegator */
            window.location.href = 'login/impersonate?username=' + userToSwitchTo;
        }
    }
}

export function dismissAlert(id, alertType) {
    return function (dispatch) {
    axios.delete(`api/sr/notification/${id}`)
        .then(function (response) {
            dispatch(deleteAlert(id, alertType));
        });
    }
}

export function sendToServerAndDismissAlert(id, alertType, defnOfRESTCall) {
    return function (dispatch) {
        axios.post(defnOfRESTCall.url, defnOfRESTCall.data)
        .then(function (response) {
            dispatch(deleteAlert(id, alertType));
        });
    }
}


export function addAlerts(type, alerts) {
    return { type: type, alerts }
}

export function setAlertFilter(alertFilter) {
    return { type: AlertActions.SET_ALERT_FILTER, alertFilter : alertFilter }
}
export function setUser(user) {
    return { type: USER_CHANGED, user : user }
}

/* MDM ACTION CREATORS */

export function mdmMasterUpdateStarted() {
    return {
        type: MdmActions.MDM_MASTER_UPDATE_STARTED,
    }
}
export function mdmMasterDefnsReceived(masterDefns) {
    return {
        type: MdmActions.MDM_MASTER_METADATA_RECEIVED,
        masterDefns : masterDefns
    }
}
export function mdmMasterDataReceived(masterData) {
    return {
        type: MdmActions.MDM_MASTER_DATA_RECEIVED,
        masterData : masterData
    }
}
export function mdmMasterUpdateComplete(response) {
    return {
        type: MdmActions.MDM_MASTER_UPDATE_COMPLETE,
        response : response
    }
}
export function mdmMasterUpdateRow(row) {
    return {
        type: MdmActions.MDM_MASTER_UPDATE_ROW,
        row : row
    }
}
export function mdmMasterAddRow() {
    return {
        type: MdmActions.MDM_MASTER_ADD_ROW,
    }
}
export function mdmMasterDeleteRow(cid) {
    return {
        type: MdmActions.MDM_MASTER_DELETE_ROW,
        cid : cid
    }
}
export function mdmMasterSwitching() {
    return {
        type: MdmActions.MDM_MASTER_SWITCHING,
    }
}
export function mdmModuleActivated() {
    return function (dispatch) {
        axios.get('api/sr/mdm/masters')
        .then(function (response) {
            dispatch(mdmMasterDefnsReceived(response.data));
        });
    }
}
export function mdmMasterChosen(masterName) {
    return function (dispatch) {
        /* Tell the world that we are switching a master The reducer should clear out any state related to this master - changed rows, new rows, errors etc */
        dispatch(mdmMasterSwitching());
        axios.post('api/sr/mdm/data/' + masterName)
        .then(function (response) {
                let masterData = response.data;
                masterData['master'] = masterName;
                dispatch(mdmMasterDataReceived(masterData));
        });
    }
}
export function mdmMasterChanged(mdmDTO) {
    return function (dispatch) {
        axios.post('api/sr/mdm/update', mdmDTO)
        .then(function (response) {
            Application.showInfoNotification("Changes submitted successfully. ");
            dispatch(mdmMasterUpdateComplete());
        })
        .catch(function (error) {
            if(error.response.status == 409) {
                let message = "<div class='mdm-error-msg-header'>" + Application.Localize('ui.mdm.error', mdmDTO.master) + "</div>";
                Application.showErrorNotification(message);
                dispatch(mdmMasterUpdateComplete(error.response.data));
                //FIXME - handle the case where the error is NOT 409 - we need to dispatch something and set the state appropriately
            }
        });
    }
}

/* GRID Action Creators */
export function gridReceivedParameters(gridName, gridParams) {
    return {
        type: GridReportActions.GRID_REPORT_PARAMS_RECEIVED,
        gridParams : gridParams,
        gridName : gridName
    }
}

export function gridDataReceived(gridData, gridName) {
    return {
        type: GridReportActions.GRID_REPORT_DATA_RECEIVED,
        gridData : gridData,
        gridName : gridName
    }
}
export function gridDataUpdateReceived(receivedPushData, gridName) {
    return {
        type: GridReportActions.GRID_REPORT_DATA_UPDATE_RECEIVED,
        updatedData : receivedPushData,
        gridName : gridName
    }
}

export function gridParamRequest(gridName, params) {
    return function (dispatch) {
        axios.post('api/sr/gridParams/' + gridName, params)
        .then(function (response) {
                dispatch(gridReceivedParameters(gridName, response.data));
        });
    }
}
export function gridDataRequest(gridName, params) {
    return function (dispatch) {
        axios.post('api/sr/getGridData/' + gridName, params)
        .then(function (response) {
            let gridReport = response.data;
            gridReport['gridName'] = gridName;
            dispatch(gridDataReceived(gridReport, gridName));
        });
    }
}