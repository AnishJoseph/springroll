import Application from 'App.js';

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
    MDM_MASTER_UPDATE_COMPLETE : 'MDM_MASTER_UPDATE_COMPLETE'
};
export const GridReportActions = {
    GRID_REPORT_PARAMS_RECEIVED : 'GRID_REPORT_PARAMS_RECEIVED',
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
        var deferred = $.Deferred();
        $.ajax({
            type: "POST",
            url: "/logout"
        });
        window.location.href = '/';
        return deferred;
    }
}

export function switchUser(userToSwitchTo, realLoggedInUser) {
    return function (dispatch) {
        var deferred = $.Deferred();
        if (realLoggedInUser != null && realLoggedInUser == userToSwitchTo) {
            /* If I am currently running as a delegate and I am switching back to myself
            *  realLoggedInUser will be NULL if i am NOT running as a delegate for someone else */
            window.location.href = 'logout/impersonate';
        } else {
            /* Either i am running as myself, OR i am running as a delegate and I am switching to some other delegator */
            window.location.href = 'login/impersonate?username=' + userToSwitchTo;
        }
        return deferred;
    }
}

export function dismissAlert(id, alertType) {
    return function (dispatch) {
    var deferred = $.Deferred();
        $.ajax({
            url: `api/sr/notification/${id}`,
            type: 'DELETE',
            success: function (user) {
                deferred.resolve();
                dispatch(deleteAlert(id, alertType));
            },
            error: function (jqXHR, textStatus, errorThrown) {
                deferred.resolve();
            }
        });
        return deferred;
    }
}

export function sendToServerAndDismissAlert(id, alertType, defnOfRESTCall) {
    return function (dispatch) {
        var deferred = $.Deferred();
        if(defnOfRESTCall.type == "POST") {
            $.ajax({
                url: defnOfRESTCall.url,
                type: 'POST',
                data: defnOfRESTCall.data,
                success: function (templateData) {
                    deferred.resolve();
                    dispatch(deleteAlert(id, alertType));
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    deferred.resolve();
                }
            });
            return deferred;
        }
        if(defnOfRESTCall.type == "GET") {
            $.ajax({
                url: defnOfRESTCall.url,
                type: 'GET',
                success: function (templateData) {
                    deferred.resolve();
                    dispatch(deleteAlert(id, alertType));
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    deferred.resolve();
                }
            });
            return deferred;
        }
        if(defnOfRESTCall.type == "DELETE") {
            $.ajax({
                url: defnOfRESTCall.url,
                type: 'GET',
                success: function (templateData) {
                    deferred.resolve();
                    dispatch(deleteAlert(id, alertType));
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    deferred.resolve();
                }
            });
            return deferred;
        }
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
export function mdmMasterUpdateComplete(status, response) {
    return {
        type: MdmActions.MDM_MASTER_UPDATE_COMPLETE,
        status : status,
        response : response
    }
}
export function mdmModuleActivated(masterName) {
    return function (dispatch) {
        var deferred = $.Deferred();
        $.ajax({
            url: 'api/sr/mdm/masters',
            type: 'GET',
            success: function (masterDefns) {
                deferred.resolve();
                dispatch(mdmMasterDefnsReceived(masterDefns));
            }.bind(this),
            error : function (jqXHR, textStatus, errorThrown ){
                deferred.resolve();
                console.error("Unable to load MDM Definitions - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            }.bind(this)
        });
        return deferred;
    }
}
export function mdmMasterChosen(masterName) {
    return function (dispatch) {
        var deferred = $.Deferred();
        $.ajax({
            url: 'api/sr/mdm/data/' + masterName,
            type: 'POST',
            success: function (masterData) {
                deferred.resolve();
                masterData['master'] = masterName;
                dispatch(mdmMasterDataReceived(masterData));
            }.bind(this),
            error : function (jqXHR, textStatus, errorThrown ){
                deferred.resolve();
                console.error("Unable to load MDM Definitions - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            }.bind(this)
        });
        return deferred;
    }
}
export function mdmMasterChanged(mdmDTO) {
    return function (dispatch) {
        var deferred = $.Deferred();
        $.ajax({
            url: 'api/sr/mdm/update/',
            type: 'POST',
            data: JSON.stringify(mdmDTO),
            success: function (masterData) {
                deferred.resolve();
                Application.showInfoNotification("Changes submitted successfully. ");
                dispatch(mdmMasterUpdateComplete(true));

            }.bind(this),
            error : function (jqXHR, textStatus, errorThrown ){
                deferred.resolve();
                dispatch(mdmMasterUpdateComplete(false, jqXHR.responseJSON));
                jqXHR['errorHandled'] = true;
                let message = "<div class='mdm-error-msg-header'>Error(s) encountered while updating master '" + mdmDTO.master +"' - correct and resubmit</div>";
                _.each(jqXHR.responseJSON, function(violation){
                    message += "<div class='mdm-error-msg'>Row : " + (parseInt(violation.cookie) + 1) + " - Field : " + violation.field + " - " + violation.message + "</div>";
                });
                Application.showErrorNotification(message);
            }.bind(this)
        });
        return deferred;
    }
}


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

export function gridParamRequest(gridName, params) {
    return function (dispatch) {
        var deferred = $.Deferred();
        $.ajax({
            url: 'api/sr/gridParams/' + gridName,
            type: 'POST',
            data: JSON.stringify(params),
            success: function (gridParams) {
                deferred.resolve();
                dispatch(gridReceivedParameters(gridName, gridParams));

            }.bind(this),
            error : function (jqXHR, textStatus, errorThrown ){
                deferred.resolve();
            }.bind(this)
        });
        return deferred;
    }
}
export function gridDataRequest(gridName, params) {
    return function (dispatch) {
        var deferred = $.Deferred();
        $.ajax({
            url: 'api/sr/getGridData/' + gridName,
            type: 'POST',
            data: JSON.stringify(params),
            success: function (gridReport) {
                deferred.resolve();
                gridReport['gridName'] = gridName;
                dispatch(gridDataReceived(gridReport, gridName));
            }.bind(this),
            error : function (jqXHR, textStatus, errorThrown ){
                deferred.resolve();
            }.bind(this)
        });
        return deferred;
    }
}