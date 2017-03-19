import React from 'react';
import Application from 'App';
import DateFormatter from 'DateFormatter';
import DatePicker from 'DatePicker';
import Select from 'Select';
import DateTimeFormatter from 'DateTimeFormatter';
import { Router, Route } from 'react-router'
import { NavDropdown, Nav, Navbar, MenuItem } from 'react-bootstrap';
const ReactDataGrid = require('react-data-grid');
const { Editors, Formatters, Toolbar, Data: { Selectors } } = require('react-data-grid-addons');



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
        this.getMap = this.getMap.bind(this);
        this.state = {masterDefns : undefined, hasData : false, rows : [], filters: {}, sortColumn: null, sortDirection: null };
        this.changedRows = {};
    }
    handleGridSort(sortColumn, sortDirection) {
        this.setState({ sortColumn: sortColumn, sortDirection: sortDirection });
    }

    onClearFilters() {
        this.setState({ filters: {} });
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
        let changedRecords = _.map(Object.keys(this.changedRows), function(changedRowIndex){
            return {id : that.state.rows[changedRowIndex]['id'], mdmChangedColumns : that.getMap(that.changedRows[changedRowIndex], changedRowIndex)};
        });
        var mdmDTO = {master : this.currentMaster, changedRecords : changedRecords, newRecords : []};

        $.ajax({
            url: "api/sr/mdm/update/",
            type: 'POST',
            data: JSON.stringify(mdmDTO),
            success: function(response){
                console.log("MDM Posted");
            }.bind(this),
            error: function(xhr, reason, exception) {
                console.log("Error");
            }
        });


    }

    handleGridRowsUpdated({ fromRow, toRow, updated }) {
        let rows = this.state.rows.slice();

        for (let i = fromRow; i <= toRow; i++) {
            let alreadyMadeChanges = this.changedRows[i] || {};
            let rowToUpdate = rows[i];
            let updatedRow = Object.assign({}, rowToUpdate, updated);
            alreadyMadeChanges = Object.assign(alreadyMadeChanges, updated);
            rows[i] = updatedRow;
            this.changedRows[i] = alreadyMadeChanges;
        }

        this.setState({ rows });
    }

    masterChosen(masterName) {
        this.props.router.push('/mdm/' + masterName);
        this.fetchAndDisplayMaster(masterName);
    }
    fetchAndDisplayMaster(masterName){
        this.currentMaster = masterName;
        $.ajax({
            url: "api/sr/mdm/data/" + masterName,
            type: 'POST',
            success: function(response){
                var that = this;

                this._columns = _.map(response.colDefs, function(colDef){
                    if(colDef.type == 'date')
                        return ({key : colDef.name, name : colDef.name, sortable : true, formatter : DateFormatter, filterable: true, editable : colDef.writeable, editor: DatePicker});
                    if(colDef.type == 'datetime')
                        return ({key : colDef.name, name : colDef.name, sortable : true, formatter : DateTimeFormatter, filterable: true, editable : colDef.writeable});
                    let defn = ({key : colDef.name, name : colDef.name, sortable : true, filterable: true, editable : colDef.writeable, resizable : true});
                    if(colDef.multiSelect == true) defn['sortable'] = false;
                    if(colDef.lovList != undefined && colDef.lovList != null){
                        let opts = _.map(colDef.lovList, function(lov){return {value : lov.value, label : lov.name}});
                        let editor = <Select options={opts} multiSelect={colDef.multiSelect}/>;
                        defn['editor'] = editor;
                    }
                    return defn;
                });
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
                    return row;
                });
                this.setState({rows, rows, hasData : true});
                this.originalRows = rows.slice(0);

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
                <span data-toggle="tooltip" title="Save" onClick={this.saveClicked} className="pull-right alertActionsPanelItem glyphicon glyphicon-save"></span>
                {this.state.hasData == true &&
                    <ReactDataGrid onGridRowsUpdated={this.handleGridRowsUpdated}
                                   onClearFilters={this.onClearFilters}
                                   toolbar={<Toolbar enableAddRow={true} enableFilter={true}/>}
                                   onAddFilter={this.handleFilterChange}
                                   columns={this._columns} rowGetter={this.rowGetter}
                                   rowsCount={this.getSize()} minHeight={500}
                                   onGridSort={this.handleGridSort}
                                   enableCellSelect={true}

                    />}
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




