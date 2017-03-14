import React from 'react'
import Application from 'App.js';
import ReviewModal from 'ReviewModal.jsx';
import ReviewMoreInfo from 'ReviewMoreInfo.jsx';

class ReviewFYIAlertItem extends React.Component {
    constructor(props){
        super(props);
        this.onSubmit = this.onSubmit.bind(this);
        this.handleInfoClicked = this.handleInfoClicked.bind(this);
        this.handleModalClosed = this.handleModalClosed.bind(this);
        this.state = {showMoreInfo : false}
    }
    onSubmit(action, comment){
        this.setState({showMoreInfo: false});
        var data = JSON.stringify({ reviewStepId: this.props.alert.reviewStepId, approved: action, reviewComment: comment});
        let defnOfRESTCall = {type : 'POST', data : data, url : 'api/sr/reviewaction'};
        this.props.onSendToServerAndDismissAlert(defnOfRESTCall);
    }

    handleInfoClicked(){
        this.setState({showMoreInfo: true});
    }
    handleModalClosed(){
        this.setState({showMoreInfo: false});
    }
    render() {
        let message = Application.Localize(this.props.alert.messageKey, this.props.alert.args);
        return (
            <span>
                <div id='message' className="alertMessage">{message}</div>
                <div id='action'>
                    <span onClick={this.props.onDismissAlert} data-toggle="tooltip" title={Application.Localize('ui.dismiss')}  className="alertActionsPanelItem glyphicon glyphicon-trash"></span>
                    <span id='info' onClick={this.handleInfoClicked} data-toggle="tooltip" title={Application.Localize('ui.info')} className="alertActionsPanelItem glyphicon glyphicon-info-sign"></span>
                    <span className="alertActionsPanelTime">{this.props.alert.creationTimeMoment}</span>
                    <div className="alertActionsPanelBorder"/>
                </div>
                {
                    this.state.showMoreInfo &&
                    <ReviewModal onDismissAlert={this.props.onDismissAlert} onModalClosed={this.handleModalClosed} title={message}>
                        <ReviewMoreInfo alert={this.props.alert}/>
                    </ReviewModal>
                }

            </span>
        );
    }
}
export default ReviewFYIAlertItem;