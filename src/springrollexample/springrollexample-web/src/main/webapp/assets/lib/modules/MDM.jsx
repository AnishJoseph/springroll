import React from 'react';
import Application from 'App';
import { Router, Route } from 'react-router'
import { NavDropdown, Nav, Navbar, MenuItem } from 'react-bootstrap';

class MDM extends React.Component {
    constructor(props){
        super(props);
        this.onSubmit = this.onSubmit.bind(this);
        this.masterChosen = this.masterChosen.bind(this);
        this.state = {masterDefns : undefined };
    }
    onSubmit(action, comment){
        console.log("Hello");
    }

    masterChosen(masterName) {
        this.props.router.push('/mdm/' + masterName);
        $.ajax({
            url: "api/sr/mdm/data/" + masterName,
            type: 'POST',
            success: function(response){
                console.log("Yay");
            },
            error: function(xhr, reason, exception) {
                console.log("Error");
            }
        });


    }
    render() {
        return (
                <Nav bsStyle="pills" onSelect={this.masterChosen}>
                    {this.state.masterDefns != undefined && Object.keys(this.state.masterDefns).map((groName, index) => {
                        return (
                            <NavDropdown id="mdm-nav-dropdown" title={groName} key={groName}>
                                {this.state.masterDefns[groName].map((masterName, index) => {return (<MenuItem eventKey={masterName} key={masterName}>{masterName}</MenuItem>)})}
                            </NavDropdown>
                        );
                    })}
                </Nav>
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




