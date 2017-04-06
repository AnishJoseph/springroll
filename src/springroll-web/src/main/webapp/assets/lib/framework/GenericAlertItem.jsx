import React from 'react'
import Application from 'App.js';
import {DateTimeFormatter} from 'Formatters';


const GenericAlertItem = ({alert, onDismissClicked, onMoreInfoClicked}) => {
    return (
        <span>
            <div id='message' className="alertMessage">{Application.Localize(alert.messageKey, alert.args)}</div>
            <div id='action'>
                { onDismissClicked  &&
                    <span onClick={onDismissClicked}
                          data-toggle="tooltip"
                          title={Application.Localize('ui.dismiss')}
                          className="alertActionsPanelItem glyphicon glyphicon-trash">
                    </span>
                }
                { onMoreInfoClicked &&
                    <span
                        id='info'
                        onClick={onMoreInfoClicked}
                        data-toggle="tooltip"
                        title={Application.Localize('ui.info')}
                        className="alertActionsPanelItem glyphicon glyphicon-info-sign">
                    </span>
                }
                <span className="alertActionsPanelTime"><DateTimeFormatter value={alert.creationTime}/></span>
                <div className="alertActionsPanelBorder"/>
            </div>
        </span>
    );
};

export default GenericAlertItem;
