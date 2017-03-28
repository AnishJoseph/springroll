import React from 'react';
import Application from 'App';
import DatePicker from 'DatePicker';
import Select from 'Select.jsx';

class ReportParams extends React.Component {
    constructor(props){
        super(props);
        this.onChange = this.onChange.bind(this);
        this.onTextInput = this.onTextInput.bind(this);
        this.state = {paramValues : this.props.paramValues}
    }

    onChange(paramName, value){
        console.log("Param " + paramName + " changed to " + value);
        let newValue = {};
        newValue[paramName] = value;
        this.setState({paramValues : Object.assign({}, this.state.paramValues, newValue )});
    }
    onTextInput(paramName, value){
        this.onChange(paramName, value);
    }

    render() {
        return (
           <div className="panel panel-default">
               <div className="panel-body">
                   <form className="form-inline report-param-form">
                       <div className="container-fluid">
                       {
                           this.props.params.map((parameter) => {
                               if(parameter.lovList != null){
                                return (
                                    <div key={parameter.name} className="form-group rep-param col-md-3">
                                        <div>{Application.Localize(parameter.name)}</div>
                                        <Select key={parameter.name} options={parameter.lovList} multiSelect={parameter.multiSelect} onChange={ (value) => this.onChange(parameter.name, value)}  value={this.state.paramValues[parameter.name]}/>
                                    </div>
                                )
                               } else if ( parameter.javaType == "java.lang.Integer" || parameter.javaType == "java.lang.Long" || parameter.javaType == "java.math.BigInteger" || parameter.javaType == "java.lang.Short"){
                                   return (
                                       <div key={parameter.name} className="form-group rep-param col-md-3">
                                           <div>{Application.Localize(parameter.name)}</div>
                                           <input required className="form-control " type="number" step="1" onChange={ (value) => this.onChange(parameter.name, value.target.value)}   value={this.state.paramValues[parameter.name]}/>
                                       </div>
                                   )
                               } else if ( parameter.javaType == "java.lang.Double"  || parameter.javaType == "java.lang.Float" || parameter.javaType == "java.math.BigDecimal"){
                                   return (
                                       <div key={parameter.name} className="form-group rep-param col-md-3">
                                           <div>{Application.Localize(parameter.name)}</div>
                                           <input required className="form-control " type="number" onChange={ (value) => this.onChange(parameter.name, value.target.value)}  value={this.state.paramValues[parameter.name]}/>
                                       </div>
                                   )
                               } else if (parameter.javaType == "java.lang.Boolean"){
                                   let lovList = [{value : true, label : Application.Localize('ui.true')}, {value : false, label : Application.Localize('ui.false')}];
                                   return (
                                       <div key={parameter.name} className="form-group rep-param col-md-3">
                                           <div>{Application.Localize(parameter.name)}</div>
                                           <Select key={parameter.name} options={lovList} multiSelect={false} onChange={ (value) => this.onChange(parameter.name, value)}   value={this.state.paramValues[parameter.name]} />
                                       </div>
                                   )
                               } else if (parameter.javaType == "java.time.LocalDate" || (parameter.javaType == "java.time.LocalDateTime" && (parameter.setTime === 'START_OF_DAY' || parameter.setTime === 'END_OF_DAY'))){
                                   return (
                                       <div key={parameter.name} className="form-group rep-param col-md-3">
                                           <div>{Application.Localize(parameter.name)}</div>
                                           <DatePicker onChange={ (value) => this.onChange(parameter.name, value)}  value={this.state.paramValues[parameter.name]}/>
                                       </div>
                                   )
                               } else if (parameter.javaType == "java.time.LocalDateTime"){
                                   return (
                                       <div key={parameter.name} className="form-group rep-param col-md-3">
                                           <div>{Application.Localize(parameter.name)}</div>
                                           //<DupDateTimePicker onChange={ (value) => this.onChange(parameter.name, value)}  value={this.state.paramValues[parameter.name]}/>
                                           <div>DATETIME TBD</div>
                                       </div>
                                   )
                               } else {
                                   return (
                                       <div key={parameter.name} className="form-group rep-param col-md-3">
                                           <div>{Application.Localize(parameter.name)}</div>
                                           <input required className="form-control " value={this.state.paramValues[parameter.name]} type="text" onChange={ (value) => this.onTextInput(parameter.name, value.target.value)}  value={this.state.paramValues[parameter.name]}/>
                                       </div>
                                   )
                               }
                           })
                       }
                       </div>
                       <div  className="formSubmit">
                           <span title={Application.Localize('Apply')} onClick={() => this.props.onParamsSelected(this.state.paramValues)} className="form-control submit glyphicon glyphicon-ok"></span>
                       </div>
                   </form>
               </div>
           </div>
        );
    }
}

export default ReportParams;




