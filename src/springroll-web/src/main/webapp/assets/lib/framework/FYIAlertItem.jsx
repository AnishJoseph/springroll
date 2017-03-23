import React from 'react'
import GenericAlertItem from 'GenericAlertItem.jsx';

const FYIAlertItem = ({alert, onDismissAlert}) => {
    return (
        <GenericAlertItem alert={alert} onDismissClicked={onDismissAlert}/>
    );
};

export default FYIAlertItem;
