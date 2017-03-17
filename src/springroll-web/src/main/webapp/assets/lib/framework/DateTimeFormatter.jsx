import React from 'react';
import Application from 'App.js';
var moment = require('moment');

const DateTimeFormatter = (value) => {
    return (
        <div> {moment(value.value).format(Application.getMomentFormatForDateTime())} </div>
    );
};

export default DateTimeFormatter;