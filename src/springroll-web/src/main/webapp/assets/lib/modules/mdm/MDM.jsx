import React from 'react';
import MDMGrid from 'mdm/MDMTable';
import { Router, Route } from 'react-router'
import { NavDropdown, Nav, Navbar, MenuItem } from 'react-bootstrap';

class MDM extends React.Component {
    constructor(props){
        super(props);
        this.masterChosen = this.masterChosen.bind(this);
    }

    masterChosen(masterName) {
        /* First change the URL */
        this.props.router.push('/mdm/' + masterName);
        /* let upstairs know that a master was chosen */
        this.props.onMdmMasterChosen(masterName);
    }

    render() {
        /* For each master group create a menu item, and for each master in that master group, add the master to the dropdown */
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
                {this.props.masterData !== undefined && <MDMGrid {...this.props}/>}
            </div>
        );
    }
    componentDidMount(){
        /* The MDM menu (level 1) was chosen - fire a Action to go and fetch the MDM masters from the servers */
        /* Note : This is not getting any master data - just the meta data so that we can create the second level menus */
        this.props.onMdmModuleActivated(this.props.params.master);
        /* Ah a specific master is part or the URL - for example mdm/UserMaster. In this case since we have a specific master that
           the user wants to view/edit we fire an event that will go fetch the master data for this master
        */
        if(this.props.params.master !== undefined)this.props.onMdmMasterChosen(this.props.params.master);
    }
}

export default MDM;




