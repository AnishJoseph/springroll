import React from 'react';
import Application from 'App.js';
var moment = require('moment');

const ReviewLogFormatter = ({value}) => {
    if(value !== undefined && value != null){
        return (<span>
            {
                value.map((reviewLog, index) => {
                    let appORrej = reviewLog.approved ? Application.Localize('ui.Approved') : Application.Localize('ui.Rejected');
                    let valueToDisplay = reviewLog.reviewer + ":" + appORrej + ":" + moment(reviewLog.time).format(Application.getMomentFormatForDateTime());
                    return (<div key={index}>{valueToDisplay}</div>);
                })
            }
            </span>)
    }
    return (
        <div></div>
    );
};

export default ReviewLogFormatter;