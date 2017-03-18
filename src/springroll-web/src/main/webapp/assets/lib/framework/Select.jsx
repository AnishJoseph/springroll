import React from 'react';
import Application from 'App';
import ReactDOM from 'react-dom';
import ReactSelect from 'react-select';

class Select extends React.Component {
    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
        this.getValue = this.getValue.bind(this);
        this.state = {value : this.props.value}
    }

    getInputNode() {
        return ReactDOM.findDOMNode(this);
    }
    onChange(value){
        this.setState({ value });

    }
    getValue() {
        let updated = {};
        updated[this.props.column.key] = this.state.value;
        return updated;
    }
    render() {
        return (<ReactSelect
            name="form-field-name"
            options={this.props.options}
            onChange={this.onChange}
            multi={this.props.multiSelect}
            delimiter=","
            joinValues={true}
            simpleValue={true}
            onInputChange={this.onInputChange}
            value={this.state.value}
        />);
    }
}

export default Select;
