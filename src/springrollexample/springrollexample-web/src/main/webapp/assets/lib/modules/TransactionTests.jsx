import Application from 'App.js';
import React from 'react';
import axios from 'axios';

class TransactionTests extends React.Component {

    constructor(props) {
        super(props);
        this.state = {testCase: 1, testLocation : 0};

        this.handleTestCaseChange = this.handleTestCaseChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleTestLocationChange = this.handleTestLocationChange.bind(this);
    }

    handleTestCaseChange(event) {
        this.setState({testCase: event.target.value});
    }
    handleTestLocationChange(event) {
        this.setState({testLocation: event.target.value});
    }

    handleSubmit(event) {
        axios.post('api/testPipelineSimple', {testLocation :  this.state.testLocation, testCase : this.state.testCase});
        event.preventDefault();
    }

    render() {
        return (
            <div className="full-overflow">
                <form onSubmit={this.handleSubmit}>
                    <label>
                        Test Case:
                        <input type="text" value={this.state.testCase} onChange={this.handleTestCaseChange} />
                    </label>
                    <br />
                    <label>
                        Test Location:
                        <input type="text" value={this.state.testLocation} onChange={this.handleTestLocationChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
            </div>
        );
    }
}
module.exports = TransactionTests;

/* This is an example of a simple menu
    - its a top level menu (will be shown first, as the index is set to 1
    - it has no submenu
    - a form is rendered when clicked - what gets rendered is specified by 'component'
    - the route is /TransactionTests - i.e when clicked the url changes 
 */
Application.addMenu({
    title: 'ui.menu.TransactionTests',
    index: 1,
    type: "menuitem",
    route : 'TransactionTests',
    component : TransactionTests
});