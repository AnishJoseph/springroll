import React from 'react';
import Application from 'App';
import DateFormatter from 'DateFormatter';
import DatePicker from 'DatePicker';
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
        this.state = {masterDefns : undefined, hasData : false, rows : [], filters: {}, sortColumn: null, sortDirection: null };
        this.changedRows = {};
    }
    handleGridSort(sortColumn, sortDirection) {
        this.setState({ sortColumn: sortColumn, sortDirection: sortDirection });
    }

    onClearFilters() {
        this.setState({ filters: {} });
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
        $.ajax({
            url: "api/sr/mdm/data/" + masterName,
            type: 'POST',
            success: function(response){
                var that = this;
                this._columns = _.map(response.colDefs, function(colDef){
                    if(colDef.type == 'date')
                        return ({key : colDef.name, name : colDef.name, sortable : true, formatter : DateFormatter, filterable: true, editable : true, editor: DatePicker});
                    if(colDef.type == 'datetime')
                        return ({key : colDef.name, name : colDef.name, sortable : true, formatter : DateTimeFormatter, filterable: true,});
                    if(colDef.multiSelect == true)
                        return ({key : colDef.name, name : colDef.name, sortable : false });
                    return ({key : colDef.name, name : colDef.name, sortable : true, filterable: true, editable : true});
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

                console.log("Yay");
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




