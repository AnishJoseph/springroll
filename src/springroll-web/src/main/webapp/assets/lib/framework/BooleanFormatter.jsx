import React from 'react';
import Application from 'App';

const BooleanFormatter = ( {value}) => {
    var valueToDisplay;
    if(value !== undefined && _.isBoolean(value)){
        valueToDisplay = value ? Application.Localize('ui.true') : Application.Localize('ui.false');
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export default BooleanFormatter;