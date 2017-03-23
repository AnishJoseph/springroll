import React from 'react';
import Application from 'App';
import MDMGrid from 'mdm/MDMGrid';
import { Router, Route } from 'react-router'
import { NavDropdown, Nav, Navbar, MenuItem } from 'react-bootstrap';

class MDM extends React.Component {
    constructor(props){
        super(props);
        this.masterChosen = this.masterChosen.bind(this);
    }

    masterChosen(masterName) {
        this.props.router.push('/mdm/' + masterName);
        this.props.onMdmMasterChosen(masterName);
    }

    render() {
        return (
            <div>
                <Nav bsStyle="pills" onSelect={this.masterChosen}>
                    {this.props.masterDefns != undefined && Object.keys(this.props.masterDefns).map((groName, index) => {
                        return (
                            <NavDropdown id="mdm-nav-dropdown" title={groName} key={groName}>
                                {this.props.masterDefns[groName].map((masterName, index) => {return (<MenuItem eventKey={masterName} key={masterName}>{masterName}</MenuItem>)})}
                            </NavDropdown>
                        );
                    })}
                </Nav>
                {this.props.masterData !== undefined && <MDMGrid masterData={this.props.masterData} onMdmMasterChanged={this.props.onMdmMasterChanged} updateStatus={this.props.updateStatus} updateResponse={this.props.updateResponse}/>}
            </div>
        );
    }
    componentDidMount(){
        this.props.onMdmModuleActivated(this.props.params.master);
        if(this.props.params.master !== undefined)this.props.onMdmMasterChosen(this.props.params.master);
    }
}

export default MDM;




