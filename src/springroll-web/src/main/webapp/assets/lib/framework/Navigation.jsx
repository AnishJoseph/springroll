require('bootstrap-loader');
import React from 'react';
import ReactDOM from 'react-dom';
import Application from 'App.js';
import MenuItem from 'MenuItem.jsx';
import DelegatorMenu from 'DelegatorMenu.jsx';
import AlertSummaryContainer from 'AlertSummaryContainer.jsx';

const Navigation = ({ delegators, username, onLogout, onSwitchUser, realLoggedInUser}) => {
    return (
        <div id='main-menu' className='root-menu'>
            <nav className="navbar navbar-default spaRootElement">
                <div className="container-fluid">
                    <div id="navbar" className="navbar-collapse collapse">
                        <ul className="nav navbar-nav nav-pills">
                            { Application.getMenuDefns().map((menusDefn, index) => ( <MenuItem key={index} menuDefn={menusDefn}/>)) }
                        </ul>
                        <p onClick={onLogout} data-toggle="tooltip" title={Application.Localize('ui.logout')} className="navbar-text navbar-right glyphicon glyphicon-off" aria-hidden="true" id="logout"/>
                        <DelegatorMenu delegators={delegators} username={username} onSwitchUser={onSwitchUser} realLoggedInUser={realLoggedInUser}/>
                        <AlertSummaryContainer />
                    </div>
                </div>
            </nav>
        </div>
    );
};

export default Navigation;