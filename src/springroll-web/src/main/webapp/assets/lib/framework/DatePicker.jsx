import React from 'react';
import Application from 'App';
var moment = require('moment');
import { DateField } from 'react-date-picker'

class DatePicker extends React.Component {
    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
    }

    onChange(dateString, { dateMoment, timestamp }){
        if(dateMoment == null || dateMoment == undefined)return;
        this.date = dateMoment.valueOf();
        this.props.onChange(this.date);
    }
    render() {
        this.date = this.props.value;
        return (<DateField
            dateFormat={Application.getMomentFormatForDate()}
            defaultValue={this.date}
            onChange={this.onChange}
            updateOnDateClick={true}
            collapseOnDateClick={true}
        />);
    }
}

export default DatePicker;
