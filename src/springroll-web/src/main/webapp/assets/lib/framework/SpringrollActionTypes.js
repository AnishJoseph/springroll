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
/* Action Creators */
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
    return { type: AlertActions.SET_ALERT_FILTER, alertFilter : alertFilter }
}
export function setUser(user) {
    return { type: USER_CHANGED, user : user }
}