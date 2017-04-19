import React from 'react';
import Application from 'App';
import {DeleteFormatter, WrapperForFormatter, TextFormatter, DateTimeFormatter, DateFormatter, BooleanFormatter} from 'Formatters';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import ReactSelect from 'react-select';
import {DatePicker, DateField} from 'react-date-picker';
var moment = require('moment');
import MdmToolbar from 'MdmToolbar';

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
            value={this.props.defaultValue === undefined ? undefined : moment(this.props.defaultValue)}
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
    }
    search(e){
        this.refs.table.handleSearch(e.target.value);
    }
    deleteRow(row){
        if(row['id'] !== -1)return;
        // delete this.newRows[row['cid']];
        this.props.onMdmMasterDeleteRow(row['cid']);
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
        /* We always return false as we dont want react-bootstrap-table to change anything. Instead
           we fire and event that will change the values in the redux store 
         */
        // let changedRowData = {id : row['id'], cid : row['cid'], cellName : cellName, cellValue : cellValue};
        // this.props.onMdmMasterRowChanged(changedRowData);
        return false;
    }
    
    render() {
        const cellEditProp = {
            mode: 'click',
            blurToSave: true,
            beforeSaveCell: this.beforeSaveCell
        };
        let title = Application.Localize('ui.mdm.title', Application.Localize('ui.mdm.master.'+ this.props.masterData.master));

        /* Filter out any deleted row */
        let data = _.filter(this.props.masterData.data, row => row !== null);
        return (
            <span>
                <MdmToolbar title={title} enableAddRow={true} onMdmMasterAddRow={this.props.onMdmMasterAddRow} enableFilter={true} onSaveClicked={this.saveClicked} needsSave={this.props.changedRowData !== undefined || this.props.newRowData !== undefined} onSearch={this.search}/>
                <BootstrapTable ref="table" data={data} striped hover search={false}
                                keyField={'id'} height='800px' scrollTop={ 'Top' }
                                multiColumnSort={3}
                                cellEdit={ cellEditProp }
                                trClassName="mdmTable"
                >
                    {
                        this.props.masterData.colDefs.map((colDef, index) => {
                            let formatter = TextFormatter, dataFormatter, sorter = undefined, customEditor = undefined;
                            if (colDef.type == 'date') {
                                formatter = DateFormatter;
                                customEditor = {getElement : dateEditor, customEditorParameters : {isDateTime : false}};
                            }
                            if (colDef.type == 'datetime') {
                                formatter = DateTimeFormatter;
                                customEditor = {getElement : dateEditor, customEditorParameters : {isDateTime : true}};
                            }
                            if (colDef.type == 'boolean') {
                                formatter = BooleanFormatter;
                                customEditor = {getElement : selectEditor, customEditorParameters : {options : booleanLovList, multi : false}};
                            }

                            if(colDef.lovList != undefined && colDef.lovList != null){
                                customEditor = {getElement : selectEditor, customEditorParameters : {options : colDef.lovList, multi : colDef.multiSelect}};
                            }
                            if(customEditor !== undefined) {
                                customEditor.customEditorParameters.onMdmMasterRowChanged =  this.props.onMdmMasterRowChanged;
                                customEditor.customEditorParameters.cellName =  colDef.name;
                            }

                            dataFormatter = (cell, row) => WrapperForFormatter(cell, formatter, colDef, row, this.props.updateResponse);
                            return (
                                <TableHeaderColumn
                                    hidden={colDef.name === 'id'}
                                    filterFormatted
                                    tdStyle={ {whiteSpace: 'noWrap'} }
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
                </BootstrapTable>
            </span>
        );
    }
}


export default MDMGrid;




