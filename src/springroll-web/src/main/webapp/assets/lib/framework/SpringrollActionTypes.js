export const SET_ALERT_FILTER = "SET_ALERT_FILTER";

export const AlertFilters = {
    ALERT_FILTER_ACTION : 'ALERT_FILTER_ACTION',
    ALERT_FILTER_ERROR  : 'ALERT_FILTER_ERROR',
    ALERT_FILTER_INFO   : 'ALERT_FILTER_INFO',
    ALERT_FILTER_NONE   : 'ALERT_FILTER_NONE'
};

export const AlertActions = {
    INFO_ALERTS   : 'INFO_ALERTS',
    ERROR_ALERTS  : 'ERROR_ALERTS',
    ACTION_ALERTS : 'ACTION_ALERTS',
    ALERT_DELETE  : 'ALERT_DELETE',
    ALERT_DISMISS : 'ALERT_DISMISS'
};
/* Action Creators */
export function deleteAlert(id, alertType) {
    return {
        type: AlertActions.ALERT_DELETE,
        id: id,
        alertType : alertType
    }
}

export function dismissAlert(id, alertType) {
    return function (dispatch) {
    var deferred = $.Deferred();
        $.ajax({
            url: `api/sr/notification/${id}`,
            type: 'DELETE',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
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
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
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
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
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
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
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
    return { type: SET_ALERT_FILTER, alertFilter : alertFilter }
}