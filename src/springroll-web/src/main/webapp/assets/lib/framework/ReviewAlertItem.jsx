import React from 'react'
import Application from 'App.js';
import Modal from 'SrModal.jsx';
var moment = require('moment');

const ReviewMoreInfo = ({alert}) => {
    return (
        <span>
            {
                alert.reviewLog != null && alert.reviewLog.length > 0 &&
                <span>
                    <h4>{Application.Localize("ui.review.previousReviewers")}</h4>
                    <table className="table table-hover table-striped">
                        <thead>
                            <tr>
                                <th>{Application.Localize("ui.review.reviewer")}</th>
                                <th>{Application.Localize("ui.review.comments")}</th>
                                <th>{Application.Localize("ui.review.time")}</th>
                            </tr>
                        </thead>
                        <tbody>
                        {
                            alert.reviewLog.map((review, index) =>
                                (<tr key={index}>
                                    <td>{review.reviewer}</td>
                                    <td>{review.reviewComment}</td>
                                    <td>{moment(review.time).format(Application.getMomentFormatForDateTime())}</td>
                                </tr>))
                        }
                        </tbody>
                    </table>
                </span>
            }
            <table className="table table-hover table-striped">
                <thead>
                <tr>
                    <th>{Application.Localize("ui.review.breachedRuleName")}</th>
                    <th>{Application.Localize("ui.review.breachedRuleText")}</th>
                </tr>
                </thead>
                <tbody>
                {
                    alert.businessValidationResult.map((violation, index) =>
                        (<tr key={index}>
                            <td>{Application.Localize(violation.violatedRule)}</td>
                            <td>{Application.Localize(violation.messageKey, violation.args)}</td>
                        </tr>))
                }
                </tbody>
            </table>
        </span>
    );
};

class ReviewAlertItem extends React.Component {
    constructor(props){
        super(props);
        this.onSubmit = this.onSubmit.bind(this);
        this.handleInfoClicked = this.handleInfoClicked.bind(this);
        this.handleModalClosed = this.handleModalClosed.bind(this);
        this.submitToServer = this.submitToServer.bind(this);
        this.state = {showMoreInfo : false}
    }
    onSubmit(action, comment){
        this.props.onDeleteAlert(this.props.alert.id);
        this.setState({showMoreInfo: false});
        this.submitToServer(action, comment);
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
                    <span id='info' onClick={this.handleInfoClicked} data-toggle="tooltip" title={Application.Localize('ui.info')} className="alertActionsPanelItem glyphicon glyphicon-info-sign"></span>
                    <span className="alertActionsPanelTime">{this.props.alert.creationTimeMoment}</span>
                    <div className="alertActionsPanelBorder"/>
                </div>
                {
                    this.state.showMoreInfo &&
                    <Modal onSubmit={this.onSubmit} onModalClosed={this.handleModalClosed} title={message}>
                        <ReviewMoreInfo alert={this.props.alert}/>
                    </Modal>
                }

            </span>
        );
    }

    submitToServer(action, comment) {
        var data = JSON.stringify({
            reviewStepId: this.props.alert.reviewStepId,
            approved: action,
            reviewComment: comment
        });
        $.ajax({
            url: 'api/sr/reviewaction',
            type: 'POST',
            data: data,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (templateData) {
                console.log("Templates loaded.");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Unable to load templates - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            }
        });
    }

}
export default ReviewAlertItem;
