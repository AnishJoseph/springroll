import React, { Component } from 'react';
import Application from 'App';
import { Field, reduxForm } from 'redux-form';
import Select from "./Select";
import DatePicker from 'DatePicker';
import Input from 'Input.jsx';

const booleanLovList = [{value : true, label : Application.Localize('ui.true')}, {value : false, label : Application.Localize('ui.false')}];
const floatPattern = new RegExp("^[-+]?[0-9]*\\.?[0-9]*$");
const intPattern = new RegExp("^[-+]?[0-9]*$");

const normalizeInt = (value, previousValue) => {
    if(intPattern.test(value))
        return value
    return previousValue;
};
const normalizeFloat = (value, previousValue) => {
    if(floatPattern.test(value))
        return value
    return previousValue;
};

const renderDate = ({ input, isDateTime }) => 
     <DatePicker {...input} className="form-control" isDateTime={isDateTime} value={input.value}/>

const renderMultiselect = ({ input, multiSelect, options }) => {
    console.log("RENDER Input value = " + input.value);
    return (<Select {...input}
            className="form-control"
            options={options}
            multiSelect={multiSelect}
            value={input.value}
    />)
};

const renderField = (props) => {
    let { input, type, Renderer, meta: { touched, error, warning } } = props;
    return (
        <div className="form-group rep-param col-md-3">
            <label>{Application.Localize(input.name)}</label>
            <div>
                <Renderer {...props} {...input}/>
                {touched && ((error && <span>{error}</span>) || (warning && <span>{warning}</span>))}
            </div>
        </div>
    )
};


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
                                let normalizer = undefined;
                                if (parameter.javaType == "boolean"){
                                    return (
                                        <Field key={parameter.name} name={parameter.name} component={renderField} Renderer={renderMultiselect} options={booleanLovList} multiSelect={false}/>
                                    )
                                } else if(parameter.lovList != null){
                                    return (
                                        <Field key={parameter.name} name={parameter.name} component={renderField} Renderer={renderMultiselect} options={parameter.lovList} multiSelect={parameter.multiSelect}/>
                                    )
                                } else if (parameter.javaType == "date" || parameter.setTime === 'START_OF_DAY' || parameter.setTime === 'END_OF_DAY'){
                                    return (
                                        <Field key={parameter.name} name={parameter.name} component={renderField} Renderer={renderDate} isDateTime={false} />
                                    )
                                }else if (parameter.javaType == "dateTime"){
                                    return (
                                        <Field key={parameter.name} name={parameter.name} component={renderField} Renderer={renderDate} isDateTime={true} />
                                    )
                                }else if ( parameter.javaType == "int"){
                                    normalizer = normalizeInt;
                                } else if ( parameter.javaType == "float"){
                                    normalizer = normalizeFloat;
                                }
                                return (
                                        <Field key={parameter.name} name={parameter.name} component={renderField} Renderer={Input} classes="form-control" normalize={normalizer}/>
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