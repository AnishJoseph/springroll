import React from 'react';
import Application from 'App';
import DateFormatter from 'DateFormatter';
import DateTimeFormatter from 'DateTimeFormatter';
import { Router, Route } from 'react-router'
import { NavDropdown, Nav, Navbar, MenuItem } from 'react-bootstrap';
const ReactDataGrid = require('react-data-grid');


class MDM extends React.Component {
    constructor(props){
        super(props);
        this.masterChosen = this.masterChosen.bind(this);
        this.rowGetter = this.rowGetter.bind(this);
        this.handleGridSort = this.handleGridSort.bind(this);
        this.state = {masterDefns : undefined, hasData : false, originalRows : [], rows : [] };
    }

    handleGridSort(sortColumn, sortDirection) {
        const comparer = (a, b) => {
            if (sortDirection === 'ASC') {
                console.log ("A - " + a[sortColumn] + " ::  B " + b[sortColumn] + " DIFF " + a[sortColumn] > b[sortColumn]);
                return (a[sortColumn] > b[sortColumn]) ? 1 : -1;
            } else if (sortDirection === 'DESC') {
                console.log ("A - " + a[sortColumn] + " ::  B " + b[sortColumn] + " DIFF " + a[sortColumn] < b[sortColumn]);
                return (a[sortColumn] < b[sortColumn]) ? 1 : -1;
            }
        };

        const rows = sortDirection === 'NONE' ? this.state.originalRows.slice(0) : this.state.rows.sort(comparer);

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
                        return ({key : colDef.name, name : colDef.name, sortable : true, formatter : DateFormatter});
                    if(colDef.type == 'datetime')
                        return ({key : colDef.name, name : colDef.name, sortable : true, formatter : DateTimeFormatter});
                    if(colDef.multiSelect == true)
                        return ({key : colDef.name, name : colDef.name, sortable : false });
                    return ({key : colDef.name, name : colDef.name, sortable : true });
                });
                this._colDefs = response.colDefs;
                this.colnames = _.map(this._colDefs, function(colDef, index) {
                    return colDef.name;
                });
                let originalRows = _.map(response.data, function(rowData, index){
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
                let rows = originalRows.slice(0);
                this.setState({originalRows, rows, hasData : true});

                console.log("Yay");
            }.bind(this),
            error: function(xhr, reason, exception) {
                console.log("Error");
            }
        });


    }
    rowGetter(i) {
        return this.state.rows[i];
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
                {this.state.hasData == true && <ReactDataGrid columns={this._columns} rowGetter={this.rowGetter} rowsCount={this.state.rows.length} minHeight={500} onGridSort={this.handleGridSort}
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




