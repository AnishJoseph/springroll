import React from 'react';

const BooleanFormatter = ( {value}) => {
    var valueToDisplay;
    if(_.isBoolean(value)){
        valueToDisplay = value ? 'True' : 'False';
    } else {
        valueToDisplay = (
            <div>
                <div>{value.val ? 'True' : 'False'}</div>
                {value.changed && <div className="text-muted mdm-prev-value">{value.prevVal ? 'True' : 'False'}</div>}
            </div>
        )
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export default BooleanFormatter;