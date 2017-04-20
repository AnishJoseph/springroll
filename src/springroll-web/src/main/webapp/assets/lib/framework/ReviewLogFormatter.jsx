import React from 'react';
var moment = require('moment');
import {BooleanToString, DateTimeToString} from 'Formatters';
import {each}  from 'lodash';

export const ReviewLogFormatterForDisplay = ({value}) => {
    if(value !== undefined && value != null){
        return (<span>
            {
                value.map((reviewLog, index) => {
                    let appORrej = BooleanToString(reviewLog.approved);
                    let valueToDisplay = reviewLog.reviewer + ":" + appORrej + ":" + DateTimeToString(reviewLog.time);
                    return (<div key={index}>{valueToDisplay}</div>);
                })
            }
            </span>)
    }
    return (
        <div></div>
    );
};

export const ReviewLogFormatterForExport = (value) => {
    let displayString = '';
    if(value !== undefined && value != null){
        each(value, (reviewLog, index) => {
            let appORrej = BooleanToString(reviewLog.approved);
            displayString += reviewLog.reviewer + ":" + appORrej + ":" + DateTimeToString(reviewLog.time) + " :: ";
        });
    }
    return displayString;
};