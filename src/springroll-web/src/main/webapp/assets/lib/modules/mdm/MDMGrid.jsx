import React from 'react';
import Application from 'App';
import DateFormatter from 'DateFormatter';
import DatePicker from 'DatePicker';
import MdmToolbar from 'MdmToolbar';
import Select from 'Select';
import DateTimeFormatter from 'DateTimeFormatter';
import ArrayFormatter from 'ArrayFormatter';
import BooleanFormatter from 'BooleanFormatter';
import DeleteFormatter from 'DeleteFormatter';
const ReactDataGrid = require('react-data-grid');
const { Row } = ReactDataGrid;
const { Data: { Selectors } } = require('react-data-grid-addons');

class RowRenderer extends React.Component {
    constructor(props) {
        super(props);
    }
    setScrollLeft(scrollBy) {
        // if you want freeze columns to work, you need to make sure you implement this as a pass through
        this.refs.row.setScrollLeft(scrollBy);
    }
    render() {
        if(this.props.row.disabled === true) {
            return (<div><Row extraClasses="text-muted mdm-disabled" ref="row" {...this.props}/></div>);
        }
        if(this.props.row.hasError === true) {
            return (<div><Row extraClasses="text-muted mdm-error" ref="row" {...this.props}/></div>);
        }
        return (<div><Row ref="row" {...this.props}/></div>);
    }
}

class MDMGrid extends React.Component {
    constructor(props){
        super(props);
        this.rowGetter = this.rowGetter.bind(this);
        this.handleGridSort = this.handleGridSort.bind(this);
        this.getRows = this.getRows.bind(this);
        this.getSize = this.getSize.bind(this);
        this.handleFilterChange = this.handleFilterChange.bind(this);
        this.handleGridRowsUpdated = this.handleGridRowsUpdated.bind(this);
        this.onClearFilters = this.onClearFilters.bind(this);
        this.saveClicked = this.saveClicked.bind(this);
        this.delete = this.delete.bind(this);
        this.handleAddRow = this.handleAddRow.bind(this);
        this.onCheckCellIsEditable = this.onCheckCellIsEditable.bind(this);
        this.getMap = this.getMap.bind(this);

        let rows = this.massageMasterData(this.props.masterData);
        this.state = {hasData : true, rows : rows, filters: {}, sortColumn: null, sortDirection: null, needsSave : false, hello : false };
        this.changedRows = {};
        this.newRows = {};
        this.recIdsUnderReview = [];
    }

    delete(evt, args){
        if(this.getRows()[args.rowIdx].new !== true)return;
        if(this.getRows()[args.rowIdx].disabled === true)return;
        let cid = this.getRows()[args.rowIdx].cid;

        let rows = this.state.rows.slice();
        rows.splice(cid, 1);
        delete this.newRows[cid];

        if(Object.keys(this.newRows).length === 0 && Object.keys(this.changedRows).length === 0){
            this.setState({rows : rows, needsSave : false});
        } else {
            this.setState({rows : rows});
        }
    }
    handleGridSort(sortColumn, sortDirection) {
        this.setState({ sortColumn: sortColumn, sortDirection: sortDirection });
    }

    onClearFilters() {
        this.setState({ filters: {} });
    }
    onCheckCellIsEditable(meta){
        if(meta.row.disabled) return false;
        let isUnderReview = _.find(this.recIdsUnderReview, id =>  id == meta.row.id);
        if(isUnderReview) return false;
        if(meta.column.writeable) return true;
        if(meta.row.new) return true;
        return false;
    }
    handleAddRow( {newRowIndex}) {
        let newrow = {'cid' : newRowIndex, new : true, delete : true};
        _.each(this.colDefs, colDef => {
           if(colDef.defVal != null){
               if(colDef.type == 'boolean'){
                   newrow[colDef.name] = colDef.defVal == 'true' ? true : false;
               } else {
                   newrow[colDef.name] = colDef.defVal;
               }
           }
        });
        let rows = this.state.rows.slice();
        rows.push(newrow);
        this.setState({rows : rows, needsSave : true});
        this.newRows[newRowIndex] = newrow;

    }

    getMap(changedVars, changedRowIndex){
        var that = this;
        let mdmChangedColumns = {};
        _.each(Object.keys(changedVars), function(colName){
            mdmChangedColumns[colName] = { val : changedVars[colName], changed : true};
        });
        return mdmChangedColumns;
    }

    saveClicked(){
        var that = this;

        let changedRecords = _.map(Object.keys(this.changedRows), function(changedRowIndex) {
            return {
                id: that.state.rows[changedRowIndex]['id'],
                mdmChangedColumns: that.getMap(that.changedRows[changedRowIndex], changedRowIndex)
            };
        });

        let newRecords =  _.chain(Object.keys(this.newRows))
            .map(key => Object.assign({}, this.newRows[key]) )
            .filter( row => row.new === true)
            .each(row => {delete row['new']; delete row['delete'], delete row['hasError']})
            .value();

        var mdmDTO = {master : this.props.masterData.master, changedRecords : changedRecords, newRecords : newRecords};

        this.props.onMdmMasterChanged(mdmDTO);

        let rows = this.state.rows.slice();
        _.each(Object.keys(this.newRows), (cid) => {
            let rowToUpdate = rows[cid];
            rowToUpdate['disabled'] = true;
            delete rowToUpdate['hasError'];
        });
        this.prevNewRows = this.newRows;
        this.newRows = {};
        _.each(Object.keys(this.changedRows), (cid) => {
            let rowToUpdate = rows[cid];
            rowToUpdate['disabled'] = true;
            delete rowToUpdate['hasError'];
        });
        this.prevChangedRows = this.changedRows;
        this.changedRows = {};

        this.setState({ rows, needsSave : false});

    }

    handleGridRowsUpdated({ cellKey, fromRow, toRow, updated, rowIds, action}) {
        let needsSave = true;
        let rows = this.state.rows.slice();
        var that = this;
        _.each(rowIds, function(cid){
            let rowToUpdate = rows[cid];
            let alreadyMadeChanges = rowToUpdate.new === true ? that.newRows[cid] : that.changedRows[cid] || { cid : cid};
            let updatedRow = Object.assign(rowToUpdate, updated);
            alreadyMadeChanges = Object.assign(alreadyMadeChanges, updated);
            if(rowToUpdate.new !== true ){
                that.changedRows[cid] = alreadyMadeChanges;
                if(alreadyMadeChanges[cellKey] === that.originalRows[cid][cellKey]){
                    //The user changd the value and then changed it back to the original value
                    delete alreadyMadeChanges[cellKey];
                    if(Object.keys(alreadyMadeChanges).length == 1){
                        // There are no changes for this row
                        delete that.changedRows[cid];
                        if(Object.keys(that.newRows).length === 0 && Object.keys(that.changedRows).length === 0){
                            needsSave = false;
                        }
                    }
                }
            }
        });
        this.setState({ rows, needsSave : needsSave });
    }

    massageMasterData(masterData){
        var that = this;
        this.recIdsUnderReview = masterData.recIdsUnderReview;
        this._columns = _.chain(masterData.colDefs).filter(colDef => colDef.name != 'id').map(function(colDef){
            let defn = ({key : colDef.name, name : colDef.name, sortable : true, filterable: true, resizable : true, editable : true, writeable : colDef.writeable});

            if(colDef.type == 'date') defn = Object.assign(defn, {formatter : DateFormatter, editor: DatePicker});
            if(colDef.type == 'datetime') defn = Object.assign(defn, {formatter : DateTimeFormatter, editor: DatePicker});
            if(colDef.type == 'boolean') defn['formatter'] = BooleanFormatter;
            if(colDef.multiSelect == true) {
                defn['sortable'] = false;
                defn['formatter'] = ArrayFormatter;
            }

            if(colDef.lovList != undefined && colDef.lovList != null){
                defn['editor'] = <Select options={colDef.lovList} multiSelect={colDef.multiSelect}/>;
            }

            return defn;
        }).value();
        this._columns.push({width : 50, key : 'delete', name : Application.Localize('ui.mdm.delete'), sortable : false, filterable : false, editable : false, writeable : false, formatter : DeleteFormatter, events : { onClick : this.delete}});

        this.colnames = _.map(masterData.colDefs, function(colDef, index) {
            return colDef.name;
        });
        let rows = _.map(masterData.data, function(rowData, index){
            var row = {};
            for (var j = 0; j < that.colnames.length; ++j) {
                if (rowData[j] == undefined) continue;
                if (rowData[j] == null) continue;
                row[that.colnames[j]] = rowData[j];
            }
            row['cid'] = index;
            let underReview = _.find(masterData.recIdsUnderReview, id => id == row['id']);
            if(underReview) row['disabled'] = true;
            return row;
        });
        this.originalRows = rows.map(a => Object.assign({}, a));
        this.colDefs = masterData.colDefs;
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
        return Object.assign({}, rows[rowIdx]);
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
                <ReactDataGrid onGridRowsUpdated={this.handleGridRowsUpdated}
                               onClearFilters={this.onClearFilters}
                               toolbar={<MdmToolbar enableAddRow={true} onAddRow={this.handleAddRow} enableFilter={true} onSaveClicked={this.saveClicked} needsSave={this.state.needsSave}/>}
                               onAddFilter={this.handleFilterChange}
                               columns={this._columns} rowGetter={this.rowGetter}
                               rowsCount={this.getSize()} minHeight={500}
                               onGridSort={this.handleGridSort}
                               enableCellSelect={true}
                               rowKey="cid"
                               onCheckCellIsEditable={this.onCheckCellIsEditable}
                               rowRenderer={RowRenderer}
                />
            </div>
        );
    }
    componentWillReceiveProps(nextProps){
        /*  Some props have changed - could be because EITHER
            a) A new master was chosen and the data for the new master was retrieved causing prop 'masterData' to change
            b) The master was changed and updated to the server - server responded with success or failuer
            c) A new master was chosen and a server request to fetch data either succeeded or failed
         */
        /* CASE A */
        if(nextProps.updateStatus === undefined) {
            console.log("DATA received - massaging data and returning");
            this.setState({rows: this.massageMasterData(nextProps.masterData)});
            this.prevNewRows = undefined;
            this.prevChangedRows = undefined;
            return;
        }


        /*
            CASE B. Rollback some state since we presumed that the update would be successful
        */
        if(nextProps.updateStatus === false){
            let rows = this.state.rows.slice();
            if(this.prevNewRows !== undefined) {
                _.each(Object.keys(this.prevNewRows), (cid) => {
                    let rowToUpdate = rows[cid];
                    delete rowToUpdate['disabled'];
                });
                this.newRows = this.prevNewRows;
                this.prevNewRows = undefined;
            }

            if(this.prevChangedRows !== undefined) {
                _.each(Object.keys(this.prevChangedRows), (cid) => {
                    let rowToUpdate = rows[cid];
                    delete rowToUpdate['disabled'];
                });
                this.changedRows = this.prevChangedRows;
                this.prevChangedRows = undefined;
            }

            _.each(nextProps.updateResponse, function (violation) {
                let rowIdx = parseInt(violation.cookie);
                let rowToUpdate = rows[rowIdx];
                rowToUpdate['hasError'] = true;
            });

            this.setState({ rows, needsSave : true});

        } else if (nextProps.updateStatus === true){
            this.prevNewRows = undefined;
            this.prevChangedRows = undefined;
        }
    }
}


export default MDMGrid;




