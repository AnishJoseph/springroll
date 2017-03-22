import React from 'react';
import Application from 'App.js';
var moment = require('moment');

const DateTimeFormatter = ({value}) => {
    var valueToDisplay;
    if(value !== undefined && _.isNumber(value)){
        valueToDisplay = moment(value).format(Application.getMomentFormatForDateTime())
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export default DateTimeFormatter;