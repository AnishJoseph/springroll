import React from 'react';
import Application from 'App';
import Navigation from 'Navigation.jsx';
import AlertPanel from 'AlertPanel.jsx';
import { AlertActions} from 'SpringrollActionTypes'

var moment = require('moment');


class Root extends React.Component {
    constructor(props){
        super(props);
        this.handleAlertsReceived = this.handleAlertsReceived.bind(this);
        let that = this;
        _.each(Object.keys(Application.getSubscribersToAlerts()), function(channel){
            Application.subscribe(channel, that.handleAlertsReceived);
        });
    }
    handleAlertsReceived(response){
        _.each(response.data, alert => (alert['creationTimeMoment'] = moment(alert.creationTime).format(Application.getMomentFormatForDateTime())));

        if(response.data[0].alertType == 'ACTION') this.props.onAddAlerts(AlertActions.ACTION_ALERTS, response.data);
        if(response.data[0].alertType == 'ERROR')  this.props.onAddAlerts(AlertActions.ERROR_ALERTS,  response.data);
        if(response.data[0].alertType == 'INFO')   this.props.onAddAlerts(AlertActions.INFO_ALERTS,   response.data);
    }

    render() {
        return (
            <div>
                <Navigation />
                <div id='main-body' className='main-body'>
                    <div id='content' className='main-body-content'>
                        {this.props.children}
                    </div>
                </div>
                {this.props.currentAlerts != undefined  &&
                    <AlertPanel onHideAlerts={this.props.onHideAlerts}currentAlerts={this.props.currentAlerts} currentAlertsTitle={this.props.currentAlertsTitle} alertType={this.props.alertType} onDeleteAlert={this.props.onDeleteAlert}/>
                }
                <div id='indicator' className='alertsIndicator'/>
            </div>
        );
    }
}

export default Root;