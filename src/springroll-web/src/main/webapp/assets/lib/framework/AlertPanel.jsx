import React from 'react';
import ReviewAlertItem from 'ReviewAlertItem.jsx';
import Application from 'App.js';

class AlertPanel extends React.Component {

    render() {
        let renderers = Application.getSubscribersToAlerts();
        return (
            <div id='alerts'    className='alertsPanel'>
                <div id="alerts-container" className="alertsContainer">
                    <div className="alertActionsPanelBorder">
                        <div id="alerts-handle" onClick={this.props.onHideAlertPanel} className="alertHandle glyphicon glyphicon-eye-close"></div>
                        <div className="alertTitle"><p>{this.props.currentAlertsTitle}</p></div>
                        {
                            this.props.currentAlerts.map((alert, index) =>
                            {
                                let AlertRenderer = renderers[alert.channel];
                                return (<AlertRenderer onDeleteAlert={this.props.onDeleteAlert} key={alert.id} alert={alert}/>);
                            })
                        }
                    </div>
                </div>
            </div>
        );
    }
}

export default AlertPanel;