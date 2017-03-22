import React from 'react';

const ArrayFormatter = ({value}) => {
    var valueToDisplay;
    if (value !== undefined && value.constructor === Array) {
        valueToDisplay = value.toString();
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export default ArrayFormatter;