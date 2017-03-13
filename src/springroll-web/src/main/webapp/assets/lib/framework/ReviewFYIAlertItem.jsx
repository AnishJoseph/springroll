import React from 'react'
import Application from 'App.js';


class ReviewFYIAlertItem extends React.Component {
    render() {
        return (
            <span>
                <div id='message' className="alertMessage">{Application.Localize(this.props.alert.messageKey, this.props.alert.args)}</div>
                <div id='action'>
                    <span onClick={() => this.props.onDeleteAlert(this.props.alert.id)} data-toggle="tooltip" title={Application.Localize('ui.dismiss')}  className="alertActionsPanelItem glyphicon glyphicon-trash"></span>
                    <span className="alertActionsPanelTime">{this.props.alert.creationTimeMoment}</span>
                    <div className="alertActionsPanelBorder"/>
                </div>
            </span>
        );
    }
}
export default ReviewFYIAlertItem;
