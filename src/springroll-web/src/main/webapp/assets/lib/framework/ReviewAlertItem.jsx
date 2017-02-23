import React from 'react'
import Application from 'App.js';
import ReviewMoreInfo from 'ReviewMoreInfo.jsx';

class ReviewAlertItem extends React.Component {
    constructor(props){
        super(props);
        this.alertHandled = this.alertHandled.bind(this);
    }
    alertHandled(alertId){
        this.props.onDeleteAlert(alertId);
    }
    render() {
        return (
            <span>
                <div id='message' className="alertMessage">{Application.Localize(this.props.alert.messageKey, this.props.alert.args)}</div>
                <div id='action'>
                    <span id='info' onClick={() => this.props.showModal(this.props.alert, ReviewMoreInfo, this.alertHandled)} data-toggle="tooltip" title={Application.Localize('ui.info')}     className="alertActionsPanelItem glyphicon glyphicon-info-sign"></span>
                    <span className="alertActionsPanelTime">{this.props.alert.creationTimeMoment}</span>
                    <div className="alertActionsPanelBorder"/>
                </div>
            </span>
        );
    }
}
export default ReviewAlertItem;
