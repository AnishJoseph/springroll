import React from 'react';
import Application from 'App.js';
const moment = require('moment');


export const DateToString = (value) => {
    return moment(value).format(Application.getMomentFormatForDate());
};
export const DateTimeToString = (value) => {
    return moment(value).format(Application.getMomentFormatForDateTime());
};
export const BooleanToString = (value) => {
    return value ? Application.Localize('ui.true') : Application.Localize('ui.false');
};

export const BooleanFormatter = ( {value}) => {
    let valueToDisplay;
    if(value !== undefined && _.isBoolean(value)){
        valueToDisplay = BooleanToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export const DateFormatter = ({value}) => {
    let valueToDisplay;
    if(value !== undefined && _.isNumber(value)){
        valueToDisplay = DateToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export const DateTimeFormatter = ({value}) => {
    let valueToDisplay;
    if(value !== undefined && _.isNumber(value)){
        valueToDisplay = DateTimeToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};



