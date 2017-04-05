import React from 'react';
import Application from 'App';
import DateField from 'react-datetime';
var moment = require('moment');

class DatePicker extends React.Component {
    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
    }

    onChange(dateMoment){
        if(dateMoment == null || dateMoment == undefined)return;
        this.props.onChange(dateMoment.valueOf());
    }
    render() {
        let value = this.props.value === undefined ? undefined : moment(this.props.value);
        return (<DateField
            value={value}
            dateFormat={Application.getMomentFormatForDate()}
            onChange={this.onChange}
            closeOnSelect={true}
            timeFormat={this.props.isDateTime}
        />);
    }
}

export default DatePicker;
