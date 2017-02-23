import React from 'react'
import Application from 'App.js';
import ReactDOM from 'react-dom';

class ReviewMoreInfo extends React.Component {
    constructor(props){
        super(props);
        this.handleCommentsChange = this.handleCommentsChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.state = {comment: ''};

    }
    handleCommentsChange(e){
        this.setState({comment : e.target.value});
    }
    handleSubmit(action){
        this.props.onSubmit(action, this.state.comment);
        event.preventDefault();
    }

    render() {
        return (
            <span>
                <table className="table table-hover table-striped">
                    <thead>
                        <tr>
                            <th>{Application.Localize("ui.review.breachedRuleName")}</th>
                            <th>{Application.Localize("ui.review.breachedRuleText")}</th>
                        </tr>
                    </thead>
                    <tbody>
                    {
                        this.props.alert.businessValidationResult.map((violation, index) =>
                            (<tr key={index}>
                                <td>{Application.Localize(violation.violatedRule)}</td>
                                <td>{Application.Localize(violation.messageKey, violation.args)}</td>
                            </tr>))
                    }
                    </tbody>
                </table>
                <div className="modal-footer">
                    <div className="row">
                        <div className="col-md-8">
                            <input onChange={this.handleCommentsChange} type="text" ref='comments' className="form-control" />
                        </div>
                        <div className="col-md-1">
                            {this.state.comment.length > 0 &&
                                <span ref='accept' onClick={() => this.handleSubmit(true)} className="glyphicon glyphicon-ok-sign form-control form-control" title={Application.Localize('Approve')}/>
                            }
                        </div>
                        <div className="col-md-1">
                            {this.state.comment.length > 0 &&
                                <span ref='reject' onClick={() => this.handleSubmit(false)} className="glyphicon glyphicon-remove-sign form-control form-control" title={Application.Localize('Reject')}/>
                            }
                        </div>
                        <div className="col-md-1">
                            <span ref='dismiss' className="glyphicon glyphicon-trash form-control hidden form-control" title={Application.Localize('Dismiss')}/>
                        </div>
                    </div>
                </div>
            </span>
        );
    }
}
export default ReviewMoreInfo;