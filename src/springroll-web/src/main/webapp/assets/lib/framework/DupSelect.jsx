import React from 'react';
import Application from 'App';
import ReactDOM from 'react-dom';
import ReactSelect from 'react-select';

class DupSelect extends React.Component {
    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
        this.state = {value : this.props.value}
    }

    onChange(value){
        console.log(JSON.stringify(value));
        let choices = undefined;
        if(this.props.multiSelect) {
            choices = _.pluck(value, 'value');
        } else {
            choices = value.value;
        }
        this.props.onChange(choices);
        this.setState({value : choices});

    }
    render() {
        return (<ReactSelect
            options={this.props.options}
            onChange={this.onChange}
            multi={this.props.multiSelect}
            value={this.state.value}
        />);
    }
}

export default DupSelect;
