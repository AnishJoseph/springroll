import React from 'react';
import ReviewAlertItem from 'ReviewAlertItem.jsx';
import Application from 'App.js';
import { AlertFilters} from 'SpringrollActionTypes';


const AlertPanel = ({ currentAlerts, currentAlertsTitle, alertType, onHideAlerts, onDeleteAlert, onDismissAlert }) => {
    let renderers = Application.getSubscribersToAlerts();
    if(currentAlerts != undefined) {
        return (
            <div id='alerts' className='alertsPanel'>
                <div id="alerts-container" className="alertsContainer">
                    <div className="alertActionsPanelBorder">
                        <div id="alerts-handle" onClick={() => onHideAlerts(AlertFilters.ALERT_FILTER_NONE)}
                             className="alertHandle glyphicon glyphicon-eye-close"></div>
                        <div className="alertTitle"><p>{currentAlertsTitle}</p></div>
                        {
                            currentAlerts.map((alert, index) => {
                                let AlertRenderer = renderers[alert.channel];
                                return (<AlertRenderer onDismissAlert={() => onDismissAlert(alert.id, alertType)} onDeleteAlert={() => onDeleteAlert(alert.id, alertType)}
                                                       key={alert.id} alert={alert}/>);
                            })
                        }
                    </div>
                </div>
            </div>
        );
    }
    return <div></div>;
};


export default AlertPanel;