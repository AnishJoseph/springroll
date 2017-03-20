import React from 'react';
import Application from 'App.js';
var moment = require('moment');

const DateFormatter = ({value}) => {
    var valueToDisplay;
    if(_.isNumber(value)){
        valueToDisplay = moment(value).format(Application.getMomentFormatForDate())
    } else {
        valueToDisplay = (
            <div>
                <div>{moment(value.val).format(Application.getMomentFormatForDate())}</div>
                {value.changed && <div className="text-muted mdm-prev-value">{moment(value.prevVal).format(Application.getMomentFormatForDate())}</div>}
            </div>
        )
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export default DateFormatter;