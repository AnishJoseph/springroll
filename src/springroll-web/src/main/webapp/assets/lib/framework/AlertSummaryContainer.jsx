import AlertSummary from 'AlertSummary.jsx';
import { connect } from 'react-redux';
import { AlertFilters, setAlertFilter } from 'SpringrollActionTypes';


const mapStateToProps = (state) => {
    return {
        errorAlertsLen: state.alerts.errors.length,
        infoAlertsLen: state.alerts.info.length,
        actionAlertsLen: state.alerts.actions.length
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        setAlertFilter: (alertFilter) => {
            dispatch(setAlertFilter(alertFilter));
        }
    }
};
const AlertSummaryContainer = connect(mapStateToProps, mapDispatchToProps )(AlertSummary);
export default AlertSummaryContainer;