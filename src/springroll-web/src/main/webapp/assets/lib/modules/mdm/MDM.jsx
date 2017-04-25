import React from 'react';
import MDMGrid from 'mdm/MDMTable';
import { Router, Route } from 'react-router'
import { NavDropdown, Nav, Navbar, MenuItem } from 'react-bootstrap';
import SpringrollTable from 'SpringrollTable';
import Application from 'App';
import {each, filter, indexOf}  from 'lodash';

class MDM extends React.Component {
    constructor(props){
        super(props);
        this.masterChosen = this.masterChosen.bind(this);
        this.saveClicked = this.saveClicked.bind(this);
        this.deleteRow = this.deleteRow.bind(this);
        this.editable = this.editable.bind(this);
    }

    masterChosen(masterName) {
        /* First change the URL */
        this.props.router.push('/mdm/' + masterName);
        /* let upstairs know that a master was chosen */
        this.props.onMdmMasterChosen(masterName);
    }
    trClassFormat(row, rowIndex){
        if(row['id'] === -1 && row['rowIsNew'] === undefined)
            return 'mdm-disabled';
        if(row['__hasChanged'] === '__hasChanged')
            return 'mdm-changed';

        return "mdmTable";
    }

    saveClicked(){
        let changedRecords = [], newRecords = [];
        /* Marshall the changes to existing rows into MdmDTO */
        each(Object.keys(this.props.changedRowData || {}),  rowId => {
            let mdmChangedColumns = {};
            let rowChanges = this.props.changedRowData[rowId];
            /* This is an existing row that has changed */
            each(Object.keys(rowChanges), colName => {
                if(colName === 'id')return;
                mdmChangedColumns[colName] = rowChanges[colName];
            });
            let changedRecord = {id : rowChanges['id'], mdmChangedColumns : mdmChangedColumns};
            mdmChangedColumns['cid'] = {val : rowId};
            changedRecords.push(changedRecord);
        });

        /* Marshall the new records into MdmDTO */
        each(Object.keys(this.props.newRowData || {}), rowId => {
            let attrsForNewRecord = {};
            let rowChanges = this.props.newRowData[rowId];
            each(Object.keys(rowChanges), colName => {
                if(colName === 'id') return;
                attrsForNewRecord[colName] = rowChanges[colName];
            });
            attrsForNewRecord['cid'] = rowId;
            newRecords.push(attrsForNewRecord);
        });

        let mdmDTO = {master : this.props.masterData.master, changedRecords : changedRecords, newRecords : newRecords};
        this.props.onMdmMasterUpdateStarted();
        this.props.onMdmMasterChanged(mdmDTO);
    }
    deleteRow(cid){
        this.props.onMdmMasterDeleteRow(cid);
    }
    editable(cell, row, rowIndex, columnIndex){
        if(this.props.updateInProgress) return false;
        if(row['id'] === -1 && row['rowIsNew'] === true)return true;
        if (this.props.masterData.colDefs[columnIndex].writeable === false)
            return false;
        if(indexOf(this.props.masterData.recIdsUnderReview, row['id'], 0) !== -1)
            return false;
        return true;
    }

    render() {
        /* For each master group create a menu item, and for each master in that master group, add the master to the dropdown */
        let title = this.props.masterData !== undefined ? Application.Localize('ui.mdm.title', Application.Localize('ui.mdm.master.'+ this.props.masterData.master)): '';
        let needsSave = this.props.changedRowData !== undefined || this.props.newRowData !== undefined;
        let data = this.props.masterData?filter(this.props.masterData.data, row => row !== null) : undefined;
        return (
            <div>
                <Nav bsStyle="pills" onSelect={this.masterChosen} className="secondary-menu">
                    {this.props.masterDefns != undefined && Object.keys(this.props.masterDefns).map((groName, index) => {
                        return (
                            <NavDropdown id="mdm-nav-dropdown" title={groName} key={groName} noCaret>
                                {this.props.masterDefns[groName].map((masterName, index) => {return (<MenuItem eventKey={masterName} key={masterName}>{masterName}</MenuItem>)})}
                            </NavDropdown>
                        );
                    })}
                </Nav>
                {this.props.masterData !== undefined &&
                    <SpringrollTable
                        data={data}
                        columnDefinitions={this.props.masterData.colDefs}
                        editable={this.editable}
                        keyName='id'
                        title={title}
                        onRowChange={this.props.onMdmMasterRowChanged}
                        onAddRow={this.props.onMdmMasterAddRow}
                        onDeleteRow={this.deleteRow}
                        onSaveClicked={this.saveClicked}
                        needsSave={needsSave}
                        trClassFormat={this.trClassFormat}
                        updateResponse={this.props.updateResponse}
                    />
                }
            </div>
        );
    }
    componentDidMount(){
        /* The MDM menu (level 1) was chosen - fire a Action to go and fetch the MDM masters from the servers */
        /* Note : This is not getting any master data - just the meta data so that we can create the second level menus */
        this.props.onMdmModuleActivated();
        /* Ah a specific master is part or the URL - for example mdm/UserMaster. In this case since we have a specific master that
           the user wants to view/edit we fire an event that will go fetch the master data for this master
        */
        if(this.props.params.master !== undefined)this.props.onMdmMasterChosen(this.props.params.master);

        /* Picture this - The user navigated to a master and then user navigated away and then came back. After this if the
           user clicks on the MDM menu - i.e no specific master was chosen - however data for the master viewed earlier is already
           available in redux - we can either say since the route does not specify as master we dont show anything, or simply
           push he route for the existing master and show what is already present in redux 
         */
        if(this.props.masterData != undefined && this.props.masterData.master !== undefined) this.masterChosen(this.props.masterData.master);
    }
}

export default MDM;




