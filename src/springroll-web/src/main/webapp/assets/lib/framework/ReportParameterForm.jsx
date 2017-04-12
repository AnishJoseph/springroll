import React, { Component } from 'react';
import Application from 'App';
import { Field, reduxForm } from 'redux-form';
import Select from "./Select";
import DatePicker from 'DatePicker';
import Input from 'Input.jsx';

const booleanLovList = [{value : true, label : Application.Localize('ui.true')}, {value : false, label : Application.Localize('ui.false')}];
const floatPattern = new RegExp("^[-+]?[0-9]*\\.?[0-9]*$");
const intPattern = new RegExp("^[-+]?[0-9]*$");


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
                                let pattern = undefined;
                                if (parameter.javaType == "java.lang.Boolean"){
                                    return (
                                        <div key={parameter.name} className="form-group rep-param col-md-3">
                                            <label>{Application.Localize(parameter.name)}</label>
                                            <Field name={parameter.name} component={renderMultiselect} multiSelect={false} options={booleanLovList}/>
                                        </div>
                                    )
                                } else if(parameter.lovList != null){
                                    return (
                                        <div key={parameter.name} className="form-group rep-param col-md-3">
                                            <label>{Application.Localize(parameter.name)}</label>
                                            <Field name={parameter.name} component={renderMultiselect} multiSelect={parameter.multiSelect} options={parameter.lovList}/>
                                        </div>
                                    )
                                } else if (parameter.javaType == "java.time.LocalDate" || (parameter.javaType == "java.time.LocalDateTime" && (parameter.setTime === 'START_OF_DAY' || parameter.setTime === 'END_OF_DAY'))){
                                    return (
                                        <div key={parameter.name} className="form-group rep-param col-md-3">
                                            <label>{Application.Localize(parameter.name)}</label>
                                            <Field name={parameter.name} component={renderDate}  isDateTime={false}/>
                                        </div>
                                    )
                                }else if (parameter.javaType == "java.time.LocalDateTime"){
                                    return (
                                        <div key={parameter.name} className="form-group rep-param col-md-3">
                                            <label>{Application.Localize(parameter.name)}</label>
                                            <Field name={parameter.name} component={renderDate}  isDateTime={true}/>
                                        </div>
                                    )
                                }else if ( parameter.javaType == "java.lang.Integer" || parameter.javaType == "java.lang.Long" || parameter.javaType == "java.math.BigInteger" || parameter.javaType == "java.lang.Short"){
                                    pattern = intPattern;
                                } else if ( parameter.javaType == "java.lang.Double"  || parameter.javaType == "java.lang.Float" || parameter.javaType == "java.math.BigDecimal"){
                                    pattern = floatPattern;
                                }
                                return (
                                    <div key={parameter.name} className="form-group rep-param col-md-3">
                                        <label htmlFor={parameter.name}>{Application.Localize(parameter.name)}</label>
                                        <Field name={parameter.name} component="Input" type="text" pattern={pattern}/>
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