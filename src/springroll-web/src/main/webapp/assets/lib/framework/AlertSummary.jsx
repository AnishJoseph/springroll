import React from 'react';
import Application from 'App.js';
import { AlertFilters } from 'SpringrollActionTypes';


class AlertSummary extends React.Component {
    render() {
        return (
            <div className="alert-summary-container navbar-right">
                <span onClick={() => this.props.setAlertFilter(AlertFilters.ALERT_FILTER_ACTION)} className="alertAction " data-toggle="tooltip" title={Application.Localize('ui.view.approvals')}>
                    <span  className="alertLabel   glyphicon glyphicon-ok-sign" aria-hidden="true"/>
                    <span className="alertActionCount alertCount">{this.props.actionAlertsLen}</span>
                </span>

                <span onClick={() => this.props.setAlertFilter(AlertFilters.ALERT_FILTER_ERROR)} className="alertError " data-toggle="tooltip" title={Application.Localize('ui.view.errors')}>
                    <span className="alertLabel  glyphicon glyphicon-warning-sign" aria-hidden="true"/>
                    <span className="alertErrorCount alertCount">{this.props.errorAlertsLen}</span>
                </span>

                <span  onClick={() => this.props.setAlertFilter(AlertFilters.ALERT_FILTER_INFO)} className="alertInfo " data-toggle="tooltip" title={Application.Localize('ui.view.info')}>
                    <span className="alertLabel  glyphicon glyphicon-info-sign" aria-hidden="true"></span>
                    <span className="alertInfoCount alertCount">{this.props.infoAlertsLen}</span>
                </span>
            </div>
        );
    }
}

export default AlertSummary;