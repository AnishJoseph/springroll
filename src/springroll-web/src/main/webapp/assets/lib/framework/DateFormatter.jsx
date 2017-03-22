import React from 'react';
import Application from 'App.js';
var moment = require('moment');

const DateFormatter = ({value}) => {
    var valueToDisplay;
    if(value !== undefined && _.isNumber(value)){
        valueToDisplay = moment(value).format(Application.getMomentFormatForDate())
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export default DateFormatter;