import React from 'react';
import Application from 'App';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import DateTimeFormatter from 'DateTimeFormatter';
import DateFormatter from 'DateFormatter';
import BooleanFormatter from 'BooleanFormatter';
import DebounceInput from 'react-debounce-input';
import {CSVLink} from 'react-csv';
const data = [
    ['name', 'age'],
    ['Ahmed', 12],
    ['John', 8]
];

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
        this.state = {"searchValue" : ''};
    }

    search(e){
        this.setState({searchValue: e.target.value})
        this.refs.table.handleSearch(e.target.value);
    }
    afterSearch(searchText, result){
        this.currentRows = result;
    }

    download(){
        /* this.currentRows has the filtered set of rows (if a search was done) - if undefined it means that no filtering was done */
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
                                <CSVLink ref={(input) => { this.textInput = input; }} data={data} filename={this.props.title + ".csv"} target="_blank"><span onClick={this.download} className="control-panel-icon glyphicon glyphicon-download"/></CSVLink>
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
                                if (this.props.formatters !== undefined && this.props.formatters[colDef.type]) {
                                    formatter = this.props.formatters[colDef.type];
                                }
                                /* If the caller has specified a sorter (tied to a type) then use that sorter - it overrides everything else */
                                if (this.props.sorter !== undefined && this.props.sorter[colDef.type]) {
                                    sorter = this.props.sorter[colDef.type];
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




