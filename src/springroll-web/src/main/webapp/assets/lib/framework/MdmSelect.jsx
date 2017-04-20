import React from 'react';
import Application from 'App';
import ReactDOM from 'react-dom';
import ReactSelect from 'react-select';
import {pluck}  from 'lodash';

class MdmSelect extends React.Component {
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
        let choices = undefined;
        if(this.props.multiSelect) {
            choices = pluck(value, 'value');
        } else {
            choices = value.value;
        }
        this.setState({ value : choices });

    }
    getValue() {
        let updated = {};
        updated[this.props.column.key] = this.state.value;
        return updated;
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

export default MdmSelect;
