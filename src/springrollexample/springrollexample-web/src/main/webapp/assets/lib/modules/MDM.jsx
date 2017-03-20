import React from 'react';
import Application from 'App';
import DateFormatter from 'DateFormatter';
import DatePicker from 'DatePicker';
import MdmToolbar from 'MdmToolbar';
import Select from 'Select';
import DateTimeFormatter from 'DateTimeFormatter';
import { Router, Route } from 'react-router'
import { NavDropdown, Nav, Navbar, MenuItem } from 'react-bootstrap';
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
        if(this.props.row.disabled == false) {
            return (<div className='mdm-disabled text-muted'><Row ref="row" {...this.props}/></div>);
        }
        return (<div><Row ref="row" {...this.props}/></div>);
    }
}

class MDM extends React.Component {
    constructor(props){
        super(props);
        this.masterChosen = this.masterChosen.bind(this);
        this.rowGetter = this.rowGetter.bind(this);
        this.handleGridSort = this.handleGridSort.bind(this);
        this.getRows = this.getRows.bind(this);
        this.getSize = this.getSize.bind(this);
        this.handleFilterChange = this.handleFilterChange.bind(this);
        this.handleGridRowsUpdated = this.handleGridRowsUpdated.bind(this);
        this.onClearFilters = this.onClearFilters.bind(this);
        this.saveClicked = this.saveClicked.bind(this);
        this.handleAddRow = this.handleAddRow.bind(this);
        this.onCheckCellIsEditable = this.onCheckCellIsEditable.bind(this);
        this.getMap = this.getMap.bind(this);
        this.state = {masterDefns : undefined, hasData : false, rows : [], filters: {}, sortColumn: null, sortDirection: null, master : undefined, needsSave : false };
        this.changedRows = {};
        this.newRows = {};
        this.recIdsUnderReview = [];
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
        let newrow = {};
        newrow['cid'] = newRowIndex;
        newrow['new'] = true;
        let rows = this.state.rows.slice();
        rows.push(newrow);
        this.setState({rows : rows, needsSave : true});
        this.newRows[newRowIndex] = newrow;

    }

    getMap(changedVars, changedRowIndex){
        //FIXME - should not be setting prevVal here
        var that = this;
        let mdmChangedColumns = {};
        _.each(Object.keys(changedVars), function(colName){
            mdmChangedColumns[colName] = { val : changedVars[colName], prevVal : that.originalRows[changedRowIndex][colName], changed : true};
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
            .map(key => this.newRows[key])
            .filter( row => row.new === true)
            .each(row => delete row['new'])
            .value();

        var mdmDTO = {master : this.state.master, changedRecords : changedRecords, newRecords : newRecords};

        $.ajax({
            url: "api/sr/mdm/update/",
            type: 'POST',
            data: JSON.stringify(mdmDTO),
            success: function(response){
                let rows = this.state.rows.slice();
                _.each(Object.keys(this.newRows), (cid) => {
                    let rowToUpdate = rows[cid];
                    rowToUpdate['disabled'] = true;
                });
                this.newRows = {};
                _.each(Object.keys(this.changedRows), (cid) => {
                    let rowToUpdate = rows[cid];
                    rowToUpdate['disabled'] = true;
                });
                this.changedRows = {};

                this.setState({ rows, needsSave : false});

            }.bind(this),
            error: function(xhr, reason, exception) {
                console.log("Error");
            }
        });
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
                        needsSave = false;
                        delete that.changedRows[cid];
                    }
                }
            }
        });
        this.setState({ rows, needsSave : needsSave });
    }

    masterChosen(masterName) {
        this.props.router.push('/mdm/' + masterName);
        this.fetchAndDisplayMaster(masterName);
    }
    fetchAndDisplayMaster(masterName){
        this.setState({master : masterName});
        $.ajax({
            url: "api/sr/mdm/data/" + masterName,
            type: 'POST',
            success: function(response){
                var that = this;
                this.recIdsUnderReview = response.recIdsUnderReview;
                this._columns = _.chain(response.colDefs).filter(colDef => colDef.name != 'id').map(function(colDef){
                    let defn = ({key : colDef.name, name : colDef.name, sortable : true, filterable: true, resizable : true, editable : true, writeable : colDef.writeable});

                    if(colDef.type == 'date') defn = Object.assign(defn, {formatter : DateFormatter, editor: DatePicker});

                    if(colDef.type == 'datetime') defn = Object.assign(defn, {formatter : DateTimeFormatter, editor: DatePicker});

                    if(colDef.multiSelect == true) defn['sortable'] = false;

                    if(colDef.lovList != undefined && colDef.lovList != null){
                        let opts = _.map(colDef.lovList, function(lov){return {value : lov.value, label : lov.name}});
                        let editor = <Select options={opts} multiSelect={colDef.multiSelect}/>;
                        defn['editor'] = editor;
                    }

                    return defn;
                }).value();
                this._colDefs = response.colDefs;
                this.colnames = _.map(this._colDefs, function(colDef, index) {
                    return colDef.name;
                });
                let rows = _.map(response.data, function(rowData, index){
                    var row = {};
                    for (var j = 0; j < that.colnames.length; ++j) {
                        if (rowData[j] == undefined) continue;
                        if (rowData[j] == null) continue;
                        if (rowData[j].constructor === Array){
                            row[that.colnames[j]] = rowData[j].toString();
                        } else {
                            row[that.colnames[j]] = rowData[j];
                        }
                    }
                    row['cid'] = index;
                    return row;
                });
                this.setState({rows, rows, hasData : true});
                this.originalRows = rows.map(a => Object.assign({}, a));

            }.bind(this),
            error: function(xhr, reason, exception) {
                console.log("Error");
            }
        });


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
            <div>
                <Nav bsStyle="pills" onSelect={this.masterChosen}>
                    {this.state.masterDefns != undefined && Object.keys(this.state.masterDefns).map((groName, index) => {
                        return (
                            <NavDropdown id="mdm-nav-dropdown" title={groName} key={groName}>
                                {this.state.masterDefns[groName].map((masterName, index) => {return (<MenuItem eventKey={masterName} key={masterName}>{masterName}</MenuItem>)})}
                            </NavDropdown>
                        );
                    })}
                </Nav>
                {this.state.hasData == true &&
                    <div className="springroll-table"><ReactDataGrid onGridRowsUpdated={this.handleGridRowsUpdated}
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
                    /></div>}
            </div>
        );
    }
    componentDidMount(){
        $.ajax({
            url: 'api/sr/mdm/masters',
            type: 'GET',
            success: function (masterDefns) {
                this.setState({masterDefns: masterDefns});
            }.bind(this),
            error : function (jqXHR, textStatus, errorThrown ){
                console.error("Unable to load MDM Definitions - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            }.bind(this)
        });
        if(this.props.params.master != undefined){
            this.fetchAndDisplayMaster(this.props.params.master, true);
        }
    }

}

const routes = (<Route key="mdm" path="/mdm" component={MDM}>
        <Route key="mm" path="/mdm/:master" component={MDM}/>
    </Route>
);

Application.addMenu({
    title: 'ui.menu.mdm',
    index: 4,
    type: "menuitem",
    route : routes,
    component : MDM,
    routeOnClick: 'mdm'
});

export default MDM;




