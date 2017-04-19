import React, { Component } from 'react';
import Application from 'App';
import { Field, reduxForm } from 'redux-form';
import ReactSelect from 'react-select';
import DateField from 'react-datetime';
import {floatPattern, intPattern} from 'Formatters';
var moment = require('moment');

/* Fixme there are 2 booleanLovList */
const booleanLovList = [{value : true, label : Application.Localize('ui.true')}, {value : false, label : Application.Localize('ui.false')}];

const mandatory = value => value ? undefined : Application.Localize('ui.report.parameters.empty');

const normalizeInt = (value, previousValue) => {
    if(intPattern.test(value))
        return value;
    return previousValue;
};
const normalizeFloat = (value, previousValue) => {
    if(floatPattern.test(value))
        return value;
    return previousValue;
};

const renderDate = ({ input, isDateTime }) => {
    return (<DateField
        value={input.value === undefined ? undefined : moment(input.value)}
        dateFormat={Application.getMomentFormatForDate()}
        onChange={ value => {
            if(value == null || value == undefined)return;
            input.onChange(value.valueOf());
        }}
        closeOnSelect={true}
        timeFormat={isDateTime}
    />);

};

const renderSelect = ({ input, multiSelect, options }) => {
    return (<ReactSelect
        options={options}
        onChange={ value => {
            if(value == null)return;
            let choices = undefined;
            if(multiSelect) {
                choices = _.pluck(value, 'value');
            } else {
                choices = value.value;
            }
            input.onChange(choices);
        }}
        multi={multiSelect}
        value={input.value}
        className="form-control"
    />);
};

const renderInput = ({ input}) => {
    return (
        <input className="form-control" value={input.value} type="text" onChange={ value => input.onChange(value.target.value)} />)
};

const renderField = (props) => {
    let { input, type, Renderer, width, meta: { touched, error, warning } } = props;
    let colWidth = "col-md-" + width;
    return (
        <div className={"form-group rep-param " + colWidth }>
            <label>{Application.Localize("ui."+input.name)}</label>
            <div>
                <Renderer {...props} {...input}/>
            </div>
            {touched && (error && <label className="text-danger"><small>{error}</small></label>)}
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
                                let validators = [];
                                if(parameter.mandatory) validators.push(mandatory);
                                if (parameter.javaType == "boolean"){
                                    return (
                                        <Field validate={validators} key={parameter.name} name={parameter.name} component={renderField} Renderer={renderSelect} options={booleanLovList} multiSelect={false} width={parameter.width}/>
                                    )
                                } else if(parameter.lovList != null){
                                    return (
                                        <Field validate={validators} key={parameter.name} name={parameter.name} component={renderField} Renderer={renderSelect} options={parameter.lovList} multiSelect={parameter.multiSelect} width={parameter.width}/>
                                    )
                                } else if (parameter.javaType == "date" || parameter.setTime === 'START_OF_DAY' || parameter.setTime === 'END_OF_DAY'){
                                    return (
                                        <Field validate={validators} key={parameter.name} name={parameter.name} component={renderField} Renderer={renderDate} isDateTime={false}  width={parameter.width}/>
                                    )
                                }else if (parameter.javaType == "dateTime"){
                                    return (
                                        <Field validate={validators} key={parameter.name} name={parameter.name} component={renderField} Renderer={renderDate} isDateTime={true} width={parameter.width} />
                                    )
                                }else if ( parameter.javaType == "int"){
                                    normalizer = normalizeInt;
                                } else if ( parameter.javaType == "float"){
                                    normalizer = normalizeFloat;
                                }
                                return (
                                        <Field validate={validators} key={parameter.name} name={parameter.name} component={renderField} Renderer={renderInput} classes="form-control" normalize={normalizer} width={parameter.width}/>
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