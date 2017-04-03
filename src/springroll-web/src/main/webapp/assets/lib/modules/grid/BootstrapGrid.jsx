import React from 'react';
import Application from 'App';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import DateTimeFormatter from 'DateTimeFormatter';
import DateFormatter from 'DateFormatter';
import BooleanFormatter from 'BooleanFormatter';
import DebounceInput from 'react-debounce-input';

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
        this.state = {"searchValue" : ''};
    }

    massageMasterData(gridData){
        if(gridData == undefined)return;
        var that = this;

        this.colnames = _.map(gridData.columns, function(colDef) {
            return colDef.title;
        });
        let rows = _.map(gridData.data, function(rowData){
            var row = {};
            for (var j = 0; j < that.colnames.length; ++j) {
                if (rowData[j] == undefined || rowData[j] == null) continue;
                row[that.colnames[j]] = rowData[j];
            }
            return row;
        });
        return rows;
    }
    search(e){
        this.setState({searchValue: e.target.value})
        this.refs.table.handleSearch(e.target.value);
    }
    render() {
        let rows;
        if(this.props.gridData !== undefined) {
            rows = this.massageMasterData(this.props.gridData);
        } else {
            return null;
        }
        return (
            <div>
                <DebounceInput minLength={2} debounceTimeout={300} onChange={this.search} placeholder={Application.Localize('ui.search')}/>

                <BootstrapTable ref="table" data={rows} striped hover search={false} keyField={this.props.gridData.key} height='800px' scrollTop={ 'Top' } multiColumnSort={3} >
                {
                    this.props.gridData.columns.map((colDef, index) => {
                        let formatter = undefined, dataFormatter, sorter = undefined;
                        if(colDef.type == 'date')     {formatter = DateFormatter; sorter = dateTimeSorter;}
                        if(colDef.type == 'datetime') {formatter = DateTimeFormatter; sorter = dateTimeSorter;}
                        if(colDef.type == 'boolean')  formatter = BooleanFormatter;

                        /* If the caller has specified a formatter (tied to a type) then use that formatter - it overrides everything else */
                        if(this.props.formatters !== undefined && this.props.formatters[colDef.type]){
                            formatter = this.props.formatters[colDef.type];
                        }
                        /* If the caller has specified a sorter (tied to a type) then use that sorter - it overrides everything else */
                        if(this.props.sorter !== undefined && this.props.sorter[colDef.type]){
                            sorter = this.props.sorter[colDef.type];
                        }
                        if(formatter){
                            dataFormatter = (cell, row) => bsFormatter(cell, formatter);
                        }
                        let align = colDef.align.toLowerCase();

                        return <TableHeaderColumn sortFunc={sorter} dataAlign={align} dataSort={colDef.sortable} hidden={!colDef.visible} width={colDef.width} tdStyle={ { whiteSpace: 'normal' } } key={index} dataFormat={dataFormatter} dataField={colDef.title}>{Application.Localize(colDef.title)}</TableHeaderColumn>

                    })
                }
            </BootstrapTable>
            </div>
        );
    }
}

export default BootstrapGrid;




