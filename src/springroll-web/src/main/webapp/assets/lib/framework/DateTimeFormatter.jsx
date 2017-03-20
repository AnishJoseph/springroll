import React from 'react';
import Application from 'App.js';
var moment = require('moment');

const DateTimeFormatter = ({value}) => {
    var valueToDisplay;
    if(_.isNumber(value)){
        valueToDisplay = moment(value).format(Application.getMomentFormatForDateTime())
    } else {
        valueToDisplay = (
            <div>
                <div>{moment(value.val).format(Application.getMomentFormatForDateTime())}</div>
                {value.changed && <div className="text-muted mdm-prev-value">{moment(value.prevVal).format(Application.getMomentFormatForDateTime())}</div>}
            </div>
        )
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export default DateTimeFormatter;