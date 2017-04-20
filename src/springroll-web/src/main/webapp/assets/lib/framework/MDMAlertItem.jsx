import React from 'react'
import Application from 'App.js';
import ReviewModal from 'ReviewModal.jsx';
import MDMReviewMoreInfo from 'MMDMReviewMoreInfo.jsx';
import GenericAlertItem from 'GenericAlertItem.jsx';

class MDMAlertItem extends React.Component {
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
                <GenericAlertItem alert={this.props.alert} onMoreInfoClicked={this.handleInfoClicked} />
                {
                    this.state.showMoreInfo &&
                    <ReviewModal onSubmit={this.onSubmit} onModalClosed={this.handleModalClosed} title={message}>
                        <MDMReviewMoreInfo alert={this.props.alert}/>
                    </ReviewModal>
                }
            </span>
        );
    }
}
export default MDMAlertItem;
