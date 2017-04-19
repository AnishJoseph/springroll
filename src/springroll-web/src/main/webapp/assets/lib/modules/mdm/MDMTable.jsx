import React from 'react';
import Application from 'App';
import {intPattern, floatPattern, DeleteFormatter, WrapperForFormatter, TextFormatter, DateTimeFormatter, DateFormatter, BooleanFormatter, ArrayFormatter} from 'Formatters';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import ReactSelect from 'react-select';
import {DatePicker, DateField} from 'react-date-picker';
var moment = require('moment');
import MdmToolbar from 'MdmToolbar';

/* Fixme there are 2 booleanLovList */
export const booleanLovList = [{value : true, label : Application.Localize('ui.true')}, {value : false, label : Application.Localize('ui.false')}];

const selectEditor = (onUpdate, props) => {
    return (<ReactSelect
        ref='inputRef'
        options={props.options}
        onBlur={() => onUpdate(props.defaultValue)}
        onChange={ value => {
            if (value == null)return;
            let choices = undefined;
            if (props.multi) {
                choices = _.pluck(value, 'value');
            } else {
                choices = value.value;
            }
            let changedRowData = {id : props.row['id'], cid : props.row['cid'], cellName : props.cellName, cellValue : choices};
            props.onMdmMasterRowChanged(changedRowData);
        }}
        multi={props.multi}
        value={props.defaultValue}
    />)
};
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

class MDMGrid extends React.Component {
    constructor(props){
        super(props);
        this.beforeSaveCell = this.beforeSaveCell.bind(this);
        this.saveClicked = this.saveClicked.bind(this);
        this.search = this.search.bind(this);
        this.editable = this.editable.bind(this);
        this.deleteRow = this.deleteRow.bind(this);
        this.onShowModified = this.onShowModified.bind(this);
        this.trClassFormat = this.trClassFormat.bind(this);
        this.isModifiedFilterOn = false;
    }
    search(e){
        this.refs.table.handleSearch(e.target.value);
    }
    onShowModified(){
        let filterSpec = this.isModifiedFilterOn ? {} : { __hasChanged: "__hasChanged"};
        this.refs.table.handleFilterData(filterSpec);
        this.isModifiedFilterOn = !this.isModifiedFilterOn;
    }
    deleteRow(row){
        /* Fire a delete event ONLY if this is a new row */
        if(row['id'] === -1 && row['rowIsNew'] === true){
            this.props.onMdmMasterDeleteRow(row['cid']);
        }
    }
    /* FIXME - Editable seems to have a bug - even if we return false here the custom editor is shown */
    editable(cell, row, rowIndex, columnIndex){
        if(this.props.updateInProgress) return false;
        if(row['id'] === -1 && row['rowIsNew'] === true)return true;
        if (this.props.masterData.colDefs[columnIndex].writeable === false)
            return false;
        if(_.indexOf(this.props.masterData.recIdsUnderReview, row['id'], 0) !== -1)
            return false;
        return true;
    }
    saveClicked(){
        let changedRecords = [], newRecords = [];
        /* Marshall the changes to existing rows into MdmDTO */
        _.each(Object.keys(this.props.changedRowData || {}),  rowId => {
            let mdmChangedColumns = {};
            let rowChanges = this.props.changedRowData[rowId];
            /* This is an existing row that has changed */
            _.each(Object.keys(rowChanges), colName => {
                if(colName === 'id')return;
                mdmChangedColumns[colName] = rowChanges[colName];
            });
            let changedRecord = {id : rowChanges['id'], mdmChangedColumns : mdmChangedColumns};
            mdmChangedColumns['cid'] = {val : rowId};
            changedRecords.push(changedRecord);
        });

        /* Marshall the new records into MdmDTO */
        _.each(Object.keys(this.props.newRowData || {}), rowId => {
            let attrsForNewRecord = {};
            let rowChanges = this.props.newRowData[rowId];
            _.each(Object.keys(rowChanges), colName => {
                if(colName === 'id') return;
                attrsForNewRecord[colName] = rowChanges[colName];
            });
            attrsForNewRecord['cid'] = rowId;
            newRecords.push(attrsForNewRecord);
        });

        let mdmDTO = {master : this.props.masterData.master, changedRecords : changedRecords, newRecords : newRecords};
        this.props.onMdmMasterUpdateStarted();
        this.props.onMdmMasterChanged(mdmDTO);
    }

    beforeSaveCell(row, cellName, cellValue) {
        /* We always return false as we dont want react-bootstrap-table to change anything.
           The editor would alreacy have fired an event to chnage the value in the redux store
         */
        return false;
    }
    trClassFormat(row, rowIndex){
        if(row['id'] === -1 && row['rowIsNew'] === undefined)
            return 'mdm-disabled';
        if(row['__hasChanged'] === '__hasChanged')
            return 'mdm-changed';

        return "mdmTable";
    }
    render() {
        const cellEditProp = {
            mode: 'click',
            blurToSave: true,
            beforeSaveCell: this.beforeSaveCell
        };
        let title = Application.Localize('ui.mdm.title', Application.Localize('ui.mdm.master.'+ this.props.masterData.master));
        let needsSave = this.props.changedRowData !== undefined || this.props.newRowData !== undefined;
        /* Filter out any deleted row */
        let data = _.filter(this.props.masterData.data, row => row !== null);
        return (
            <span>
                <MdmToolbar title={title} enableAddRow={true} onMdmMasterAddRow={this.props.onMdmMasterAddRow} enableFilter={needsSave} onShowModified={this.onShowModified} onSaveClicked={this.saveClicked} needsSave={needsSave} onSearch={this.search}/>
                <BootstrapTable ref="table" data={data} striped hover search={false}
                                keyField={'id'} height='800px' scrollTop={ 'Top' }
                                multiColumnSort={3}
                                cellEdit={ cellEditProp }
                                trClassName={ this.trClassFormat }
                >
                    {
                        this.props.masterData.colDefs.map((colDef, index) => {
                            let formatter = TextFormatter, dataFormatter, sorter = undefined, customEditor = undefined;
                            if (colDef.type == 'date') {
                                formatter = DateFormatter;
                                customEditor = {getElement : dateEditor, customEditorParameters : {isDateTime : false}};
                            } else if (colDef.type == 'datetime') {
                                formatter = DateTimeFormatter;
                                customEditor = {getElement : dateEditor, customEditorParameters : {isDateTime : true}};
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
                                customEditor.customEditorParameters.onMdmMasterRowChanged =  this.props.onMdmMasterRowChanged;
                                customEditor.customEditorParameters.cellName =  colDef.name;
                            }

                            dataFormatter = (cell, row) => WrapperForFormatter(cell, formatter, colDef, row, this.props.updateResponse);
                            return (
                                <TableHeaderColumn
                                    width={colDef.width}
                                    hidden={colDef.name === 'id'}
                                    filterFormatted
                                    key={index}
                                    dataFormat={dataFormatter}
                                    dataField={colDef.name}
                                    customEditor={ customEditor }
                                    editable={ this.editable }>
                                    {Application.Localize(colDef.name)}
                                </TableHeaderColumn>
                            )
                        })
                    }
                    <TableHeaderColumn width={"50px"} key={5000} dataField={'delete'} editable={ false } dataFormat={(cell, row) => DeleteFormatter(cell, row, this.deleteRow)}> {''} </TableHeaderColumn>
                    <TableHeaderColumn hidden={true} width={"50px"} key={5000} dataField={'__hasChanged'} editable={ false }> {''} </TableHeaderColumn>
                </BootstrapTable>
            </span>
        );
    }
}


export default MDMGrid;




