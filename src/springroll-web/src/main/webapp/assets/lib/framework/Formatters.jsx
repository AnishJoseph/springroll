import React from 'react';
import Application from 'App.js';
const moment = require('moment');
var numeral = require('numeral');


export function WrapperForFormatter(cell, formatter, coDef, row, updateResponse) {
    let Formatter = formatter;
    let className = 'hidden';
    let errorMessage = '';
    if(updateResponse !== undefined) {
        let cid = row['cid'];
        let errors = updateResponse[cid];
        if (errors !== undefined) {
            errorMessage = errors[coDef['name']];
            if(errorMessage !== undefined) className = 'mdm-error glyphicon glyphicon-warning-sign';
        }
    }
    return (
        <span>
            <Formatter value={cell} colDef={coDef}/>
            <div className={className}><span style={{paddingLeft: "4px"}}>{errorMessage}</span></div>
        </span>
    );
}

export const DeleteFormatter = (cell, row, onDeleteRow) => {
    let classValue = row['id'] === -1 && row['rowIsNew']? "springroll-icon glyphicon glyphicon-trash" : 'springroll-icon glyphicon glyphicon-trash icon-muted';
    return (
        <span style={{paddingLeft : 5 +'px'}}
              data-toggle="tooltip"
              title={Application.Localize('ui.mdm.delete')}
              className={classValue} onClick={() => onDeleteRow(row)}>
        </span>
    );
};


export const DateToString = (value) => {
    return moment(value).format(Application.getMomentFormatForDate());
};
export const DateTimeToString = (value) => {
    return moment(value).format(Application.getMomentFormatForDateTime());
};
export const BooleanToString = (value) => {
    return value ? Application.Localize('ui.true') : Application.Localize('ui.false');
};

export const BooleanFormatter = ( {value}) => {
    let valueToDisplay;
    if(value !== undefined && _.isBoolean(value)){
        valueToDisplay = BooleanToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};
export const TextFormatter = ( {value}) => {
    return (
        <div> {value} </div>
    );
};

export const DateFormatter = ({value}) => {
    let valueToDisplay;
    if(value !== undefined && _.isNumber(value)){
        valueToDisplay = DateToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};

export const DateTimeFormatter = ({value}) => {
    let valueToDisplay;
    if(value !== undefined && _.isNumber(value)){
        valueToDisplay = DateTimeToString(value);
    }
    return (
        <div> {valueToDisplay} </div>
    );
};
export const NumberFormatter = ({value, colDef}) => {
    return (
        <div> { numeral(value).format(colDef.format)} </div>
    );
};



