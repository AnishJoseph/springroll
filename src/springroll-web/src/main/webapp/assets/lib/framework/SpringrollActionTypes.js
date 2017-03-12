export const SET_ALERT_FILTER = "SET_ALERT_FILTER";

export const AlertFilters = {
    ALERT_FILTER_ACTION : 'ALERT_FILTER_ACTION',
    ALERT_FILTER_ERROR : 'ALERT_FILTER_ERROR',
    ALERT_FILTER_INFO : 'ALERT_FILTER_INFO',
    ALERT_FILTER_NONE : 'ALERT_FILTER_NONE'
};

export const AlertActions = {
    INFO_ALERTS : 'INFO_ALERTS',
    ERROR_ALERTS : 'ERROR_ALERTS',
    ACTION_ALERTS : 'ACTION_ALERTS',
    ALERT_DELETE : 'ALERT_DELETE'
};
/* Action Creators */
export function deleteAlert(id, alertType) {
    return {
        type: AlertActions.ALERT_DELETE,
        id: id,
        alertType : alertType
    }
}

export function addAlerts(type, alerts) {
    return { type: type, alerts }
}

export function setAlertFilter(alertFilter) {
    return { type: SET_ALERT_FILTER, alertFilter : alertFilter }
}