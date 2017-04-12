import React from 'react';
import Application from 'App';

class Input extends React.Component {
    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
    }

    onChange(value){
        if(this.props.pattern){
            if(!this.props.pattern.test(value.target.value)){
                return;
            }
        }
        console.log("Passes or no patern");
        this.props.onChange(value.target.value);
    }

    render() {
        return (
            <input required className={this.props.classes} value={this.props.value} type="text" onChange={this.onChange} />
        );
    }
}

export default Input;

