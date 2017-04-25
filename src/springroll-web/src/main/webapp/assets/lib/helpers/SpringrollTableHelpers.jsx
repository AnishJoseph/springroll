import React from 'react';
import Application from 'App';
import {DatePicker, DateField} from 'react-date-picker';
var moment = require('moment');
import {isBoolean, isNumber, map}  from 'lodash';
var numeral = require('numeral');
import ReactSelect from 'react-select';


export function dateTimeSorter(a, b, order, sortField){
    let aValue = a[sortField] == null ? 0: a[sortField];
    let bValue = b[sortField] == null ? 0: b[sortField];
    if (order === 'desc') {
        return aValue - bValue;
    } else {
        return bValue - aValue;
    }
}

export const selectEditor = (onUpdate, props) => {
    return (<ReactSelect
        ref='inputRef'
        options={props.options}
        onBlur={() => onUpdate(props.defaultValue)}
        onChange={ value => {
            if (value == null)return;
            let choices = undefined;
            if (props.multi) {
                choices = map(value, 'value');
            } else {
                choices = value.value;
            }
            let changedRowData = {id : props.row['id'], cid : props.row['cid'], cellName : props.cellName, cellValue : choices};
            props.onRowChange(changedRowData);
        }}
        multi={props.multi}
        value={props.defaultValue}
    />)
};

export const dateEditor = (onUpdate, props) => (<DateEditor onUpdate={ onUpdate } {...props}/>);

class DateEditor extends React.Component {
    focus() {
        //FIXME - need to talk to react-bootstrap-table and see if we can avoid this dummy focus
    }
    render() {
        let updateOnDateClick = !this.props.isDateTime;

        let dateFormat = this.props.isDateTime ? Application.getMomentFormatForDateTime() : Application.getMomentFormatForDate();
        return (<DateField
                ref='inputRef'
                value={this.props.defaultValue === undefined || this.props.defaultValue == ''? undefined: moment(this.props.defaultValue)}
                dateFormat={dateFormat}
                expanded={true}
                updateOnDateClick={updateOnDateClick}
                onChange={ (dateString, {dateMoment}) => {
                    if(dateMoment == null || dateMoment == undefined)return;
                    let changedRowData = {id : this.props.row['id'], cid : this.props.row['cid'], cellName : this.props.cellName, cellValue : dateMoment.valueOf()};
                    this.props.onRowChange(changedRowData);
                    this.props.onUpdate(this.props.defaultValue);
                }}>
            </DateField>
        )
    }
}

export const patternEditor = (onUpdate, props) => (<PatternEditor onUpdate={ onUpdate } {...props}/>);

class PatternEditor extends React.Component {
    constructor(props){
        super(props);
        this.onChange = this.onChange.bind(this);
        this.onBlur = this.onBlur.bind(this);
        this.pattern = props.type == 'int'? intPattern : props.type == 'num' ? floatPattern : undefined;
    }
    onBlur(){
        this.props.onUpdate(this.props.defaultValue);
    }
    focus() {
        console.log("focus");
        //FIXME - need to talk to react-bootstrap-table and see if we can avoid this dummy focus
    }
    onChange(event){
        let value = event.target.value;
        if(this.pattern === undefined || this.pattern.test(value)){
            let changedRowData = {id : this.props.row['id'], cid : this.props.row['cid'], cellName : this.props.cellName, cellValue : value};
            this.props.onRowChange(changedRowData);
        }
    }

    render() {
        return (
            <input autoFocus style={{width :"100%"}} type="text" onChange={this.onChange} value={this.props.defaultValue} onBlur={this.onBlur}/>
        )
    }
}

export const customEditorHandler = (onUpdate, props) => (<props.editor onUpdate={ onUpdate } {...props}/>);

export const floatPattern = new RegExp("^[-+]?[0-9]*\\.?[0-9]*$");
export const intPattern = new RegExp("^[-+]?[0-9]*$");

export function WrapperForFormatter(cell, formatter, coDef, row, updateResponse) {
    let Formatter = formatter;
    let className = 'hidden';
    let errorMessage = '';
    if(updateResponse !== undefined) {
        let cid = row['cid'];
        let errors = updateResponse[cid];
        if (errors !== undefined) {
            errorMessage = errors[coDef['name']];
            if(errorMessage !== undefined) className = 'mdm-error glyphicon glyphicon-warning-sign';
        }
    }
    return (
        <span>
            <Formatter value={cell} colDef={coDef}/>
            <div className={className}><span style={{paddingLeft: "4px"}}>{errorMessage}</span></div>
        </span>
    );
}

export const DeleteFormatter = (cell, row, onDeleteRow) => {
    let classValue = row['id'] === -1 && row['rowIsNew']? "springroll-icon glyphicon glyphicon-trash" : 'springroll-icon glyphicon glyphicon-trash icon-muted';
    return (
        <span style={{paddingLeft : 5 +'px'}}
              data-toggle="tooltip"
              title={Application.Localize('ui.mdm.delete')}
              className={classValue} onClick={() => onDeleteRow(row)}>
        </span>
    );
};


export const DateToString = (value) => {
    return moment(value).format(Application.getMomentFormatForDate());
};
export const DateTimeToString = (value) => {
    return moment(value).format(Application.getMomentFormatForDateTime());
};
export const BooleanToString = (value) => {
    return value ? Application.Localize('ui.true') : Application.Localize('ui.false');
};

export const ArrayFormatter = ( {value}) => {
    let valueToDisplay = undefined;
    if(value !== undefined && value !== null && Array.isArray(value)) {
        valueToDisplay = value.join();
    }
    return (
        <div> {valueToDisplay} </div>
    );
};
export const BooleanFormatter = ( {value}) => {
    let valueToDisplay;
    if(value !== undefined && isBoolean(value)){
        valueToDisplay = BooleanToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};
export const TextFormatter = ( {value}) => <div> {value} </div>

export const DateFormatter = ({value}) => {
    let valueToDisplay;
    if(value !== undefined && isNumber(value)){
        valueToDisplay = DateToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export const DateTimeFormatter = ({value}) => {
    let valueToDisplay;
    if(value !== undefined && isNumber(value)){
        valueToDisplay = DateTimeToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};
export const NumberFormatter = ({value, colDef}) => {
    return (
        <div> { numeral(value).format(colDef.format)} </div>
    );
};