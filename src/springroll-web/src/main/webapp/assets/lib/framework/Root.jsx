import React from 'react';
import Application from 'App';
import Navigation from 'Navigation.jsx';
import AlertPanel from 'AlertPanel.jsx';


const Root = ({ currentAlerts, onHideAlerts, currentAlertsTitle, alertType, onDeleteAlert, children }) => {
    return (
        <div>
            <Navigation />
            <div id='main-body' className='main-body'>
                <div id='content' className='main-body-content'>
                    {children}
                </div>
            </div>
            {currentAlerts != undefined  &&
                <AlertPanel onHideAlerts={onHideAlerts} currentAlerts={currentAlerts} currentAlertsTitle={currentAlertsTitle} alertType={alertType} onDeleteAlert={onDeleteAlert}/>
            }
            <div id='indicator' className='alertsIndicator'/>
        </div>
    );
}

export default Root;