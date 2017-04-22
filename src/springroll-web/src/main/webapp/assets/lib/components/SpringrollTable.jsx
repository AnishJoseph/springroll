import React from 'react';
import Application from 'App';
import {intPattern, floatPattern, DeleteFormatter, WrapperForFormatter, TextFormatter, DateTimeFormatter, DateFormatter, BooleanFormatter, ArrayFormatter} from 'Formatters';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import ReactSelect from 'react-select';
import {DatePicker, DateField} from 'react-date-picker';
var moment = require('moment');
import {each, pluck, indexOf, filter}  from 'lodash';
import DebounceInput from 'react-debounce-input';

/* SpringrollTable props
    1) data*
    2) columnDefinitions*
    3) options - object with Editors, sorters and formatters
    5) Toolbar
    6) isEditable 
    7) trClassFormat - function
    8) title - string
    9) height
    10) key


    coldef :
 type
 lovList
 multiSelect
 width
 hidden
 name
 title
 format (for numbers - numeral.js
 sortable
    
 */

function dateTimeSorter(a, b, order, sortField){
    let aValue = a[sortField] == null ? 0: a[sortField];
    let bValue = b[sortField] == null ? 0: b[sortField];
    if (order === 'desc') {
        return aValue - bValue;
    } else {
        return bValue - aValue;
    }
}

const dateEditor = (onUpdate, props) => (<DateEditor onUpdate={ onUpdate } {...props}/>);

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
                    this.props.onMdmMasterRowChanged(changedRowData);
                    this.props.onUpdate(this.props.defaultValue);
                }}>
            </DateField>
        )
    }
}

const patternEditor = (onUpdate, props) => (<PatternEditor onUpdate={ onUpdate } {...props}/>);

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
            this.props.onMdmMasterRowChanged(changedRowData);
        }
    }

    render() {
        return (
            <input autoFocus style={{width :"100%"}} type="text" onChange={this.onChange} value={this.props.defaultValue} onBlur={this.onBlur}/>
        )
    }
}

const customEditorHandler = (onUpdate, props) => (<props.editor onUpdate={ onUpdate } {...props}/>);

class SpringrollTable extends React.Component {
    constructor(props){
        super(props);
        this.beforeSaveCell = this.beforeSaveCell.bind(this);
        this.afterSearch = this.afterSearch.bind(this);
        this.search = this.search.bind(this);
        this.editable = this.editable.bind(this);
        this.trClassFormat = this.trClassFormat.bind(this);
    }
    search(e){
        this.refs.table.handleSearch(e.target.value);
    }

    editable(cell, row, rowIndex, columnIndex){
        return this.props.editable ? this.props.editable(cell, row, rowIndex, columnIndex):  false;
    }

    afterSearch(searchText, result){
        this.currentRows = result;
    }
    beforeSaveCell(row, cellName, cellValue) {
        /* We always return false as we dont want react-bootstrap-table to change anything.
           The editor would already have fired an event to change the value in the redux store
         */
        return false;
    }
    trClassFormat(row, rowIndex){
        this.props.trClassFormat(row, rowIndex);
    }
    render() {
        const cellEditProp = {
            mode: 'click',
            afterSearch: this.afterSearch,
            blurToSave: true,
            beforeSaveCell: this.beforeSaveCell
        };
        return (
            <span>
                <div className="control-panel">
                    <div className="row">
                        <span className="text-info toolbar-title">{this.props.title}</span>
                        <DebounceInput className="pull-right" minLength={2} debounceTimeout={300} onChange={this.search} placeholder={Application.Localize('ui.search')}/>
                    </div>
                </div>
                <BootstrapTable ref="table" data={this.props.data} striped hover search={false} keyField={this.props.keyName} height={this.props.height || '800px'} scrollTop={ 'Top' } multiColumnSort={3} cellEdit={ cellEditProp } trClassName={ this.props.trClassFormat }>
                    {
                        this.props.columnDefinitions.map((colDef, index) => {
                            let formatter = TextFormatter, dataFormatter, sorter = undefined, customEditor = undefined, filterValue, filterFormatted=false;
                            if (colDef.type == 'date' || colDef.type === 'datetime') {
                                let isDateTime =  colDef.type === 'datetime';
                                formatter = isDateTime ? DateTimeFormatter : DateFormatter;
                                customEditor = {getElement : dateEditor, customEditorParameters : {isDateTime : isDateTime}};
                                sorter = dateTimeSorter;
                                filterValue = (cell, row) => isDateTime ? moment(cell).format(Application.getMomentFormatForDateTime()) : moment(cell).format(Application.getMomentFormatForDate());
                            } else if (colDef.type == 'boolean') {
                                formatter = BooleanFormatter;
                                customEditor = {getElement : selectEditor, customEditorParameters : {options : booleanLovList, multi : false}};
                            } else if (colDef.type == 'int') {
                                customEditor = {getElement : patternEditor, customEditorParameters : {type : colDef.type}};
                            } else if (colDef.type == 'num') {
                                customEditor = {getElement : patternEditor, customEditorParameters : {type : colDef.type}};
                            } else {
                                customEditor = {getElement : patternEditor, customEditorParameters : {type : colDef.type}};
                            }

                            if(colDef.lovList != undefined && colDef.lovList != null){
                                customEditor = {getElement : selectEditor, customEditorParameters : {options : colDef.lovList, multi : colDef.multiSelect}};
                                if(colDef.multiSelect) formatter = ArrayFormatter;
                            }
                            if(customEditor !== undefined) {
                                customEditor.customEditorParameters.cellName =  colDef.name;
                            }

                            /* If the caller has specified a formatter (tied to a type) then use that formatter - it overrides everything else */
                            if (this.props.options.formatters && this.props.options.formatters[colDef.type] && this.props.options.formatters[colDef.type].forDisplay) {
                                formatter = this.props.options.formatters[colDef.type].forDisplay;
                            }
                            /* If the caller has specified a sorter (tied to a type) then use that sorter - it overrides everything else */
                            if (this.props.options.sorter && this.props.options.sorter[colDef.type]) {
                                sorter = this.props.options.sorter[colDef.type];
                            }
                            /* If the caller has specified an editor (tied to a type) then use that editor - it overrides everything else */
                            if (this.props.options.editor && this.props.options.editor[colDef.type]) {
                                customEditor = {getElement : customEditorHandler, customEditorParameters : {editor :this.props.options.editor[colDef.type],  colDef : colDef}};
                            }

                            dataFormatter = (cell, row) => WrapperForFormatter(cell, formatter, colDef, row);
                            return (
                                <TableHeaderColumn
                                    dataSort={colDef.sortable === undefined? true : colDef.sortable}
                                    sortFunc={sorter}
                                    width={colDef.width}
                                    hidden={colDef.hidden}
                                    filterFormatted={filterFormatted}
                                    key={index}
                                    dataFormat={dataFormatter}
                                    dataField={colDef.name}
                                    customEditor={ customEditor }
                                    editable={ this.editable }
                                    filterValue={filterValue }
                                    searchable={this.props.searchable === undefined ? true : this.props.searchable}
                                >
                                    {Application.Localize(colDef.title)}
                                </TableHeaderColumn>
                            )
                        })
                    }
                </BootstrapTable>
            </span>
        );
    }
}


export default SpringrollTable;




