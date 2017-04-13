import React from 'react';
import Application from 'App';

class Input extends React.Component {
    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
    }

    onChange(value){
        this.props.onChange(value.target.value);
    }

    render() {
        return (
            <input required className={this.props.classes} value={this.props.value} type="text" onChange={this.onChange} />
        );
    }
}

export default Input;

