import React from 'react';
import Application from 'App';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import {DateTimeFormatter, DateFormatter, BooleanFormatter, BooleanToString, DateTimeToString, DateToString} from 'Formatters';
import DebounceInput from 'react-debounce-input';
import {CSVLink} from 'react-csv';
var json2csv = require('json2csv');
var moment = require('moment');


function bsFormatter(cell, formatter) {
    let Formatter = formatter;
    return (
        <Formatter value={cell} />
    );
}

function dateTimeSorter(a, b, order, sortField){
    let aValue = a[sortField] == null ? 0: a[sortField];
    let bValue = b[sortField] == null ? 0: b[sortField];
    if (order === 'desc') {
        return aValue - bValue;
    } else {
        return bValue - aValue;
    }
}

class BootstrapGrid extends React.Component {
    constructor(props){
        super(props);
        this.search = this.search.bind(this);
        this.download = this.download.bind(this);
        this.afterSearch = this.afterSearch.bind(this);
        this.state = {dataToDownload : []};
    }

    search(e){
        this.refs.table.handleSearch(e.target.value);
    }
    afterSearch(searchText, result){
        this.currentRows = result;
    }

    download(){
        var that = this;
        /* this.currentRows has the filtered set of rows (if a search was done) - if undefined it means that no filtering was done */
        let rowsToDownload = this.currentRows;
        if(this.currentRows == undefined){
            rowsToDownload = this.props.gridData.data;
        }
        let fields = [];
        _.each(this.props.gridData.columns, function (colDef) {
            var title = colDef.title;
            if(!colDef.visible)return;
            var fldDefn = {
                 label: Application.Localize(colDef.title),
                 value: colDef.title,
                 default: ''
            };
            if (colDef.type == 'boolean') {
                fldDefn['value'] = function (row, field, data) {
                    return BooleanToString(row[title]);
                }
            }
            if (colDef.type == 'date') {
                fldDefn['value'] = function (row, field, data) {
                    return DateToString(row[title]);
                }
            }
            if (colDef.type == 'datetime') {
                fldDefn['value'] = function (row, field, data) {
                    return DateTimeToString(row[title]);
                }
            }
            /* If the caller has specified a formatter (tied to a type) then use that formatter - it overrides everything else */
            if (that.props.options.formatters !== undefined && that.props.options.formatters[colDef.type] && that.props.options.formatters[colDef.type].forExport) {
                fldDefn['value'] = function (row, field, data) {
                    return that.props.options.formatters[colDef.type].forExport(row[title]);
                }
            }

            fields.push(fldDefn);

        });
        this.setState({dataToDownload :json2csv({ data: rowsToDownload, fields: fields })});
    }
    render() {
        const options = {
            afterSearch: this.afterSearch
        };
        return (
            <div>
                <div className="control-panel">
                    <div style={{textAlign : 'right'}}>
                        <h4 className=' pull-left text-info'>{this.props.title}</h4>
                        {
                            (this.props.gridParams !== undefined && this.props.gridParams.length > 0) &&
                            <span onClick={this.props.onFilterClick} title={Application.Localize('ui.report.parameters')} className='control-panel-icon glyphicon glyphicon-tasks'></span>
                        }
                        {
                            this.props.gridData !== undefined &&
                            <span>
                                <CSVLink ref={(input) => { this.textInput = input; }} data={this.state.dataToDownload} filename={this.props.title + ".csv"} target="_blank"><span onClick={this.download} className="control-panel-icon glyphicon glyphicon-download"/></CSVLink>
                                <DebounceInput minLength={2} debounceTimeout={300} onChange={this.search} placeholder={Application.Localize('ui.search')}/>
                            </span>
                        }
                    </div>
                </div>
                {
                    this.props.gridData !== undefined &&
                    <BootstrapTable options={options} ref="table" data={this.props.gridData.data} striped hover search={false}
                                    keyField={this.props.gridData.key} height='800px' scrollTop={ 'Top' }
                                    multiColumnSort={3}>
                        {
                            this.props.gridData.columns.map((colDef, index) => {
                                let formatter = undefined, dataFormatter, sorter = undefined;
                                if (colDef.type == 'date') {
                                    formatter = DateFormatter;
                                    sorter = dateTimeSorter;
                                }
                                if (colDef.type == 'datetime') {
                                    formatter = DateTimeFormatter;
                                    sorter = dateTimeSorter;
                                }
                                if (colDef.type == 'boolean') formatter = BooleanFormatter;

                                /* If the caller has specified a formatter (tied to a type) then use that formatter - it overrides everything else */
                                if (this.props.options.formatters && this.props.options.formatters[colDef.type] && this.props.options.formatters[colDef.type].forDisplay) {
                                    formatter = this.props.options.formatters[colDef.type].forDisplay;
                                }
                                /* If the caller has specified a sorter (tied to a type) then use that sorter - it overrides everything else */
                                if (this.props.options.sorter && this.props.options.sorter[colDef.type]) {
                                    sorter = this.props.options.sorter[colDef.type];
                                }
                                if (formatter) {
                                    dataFormatter = (cell, row) => bsFormatter(cell, formatter);
                                }
                                let align = colDef.align.toLowerCase();

                                return <TableHeaderColumn sortFunc={sorter} dataAlign={align} dataSort={colDef.sortable}
                                                          hidden={!colDef.visible} width={colDef.width}
                                                          tdStyle={ {whiteSpace: 'normal'} } key={index}
                                                          dataFormat={dataFormatter}
                                                          dataField={colDef.title}>{Application.Localize(colDef.title)}</TableHeaderColumn>

                            })
                        }
                    </BootstrapTable>
                }
            </div>
        );
    }
}

export default BootstrapGrid;




