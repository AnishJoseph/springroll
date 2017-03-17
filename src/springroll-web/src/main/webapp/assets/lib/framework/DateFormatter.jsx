import React from 'react';
import Application from 'App.js';
var moment = require('moment');

const DateFormatter = (value) => {
    return (
        <div> {moment(value.value).format(Application.getMomentFormatForDate())} </div>
    );
};

export default DateFormatter;