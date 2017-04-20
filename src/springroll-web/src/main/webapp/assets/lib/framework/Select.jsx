import React from 'react';
import ReactSelect from 'react-select';
import {pluck}  from 'lodash';

class Select extends React.Component {
    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
    }

    onChange(value){
        if(value == null)return;
        let choices = undefined;
        if(this.props.multiSelect) {
            choices = pluck(value, 'value');
        } else {
            choices = value.value;
        }
        this.props.onChange(choices);

    }
    render() {
        return (<ReactSelect
            options={this.props.options}
            onChange={this.onChange}
            multi={this.props.multiSelect}
            value={this.props.value}
            className={this.props.className}
        />);
    }
    componentWillReceiveProps(nextProps) {
        this.setState({value : nextProps.value});
    }

}

export default Select;
