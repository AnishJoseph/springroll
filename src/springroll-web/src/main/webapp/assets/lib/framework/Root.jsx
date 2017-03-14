import React from 'react';
import Application from 'App';
import NavigationContainer from 'NavigationContainer.jsx';
import AlertPanelContainer from 'AlertPanelContainer.jsx';


const Root = ({ children }) => {
    return (
        <div>
            <NavigationContainer />
            <div id='main-body' className='main-body'>
                <div id='content' className='main-body-content'>
                    {children}
                </div>
            </div>
            <AlertPanelContainer/>
            <div id='indicator' className='alertsIndicator'/>
        </div>
    );
}

export default Root;