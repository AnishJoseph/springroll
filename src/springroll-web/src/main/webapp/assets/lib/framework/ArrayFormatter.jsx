import React from 'react';

const ArrayFormatter = ({value}) => {
    var valueToDisplay;
    if (value.constructor === Array){
        valueToDisplay = value.toString();
    } else {
        valueToDisplay = (
            <div>
                <div>{value.val.toString()}</div>
                {value.changed && <div className="text-muted mdm-prev-value">{value.prevVal.toString()}</div>}
            </div>
        )
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export default ArrayFormatter;