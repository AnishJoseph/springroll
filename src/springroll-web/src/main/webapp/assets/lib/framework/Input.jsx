import React from 'react';
import Application from 'App';

class Input extends React.Component {
    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
        this.state = {value : undefined}
    }

    onChange(value){
        let oldValue = this.state.value;
        this.setState({value : value.target.value});
        if(this.props.pattern){
            if(!this.props.pattern.test(value.target.value)){
                console.log("FAILED : old " + oldValue );
                this.setState({value : oldValue});
                return;
            }
        }
        console.log("Passes or no patern");
        this.props.onChange(value.target.value);
    }

    render() {
        return (
            <input required className={this.props.classes} value={this.state.value} type="text" onChange={this.onChange} />
        );
    }
    componentWillReceiveProps(nextProps) {
        this.setState({value : nextProps.value});
    }

}

export default Input;

