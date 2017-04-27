import React from 'react';
import Application from 'App.js';
const moment = require('moment');
var numeral = require('numeral');
import {isBoolean, isNumber}  from 'lodash';

export const floatPattern = new RegExp("^[-+]?[0-9]*\\.?[0-9]*$");
export const intPattern = new RegExp("^[-+]?[0-9]*$");

export const DateToString = (value) => {
    return moment(value).format(Application.getMomentFormatForDate());
};
export const DateTimeToString = (value) => {
    return moment(value).format(Application.getMomentFormatForDateTime());
};
export const BooleanToString = (value) => {
    return value ? Application.Localize('ui.true') : Application.Localize('ui.false');
};

export const ArrayFormatter = ( {value}) => {
    let valueToDisplay = undefined;
    if(value !== undefined && value !== null && Array.isArray(value)) {
        valueToDisplay = value.join();
    }
    return (
        <div> {valueToDisplay} </div>
    );
};
export const BooleanFormatter = ( {value}) => {
    let valueToDisplay;
    if(value !== undefined && isBoolean(value)){
        valueToDisplay = BooleanToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};
export const TextFormatter = ( {value}) => <div> {value} </div>

export const DateFormatter = ({value}) => {
    let valueToDisplay;
    if(value !== undefined && isNumber(value)){
        valueToDisplay = DateToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export const DateTimeFormatter = ({value}) => {
    let valueToDisplay;
    if(value !== undefined && isNumber(value)){
        valueToDisplay = DateTimeToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};
export const NumberFormatter = ({value, format}) => {
    return (
        <div> { numeral(value).format(format)} </div>
    );
};



