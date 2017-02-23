import React from 'react';
import Application from 'App';
import Navigation from 'Navigation.jsx';
import AlertPanel from 'AlertPanel.jsx';
import CometD from 'messenger.cometd.js';
import Modal from 'SrModal.jsx';
import Example from 'Example.jsx';
var moment = require('moment');

class Root extends React.Component {
    constructor(props){
        super(props);
        this.state = {currentAlerts: undefined, currentAlertsTitle : undefined, infoAlertsLen : 0, errorAlertsLen : 0, actionAlertsLen : 0, modalCookie : undefined, modalComponent : undefined};
        this.handleAlertPanelShowFromNavPanel = this.handleAlertPanelShowFromNavPanel.bind(this);
        this.handleAlertsReceived = this.handleAlertsReceived.bind(this);
        this.handleHideAlertPanel = this.handleHideAlertPanel.bind(this);
        this.handleShowModal = this.handleShowModal.bind(this);
        this.handleHideModal = this.handleHideModal.bind(this);
        this.handleAlertsDelete = this.handleAlertsDelete.bind(this);
        let that = this;
        _.each(Object.keys(Application.getSubscribersToAlerts()), function(channel){
            Application.subscribe(channel, that.handleAlertsReceived);
        });
        this.infoAlerts = [];
        this.errorAlerts = [];
        this.actionAlerts = [];
    }
    handleHideModal(cookie){
        this.setState({modalCookie : undefined, modalComponent : undefined});
        if(this.state.modalCompleteCallback != undefined)this.state.modalCompleteCallback(cookie);

    }
    handleShowModal(cookie, component, completeCallback){
        this.setState({modalCookie : cookie, modalComponent : component, modalCompleteCallback : completeCallback})
    }
    handleHideAlertPanel(){
        this.setState({currentAlerts : undefined})
    }
    handleAlertsDelete(alertId){
        var alerts = this.state.currentAlerts.filter(alert => (alert.id !== alertId));
        if(this.state.currentAlertsTitle == 'ui.alert.action.title') {
            this.actionAlerts = alerts;
        }
        if(this.state.currentAlertsTitle == 'ui.alert.errors.title') {
            this.errorAlerts = alerts;
        }
        if(this.state.currentAlertsTitle == 'ui.alert.info.title') {
            this.infoAlerts = alerts;
        }
        this.setState({
            currentAlerts : alerts,
            infoAlertsLen : this.infoAlerts.length,
            errorAlertsLen : this.errorAlerts.length,
            actionAlertsLen : this.actionAlerts.length
        });
    }
    handleAlertsReceived(response){
        let alertCollection;
        if(response.data[0].alertType == 'ACTION') this.actionAlerts = this.actionAlerts.concat(response.data);
        if(response.data[0].alertType == 'ERROR') this.errorAlerts = this.errorAlerts.concat(response.data);
        if(response.data[0].alertType == 'INFO') this.infoAlerts = this.infoAlerts.concat(response.data);
        this.setState({
            infoAlertsLen : this.infoAlerts.length,
            errorAlertsLen : this.errorAlerts.length,
            actionAlertsLen : this.actionAlerts.length
        });
        if(response.data[0].alertType == 'ACTION' && this.state.currentAlertsTitle == 'ui.alert.action.title'){
            this.setState({currentAlerts : this.actionAlerts});
        }
        _.each(response.data, alert => (alert['creationTimeMoment'] = moment(alert.creationTime).format(Application.getMomentFormatForDateTime())));
    }

    handleAlertPanelShowFromNavPanel(alertTypeTitle) {
        if(alertTypeTitle == 'ui.alert.action.title') {
            this.setState({currentAlerts: this.actionAlerts, currentAlertsTitle: alertTypeTitle});
        }
        if(alertTypeTitle == 'ui.alert.errors.title') {
            this.setState({currentAlerts: this.errorAlerts, currentAlertsTitle: alertTypeTitle});
        }
        if(alertTypeTitle == 'ui.alert.info.title') {
            this.setState({currentAlerts: this.infoAlerts, currentAlertsTitle: alertTypeTitle});
        }
    }
    render() {
        let ModalComponent = this.state.modalComponent;
        return (
            <div>
                <Navigation onChangeOfAlertType={this.handleAlertPanelShowFromNavPanel} actionAlertsLen={this.state.actionAlertsLen} infoAlertsLen={this.state.infoAlertsLen} errorAlertsLen={this.state.errorAlertsLen}/>
                <div id='main-body' className='main-body'>
                    <div id='content' className='main-body-content'>
                        {this.props.children}
                    </div>
                </div>
                {this.state.currentAlerts != undefined  &&
                    <AlertPanel onDeleteAlert={this.handleAlertsDelete} showModal={this.handleShowModal} currentAlerts={this.state.currentAlerts} currentAlertsTitle={Application.Localize(this.state.currentAlertsTitle)} onHideAlertPanel={this.handleHideAlertPanel}/>
                }
                <div id='indicator' className='alertsIndicator'/>
                <div id='modal'>
                    {this.state.modalComponent != undefined && <Modal hideModal={this.handleHideModal}><ModalComponent modalCookie={this.state.modalCookie} onComplete={this.handleHideModal}/></Modal>}
                </div>
            </div>
        );
    }
}

export default Root;