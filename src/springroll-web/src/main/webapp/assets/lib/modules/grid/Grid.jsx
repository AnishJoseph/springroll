import React from 'react';
import Application from 'App';
import MdmToolbar from 'MdmToolbar';
import DateTimeFormatter from 'DateTimeFormatter';
import DateFormatter from 'DateFormatter';
import BooleanFormatter from 'BooleanFormatter';
import DeleteFormatter from 'DeleteFormatter';
const ReactDataGrid = require('react-data-grid');
const { Data: { Selectors } } = require('react-data-grid-addons');

class Grid extends React.Component {
    constructor(props){
        super(props);
        this.handleGridSort = this.handleGridSort.bind(this);
        this.onClearFilters = this.onClearFilters.bind(this);
        this.rowGetter = this.rowGetter.bind(this);
        this.getRows = this.getRows.bind(this);
        this.getSize = this.getSize.bind(this);
        this.handleFilterChange = this.handleFilterChange.bind(this);

        let rows = this.massageMasterData(this.props.gridData);
        this.state = {hasData : true, rows : rows, filters: {}, sortColumn: null, sortDirection: null};
    }

    handleGridSort(sortColumn, sortDirection) {
        this.setState({ sortColumn: sortColumn, sortDirection: sortDirection });
    }

    onClearFilters() {
        this.setState({ filters: {} });
    }

    massageMasterData(gridData){
        if(gridData == undefined)return;
        var that = this;
        this._columns = _.map(gridData.columns, function(colDef){
            let defn = ({key : colDef.title, name : Application.Localize(colDef.title), sortable : true, filterable: true, resizable : true});
            if(colDef.type == 'date') defn = Object.assign(defn, {formatter : DateFormatter});
            if(colDef.type == 'datetime') defn = Object.assign(defn, {formatter : DateTimeFormatter});
            if(colDef.type == 'boolean') defn['formatter'] = BooleanFormatter;
            if(that.props.formatters[colDef.type] !== undefined){
                defn['formatter'] = that.props.formatters[colDef.type];
            }
            return defn;
        });

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
    getRows() {
        return Selectors.getRows(this.state);
    }
    getSize() {
        return this.getRows().length;
    }
    rowGetter(rowIdx) {
        const rows = this.getRows();
        return rows[rowIdx];
        //return Object.assign({}, rows[rowIdx]);
    }

    handleFilterChange(filter) {
        let newFilters = Object.assign({}, this.state.filters);
        if (filter.filterTerm) {
            newFilters[filter.column.key] = filter;
        } else {
            delete newFilters[filter.column.key];
        }

        this.setState({ filters: newFilters });
    }

    render() {
        return (
            <div className="springroll-table">
                <ReactDataGrid
                    onGridSort={this.handleGridSort}
                    enableCellSelect={false}
                    columns={this._columns}
                    rowGetter={this.rowGetter}
                    rowsCount={this.getSize()}
                    minHeight={500}
                    toolbar={<MdmToolbar  enableFilter={true}/>}
                    onAddFilter={this.handleFilterChange}
                    onClearFilters={this.onClearFilters}
                />
            </div>
        );
    }

    componentWillReceiveProps(nextProps) {
        let rows = this.massageMasterData(nextProps.gridData);
        this.setState({hasData : true, rows : rows, filters: {}, sortColumn: null, sortDirection: null});
    }
}


export default Grid;




