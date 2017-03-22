import React from 'react';

const BooleanFormatter = ( {value}) => {
    var valueToDisplay;
    if(value !== undefined && _.isBoolean(value)){
        valueToDisplay = value ? 'True' : 'False';
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export default BooleanFormatter;