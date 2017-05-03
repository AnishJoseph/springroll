import React from 'react';
import Application from 'App';
import {DatePicker, DateField} from 'react-date-picker';
var moment = require('moment');
import {isBoolean, isNumber, map}  from 'lodash';
var numeral = require('numeral');
import ReactSelect from 'react-select';
import {DateToString, DateTimeToString, BooleanToString, NumberFormatter, intPattern, floatPattern} from 'Formatters';


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
            if(event.keyCode === 9)props.onKeyDown(event);
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
            <input autoFocus onKeyDown={this.props.onKeyDown} style={{width :"100%"}} type="text" onChange={this.onChange} value={this.props.defaultValue} onBlur={this.onBlur}/>
        )
    }
}

export const customEditorHandler = (onUpdate, props) => (<props.editor onUpdate={ onUpdate } {...props}/>);

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
    let classValue = row['id'] === -1 && row['rowIsNew']? "control-panel-icon glyphicon glyphicon-trash" : 'control-panel-icon glyphicon glyphicon-trash icon-muted';
    return (
        <span style={{paddingLeft : 5 +'px'}}
              data-toggle="tooltip"
              title={Application.Localize('ui.mdm.delete')}
              className={classValue} onClick={() => onDeleteRow(row)}>
        </span>
    );
};

export const GridNumberFormatter = ({value, colDef}) => {
    return <NumberFormatter value={value} format={colDef.format}/>
};