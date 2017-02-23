import React from 'react'
import Application from 'App.js';
import ReviewMoreInfo from 'ReviewMoreInfo.jsx';
import Modal from 'SrModal.jsx';

class ReviewAlertItem extends React.Component {
    constructor(props){
        super(props);
        this.onSubmit = this.onSubmit.bind(this);
        this.handleInfoClicked = this.handleInfoClicked.bind(this);
        this.handleModalClosed = this.handleModalClosed.bind(this);
        this.state = {showMoreInfo : false}
    }
    onSubmit(action, comment){
        this.props.onDeleteAlert(this.props.alert.id);
        this.setState({showMoreInfo: false})
        alert("ACTION : " + action + "  : Comment : " + comment);
    }

    handleInfoClicked(){
        this.setState({showMoreInfo: true})
    }
    handleModalClosed(){
        this.setState({showMoreInfo: false})
    }
    render() {
        let message = Application.Localize(this.props.alert.messageKey, this.props.alert.args)
        return (
            <span>
                <div id='message' className="alertMessage">{message}</div>
                <div id='action'>
                    <span id='info' onClick={this.handleInfoClicked} data-toggle="tooltip" title={Application.Localize('ui.info')} className="alertActionsPanelItem glyphicon glyphicon-info-sign"></span>
                    <span className="alertActionsPanelTime">{this.props.alert.creationTimeMoment}</span>
                    <div className="alertActionsPanelBorder"/>
                </div>
                {
                    this.state.showMoreInfo &&
                    <Modal onModalClosed={this.handleModalClosed} title={message}>
                        <ReviewMoreInfo alert={this.props.alert} onSubmit={this.onSubmit}></ReviewMoreInfo>
                    </Modal>
                }

            </span>
        );
    }

    /*
     var data = JSON.stringify({reviewStepId: this.props.alert.reviewStepId, approved : action, reviewComment : this.state.comment});
     $.ajax({
     url: 'api/sr/reviewaction',
     type: 'POST',
     data: data,
     contentType: 'application/json; charset=utf-8',
     dataType: 'json',
     success: function (templateData) {
     console.log("Templates loaded.");
     },
     error : function (jqXHR, textStatus, errorThrown ){
     console.error("Unable to load templates - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
     }
     });

     */
}
export default ReviewAlertItem;
