import Application from 'App.js';
import React from 'react';

class Module1 extends React.Component {

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
        this.setState({testCase: event.target.value});
    }

    handleSubmit(event) {
        var data = JSON.stringify({testLocation :  this.state.testLocation, testCase : this.state.testCase});
        $.ajax({
            url: 'api/testPipelineSimple',
            type: 'POST',
            data: data,
            success: function (templateData) {
                console.log("Templates loaded.");
            },
            error : function (jqXHR, textStatus, errorThrown ){
                console.error("Unable to load templates - textStatus is " + textStatus + ' :: errorThrown is ' + errorThrown);
            }
        });

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
module.exports = Module1;

Application.addMenu({
    title: 'ui.menu.first',
    index: 1,
    type: "menuitem",
    route : 'M1',
    component : Module1
});