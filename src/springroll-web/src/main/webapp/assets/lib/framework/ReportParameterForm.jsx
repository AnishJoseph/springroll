import React, { Component } from 'react';
import Application from 'App';
import { Field, reduxForm } from 'redux-form';

/*
 If we dont want to show the submit button till the user has filled in all the parameters then
 add the line below into the submit button tag
 style={{display : pristine || submitting ? 'none' : ''}}
 */
class ReportParameterForm extends Component {
    render() {
        const { handleSubmit, pristine, reset, submitting } = this.props;
        return (
            <div className="panel panel-default">
                <div className="panel-body">
                    <form onSubmit={handleSubmit} className="report-param-form">
                        <div className="container-fluid">
                            {this.props.params.map((parameter) => {
                                return (
                                    <div key={parameter.name} className="form-group rep-param col-md-3">
                                        <label htmlFor={parameter.name}>{Application.Localize(parameter.name)}</label>
                                        <Field name={parameter.name} component="input" type="email"/>
                                    </div>
                                    )
                                })
                            }
                        </div>
                        <button className="submitButton" type="submit" >
                            <span title={Application.Localize('Apply')} className=" submit glyphicon glyphicon-ok"/>
                        </button>
                    </form>
                </div>
            </div>
        );
    }
}
export default ReportParameterForm;