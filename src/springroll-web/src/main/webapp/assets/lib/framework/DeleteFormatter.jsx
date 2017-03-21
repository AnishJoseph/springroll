import React from 'react';
import Application from 'App';

const DeleteFormatter = ({value}) => {
    let classValue = value === true ? "springroll-icon glyphicon glyphicon-trash" : 'springroll-icon glyphicon glyphicon-trash icon-muted';
    return (
        <span style={{margin : 50 +'%'}}
              data-toggle="tooltip"
              title={Application.Localize('ui.mdm.delete')}
              className={classValue}>
        </span>
    );
};

export default DeleteFormatter;