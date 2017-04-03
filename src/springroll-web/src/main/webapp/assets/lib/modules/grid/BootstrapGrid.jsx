import React from 'react';
import Application from 'App';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import DateTimeFormatter from 'DateTimeFormatter';
import DateFormatter from 'DateFormatter';
import BooleanFormatter from 'BooleanFormatter';

function bsFormatter(cell, formatter) {
    let Formatter = formatter;
    return (
        <Formatter value={cell} />
    );
}

class BootstrapGrid extends React.Component {
    constructor(props){
        super(props);
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
    render() {
        let rows;
        if(this.props.gridData !== undefined) {
            rows = this.massageMasterData(this.props.gridData);
        } else {
            return null;
        }
        return (
            <BootstrapTable data={rows} striped hover search={true} keyField={this.props.gridData.key} height='800px' scrollTop={ 'Top' }>
                {
                    this.props.gridData.columns.map((colDef, index) => {
                        let formatter = undefined, dataFormatter;
                        if(colDef.type == 'date')     formatter = DateFormatter;
                        if(colDef.type == 'datetime') formatter = DateTimeFormatter;
                        if(colDef.type == 'boolean')  formatter = BooleanFormatter;

                        /* If the caller has specified a formatter (tied to a type) then use that formatter - it overrides everything else */
                        if(this.props.formatters !== undefined && this.props.formatters[colDef.type]){
                            formatter = this.props.formatters[colDef.type];
                        }
                        if(formatter){
                            dataFormatter = (cell, row) => bsFormatter(cell, formatter);
                        }
                        let align = colDef.align.toLowerCase();

                        return <TableHeaderColumn dataAlign={align} dataSort={colDef.sortable} hidden={!colDef.visible} width={colDef.width} tdStyle={ { whiteSpace: 'normal' } } key={index} dataFormat={dataFormatter} dataField={colDef.title}>{Application.Localize(colDef.title)}</TableHeaderColumn>

                    })
                }
            </BootstrapTable>

        );
    }
}

export default BootstrapGrid;




