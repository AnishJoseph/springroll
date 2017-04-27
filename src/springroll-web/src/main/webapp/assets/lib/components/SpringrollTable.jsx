import React from 'react';
import Application from 'App';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
var moment = require('moment');
import {each, map, indexOf, filter}  from 'lodash';
import DebounceInput from 'react-debounce-input';
import {dateTimeSorter, selectEditor, dateEditor, patternEditor, customEditorHandler} from 'SpringrollTableHelpers';
import {intPattern, floatPattern, DeleteFormatter, WrapperForFormatter, TextFormatter, DateTimeFormatter, DateFormatter, BooleanFormatter, ArrayFormatter, NumberFormatter, BooleanToString, DateTimeToString, DateToString} from 'SpringrollTableHelpers';
import {CSVLink} from 'react-csv';
var json2csv = require('json2csv');

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
    11) onUpdate


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
export const booleanLovList = [{value : true, label : Application.Localize('ui.true')}, {value : false, label : Application.Localize('ui.false')}];

class SpringrollTable extends React.Component {
    constructor(props){
        super(props);
        this.beforeSaveCell = this.beforeSaveCell.bind(this);
        this.afterSearch = this.afterSearch.bind(this);
        this.search = this.search.bind(this);
        this.download = this.download.bind(this);
        this.showModified = this.showModified.bind(this);
        this.trClassFormat = this.trClassFormat.bind(this);
        this.deleteRow = this.deleteRow.bind(this);
        this.isModifiedFilterOn = false;
        this.state = {dataToDownload : []};
    }
    search(e){
        this.refs.table.handleSearch(e.target.value);
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
    showModified(){
        let filterSpec = this.isModifiedFilterOn ? {} : { __hasChanged: "__hasChanged"};
        this.refs.table.handleFilterData(filterSpec);
        this.isModifiedFilterOn = !this.isModifiedFilterOn;
    }
    deleteRow(row){
        /* Fire a delete event ONLY if this is a new row */
        if(row['id'] === -1 && row['rowIsNew'] === true){
            this.props.onDeleteRow(row['cid']);
        }
    }
    download(){
        let options = this.props.options || {exportFormatters : {}};
        var that = this;
        /* this.currentRows has the filtered set of rows (if a search was done) - if undefined it means that no filtering was done */
        let rowsToDownload = this.currentRows;
        if(this.currentRows == undefined){
            rowsToDownload = this.props.data;
        }
        let fields = [];
        each(this.props.columnDefinitions, function (colDef) {
            var name = colDef.name;
            if(colDef.hidden)return;
            var fldDefn = {
                label: Application.Localize(colDef.title),
                value: colDef.name,
                default: ''
            };
            if (colDef.type == 'boolean') {
                fldDefn['value'] = function (row, field, data) {
                    return BooleanToString(row[name]);
                }
            }
            if (colDef.type == 'date') {
                fldDefn['value'] = function (row, field, data) {
                    return DateToString(row[name]);
                }
            }
            if (colDef.type == 'datetime') {
                fldDefn['value'] = function (row, field, data) {
                    return DateTimeToString(row[name]);
                }
            }
            /* If the caller has specified a formatter (tied to a type) then use that formatter - it overrides everything else */
            if (options.exportFormatters !== undefined && options.exportFormatters[colDef.name]) {
                fldDefn['value'] = function (row, field, data) {
                    return options.exportFormatters[colDef.name](row[colDef.name]);
                }
            }

            fields.push(fldDefn);

        });
        this.setState({dataToDownload :json2csv({ data: rowsToDownload, fields: fields })});
    }
    render() {
        var that = this;
        let customButtons = this.props.customButtons || [];
        let options = this.props.options || {};
        const tableOptions = {
            afterSearch: this.afterSearch,
        };
        const cellEditProp = {
            mode: 'click',
            blurToSave: true,
            beforeSaveCell: this.beforeSaveCell
        };
        return (
            <span>
                <div className="control-panel">
                    <div className="row">
                        <span className="text-info toolbar-title">{this.props.title}</span>
                        {
                            customButtons.map ( (button, index) => {
                                if(button.visible)
                                return (
                                    <span key={index} data-toggle="tooltip"
                                          title={button.title}
                                          onClick={button.onClick}
                                          className={button.className}>
                                    </span>
                                )
                                return null;
                            })
                        }
                        {
                            this.props.onAddRow && this.props.needsSave &&
                            <span data-toggle="tooltip" title={Application.Localize('ui.mdm.Save')}
                                  onClick={this.props.onSaveClicked}
                                  className="springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-floppy-disk">
                            </span>
                        }
                        {
                            this.props.data &&
                            <span>
                                <DebounceInput className="pull-right" minLength={2} debounceTimeout={300} onChange={this.search} placeholder={Application.Localize('ui.search')}/>
                                <CSVLink data={this.state.dataToDownload} filename={this.props.title + ".csv"} target="_blank">
                                    <span onClick={this.download} className="control-panel-icon glyphicon glyphicon-download"/>
                                </CSVLink>
                            </span>

                        }
                        {
                            this.props.onAddRow &&
                            <span data-toggle="tooltip" title={Application.Localize('ui.mdm.New')}
                                  onClick={this.props.onAddRow}
                                  className="springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-plus">
                            </span>
                        }
                        {
                            this.props.onAddRow && this.props.needsSave &&
                            <span data-toggle="tooltip" title={Application.Localize('ui.mdm.changeToggle')}
                                  onClick={this.showModified}
                                  className="springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-filter">
                            </span>
                        }


                    </div>
                </div>
                {this.props.data &&
                    <BootstrapTable options={tableOptions} ref="table" data={this.props.data} striped hover search={false}
                                 keyField={this.props.keyName} height={this.props.height || '800px'} scrollTop={ 'Top' }
                                 multiColumnSort={3} cellEdit={ cellEditProp } trClassName={ this.props.trClassFormat }>
                    {
                        this.props.columnDefinitions.map((colDef, index) => {
                            let formatter = TextFormatter, dataFormatter, sorter = undefined, customEditor = undefined, filterValue, filterFormatted = false;
                            if (colDef.type == 'date' || colDef.type === 'datetime') {
                                let isDateTime = colDef.type === 'datetime';
                                formatter = isDateTime ? DateTimeFormatter : DateFormatter;
                                customEditor = {
                                    getElement: dateEditor,
                                    customEditorParameters: {isDateTime: isDateTime}
                                };
                                sorter = dateTimeSorter;
                                filterValue = (cell, row) => isDateTime ? moment(cell).format(Application.getMomentFormatForDateTime()) : moment(cell).format(Application.getMomentFormatForDate());
                            } else if (colDef.type == 'boolean') {
                                formatter = BooleanFormatter;
                                customEditor = {
                                    getElement: selectEditor,
                                    customEditorParameters: {options: booleanLovList, multi: false}
                                };
                            } else if (colDef.type == 'int') {
                                customEditor = {getElement: patternEditor, customEditorParameters: {type: colDef.type}};
                            } else if (colDef.type == 'num') {
                                customEditor = {getElement: patternEditor, customEditorParameters: {type: colDef.type}};
                            } else if (colDef.type == 'num-fmt') {
                                formatter = NumberFormatter;
                            } else {
                                customEditor = {getElement: patternEditor, customEditorParameters: {type: colDef.type}};
                            }

                            if (colDef.lovList != undefined && colDef.lovList != null) {
                                customEditor = {
                                    getElement: selectEditor,
                                    customEditorParameters: {options: colDef.lovList, multi: colDef.multiSelect}
                                };
                                if (colDef.multiSelect) formatter = ArrayFormatter;
                            }
                            if (customEditor !== undefined) {
                                customEditor.customEditorParameters.cellName = colDef.name;
                            }

                            /* If the caller has specified a formatter (tied to a type) then use that formatter - it overrides everything else */
                            if (options.formatters && options.formatters[colDef.name]) {
                                formatter = options.formatters[colDef.name];
                            }
                            /* If the caller has specified a sorter (tied to a type) then use that sorter - it overrides everything else */
                            if (options.sorter && options.sorter[colDef.type]) {
                                sorter = options.sorter[colDef.type];
                            }
                            /* If the caller has specified an editor (tied to a type) then use that editor - it overrides everything else */
                            if (options.editor && options.editor[colDef.type]) {
                                customEditor = {
                                    getElement: customEditorHandler,
                                    customEditorParameters: {editor: options.editor[colDef.type], colDef: colDef}
                                };
                            }

                            dataFormatter = (cell, row) => WrapperForFormatter(cell, formatter, colDef, row, that.props.updateResponse);
                            if (customEditor !== undefined) {
                                customEditor.customEditorParameters.onRowChange = this.props.onRowChange;
                                customEditor.customEditorParameters.cellName = colDef.name;
                            }
                            let align = colDef.align == undefined ? 'left' : colDef.align.toLowerCase();

                            return (
                                <TableHeaderColumn
                                    dataAlign={align}
                                    dataSort={colDef.sortable === undefined? true : colDef.sortable}
                                    sortFunc={sorter}
                                    width={colDef.width}
                                    hidden={colDef.hidden}
                                    filterFormatted={filterFormatted}
                                    key={index}
                                    dataFormat={dataFormatter}
                                    dataField={colDef.name}
                                    customEditor={ customEditor }
                                    editable={ this.props.editable === undefined? false : this.props.editable  }
                                    filterValue={filterValue }
                                    searchable={this.props.searchable === undefined ? true : this.props.searchable}
                                    tdStyle={ {whiteSpace: 'normal'} }
                                >
                                    {Application.Localize(colDef.title)}
                                </TableHeaderColumn>
                            )
                        })
                    }
                    <TableHeaderColumn hidden={this.props.onAddRow === undefined} width={"50px"} key={'__deleteKey'}
                                       dataField={'delete'} editable={ false }
                                       dataFormat={(cell, row) => DeleteFormatter(cell, row, this.deleteRow)}> {''}
                    </TableHeaderColumn>
                </BootstrapTable>
                }
            </span>
        );
    }
}


export default SpringrollTable;

/*
 {
 this.props.onAddRow !== undefined &&
 <TableHeaderColumn width={"50px"} key={'__deleteKey'} dataField={'delete'} editable={ false }
 dataFormat={(cell, row) => DeleteFormatter(cell, row, this.deleteRow)}> {''}
 </TableHeaderColumn>
 }

 */



