import React from 'react';
import Application from 'App';
var moment = require('moment');
import ReactDOM from 'react-dom';
import { DateField } from 'react-date-picker'

import 'react-date-picker/index.css'

class DatePicker extends React.Component {
    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
        this.getValue = this.getValue.bind(this);
    }

    getInputNode() {
        return ReactDOM.findDOMNode(this);
    }
    onChange(dateString, { dateMoment, timestamp }){
        if(dateMoment == null || dateMoment == undefined)return;
        this.date = dateMoment.valueOf();
        //this.props.onCommit();
    }
    getValue() {
        let updated = {};
        updated[this.props.column.key] = this.date;
        return updated;
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
