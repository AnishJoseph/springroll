import React from 'react';
import Application from 'App';
import DatePicker from 'DatePicker';
import Select from 'Select.jsx';
import Input from 'Input.jsx';

const floatPattern = new RegExp("^[-+]?[0-9]*\\.?[0-9]*$");
const intPattern = new RegExp("^[-+]?[0-9]*$");

class ReportParams extends React.Component {
    constructor(props){
        super(props);
        this.onChange = this.onChange.bind(this);
        this.getErrorIcon = this.getErrorIcon.bind(this);
        this.onSubmitClicked = this.onSubmitClicked.bind(this);
        this.state = {paramValues : {}, paramsWithError : {}}
    }

    onChange(paramName, value){
        console.log("Param " + paramName + " changed to " + value);
        let newValue = {};
        newValue[paramName] = value;
        this.setState({paramValues : Object.assign({}, this.state.paramValues, newValue )});
        if(value !== undefined){
            let newErrorState = Object.assign({}, this.state.paramsWithError);
            delete newErrorState[paramName];
            this.setState({paramsWithError : newErrorState});
        }
    }
    getErrorIcon(parameterName){
        if(this.state.paramsWithError[parameterName] !== undefined)
            return <span title={Application.Localize('ui.report.parameters.empty')} className="error-icon glyphicon glyphicon-warning-sign"></span>
        return;
    }
    onSubmitClicked(){
        var that = this;
        let hasErrors = false;
        /* Validate the params (as much as possible) */
        let newErrorState = Object.assign({}, this.state.paramsWithError);
        _.each(this.props.params, function(param){
            if(param.visible && (that.state.paramValues[param.name] == undefined || that.state.paramValues[param.name] === '')){
                newErrorState[param.name] = true;
                hasErrors = true;
            } else {
                delete newErrorState[param.name];
            }
        });
        this.setState({paramsWithError : newErrorState});
        if(hasErrors){
            return;
        }
        this.props.onParamsSelected(this.state.paramValues);
    }
    render() {
        let pattern = undefined;
        var that = this;
        return (
           <div className="panel panel-default">
               <div className="panel-body">
                   <form className="report-param-form">
                       <div className="container-fluid">
                       {
                           this.props.params.map((parameter) => {
                               let errorIcon = that.getErrorIcon(parameter.name);
                               if(parameter.lovList != null){
                                return (
                                    <div key={parameter.name} className="form-group rep-param col-md-3">
                                        <div>{errorIcon}{Application.Localize(parameter.name)}</div>
                                        <Select className="form-control"  key={parameter.name} options={parameter.lovList} multiSelect={parameter.multiSelect} onChange={ (value) => this.onChange(parameter.name, value)}  value={this.state.paramValues[parameter.name]}/>
                                    </div>
                                )
                               } else if (parameter.javaType == "java.lang.Boolean"){
                                   let lovList = [{value : true, label : Application.Localize('ui.true')}, {value : false, label : Application.Localize('ui.false')}];
                                   return (
                                       <div key={parameter.name} className="form-group rep-param col-md-3">
                                           <div>{errorIcon}{Application.Localize(parameter.name)}</div>
                                           <Select className="form-control" key={parameter.name} options={lovList} multiSelect={false} onChange={ (value) => this.onChange(parameter.name, value)}   value={this.state.paramValues[parameter.name]} />
                                       </div>
                                   )
                               } else if (parameter.javaType == "java.time.LocalDate" || (parameter.javaType == "java.time.LocalDateTime" && (parameter.setTime === 'START_OF_DAY' || parameter.setTime === 'END_OF_DAY'))){
                                   return (
                                       <div key={parameter.name} className="form-group rep-param col-md-3">
                                           <div>{errorIcon}{Application.Localize(parameter.name)}</div>
                                           <DatePicker onChange={ (value) => this.onChange(parameter.name, value)}  value={this.state.paramValues[parameter.name]}/>
                                       </div>
                                   )
                               } else if (parameter.javaType == "java.time.LocalDateTime"){
                                   return (
                                       <div key={parameter.name} className="form-group rep-param col-md-3">
                                           <div>{errorIcon}{Application.Localize(parameter.name)}</div>
                                           //<DupDateTimePicker onChange={ (value) => this.onChange(parameter.name, value)}  value={this.state.paramValues[parameter.name]}/>
                                           <div>DATETIME TBD</div>
                                       </div>
                                   )
                               } else if ( parameter.javaType == "java.lang.Integer" || parameter.javaType == "java.lang.Long" || parameter.javaType == "java.math.BigInteger" || parameter.javaType == "java.lang.Short"){
                                   pattern = intPattern;
                               } else if ( parameter.javaType == "java.lang.Double"  || parameter.javaType == "java.lang.Float" || parameter.javaType == "java.math.BigDecimal"){
                                   pattern = floatPattern;
                               }
                               return (
                                   <div key={parameter.name} className="form-group rep-param col-md-3">
                                       <div>{errorIcon}{Application.Localize(parameter.name)}</div>
                                       <Input pattern={pattern} classes="form-control " value={this.state.paramValues[parameter.name]} onChange={ (value) => this.onChange(parameter.name, value)} />
                                   </div>
                               )
                           })
                       }
                       </div>
                       <div  className="formSubmit">
                           <span title={Application.Localize('Apply')} onClick={this.onSubmitClicked} className=" submit glyphicon glyphicon-ok"></span>
                       </div>
                   </form>
               </div>
           </div>
        );
    }
    componentWillReceiveProps(nextProps) {
        this.setState({paramValues : Object.assign({}, this.state.paramValues, this.props.paramValues)});
    }


}

export default ReportParams;




